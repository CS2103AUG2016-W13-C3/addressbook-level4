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
        applyToDoListChange(change);
        toDoListChanges.add(change);

        // Reset undo list
        toDoListUndoChanges.clear();
    }

    private void applyToDoListChange(ToDoListChange change) throws IllegalValueException {
        toDoList.remove(change.getDeletedToDos());
        toDoList.add(change.getAddedToDos());
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
     * Gets the last to-do list change
     * If a change is undone, it would not be considered "the last"
     */
    public Optional<ToDoListChange> getLastToDoListChange() {
        return toDoListChanges.isEmpty() ? Optional.empty() :
            Optional.of(toDoListChanges.get(toDoListChanges.size() - 1));
    }
}
