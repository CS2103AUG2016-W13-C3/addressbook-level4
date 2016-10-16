package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;

public class RedoCommand extends Command {
    
    public static final String COMMAND_WORD = "redo";

    @Override
    public CommandResult  execute() throws NoContextException {
        Model model = getModel();
        
        if (model.redoToDoList()) {
            return new CommandResult(Messages.REDID_COMMAND);
        } else {
            return new CommandResult(Messages.REDID_COMMAND_FAIL, true);
        }
    }

}
