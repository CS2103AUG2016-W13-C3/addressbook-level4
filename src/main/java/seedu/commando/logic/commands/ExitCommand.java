package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.logic.ExitAppRequestEvent;

//@@author A0139697H
/**
 * Terminates the program
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public ExitCommand() {}

    @Override
    public CommandResult execute() throws NoEventsCenterException {
        EventsCenter eventsCenter = getEventsCenter();

        eventsCenter.post(new ExitAppRequestEvent());
        return new CommandResult(Messages.EXIT_APPLICATION);
    }
}
