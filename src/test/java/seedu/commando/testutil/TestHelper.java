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
        return logic.getToDos().contains(readOnlyToDo);
    }

    /**
     * Checks if a to-do appears on the UI
     */
    public static boolean ifToDoExistsFiltered(Logic logic, ReadOnlyToDo readOnlyToDo) {
        return logic.getUiEventsToday().filtered(uiToDo -> uiToDo.isSameStateAs(readOnlyToDo)).size() > 0
            || logic.getUiEventsUpcoming().filtered(uiToDo -> uiToDo.isSameStateAs(readOnlyToDo)).size() > 0
            || logic.getUiTasks().filtered(uiToDo -> uiToDo.isSameStateAs(readOnlyToDo)).size() > 0;
    }
}