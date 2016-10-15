package seedu.commando.model.todo;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a list of to-dos
 */
public class ToDoList implements ReadOnlyToDoList {

    private final ObservableList<ReadOnlyToDo> list;
    private final UnmodifiableObservableList<ReadOnlyToDo> protectedList;
    {
        list = FXCollections.observableArrayList(toDo -> new Observable[] {
            toDo.getObservableValue() // track value of to-do as well
        });
        protectedList = new UnmodifiableObservableList<>(list);
    }

    public ToDoList() {}

    /**
     * Copy constructor
     */
    public ToDoList(ReadOnlyToDoList listToBeCopied) {
        reset(listToBeCopied.getToDos());
    }

    public ToDoList(List<ReadOnlyToDo> listToBeCopied) {
        reset(listToBeCopied);
    }

    //================================================================================
    // List operations
    //================================================================================

    /**
     * Adds a deep copy of the to-do to the list
     */
    public void add(ReadOnlyToDo toDo) {
        assert toDo != null;

        list.add(new ToDo(toDo));
    }

    /**
     * Removes the to-do that is equals to {@param toDo}
     * @throws IllegalValueException if not found
     */
    public boolean remove(ReadOnlyToDo toDo) throws IllegalValueException {
        assert toDo != null;

        if (list.remove(toDo)) {
            return true;
        } else {
            throw new IllegalValueException(String.format(Messages.MESSAGE_TODO_NOT_FOUND, toDo.toString()));
        }
    }

    /**
     * Clears the list and sets it to a deep copy of {@param newToDos}
     */
    public void reset(List<ReadOnlyToDo> newToDos) {
        List<ToDo> toDos = new LinkedList<>();
        newToDos.forEach(toDo -> toDos.add(new ToDo(toDo)));
        list.setAll(toDos);
    }

    //================================================================================
    // Utility methods
    //================================================================================

    @Override
    public String toString() {
        return getText();
    }

    public UnmodifiableObservableList<ReadOnlyToDo> getToDos() {
        return protectedList;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ToDoList // instanceof handles nulls
                && list.equals(((ToDoList) other).list));
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

}
