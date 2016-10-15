package seedu.commando.model.todo;

import seedu.commando.commons.core.UnmodifiableObservableList;

/**
 * Unmodifiable view of a to-do list
 */
public interface ReadOnlyToDoList {
    UnmodifiableObservableList<ReadOnlyToDo> getToDos();
}
