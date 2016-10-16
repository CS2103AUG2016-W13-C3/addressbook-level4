package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.List;
import java.util.Optional;

/**
 * Represents a command with hidden internal logic and the ability to be executed
 * All commands should only run its validation of fields and logic and throw errors when executed
 */
public abstract class Command {
    /**
     * Executes the command
     * @return result of the command
     */
    public abstract CommandResult execute(EventsCenter eventsCenter, UiLogic uiLogic, Model model)
        throws IllegalValueException;
}
