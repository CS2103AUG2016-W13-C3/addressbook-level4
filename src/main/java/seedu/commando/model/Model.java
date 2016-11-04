package seedu.commando.model;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.ui.UiToDo;

import java.util.Optional;
import java.util.Set;

//@@author A0139697H

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * Gets the internal to-do list, read-only. Call {@link #changeToDoList(ToDoListChange)}
     * to modify the list.
     *
     * @return its to-do list, read-only
     */
    ReadOnlyToDoList getToDoList();

    /**
     * Applies a change to its to-do list.
     *
     * @throws IllegalValueException if the change was invalid
     */
    void changeToDoList(ToDoListChange change) throws IllegalValueException;

    /**
     * Undos the last successful change to its to-do list.
     *
     * @return true if there was a change that was undone
     */
    boolean undoToDoList();

    /**
     * Redos the last successful undo to its to-do list.
     *
     * @return true if there was an undo that was redone
     */
    boolean redoToDoList();

    /**
     * Returns observable read-only list of UI to-dos considered as events by {@link UiToDo#isEvent()}.
     * This changes with the filter on the UI to-dos and the to-do list of model.
     * The ordering of the list is to be respected.
     *
     * @return an observable read-only list of {@link UiToDo} that are events
     */
    UnmodifiableObservableList<UiToDo> getUiEvents();

    /**
     * Return observable read-only list of UI to-dos considered as tasks by {@link UiToDo#isTask()}.
     * This changes with the filter on the UI to-dos and the to-do list of model.
     * The ordering of the list is to be respected.
     *
     * @return an observable read-only list of {@link UiToDo} that are tasks
     */
    UnmodifiableObservableList<UiToDo> getUiTasks();

    /**
     * Returns the {@link UiToDo} in {@link #getUiEvents()} and {@link #getUiTasks()}
     * that have the {@link UiToDo#getIndex()} of {@param toDoIndex}, in constant time.
     *
     * @return the UI to-do with the given index, if exists
     */
    Optional<UiToDo> getUiToDoAtIndex(int index);

    /**
     * Clears any keywords, tags or daterange filters on the UI to-dos
     * and sets the filter mode.
     * Asserts parameters to be non-null.
     */
    void clearUiToDoListFilter(FILTER_MODE filterMode);

    /**
     * Sets a filter mode, keywords filter, and tags filter on the UI to-dos.
     * Asserts parameters to be non-null.
     * <p>
     * If {@param filterMode} ==
     * - ALL: all to-dos that match keywords and tags are shown.
     * - FINISHED: finished to-dos that match keywords and tags are shown
     * - UNFINISHED: unfinished to-dos or finished to-dos that are finished on the
     * the current day, that match keywords and tags, are shown.
     */
    void setUiToDoListFilter(Set<String> keywords, Set<Tag> tags, FILTER_MODE filterMode);

    //@@author A0142230B

    /**
     * Sets a date range filter on the UI to-dos and sets filter mode to be ALL.
     * Asserts dateRange to be non-null.
     */
    void setUiToDoListFilter(DateRange dateRange);

    enum FILTER_MODE {
        FINISHED, UNFINISHED, ALL
    }
}
