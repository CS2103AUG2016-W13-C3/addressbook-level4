package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;

import java.util.Collections;

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
                Collections.emptyList(),
                model.getToDoList().getToDos()
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(Messages.TODO_LIST_CLEARED);
    }
}
