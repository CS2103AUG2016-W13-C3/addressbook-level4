package seedu.commando.model;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;

import java.util.*;

/**
 * Manages an internal to-do list provided
 * Supports adding, removing, editing, undoing and redoing
 * based on {@link ToDoListChange}
 */
public class ToDoListManager {
    private final ToDoList toDoList;
    private final ArrayList<ToDoListChange> toDoListChanges; // changes that can be undone
    private final ArrayList<ToDoListChange> toDoListUndoChanges; // changes that can be redone
    private ToDoListChange lastToDoListChange;
    {
        toDoListChanges = new ArrayList<>();
        toDoListUndoChanges = new ArrayList<>();
    }

    /**
     * Initializes with the given to-do list
     * To-do list is copied
     */
    public ToDoListManager(ReadOnlyToDoList toDoList) {
        assert toDoList != null;

        this.toDoList = new ToDoList(toDoList);
    }

    public ToDoList getToDoList() {
        return toDoList;
    }

    public void changeToDoList(ToDoListChange change) throws IllegalValueException {
        applyToDoListChange(change);
        toDoListChanges.add(change);

        // Reset undo list
        toDoListUndoChanges.clear();
    }

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

    public boolean undoToDoList() {
        if (toDoListChanges.isEmpty()) {
            return false; // Nothing else to undo
        }

        ToDoListChange change = toDoListChanges.get(toDoListChanges.size() - 1);

        try {
            applyToDoListChange(change.getReverseChange());
        } catch (IllegalValueException exception) {
            assert false; // Undo should always work
            return false; // Undo failed
        }

        // move changes to undo list
        toDoListUndoChanges.add(change);
        toDoListChanges.remove(toDoListChanges.size() - 1);


        return true;
    }

    public boolean redoToDoList() {
        if (toDoListUndoChanges.isEmpty()) {
            return false; // No undos to redo
        }

        ToDoListChange change = toDoListUndoChanges.get(toDoListUndoChanges.size() - 1);

        try {
            applyToDoListChange(change);
        } catch (IllegalValueException exception) {
            assert false; // Redo should always work
            return false; // Redo failed
        }

        // move changes from undo list
        toDoListChanges.add(change);
        toDoListUndoChanges.remove(toDoListUndoChanges.size() - 1);

        return true;
    }

    /**
     * Latest change on the to-do list, including undo/redo
     */
    public Optional<ToDoListChange> getLastToDoListChange() {
        return Optional.ofNullable(lastToDoListChange);
    }
}
