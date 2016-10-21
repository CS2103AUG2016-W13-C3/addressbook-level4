package seedu.commando.model.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.ToDoListManager;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Handles any logic in displaying on the UI, eg, splitting/filtering the to-do list
 */
public class UiModel {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final ToDoListManager toDoListManager;
    private final FilteredList<ReadOnlyToDo> filteredToDoList;
    private final ObservableList<UiToDo> todayEvents;
    private final ObservableList<UiToDo> upcomingEvents;
    private final ObservableList<UiToDo> tasks;
    private final UnmodifiableObservableList<UiToDo> protectedUpcomingEvents;
    private final UnmodifiableObservableList<UiToDo> protectedTodayEvents;
    private final UnmodifiableObservableList<UiToDo> protectedTasks;
    private final ArrayList<UiToDo> toDoAtIndices;
    private int runningIndex;
    {
        upcomingEvents = FXCollections.observableArrayList();
        todayEvents = FXCollections.observableArrayList();
        tasks = FXCollections.observableArrayList();
        protectedUpcomingEvents = new UnmodifiableObservableList<>(upcomingEvents);
        protectedTodayEvents = new UnmodifiableObservableList<>(todayEvents);
        protectedTasks = new UnmodifiableObservableList<>(tasks);
        toDoAtIndices = new ArrayList<>();
    }

    public UiModel(ToDoListManager toDoListManager) {
        this.toDoListManager = toDoListManager;
        filteredToDoList = new FilteredList<>(toDoListManager.getToDoList().getToDos());

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
    public void setToDoListFilter(Set<String> keywords, Set<Tag> tags) {
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

    public UnmodifiableObservableList<UiToDo> getTodayEvents() {
        return protectedTodayEvents;
    }

    public UnmodifiableObservableList<UiToDo> getTasks() {
        return protectedTasks;
    }

    public UnmodifiableObservableList<UiToDo> getUpcomingEvents() {
        return protectedUpcomingEvents;
    }

    /**
     * Called to update the to-do list when it changes
     */
    private void updateEventsAndTasks(List<ReadOnlyToDo> list) {
        // Sort and filter observableEvents and observableTasks for UI
        List<ReadOnlyToDo> events = processEvents(list);
        List<ReadOnlyToDo> tasks = processTasks(list);

        toDoAtIndices.clear();

        // Add indices to events first and set to observable events
        // Also check if the events are new with respect to last change
        runningIndex = 0;

        Optional<ToDoListChange> change = toDoListManager.getLastToDoListChange();

        List<ReadOnlyToDo> newToDos = change.isPresent()
            ? change.get().getAddedToDos() : Collections.emptyList();

        List<UiToDo> uiEvents = events.stream().map(
            toDo -> new UiToDo(toDo, ++ runningIndex, newToDos.contains(toDo))
        ).collect(Collectors.toList());

        // Split to today and upcoming
        this.todayEvents.setAll(uiEvents.stream().filter(
            // Before tomorrow
            event -> event.getDateRange().get().startDate
                .isBefore(LocalDate.now().plusDays(1).atStartOfDay())
        ).collect(Collectors.toList()));

        this.upcomingEvents.setAll(uiEvents);
        toDoAtIndices.addAll(uiEvents);

        // Then do the same for tasks
        this.tasks.setAll(tasks.stream().map(
            toDo -> new UiToDo(toDo, ++ runningIndex, newToDos.contains(toDo))
        ).collect(Collectors.toList()));
        toDoAtIndices.addAll(this.tasks);

        // running index should be incremented by no. of to-dos
        assert runningIndex == toDoAtIndices.size();

        // log events and tasks shown
        logger.info("Events: " + this.upcomingEvents.stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(",")));

        logger.info("Tasks: " + this.tasks.stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(",")));
    }

    private List<ReadOnlyToDo> processTasks(List<ReadOnlyToDo> toDos) {
        List<ReadOnlyToDo> tasks = toDos.stream().filter(this::isTask).collect(Collectors.toList());

        // For observableTasks, sort by whether they are done,
        // then whether have due dates, then chronological order
        tasks.sort((task1, task2) -> {
            if (!task1.isFinished() && task2.isFinished()) {
                return -1; // task1 first
            }

            if (task1.isFinished() && !task2.isFinished()) {
                return 1; // task2 first
            }

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

        return tasks;
    }

    private List<ReadOnlyToDo> processEvents(List<ReadOnlyToDo> toDos) {
        List<ReadOnlyToDo> events = toDos.stream()
            .filter(this::isEvent)
            .filter(
                // filter all to-dos from today onwards
                event -> event.getDateRange().get().endDate.isAfter(LocalDate.now().atStartOfDay())
            ).collect(Collectors.toList());

        // For observableEvents, sort by chronological order
        events.sort((event1, event2) -> {
            // Must have date ranges because they are observableEvents
            assert event2.getDateRange().isPresent();
            assert event2.getDateRange().isPresent();

            if (!event1.isFinished() && event2.isFinished()) {
                return -1; // event1 first
            }

            if (event1.isFinished() && !event2.isFinished()) {
                return 1; // event2 first
            }

            return event1.getDateRange().get().startDate.compareTo(event2.getDateRange().get().startDate);
        });

        return events;
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

    private boolean ifMatchesFilter(ReadOnlyToDo toDo, Set<String> keywords, Set<Tag> tags) {
        return (keywords.stream()
            .allMatch(keyword -> checkForKeyword(toDo, keyword))) // contains all keywords
            && (tags.stream()
            .allMatch(tag -> checkForTag(toDo, tag))); // and has all the tags
    }

    private boolean checkForKeyword(ReadOnlyToDo toDo, String keyword) {
        return StringUtil.substringIgnoreCase(toDo.getTitle().value, keyword) ||
            toDo.getTags().stream().anyMatch(toDoTag -> StringUtil.substringIgnoreCase(toDoTag.value, keyword));
    }

    private boolean checkForTag(ReadOnlyToDo toDo, Tag tag) {
        return toDo.getTags().stream().anyMatch(toDoTag -> toDoTag.value.equalsIgnoreCase(tag.value));
    }
}
