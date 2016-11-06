package seedu.commando.model;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.todo.ToDoListChange;

import java.util.ArrayList;
import java.util.Optional;

//@@author A0139697H
/**
 * In charge of supporting the editing, undoing and redoing of the to-do list,
 *   hinging on the {@link ToDoListChange} class.
 */
public class ToDoListManager {
    private final ToDoList toDoList;

    // changes that can be undone
    private final ArrayList<ToDoListChange> toDoListChanges = new ArrayList<>();

    // changes that can be redone
    private final ArrayList<ToDoListChange> toDoListUndoChanges = new ArrayList<>();

    private ToDoListChange lastToDoListChange;

    /**
     * Initializes with the given to-do list, which is managed internally.
     * Asserts parameters are non-null.
     *
     * @param toDoList the internal to-do list will be a deep copy of this
     */
    public ToDoListManager(ReadOnlyToDoList toDoList) {
        assert toDoList != null;

        this.toDoList = new ToDoList(toDoList);
    }

    /**
     * @see Model#getToDoList()
     */
    public ReadOnlyToDoList getToDoList() {
        return toDoList;
    }

    /**
     * @see Model#changeToDoList(ToDoListChange)
     */
    public void changeToDoList(ToDoListChange change) throws IllegalValueException {
        applyToDoListChange(change);
        toDoListChanges.add(change);

        // Reset undo list upon a change
        toDoListUndoChanges.clear();
    }

    /**
     * @see Model#undoToDoList()
     */
    public boolean undoToDoList() {
        // Nothing else to undo if the list of changes is empty
        if (toDoListChanges.isEmpty()) {
            return false;
        }

        ToDoListChange change = toDoListChanges.get(toDoListChanges.size() - 1);

        try {
            applyToDoListChange(change.getReverseChange());
        } catch (IllegalValueException exception) {
            // undo should always work
            assert false;
            return false;
        }

        // move changes from change list to to undo change list
        toDoListUndoChanges.add(change);
        toDoListChanges.remove(toDoListChanges.size() - 1);

        return true;
    }

    /**
     * @see Model#redoToDoList()
     */
    public boolean redoToDoList() {
        // Check if there are any undos to redo
        if (toDoListUndoChanges.isEmpty()) {
            return false;
        }

        ToDoListChange change = toDoListUndoChanges.get(toDoListUndoChanges.size() - 1);

        try {
            applyToDoListChange(change);
        } catch (IllegalValueException exception) {
            // Redo should always work
            assert false;
            return false;
        }

        // move changes from undo change list to change list
        toDoListChanges.add(change);
        toDoListUndoChanges.remove(toDoListUndoChanges.size() - 1);

        return true;
    }

    /**
     * Tries to apply a change to the to-do list.
     * @throws IllegalValueException if there were duplicate to-dos added or
     *   there were non-existent to-dos deleted.
     */
    private void applyToDoListChange(ToDoListChange change) throws IllegalValueException {
        lastToDoListChange = change;

        toDoList.remove(change.getDeletedToDos());

        try {
            toDoList.add(change.getAddedToDos());
        } catch (IllegalValueException exception) {
            // there were duplicate to-dos
            // revert removal of to-dos
            toDoList.add(change.getDeletedToDos());
            lastToDoListChange = null;
            throw exception;
        }
    }

    /**
     * Gets the last successful change to the to-do list, considering undos and redos
     *
     * @return an optional of the last change, empty if there was none
     */
    public Optional<ToDoListChange> getLastToDoListChange() {
        return Optional.ofNullable(lastToDoListChange);
    }
}
