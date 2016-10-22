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
    }

    /**
     * Copy constructor
     */
    public ToDoList(ReadOnlyToDoList listToBeCopied) {
        reset(listToBeCopied.getToDos());
    }

    //================================================================================
    // List operations
    //================================================================================

    /**
     * @see #add(ReadOnlyToDoList)
     */
    public ToDoList add(ReadOnlyToDo toDo) throws IllegalValueException {
        if (contains(toDo)) {
            throw new IllegalValueException(Messages.TODO_ALREADY_EXISTS);
        }

        list.add(toDo);

        return this;
    }
    /**
     * @see #remove(ReadOnlyToDoList)
     */
    public ToDoList remove(ReadOnlyToDo toDo) throws IllegalValueException {
        if (!contains(toDo)) {
            throw new IllegalValueException(Messages.TODO_NOT_FOUND);
        }

        list.remove(toDo);

        return this;
    }

    /**
     * Add all to-dos in {@param toDoList}
     * @throws IllegalValueException if any to-do in {@param toDoList} was not found for deletion
     */
    public ToDoList add(ReadOnlyToDoList toDoList) throws IllegalValueException {
        assert toDoList != null;

        for (ReadOnlyToDo toDo : toDoList.getToDos()) {
            if (contains(toDo)) {
                throw new IllegalValueException(Messages.TODO_ALREADY_EXISTS);
            }
        }

        list.addAll(toDoList.getToDos());

        return this;
    }

    /**
     * Removes all to-dos in {@param toDoList}
     * @throws IllegalValueException if any to-do in {@param toDoList} was not found for deletion
     */
    public ToDoList remove(ReadOnlyToDoList toDoList) throws IllegalValueException {
        assert toDoList != null;

        for (ReadOnlyToDo toDoToRemove : toDoList.getToDos()) {
            if (!contains(toDoToRemove)) {
                throw new IllegalValueException(Messages.TODO_NOT_FOUND);
            }
        }

        list.removeAll(toDoList.getToDos());

        return this;
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

    /**
     *  Checks if the list contains a to-do that is similar to the given
     *  @see ReadOnlyToDo#isSimilar(ReadOnlyToDo)
     */
    @Override
    public boolean contains(ReadOnlyToDo toDo) {
        return list.filtered(x -> x.isSimilar(toDo)).size() > 0;
    }

    /**
     *  Checks if to do list is is similar to the given {@param toDoList}
     *  @see ReadOnlyToDo#isSimilar(ReadOnlyToDo)
     */
    @Override
    public boolean isSimilar(ReadOnlyToDoList toDoList) {
        return list.size() == toDoList.getToDos().size()
            && list.filtered(toDoList::contains).size() == list.size();
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
