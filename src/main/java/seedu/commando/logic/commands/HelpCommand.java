package seedu.commando.logic.commands;


import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.logic.ShowHelpRequestEvent;

import java.util.Optional;

//@@author A0139697H

/**
 * Show the user guide in a new window, jumping to the appropriate
 * section of the user guide if necessary.
 */
public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";

    private final String commandWord;

    /**
     * Shows general help.
     */
    public HelpCommand() {
        commandWord = "";
    }

    /**
     * Shows help for specific command word {@param commandWord}.
     */
    public HelpCommand(String commandWord) {
        this.commandWord = commandWord;
    }

    @Override
    public CommandResult execute() throws NoEventsCenterException {
        EventsCenter eventsCenter = getEventsCenter();

        if (commandWord.isEmpty()) {
            eventsCenter.post(new ShowHelpRequestEvent(""));
        } else {
            Optional<String> anchor = Config.getUserGuideAnchorForCommandWord(commandWord);

            // Check if the command word is recognized
            if (!anchor.isPresent()) {
                return new CommandResult(Messages.HELP_COMMAND_INVALID_TOPIC, true);
            }

            eventsCenter.post(new ShowHelpRequestEvent(anchor.get()));
        }

        return new CommandResult(String.format(Messages.HELP_COMMAND, commandWord));
    }
}
