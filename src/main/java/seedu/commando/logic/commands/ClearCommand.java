package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.todo.ToDoList;

//@@author A0139697H
/**
 * Clears the to-do list
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";

    public ClearCommand() {}

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        try {
            // Delete all to-dos
            model.changeToDoList(new ToDoListChange(
                new ToDoList(),
                model.getToDoList()
            ));

            return new CommandResult(Messages.CLEAR_COMMAND);
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }
    }
}
