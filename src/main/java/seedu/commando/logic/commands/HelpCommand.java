package seedu.commando.logic.commands;


import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.logic.ShowHelpRequestEvent;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.util.Optional;

//@@author A0139697H
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
    public CommandResult execute() throws NoEventsCenterException {
        EventsCenter eventsCenter = getEventsCenter();

        if (commandWord.isEmpty()) {
            eventsCenter.post(new ShowHelpRequestEvent(""));
        } else {
            Optional<String> anchor = Config.getUserGuideAnchorForCommandWord(commandWord);

            // If the command word is recognized
            if (anchor.isPresent()) {
                eventsCenter.post(new ShowHelpRequestEvent(anchor.get()));
            } else {
                return new CommandResult(String.format(Messages.UNKNOWN_COMMAND_FOR_HELP, commandWord), true);
            }
        }

        return new CommandResult(Messages.HELP_WINDOW_SHOWN);
    }
}
