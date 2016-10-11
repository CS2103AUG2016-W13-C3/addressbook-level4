package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.ToDoList;
import seedu.address.model.todo.ReadOnlyToDo;

import java.util.List;

/**
 * Clears the to-do list
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";

    public ClearCommand() {}

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;

        model.resetData(new ToDoList());
        return new CommandResult(Messages.MESSAGE_TODO_LIST_CLEARED);
    }
}
