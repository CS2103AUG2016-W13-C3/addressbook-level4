package seedu.commando.model;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.ui.UiModel;
import seedu.commando.model.ui.UiToDo;

import java.util.Optional;
import java.util.Set;

//@@author A0139697H
/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * @return its to-do list, read-only
     */
    ReadOnlyToDoList getToDoList();

    /**
     * Applies a change to its to-do list.
     * @throws IllegalValueException if the change was invalid
     */
    void changeToDoList(ToDoListChange change) throws IllegalValueException;

    /**
     * Undos the last successful change to the to-do list.
     * @return true if there was a change that was undone
     */
    boolean undoToDoList();

    /**
     * Redos the last undo to the to-do list.
     * @return true if there was an undo that was redone
     */
    boolean redoToDoList();

    /**
     * Returns observable list of UI to-dos considered as events by ({@link UiToDo#isEvent()}
     *   to be displayed on the UI.
     * This changes with the filter on the UI to-dos and the to-do list of model.
     */
    UnmodifiableObservableList<UiToDo> getUiEvents();

    /**
     * Return observable list of UI to-dos considered as tasks by ({@link UiToDo#isTask()}
     * This changes with the filter on the UI to-dos and the to-do list of model.
     */
    UnmodifiableObservableList<UiToDo> getUiTasks();

     /**
     * @return the UI to-do with {@link UiToDo#getIndex()} == {@param toDoIndex}, if exists
     */
    Optional<UiToDo> getUiToDoAtIndex(int index);

    /**
     * @see seedu.commando.model.ui.UiModel#clearToDoListFilter(UiModel.FILTER_MODE)
     */
    void clearUiToDoListFilter(UiModel.FILTER_MODE filterMode);

    /**
     * @see seedu.commando.model.ui.UiModel#setToDoListFilter(Set, Set, UiModel.FILTER_MODE)
     */
    void setUiToDoListFilter(Set<String> keywords, Set<Tag> tags, UiModel.FILTER_MODE filterMode);

    //@@author A0142230B
    /**
     * @see seedu.commando.model.ui.UiModel#setToDoListFilter(DateRange)
     */
    void setUiToDoListFilter(DateRange dateRange);
}
