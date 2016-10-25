package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;

//@@author A0122001M

/**
 *  Redo the last undo action
 */
public class RedoCommand extends Command {
    
    public static final String COMMAND_WORD = "redo";

    @Override
    public CommandResult  execute() throws NoModelException {
        Model model = getModel();
        
        // Checks if such redo action can be executed
        if (model.redoToDoList()) {
            return new CommandResult(Messages.REDO_COMMAND);
        } else {
            return new CommandResult(Messages.REDO_COMMAND_FAIL, true);
        }
    }

}
