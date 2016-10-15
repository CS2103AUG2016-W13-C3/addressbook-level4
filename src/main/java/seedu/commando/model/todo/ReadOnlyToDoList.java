package seedu.commando.model.todo;

import java.util.List;

/**
 * Unmodifiable view of a to-do list
 */
public interface ReadOnlyToDoList {
    List<ReadOnlyToDo> getToDos();
}
