package seedu.commando.model;

import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ToDo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An immutable representation of a change in a to-do list
 * Guaranteed: To-dos are unique in {@link #getAddedToDos()} and {@link #getDeletedToDos()}
 */
public class ToDoListChange {

    private Set<ReadOnlyToDo> addedToDos;
    private Set<ReadOnlyToDo> deletedToDos;

    public ToDoListChange(Collection<ReadOnlyToDo> addedToDos, Collection<ReadOnlyToDo> deletedToDos) {
        this.addedToDos = addedToDos.stream().map(ToDo::new).collect(Collectors.toSet());
        this.deletedToDos = deletedToDos.stream().map(ToDo::new).collect(Collectors.toSet());
    }

    public List<ReadOnlyToDo> getAddedToDos() {
        return new LinkedList<>(addedToDos);
    }

    public List<ReadOnlyToDo> getDeletedToDos() {
        return new LinkedList<>(deletedToDos);
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
