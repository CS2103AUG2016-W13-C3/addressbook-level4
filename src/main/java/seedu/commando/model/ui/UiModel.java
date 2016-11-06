package seedu.commando.model.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.util.CollectionUtil;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListManager;
import seedu.commando.model.todo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//@@author A0139697H

/**
 * In charge of processing and filtering the list of to-dos for the UI.
 */
public class UiModel {
    private final ToDoListManager toDoListManager;
    private final ObservableList<UiToDo> events = FXCollections.observableArrayList();
    private final ObservableList<UiToDo> tasks = FXCollections.observableArrayList();
    private final UnmodifiableObservableList<UiToDo> protectedEvents = new UnmodifiableObservableList<>(events);
    private final UnmodifiableObservableList<UiToDo> protectedTasks = new UnmodifiableObservableList<>(tasks);
    private final ArrayList<UiToDo> toDoAtIndices = new ArrayList<>();
    private int runningIndex;

    // Parameters for filtering
    private Model.FILTER_MODE filterMode = Model.FILTER_MODE.ALL;

    /**
     * Predicate that filters to-dos based on filter mode.
     */
    private Predicate<ReadOnlyToDo> toDoFilterModePredicate = toDo -> {
        switch (filterMode) {
            case ALL:
                return true;
            case UNFINISHED:
                // if unfinished mode but to-do is finished before the current day
                return !toDo.isFinished()
                    || (toDo.isFinished() && toDo.getDateFinished().get().toLocalDate().isEqual(LocalDate.now()));
            case FINISHED:
                // if finished mode but to-do is unfinished
                return toDo.isFinished();
            default:
                assert false : "Should have covered all filter modes";
                return false;
        }
    };

    /**
     * Initializes with the given to-do list manager, setting its filter mode to UNFINISHED.
     *
     * @param toDoListManager the to-do list manager it tracks and grabs the list of to-dos from
     */
    public UiModel(ToDoListManager toDoListManager) {
        this.toDoListManager = toDoListManager;

        // Initialize the filter to show unfinished to-dos
        clearToDoListFilter(Model.FILTER_MODE.UNFINISHED);

        // Start tracking changes to-do list, and update UI to-dos when a change happens
        toDoListManager.getToDoList().getToDos().addListener(new ListChangeListener<ReadOnlyToDo>() {
            @Override
            public void onChanged(Change<? extends ReadOnlyToDo> change) {
                clearToDoListFilter(filterMode);
            }
        });
    }

    /**
     * @see Model#getUiToDoAtIndex(int)
     */
    public Optional<UiToDo> getToDoAtIndex(int index) {
        if (index - 1 < 0 || index - 1 >= toDoAtIndices.size()) {
            return Optional.empty();
        } else {
            return Optional.of(toDoAtIndices.get(index - 1));
        }
    }

    /**
     * @see Model#clearUiToDoListFilter(Model.FILTER_MODE)
     */
    public void clearToDoListFilter(Model.FILTER_MODE filterMode) {
        assert !CollectionUtil.isAnyNull(filterMode);
        setToDoListFilter(Collections.emptySet(), Collections.emptySet(), filterMode);
    }

    /**
     * @see Model#setUiToDoListFilter(Set, Set, Model.FILTER_MODE)
     */
    public void setToDoListFilter(Set<String> keywords, Set<Tag> tags, Model.FILTER_MODE filterMode) {
        assert !CollectionUtil.isAnyNull(keywords, tags, filterMode);

        this.filterMode = filterMode;

        updateEventsAndTasks(keywords, tags);
    }

    //@@author A0142230B

    /**
     * @see Model#setUiToDoListFilter(DateRange)
     */
    public void setToDoListFilter(DateRange dateRange) {
        assert dateRange != null;

        this.filterMode = Model.FILTER_MODE.ALL;

        updateEventsAndTasksByTime(dateRange);
    }

    //@@author A0139697H

    /**
     * @see Model#getUiEvents()
     */
    public UnmodifiableObservableList<UiToDo> getEvents() {
        return protectedEvents;
    }

    /**
     * @see Model#getUiTasks()
     */
    public UnmodifiableObservableList<UiToDo> getTasks() {
        return protectedTasks;
    }

    /**
     * Update the UI to-dos based on {@link #toDoListManager}'s to-do list and
     * the keywords and tags filter.
     */
    private void updateEventsAndTasks(Set<String> keywords, Set<Tag> tags) {
        // Sort and filter events and tasks for UI
        List<ReadOnlyToDo> events = filterAndSortEvents(toDoListManager.getToDoList().getToDos(), keywords, tags);
        List<ReadOnlyToDo> tasks = filterAndSortTasks(toDoListManager.getToDoList().getToDos(), keywords, tags);

        // Update its own lists of UI to-dos
        updateUiToDos(events, tasks);
    }

    //@@author A0142230B

    /**
     * Update the UI to-dos based on {@link #toDoListManager}'s to-do list and the date range filter
     */
    private void updateEventsAndTasksByTime(DateRange filterDateRange) {
        // Sort and filter events and tasks for UI
        List<ReadOnlyToDo> events = filterAndSortEventsByTime(toDoListManager.getToDoList().getToDos(), filterDateRange);
        List<ReadOnlyToDo> tasks = filterAndSortTasksByTime(toDoListManager.getToDoList().getToDos(), filterDateRange);
        // Update its own lists of UI to-dos
        updateUiToDos(events, tasks);
    }

    //@@author A0139697H

    /**
     * Populate its lists of UI to-dos based on supplied to-dos.
     */
    private void updateUiToDos(List<ReadOnlyToDo> events, List<ReadOnlyToDo> tasks) {
        toDoAtIndices.clear();
        runningIndex = 0;

        Optional<ToDoListChange> lastChange = toDoListManager.getLastToDoListChange();

        ReadOnlyToDoList newToDos = lastChange.isPresent()
            ? lastChange.get().getAddedToDos() : new ToDoList();

        // Map each event to a UI to-do and add an index to each
        // Also check if the events are new with respect to last change
        this.events.setAll(events.stream().map(
            toDo -> new UiToDo(toDo, ++runningIndex, newToDos.contains(toDo))
        ).collect(Collectors.toList()));
        toDoAtIndices.addAll(this.events);

        // Then do the same for tasks
        this.tasks.setAll(tasks.stream().map(
            toDo -> new UiToDo(toDo, ++runningIndex, newToDos.contains(toDo))
        ).collect(Collectors.toList()));
        toDoAtIndices.addAll(this.tasks);

        // running index should be incremented by no. of to-dos
        assert runningIndex == toDoAtIndices.size();
    }
    //@@author A0142230B

    /**
     * Filter tasks which are within the filterDateRange from the given toDoList.
     *
     * @return a list of filtered tasks
     */
    private List<ReadOnlyToDo> filterAndSortTasksByTime(List<ReadOnlyToDo> toDos, DateRange filterDateRange) {
        List<ReadOnlyToDo> tasks = new ArrayList<ReadOnlyToDo>();
        for (ReadOnlyToDo toDo : toDos) {
            if (isTaskInDateRange(toDo, filterDateRange)) {
                tasks.add(toDo);
            }
        }
        // For tasks, sort by created date first (latest first)
        tasks.sort((task1, task2) -> task2.getDateCreated().compareTo(task1.getDateCreated()));

        // Then, by whether they have due date and that date (latest first)
        tasks.sort((task1, task2) -> compareDueDates(task1, task2));

        // Then, by whether they are finished and finished date (latest first)
        tasks.sort((task1, task2) -> compareDateFinished(task2, task1));

        return tasks;
    }

    /**
     * Check if a toDo is a task and it is in the filterDateRange
     *
     * @return true if the toDo is a task and it is in the filterDateRange
     */
    private boolean isTaskInDateRange(ReadOnlyToDo toDo, DateRange filterDateRange) {
        if (!toDo.getDueDate().isPresent()) {
            return false;
        }
        LocalDateTime dueDate = toDo.getDueDate().get().value;

        return isTimeInRange(dueDate, filterDateRange);
    }

    /**
     * Filter events which are within the filterDateRange from the given toDoList.
     *
     * @return a list of filtered events
     */
    private List<ReadOnlyToDo> filterAndSortEventsByTime(List<ReadOnlyToDo> toDos, DateRange filterDateRange) {
        List<ReadOnlyToDo> events = new ArrayList<ReadOnlyToDo>();
        for (ReadOnlyToDo toDo : toDos) {
            if (isEventInDateRange(toDo, filterDateRange)) {
                events.add(toDo);
            }
        }

        // For events, sort by created date first (latest first)
        events.sort((event1, event2) -> event2.getDateCreated().compareTo(event1.getDateCreated()));

        // Then, by start dates (earlier first)
        events.sort((event1, event2) -> compareDateRangeStarts(event1, event2));

        // Then, by whether they are finished and finished date (latest first)
        events.sort((event1, event2) -> compareDateFinished(event2, event1));

        return events;
    }

    /**
     * Check if a toDo is a event and it is in the filterDateRange
     *
     * @return true if the toDo is a event and it is in the filterDateRange
     */
    private boolean isEventInDateRange(ReadOnlyToDo toDo, DateRange filterDateRange) {
        if (!toDo.getDateRange().isPresent()) {
            return false;
        }
        LocalDateTime startTime = toDo.getDateRange().get().startDate;
        LocalDateTime endTime = toDo.getDateRange().get().endDate;

        // Case 1: start time is in the range
        if (isTimeInRange(startTime, filterDateRange)) {
            return true;
        }
        // Case 2:end time is in the range
        if (isTimeInRange(endTime, filterDateRange)) {
            return true;
        }

        // Case 3: filterDateRange is in the range between startTime and endTime
        if (startTime.isBefore(filterDateRange.startDate) && endTime.isAfter(filterDateRange.endDate)) {
            return true;
        }

        return false;
    }

    /**
     * Check if a LocalDateTime is within a dateRange
     *
     * @return true if the time is in the dateRange
     */
    private boolean isTimeInRange(LocalDateTime time, DateRange dateRange) {
        return (time.isAfter(dateRange.startDate) || time.isEqual(dateRange.startDate))
            && (time.isBefore(dateRange.endDate) || time.isEqual(dateRange.endDate));
    }

    //@@author A0139697H
    private List<ReadOnlyToDo> filterAndSortTasks(List<ReadOnlyToDo> toDos,
                                                  Set<String> keywords, Set<Tag> tags) {
        List<ReadOnlyToDo> tasks = toDos.stream()
            .filter(toDoFilterModePredicate)
            .filter(toDo -> ifMatchesKeywordsAndTags(toDo, keywords, tags))
            .filter(UiToDo::isTask)
            .collect(Collectors.toList());

        // For tasks, sort by created date first (latest first)
        tasks.sort((task1, task2) -> task2.getDateCreated().compareTo(task1.getDateCreated()));

        // Then, by whether they have due date and that date (latest first)
        tasks.sort((task1, task2) -> compareDueDates(task1, task2));

        // Then, by whether they are finished and finished date (latest first)
        tasks.sort((task1, task2) -> compareDateFinished(task2, task1));

        return tasks;
    }

    private List<ReadOnlyToDo> filterAndSortEvents(List<ReadOnlyToDo> toDos,
                                                   Set<String> keywords, Set<Tag> tags) {
        List<ReadOnlyToDo> events = toDos.stream()
            .filter(toDoFilterModePredicate)
            .filter(toDo -> ifMatchesKeywordsAndTags(toDo, keywords, tags))
            .filter(UiToDo::isEvent)
            .collect(Collectors.toList());

        // For events, sort by created date first (latest first)
        events.sort((event1, event2) -> event2.getDateCreated().compareTo(event1.getDateCreated()));

        // Then, by start dates (earlier first)
        events.sort((event1, event2) -> compareDateRangeStarts(event1, event2));

        // Then, by whether they are finished and finished date (latest first)
        events.sort((event1, event2) -> compareDateFinished(event2, event1));

        return events;
    }

    //================================================================================
    //  Utility methods for sorting and filtering
    //================================================================================

    private int compareDateFinished(ReadOnlyToDo toDo1, ReadOnlyToDo toDo2) {
        LocalDateTime date1 = toDo1.getDateFinished().orElse(LocalDateTime.MAX);
        LocalDateTime date2 = toDo2.getDateFinished().orElse(LocalDateTime.MAX);

        return date1.compareTo(date2);
    }

    private int compareDueDates(ReadOnlyToDo toDo1, ReadOnlyToDo toDo2) {
        LocalDateTime date1 = toDo1.getDueDate().isPresent() ?
            toDo1.getDueDate().get().value : LocalDateTime.MAX;
        LocalDateTime date2 = toDo2.getDueDate().isPresent() ?
            toDo2.getDueDate().get().value : LocalDateTime.MAX;

        return date1.compareTo(date2);
    }

    private int compareDateRangeStarts(ReadOnlyToDo toDo1, ReadOnlyToDo toDo2) {
        LocalDateTime date1 = toDo1.getDateRange().isPresent() ?
            toDo1.getDateRange().get().startDate : LocalDateTime.MAX;
        LocalDateTime date2 = toDo2.getDateRange().isPresent() ?
            toDo2.getDateRange().get().startDate : LocalDateTime.MAX;

        return date1.compareTo(date2);
    }

    private boolean checkForKeyword(ReadOnlyToDo toDo, String keyword) {
        return StringUtil.substringIgnoreCase(toDo.getTitle().value, keyword) ||
            toDo.getTags().stream().anyMatch(toDoTag -> StringUtil.substringIgnoreCase(toDoTag.value, keyword));
    }

    private boolean checkForTag(ReadOnlyToDo toDo, Tag tag) {
        return toDo.getTags().stream().anyMatch(toDoTag -> toDoTag.value.equalsIgnoreCase(tag.value));
    }

    /**
     * Returns whether a to-do matches a set of keywords and tags.
     */
    private boolean ifMatchesKeywordsAndTags(ReadOnlyToDo toDo, Set<String> keywords, Set<Tag> tags) {
        return (keywords.stream()
            .allMatch(keyword -> checkForKeyword(toDo, keyword)))
            && (tags.stream()
            .allMatch(tag -> checkForTag(toDo, tag)));
    }
}
