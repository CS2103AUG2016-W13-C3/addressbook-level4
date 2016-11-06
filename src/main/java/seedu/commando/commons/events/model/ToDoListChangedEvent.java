package seedu.commando.commons.events.model;

import seedu.commando.commons.events.BaseEvent;
import seedu.commando.model.todo.ReadOnlyToDoList;

/**
 * Indicates to-do list in the model has changed
 */
public class ToDoListChangedEvent extends BaseEvent {

    public final ReadOnlyToDoList toDoList;

    public ToDoListChangedEvent(ReadOnlyToDoList toDoList){
        this.toDoList = toDoList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}