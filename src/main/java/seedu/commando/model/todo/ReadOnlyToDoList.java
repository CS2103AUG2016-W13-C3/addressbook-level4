package seedu.commando.model.todo;

import seedu.commando.commons.core.UnmodifiableObservableList;

import java.util.stream.Collectors;

//@@author A0139697H
/**
 * Unmodifiable view of a to-do list.
 */
public interface ReadOnlyToDoList {
    UnmodifiableObservableList<ReadOnlyToDo> getToDos();

    /**
     *  Checks if the list contains a to-do that is considered as similar
     *    as the given to-do. See {@link ReadOnlyToDo#isSimilar(ReadOnlyToDo)}.
     */
    boolean contains(ReadOnlyToDo toDo);

    /**
     *  Checks if the list is considered similar as the given to-do list,
     *    which means both their to-dos must be considered similar.
     *    See {@link ReadOnlyToDo#isSimilar(ReadOnlyToDo)}.
     */
    boolean isSimilar(ReadOnlyToDoList toDoList);

    /**
     * @return a complete textual representation of the to-do list as a string
     */
    default String getText() {
        return "[" + getToDos().stream().map(ReadOnlyToDo::toString).collect(Collectors.joining(", ")) + "]";
    }
}
