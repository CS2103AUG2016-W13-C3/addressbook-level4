package seedu.commando.commons.events.logic;

import seedu.commando.commons.events.BaseEvent;
import seedu.commando.model.todo.ReadOnlyToDoList;

/**
 * An event requesting to change the to-do list file path
 */

public class ToDoListFilePathChangeRequestEvent extends BaseEvent {

    public final String path;

    public ToDoListFilePathChangeRequestEvent(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Change the default to-do list file path to "+ path;
    }
}
