package seedu.address.testutil;

import seedu.address.commons.events.model.ToDoListChangedEvent;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.model.Model;
import seedu.address.model.todo.ReadOnlyToDo;

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
        return model.getToDoList().getToDoList().contains(readOnlyToDo);
    }

    /**
     * Checks if a to-do exists in the filtered list of model
     */
    public static boolean ifToDoExistsFiltered(Model model, ReadOnlyToDo readOnlyToDo) {
        return model.getFilteredToDoList().contains(readOnlyToDo);
    }
}