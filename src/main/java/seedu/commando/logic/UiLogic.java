package seedu.commando.logic;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ReadOnlyToDoList;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Handles any logic in displaying on the UI, eg, splitting/filtering the to-do list
 */
public class UiLogic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final ReadOnlyToDoList toDoList;
    private final FilteredList<ReadOnlyToDo> filteredToDoList;
    private final ObservableList<UiToDo> observableEvents;
    private final ObservableList<UiToDo> observableTasks;
    private final UnmodifiableObservableList<UiToDo> protectedObservableEvents;
    private final UnmodifiableObservableList<UiToDo> protectedObservableTasks;
    private final ArrayList<UiToDo> toDoAtIndices;
    private int runningIndex;
    {
        observableEvents = FXCollections.observableArrayList();
        observableTasks = FXCollections.observableArrayList();
        protectedObservableEvents = new UnmodifiableObservableList<>(observableEvents);
        protectedObservableTasks = new UnmodifiableObservableList<>(observableTasks);
        toDoAtIndices = new ArrayList<>();
    }

    public UiLogic(ReadOnlyToDoList toDoList) {
        this.toDoList = toDoList;
        filteredToDoList = new FilteredList<>(toDoList.getToDos());

        updateEventsAndTasks(filteredToDoList);
        filteredToDoList.addListener(new ListChangeListener<ReadOnlyToDo>() {
            @Override
            public void onChanged(Change<? extends ReadOnlyToDo> change) {
                updateEventsAndTasks(filteredToDoList);
            }
        });
    }

    /**
     * Get to-do item at index as displayed on the UI
     */
    public Optional<UiToDo> getToDoAtIndex(int index) {
        if (index - 1 < 0 || index - 1 >= toDoAtIndices.size()) {
            return Optional.empty();
        } else {
            return Optional.of(toDoAtIndices.get(index - 1));
        }
    }

    /**
     * Sets a filter on the to-do list
     * Asserts {@param keywords} and {@param tags} to be non-null
     */
    public void setToDoListFilter(Set<String> keywords, Set<String> tags) {
        assert keywords != null;
        assert tags != null;

        filteredToDoList.setPredicate(toDo -> ifMatchesFilter(toDo, keywords, tags));
    }

    /**
     * Clears the filter on the to-do list
     */
    public void clearToDoListFilter() {
        filteredToDoList.setPredicate(null);
    }

    public UnmodifiableObservableList<UiToDo> getObservableEvents() {
        return protectedObservableEvents;
    }

    public UnmodifiableObservableList<UiToDo> getObservableTasks() {
        return protectedObservableTasks;
    }

    /**
     * Called to update the to-do list when it changes
     */
    private void updateEventsAndTasks(List<ReadOnlyToDo> list) {
        List<ReadOnlyToDo> events = list.stream().filter(this::isEvent).collect(Collectors.toList());
        List<ReadOnlyToDo> tasks = list.stream().filter(this::isTask).collect(Collectors.toList());

        // Sort observableEvents and observableTasks for UI
        sortEvents(events);
        sortTasks(tasks);

        toDoAtIndices.clear();

        // Add indices to events first and set to observable events
        runningIndex = 0;
        observableEvents.setAll(events.stream().map(
            toDo -> new UiToDo(toDo, ++ runningIndex)
        ).collect(Collectors.toList()));
        toDoAtIndices.addAll(observableEvents);

        // Then do the same for tasks
        observableTasks.setAll(tasks.stream().map(
            toDo -> new UiToDo(toDo, ++ runningIndex)
        ).collect(Collectors.toList()));
        toDoAtIndices.addAll(observableTasks);

        // running index should be incremented by no. of to-dos
        assert runningIndex == toDoAtIndices.size();

        // log events and tasks shown
        logger.info("Events: " + observableEvents.stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(",")));

        logger.info("Tasks: " + observableTasks.stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(",")));
    }

    private void sortTasks(List<ReadOnlyToDo> tasks) {
        // For observableTasks, sort by whether they have due dates, then chronological order

        tasks.sort((task1, task2) -> {
            if (!task1.getDueDate().isPresent() && !task2.getDueDate().isPresent()) {
                return 0; // both don't have dates, equal
            }

            if (task1.getDueDate().isPresent() && !task2.getDueDate().isPresent()) {
                return -1; // taskl first
            }

            if (task2.getDueDate().isPresent() && !task1.getDueDate().isPresent()) {
                return 1; // task2 first
            }

            // Here, both have duedates
            assert task1.getDueDate().isPresent();
            assert task2.getDueDate().isPresent();

            return task1.getDueDate().get().value.compareTo(task2.getDueDate().get().value);
        });
    }

    private void sortEvents(List<ReadOnlyToDo> events) {
        // For observableEvents, sort by chronological order
        events.sort((event1, event2) -> {
            // Must have date ranges because they are observableEvents
            assert event1.getDateRange().isPresent();
            assert event2.getDateRange().isPresent();

            return event1.getDateRange().get().startDate.compareTo(event2.getDateRange().get().startDate);
        });
    }

    /**
     * Returns whether a to-do item is an event
     */
    private boolean isEvent(ReadOnlyToDo todo) {
        return todo.getDateRange().isPresent();
    }

    /**
     * Returns whether a to-do item is a task
     */
    private boolean isTask(ReadOnlyToDo todo) {
        return !todo.getDateRange().isPresent();
    }

    //================================================================================
    //  Private methods for filtering
    //================================================================================

    private boolean ifMatchesFilter(ReadOnlyToDo toDo, Set<String> keywords, Set<String> tags) {
        return (keywords.stream()
            .allMatch(keyword -> checkForKeyword(toDo, keyword))) // contains all keywords
            && (tags.stream()
            .allMatch(tag -> checkForTag(toDo, tag))); // and has all the tags
    }

    private boolean checkForKeyword(ReadOnlyToDo toDo, String keyword) {
        return StringUtil.substringIgnoreCase(toDo.getTitle().value, keyword) ||
            toDo.getTags().stream().anyMatch(toDoTag -> StringUtil.substringIgnoreCase(toDoTag.value, keyword));
    }

    private boolean checkForTag(ReadOnlyToDo toDo, String tag) {
        return toDo.getTags().stream().anyMatch(toDoTag -> toDoTag.value.equalsIgnoreCase(tag));
    }
}
