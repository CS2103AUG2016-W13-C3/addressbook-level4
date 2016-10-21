package seedu.commando.model.todo;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.util.*;
import java.util.stream.Collector;
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

    public ToDoList() {
        reset(new ArrayList<ReadOnlyToDo>());
    }

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
     * @see #add(List<ReadOnlyToDo>)
     */
    public void add(ReadOnlyToDo toDo) throws IllegalValueException {
        add(Collections.singletonList(toDo));
    }
    /**
     * @see #remove(List<ReadOnlyToDo>)
     */
    public void remove(ReadOnlyToDo toDo) {
        remove(Collections.singletonList(toDo));
    }


    /**
     * Adds deep copies of each of the to-dos in {@param toDos}
     * @throws IllegalValueException if there are duplicate to-dos
     */
    public void add(List<ReadOnlyToDo> toDos) throws IllegalValueException {
        assert toDos != null;

        // Ensure there are no duplicate to-dos
        if (toDos.stream().collect(Collectors.toSet()).size() < toDos.size()) {
            throw new IllegalValueException(Messages.TODO_ALREADY_EXISTS);
        }

        for (ReadOnlyToDo toDo : toDos) {
            if (list.contains(toDo)) {
                throw new IllegalValueException(Messages.TODO_ALREADY_EXISTS);
            }
        }

        list.addAll(toDos);
    }

    /**
     * Removes all to-dos that correspond to any of {@param toDos}
     */
    public void remove(List<ReadOnlyToDo> toDos) {
        assert toDos != null;

        list.removeAll(toDos);
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
    public boolean contains(ReadOnlyToDo toDo) {
        return list.contains(toDo);
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
