package seedu.commando.model;

import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ToDo;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An immutable representation of a change in a to-do list
 */
public class ToDoListChange {

    private List<ReadOnlyToDo> addedToDos;
    private List<ReadOnlyToDo> deletedToDos;

    public ToDoListChange(List<ReadOnlyToDo> addedToDos, List<ReadOnlyToDo> deletedToDos) {
        this.addedToDos = addedToDos.stream().map(ToDo::new).collect(Collectors.toList());
        this.deletedToDos = deletedToDos.stream().map(ToDo::new).collect(Collectors.toList());
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
        return "Adds " + addedToDos + " & Deletes " + deletedToDos;
    }
}
