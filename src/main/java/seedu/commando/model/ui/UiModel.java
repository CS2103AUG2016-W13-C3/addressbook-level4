package seedu.commando.model.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.ToDoListManager;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ToDoList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//@@author A0139697H
/**
 * Handles any logic in displaying on the UI, eg, splitting/filtering the to-do list
 */
public class UiModel {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final ToDoListManager toDoListManager;
    private final ObservableList<UiToDo> events = FXCollections.observableArrayList();
    private final ObservableList<UiToDo> tasks = FXCollections.observableArrayList();
    private final UnmodifiableObservableList<UiToDo> protectedEvents = new UnmodifiableObservableList<>(events);
    private final UnmodifiableObservableList<UiToDo> protectedTasks = new UnmodifiableObservableList<>(tasks);
    private final ArrayList<UiToDo> toDoAtIndices = new ArrayList<>();
    private int runningIndex;

    // Parameters for filtering
    private Set<String> keywords;
    private Set<Tag> tags;
    private boolean ifHistoryMode;

    public UiModel(ToDoListManager toDoListManager) {
        this.toDoListManager = toDoListManager;

        setToDoListFilter(Collections.emptySet(), Collections.emptySet(), false);

        updateEventsAndTasks();

        // Start tracking changes to-do list
        toDoListManager.getToDoList().getToDos().addListener(new ListChangeListener<ReadOnlyToDo>() {
            @Override
            public void onChanged(Change<? extends ReadOnlyToDo> change) {
                updateEventsAndTasks();
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
     * If {@param ifHistoryMode} is true, only filter finished to-dos
     * Else, only filter such that it is not finished or
     * its finished date is after/during the current day
     */
    public void setToDoListFilter(Set<String> keywords, Set<Tag> tags, boolean ifHistoryMode) {
        assert keywords != null;
        assert tags != null;

        this.keywords = keywords;
        this.tags = tags;
        this.ifHistoryMode = ifHistoryMode;

        updateEventsAndTasks();
    }

    /**
     * Clears the filter on the to-do list
     */
    public void clearToDoListFilter(boolean ifHistoryMode) {
        setToDoListFilter(Collections.emptySet(), Collections.emptySet(), ifHistoryMode);
    }

    public UnmodifiableObservableList<UiToDo> getEvents() {
        return protectedEvents;
    }

    public UnmodifiableObservableList<UiToDo> getTasks() {
        return protectedTasks;
    }

    /**
     * Called to update the to-do list when it changes
     */
    private void updateEventsAndTasks() {
        // Sort and filter observableEvents and observableTasks for UI
        List<ReadOnlyToDo> events = filterAndSortEvents(toDoListManager.getToDoList().getToDos());
        List<ReadOnlyToDo> tasks = filterAndSortTasks(toDoListManager.getToDoList().getToDos());

        toDoAtIndices.clear();

        // Add indices to events first and set to observable events
        // Also check if the events are new with respect to last change
        runningIndex = 0;

        Optional<ToDoListChange> change = toDoListManager.getLastToDoListChange();

        ReadOnlyToDoList newToDos = change.isPresent()
            ? change.get().getAddedToDos() : new ToDoList();

        this.events.setAll(events.stream().map(
            toDo -> new UiToDo(toDo, ++ runningIndex, newToDos.contains(toDo))
        ).collect(Collectors.toList()));
        toDoAtIndices.addAll(this.events);

        // Then do the same for tasks
        this.tasks.setAll(tasks.stream().map(
            toDo -> new UiToDo(toDo, ++ runningIndex, newToDos.contains(toDo))
        ).collect(Collectors.toList()));
        toDoAtIndices.addAll(this.tasks);

        // running index should be incremented by no. of to-dos
        assert runningIndex == toDoAtIndices.size();

        // log events and tasks shown
        logger.info("Events: " + this.events.stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(",")));

        logger.info("Tasks: " + this.tasks.stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(",")));
    }

    private Predicate<ReadOnlyToDo> toDoFilterPredicate = toDo -> {
        if (!ifHistoryMode && toDo.isFinished()
            && toDo.getDateFinished().get().toLocalDate().isBefore(LocalDate.now())) {
            return false; // if normal mode but to-do is finished before the current day
        } else if (ifHistoryMode && !toDo.isFinished()) {
            return false; // if history mode but to-do is unfinished
        }

        return ifMatchesFilter(toDo, keywords, tags);
    };

    private List<ReadOnlyToDo> filterAndSortTasks(List<ReadOnlyToDo> toDos) {
        List<ReadOnlyToDo> tasks = toDos.stream()
            .filter(toDoFilterPredicate)
            .filter(UiToDo::isTask)
            .collect(Collectors.toList());

        // For tasks, sort by created date first (latest first)
        // Then, by whether they have due date and that date (latest first)
        // Then, by whether they are finished and finished date (latest first)

        tasks.sort((task1, task2) -> task2.getDateCreated().compareTo(task1.getDateCreated()));
        tasks.sort((task1, task2) -> {
            LocalDateTime task1Date = task1.getDueDate().isPresent() ?
                task1.getDueDate().get().value : LocalDateTime.MAX;
            LocalDateTime task2Date = task2.getDueDate().isPresent() ?
                task2.getDueDate().get().value : LocalDateTime.MAX;

            return task1Date.compareTo(task2Date);
        });

        tasks.sort((task1, task2) -> {
            LocalDateTime task1Date = task1.getDateFinished().isPresent() ?
                task1.getDateFinished().get() : LocalDateTime.MAX;
            LocalDateTime task2Date = task2.getDateFinished().isPresent() ?
                task2.getDateFinished().get() : LocalDateTime.MAX;

            return task2Date.compareTo(task1Date);
        });

        return tasks;
    }

    private List<ReadOnlyToDo> filterAndSortEvents(List<ReadOnlyToDo> toDos) {
        List<ReadOnlyToDo> events = toDos.stream()
            .filter(toDoFilterPredicate)
            .filter(UiToDo::isEvent)
            .collect(Collectors.toList());

        // For events, sort by created date first (latest first)
        // Then, by start dates (earlier first)
        // Then, by whether they are finished and finished date (latest first)

        events.sort((event1, event2) -> event2.getDateCreated().compareTo(event1.getDateCreated()));
        events.sort((event1, event2) -> {
            LocalDateTime event1Date = event1.getDateRange().isPresent() ?
                event1.getDateRange().get().startDate : LocalDateTime.MAX;
            LocalDateTime event2Date = event2.getDateRange().isPresent() ?
                event2.getDateRange().get().startDate : LocalDateTime.MAX;

            return event1Date.compareTo(event2Date);
        });

        events.sort((event1, event2) -> {
            LocalDateTime event1Date = event1.getDateFinished().isPresent() ?
                event1.getDateFinished().get() : LocalDateTime.MAX;
            LocalDateTime event2Date = event2.getDateFinished().isPresent() ?
                event2.getDateFinished().get() : LocalDateTime.MAX;

            return event2Date.compareTo(event1Date);
        });

        return events;
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
