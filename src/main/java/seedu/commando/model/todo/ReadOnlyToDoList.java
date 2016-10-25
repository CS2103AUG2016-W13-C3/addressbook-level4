package seedu.commando.model.todo;

import seedu.commando.commons.core.UnmodifiableObservableList;

import java.util.stream.Collectors;

//@@author A0139697H
/**
 * Unmodifiable view of a to-do list
 */
public interface ReadOnlyToDoList {
    UnmodifiableObservableList<ReadOnlyToDo> getToDos();

    /**
     *  Checks if the list contains a to-do that is considered as similar
     *  as the given to-do
     */
    boolean contains(ReadOnlyToDo toDo);

    /**
     *  Checks if the list is considered similar as the given to-do list
     */
    boolean isSimilar(ReadOnlyToDoList toDoList);

    /**
     * Updates and returns its value, based on the current value of its fields
     */
    default String getText() {
        return "[" + getToDos().stream().map(ReadOnlyToDo::toString).collect(Collectors.joining(", ")) + "]";
    }
}
