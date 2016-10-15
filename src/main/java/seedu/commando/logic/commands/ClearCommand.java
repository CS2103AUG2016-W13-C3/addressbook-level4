package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clears the to-do list
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";

    public ClearCommand() {}

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;

        try {

            // Delete all to-dos
            model.changeToDoList(new ToDoListChange(
                Collections.emptyList(),
                toDoAtIndices
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(Messages.MESSAGE_TODO_LIST_CLEARED);
    }
}
