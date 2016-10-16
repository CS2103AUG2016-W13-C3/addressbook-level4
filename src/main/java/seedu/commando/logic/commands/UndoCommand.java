package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;

/**
 * Undo the last command
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    @Override
    public CommandResult execute() throws NoContextException {
        Model model = getModel();

        //check if there is undo action taken
        if (model.undoToDoList()) {
            return new CommandResult(Messages.UNDID_COMMAND);
        } else {
            return new CommandResult(Messages.UNDID_COMMAND_FAIL, true);
        }
    }
    

}
