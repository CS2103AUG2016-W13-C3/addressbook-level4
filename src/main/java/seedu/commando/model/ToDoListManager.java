package seedu.commando.model;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;

import java.util.*;

/**
 * Manages the to-do list provided (not copied)
 * Supports adding, removing, editing, undoing and redoing
 * based on {@link ToDoListChange}
 */
public class ToDoListManager {
    private final ToDoList toDoList;
    private final ArrayList<ToDoListChange> toDoListChanges; // changes that can be undone
    private final ArrayList<ToDoListChange> toDoListUndoChanges; // changes that can be redone
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
        toDoListChanges.add(change);
        try {
            applyToDoListChange(change);
        } catch (IllegalValueException exception) {
            // unable to apply the change
            // revert change
            toDoListChanges.remove(toDoListChanges.size() - 1);
            throw exception;
        }

        // Reset undo list
        toDoListUndoChanges.clear();
    }

    private void applyToDoListChange(ToDoListChange change) throws IllegalValueException {
        toDoList.remove(change.getDeletedToDos());

        try {
            toDoList.add(change.getAddedToDos());
        } catch (IllegalValueException exception) {
            // there were duplicate to-dos
            // revert removal of to-dos
            toDoList.add(change.getDeletedToDos());
            throw exception;
        }
    }

    public boolean undoToDoList() {
        if (toDoListChanges.isEmpty()) {
            return false; // Nothing else to undo
        }

        ToDoListChange change = toDoListChanges.get(toDoListChanges.size() - 1);

        // move changes to undo list
        toDoListUndoChanges.add(change);
        toDoListChanges.remove(toDoListChanges.size() - 1);

        try {
            applyToDoListChange(change.getReverseChange());
        } catch (IllegalValueException exception) {
            assert false; // Undo should always work
            return false; // Undo failed
        }

        return true;
    }

    public boolean redoToDoList() {
        if (toDoListUndoChanges.isEmpty()) {
            return false; // No undos to redo
        }

        ToDoListChange change = toDoListUndoChanges.get(toDoListUndoChanges.size() - 1);

        // move changes from undo list
        toDoListChanges.add(change);
        toDoListUndoChanges.remove(toDoListUndoChanges.size() - 1);

        try {
            applyToDoListChange(change);
        } catch (IllegalValueException exception) {
            assert false; // Redo should always work
            return false; // Redo failed
        }

        return true;
    }

    /**
     * Gets the last to-do list change
     * If a change is undone, it would not be considered "the last"
     */
    public Optional<ToDoListChange> getLastToDoListChange() {
        return toDoListChanges.isEmpty() ? Optional.empty() :
            Optional.of(toDoListChanges.get(toDoListChanges.size() - 1));
    }
}
