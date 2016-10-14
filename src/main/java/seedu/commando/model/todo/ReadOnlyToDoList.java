package seedu.commando.model.todo;

import java.util.List;

import seedu.commando.model.todo.ReadOnlyToDo;

/**
 * Unmodifiable view of a to-do list
 */
public interface ReadOnlyToDoList {
    List<ReadOnlyToDo> getToDoList();
}
