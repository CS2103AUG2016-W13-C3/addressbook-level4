package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoList;
import seedu.commando.model.todo.ReadOnlyToDo;

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
