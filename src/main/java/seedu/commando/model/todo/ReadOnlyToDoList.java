package seedu.commando.model.todo;

import seedu.commando.commons.core.UnmodifiableObservableList;

import java.util.stream.Collectors;

/**
 * Unmodifiable view of a to-do list
 */
public interface ReadOnlyToDoList {
    UnmodifiableObservableList<ReadOnlyToDo> getToDos();

    /** Checks if the list contains the given to-do */
    boolean contains(ReadOnlyToDo toDo);

    /**
     * Updates and returns its value, based on the current value of its fields
     */
    default String getText() {
        return "[" + getToDos().stream().map(ReadOnlyToDo::toString).collect(Collectors.joining(", ")) + "]";
    }
}
