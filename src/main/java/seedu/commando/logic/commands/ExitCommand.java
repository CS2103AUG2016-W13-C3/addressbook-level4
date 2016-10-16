package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.ui.ExitAppRequestEvent;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.List;

/**
 * Terminates the program
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public ExitCommand() {}

    @Override
    public CommandResult execute() throws NoContextException {
        EventsCenter eventsCenter = getEventsCenter();

        eventsCenter.post(new ExitAppRequestEvent());
        return new CommandResult(Messages.EXIT_APPLICATION);
    }
}
