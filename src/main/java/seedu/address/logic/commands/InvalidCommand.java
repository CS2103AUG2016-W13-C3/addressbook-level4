package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.todo.ReadOnlyToDo;

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
