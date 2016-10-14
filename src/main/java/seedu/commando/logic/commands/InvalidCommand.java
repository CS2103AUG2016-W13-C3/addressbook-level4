package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an invalid command that stores the error that made it invalid
 * Returns a CommandResult that has an error
 */
public class InvalidCommand extends Command {

    private String error;

    /**
     * Asserts error to be non-null
     */
    public InvalidCommand(String error) {
        assert error != null;

        this.error = error;
    }

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        return new CommandResult(error, true);
    }

}
