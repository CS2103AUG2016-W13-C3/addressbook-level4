package seedu.commando.commons.events.ui;

import seedu.commando.commons.events.BaseEvent;
import seedu.commando.logic.commands.Command;

/**
 * Indicates an attempt to execute an incorrect command
 */
public class IncorrectCommandAttemptedEvent extends BaseEvent {

    private final String command;

    public IncorrectCommandAttemptedEvent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " with command: " + command;
    }
}
