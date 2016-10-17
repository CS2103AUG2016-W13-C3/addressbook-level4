package seedu.commando.testutil;

import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.events.ui.ShowHelpRequestEvent;
import seedu.commando.logic.Logic;
import seedu.commando.model.todo.ReadOnlyToDo;

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
    public static boolean ifToDoExists(Logic logic, ReadOnlyToDo readOnlyToDo) {
        return logic.getUiEventList().filtered(event -> event.isSameStateAs(readOnlyToDo)).size() > 0
            || logic.getUiTaskList().filtered(task -> task.isSameStateAs(readOnlyToDo)).size() > 0;

    }

    /**
     * Checks if a to-do exists in the filtered list of model
     */
    public static boolean ifToDoExistsFiltered(Logic logic, ReadOnlyToDo readOnlyToDo) {
        return logic.getUiEventList().filtered(uiToDo -> uiToDo.isSameStateAs(readOnlyToDo)).size() > 0
            || logic.getUiTaskList().filtered(uiToDo -> uiToDo.isSameStateAs(readOnlyToDo)).size() > 0;
    }
}