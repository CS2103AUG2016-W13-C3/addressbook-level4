package seedu.commando.model.todo;

import java.util.Objects;

//@@author A0139697H
/**
 * An immutable representation of a change in {@link ReadOnlyToDoList}
 */
public class ToDoListChange {
    private final ReadOnlyToDoList addedToDos;
    private final ReadOnlyToDoList deletedToDos;

    /**
     * Constructor for a to-do list change.
     *
     * @param addedToDos   list of to-dos to be added, which is deep copied
     * @param deletedToDos list of to-dos to be deleted, which is deep copied
     */
    public ToDoListChange(ReadOnlyToDoList addedToDos, ReadOnlyToDoList deletedToDos) {
        this.addedToDos = new ToDoList(addedToDos);
        this.deletedToDos = new ToDoList(deletedToDos);
    }

    /**
     * Gets a read-only list of read-only to-dos to be added in this change
     *
     * @return list of to-dos to add
     */
    public ReadOnlyToDoList getAddedToDos() {
        return addedToDos;
    }

    /**
     * Gets a read-only list of read-only to-dos to be deleted in this change
     *
     * @return list of to-dos to delete
     */
    public ReadOnlyToDoList getDeletedToDos() {
        return deletedToDos;
    }

    /**
     * Gets the reverse of the current change.
     *
     * @return reverse of this change
     */
    public ToDoListChange getReverseChange() {
        return new ToDoListChange(deletedToDos, addedToDos);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        // instanceof handles nulls
        return other == this
            || (other instanceof ToDoListChange
            && addedToDos.equals(((ToDoListChange) other).addedToDos)
            && deletedToDos.equals(((ToDoListChange) other).deletedToDos));
    }

    @Override
    public int hashCode() {
        return Objects.hash(addedToDos, deletedToDos);
    }

    @Override
    public String toString() {
        return "To add " + addedToDos + ",a to delete " + deletedToDos;
    }
}
