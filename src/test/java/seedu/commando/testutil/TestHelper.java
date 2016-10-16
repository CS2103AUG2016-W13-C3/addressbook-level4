package seedu.commando.testutil;

import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.events.ui.ShowHelpRequestEvent;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Collection of convenience methods for testing
 */
public class TestHelper {

    public static boolean wasToDoListChangedEventPosted(EventsCollector eventsCollector) {
        return eventsCollector.hasCollectedEvent(ToDoListChangedEvent.class);
    }

    public static boolean wasShowHelpRequestEventPosted(EventsCollector eventsCollector) {
        return eventsCollector.hasCollectedEvent(ShowHelpRequestEvent.class);
    }

    /**
     * Checks if a to-do exists in the model
     */
    public static boolean ifToDoExists(Model model, ReadOnlyToDo readOnlyToDo) {
        return model.getToDoList().getToDos().contains(readOnlyToDo);
    }

    /**
     * Checks if a to-do exists in the filtered list of model
     */
    public static boolean ifToDoExistsFiltered(Model model, ReadOnlyToDo readOnlyToDo) {
        return model.getFilteredToDoList().contains(readOnlyToDo);
    }
}