package seedu.commando.model.todo;

import seedu.commando.commons.core.UnmodifiableObservableList;

import java.util.stream.Collectors;

//@@author A0139697H

/**
 * Unmodifiable view of a to-do list.
 */
public interface ReadOnlyToDoList {

    /**
     * Gets a read-only list of read-only to-dos it contains.
     *
     * @return an observable read-only list of read-only to-dos it contains
     */
    UnmodifiableObservableList<ReadOnlyToDo> getToDos();

    /**
     * Checks if the list contains a to-do that is considered as similar
     * as the given to-do.
     * See {@link ReadOnlyToDo#isSimilar(ReadOnlyToDo)}.
     *
     * @param toDo a to-do to check
     * @return whether {@param toDo} exists in the list
     */
    boolean contains(ReadOnlyToDo toDo);

    /**
     * Checks if the list is considered similar as the given to-do list,
     * which means both their to-dos must be considered similar.
     * See {@link ReadOnlyToDo#isSimilar(ReadOnlyToDo)}.
     *
     * @param toDoList a to-do list to check
     * @return whether this to-do list is considered similar to {@param toDoList}
     */
    boolean isSimilar(ReadOnlyToDoList toDoList);

    /**
     * Returns a complete textual representation of the to-do list as a string, displaying
     * all to-dos.
     *
     * @return a string that represents the to-do list
     */
    default String getText() {
        return "[" + getToDos().stream().map(ReadOnlyToDo::toString).collect(Collectors.joining(", ")) + "]";
    }
}
