package seedu.commando.model.todo;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

//@@author A0122001M

/**
 * Represents a list of to-dos.
 */
public class ToDoList implements ReadOnlyToDoList {
    private final ObservableList<ReadOnlyToDo> list;
    private final UnmodifiableObservableList<ReadOnlyToDo> protectedList;
    {
        // Initializes an observable list to store to-dos, which
        // calls its listeners when any of its to-dos change
        list = FXCollections.observableArrayList(toDo -> new Observable[]{
            toDo.getObservableValue()
        });

        // Initializes a read-only wrapper around the list of to-dos
        protectedList = new UnmodifiableObservableList<>(list);
    }

    /**
     * Initializes an empty to-do list.
     */
    public ToDoList() {
    }

    /**
     * Copy constructor
     */
    public ToDoList(ReadOnlyToDoList listToBeCopied) {
        reset(listToBeCopied.getToDos());
    }

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
     * Add all to-dos in {@param toDoList}. Duplicate to-dos are not allowed, based on
     * {@link ReadOnlyToDo#isSimilar(ReadOnlyToDo)}
     *
     * @param toDoList all to-dos to add to the to-do list
     * @throws IllegalValueException if any to-do in {@param toDoList} already exists
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
     * Removes every to-do in {@param toDoList} that is considered {@link ReadOnlyToDo#isSimilar(ReadOnlyToDo)}
     * to any to-do in the to-do list.
     *
     * @param toDoList all to-dos to delete from the to-do list
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
     * Clears the list and sets it to a deep copy of a new to-do list
     *
     * @param newToDos the new to-do list to reset to
     */
    public void reset(List<ReadOnlyToDo> newToDos) {
        List<ToDo> toDos = new LinkedList<>();
        newToDos.forEach(toDo -> toDos.add(new ToDo(toDo)));
        list.setAll(toDos);
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyToDo> getToDos() {
        return protectedList;
    }

    @Override
    public boolean contains(ReadOnlyToDo toDo) {
        return list.filtered(x -> x.isSimilar(toDo)).size() > 0;
    }

    @Override
    public boolean isSimilar(ReadOnlyToDoList toDoList) {
        return list.size() == toDoList.getToDos().size()
            && list.filtered(toDoList::contains).size() == list.size();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        // instanceof handles nulls
        return other == this
            || (other instanceof ToDoList
            && list.equals(((ToDoList) other).list));
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
