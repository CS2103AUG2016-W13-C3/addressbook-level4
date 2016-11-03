package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;

//@@author A0122001M
/**
 * Undo the last `add`, `delete`, `edit`, `finish`, `unfinish`, `import` command.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        //check if there is undo action taken
        if (model.undoToDoList()) {
            return new CommandResult(Messages.UNDO_COMMAND);
        } else {
            return new CommandResult(Messages.UNDO_COMMAND_FAIL, true);
        }
    }
}
