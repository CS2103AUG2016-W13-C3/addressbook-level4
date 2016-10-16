package seedu.commando.logic.commands;


import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.ui.ShowHelpRequestEvent;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    private final String commandWord;

    /**
     * Shows general help
     */
    public HelpCommand() {
        commandWord = "";
    }

    /**
     * Shows help for specific command word {@param commandWord}
     */
    public HelpCommand(String commandWord) {
        this.commandWord = commandWord;
    }

    @Override
    public CommandResult execute() throws NoContextException {
        EventsCenter eventsCenter = getEventsCenter();

        eventsCenter.post(new ShowHelpRequestEvent(commandWord));
        return new CommandResult(Messages.HELP_WINDOW_SHOWN);
    }
}
