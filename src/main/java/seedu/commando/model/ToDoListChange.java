package seedu.commando.model;

import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ToDoList;

import java.util.*;

/**
 * An immutable representation of a change in a to-do list
 * Input to-do lists are copied
 */
public class ToDoListChange {

    private final ToDoList addedToDos;
    private final ToDoList deletedToDos;

    public ToDoListChange(ReadOnlyToDoList addedToDos, ReadOnlyToDoList deletedToDos) {
        this.addedToDos = new ToDoList(addedToDos);
        this.deletedToDos = new ToDoList(deletedToDos);
    }

    public ReadOnlyToDoList getAddedToDos() {
        return addedToDos;
    }

    public ReadOnlyToDoList getDeletedToDos() {
        return deletedToDos;
    }

    /**
     * Get the reverse of this change
        */
    public ToDoListChange getReverseChange() {
        return new ToDoListChange(deletedToDos, addedToDos);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ToDoListChange // instanceof handles nulls
            && addedToDos.equals(((ToDoListChange) other).addedToDos)
            && deletedToDos.equals(((ToDoListChange) other).deletedToDos));
    }

    @Override
    public int hashCode() {
        return Objects.hash(addedToDos, deletedToDos);
    }

    @Override
    public String toString(){
        return "Added " + addedToDos + " & deleted " + deletedToDos;
    }
}
