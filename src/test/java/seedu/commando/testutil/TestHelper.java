package seedu.commando.testutil;

import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.events.logic.ShowHelpRequestEvent;
import seedu.commando.logic.Logic;
import seedu.commando.model.todo.ReadOnlyToDo;

//@@author A0139697H

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
    public static void assertToDoExists(Logic logic, ReadOnlyToDo readOnlyToDo) {
        assert logic.getToDoList().contains(readOnlyToDo);
    }

    /**
     * Checks if a to-do doesn't exist in the model
     */
    public static void assertToDoNotExists(Logic logic, ReadOnlyToDo readOnlyToDo) {
        assert !logic.getToDoList().contains(readOnlyToDo);
    }

    /**
     * Checks if a to-do appears on the UI
     */
    public static void assertToDoExistsFiltered(Logic logic, ReadOnlyToDo readOnlyToDo) {
        assert logic.getUiEvents().filtered(uiToDo -> uiToDo.isSimilar(readOnlyToDo)).size() > 0
            || logic.getUiTasks().filtered(uiToDo -> uiToDo.isSimilar(readOnlyToDo)).size() > 0;
    }

    /**
     * Checks if a to-do doesn't appear on the UI
     */
    public static void assertToDoNotExistsFiltered(Logic logic, ReadOnlyToDo readOnlyToDo) {
        assert logic.getUiEvents().filtered(uiToDo -> uiToDo.isSimilar(readOnlyToDo)).size() == 0
            && logic.getUiTasks().filtered(uiToDo -> uiToDo.isSimilar(readOnlyToDo)).size() == 0;
    }
}