package seedu.commando.model;

import java.util.List;

import seedu.commando.model.todo.ReadOnlyToDo;

/**
 * Unmodifiable view of a to-do list
 */
public interface ReadOnlyToDoList {
    List<ReadOnlyToDo> getToDoList();
}
