package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;

public class RedoCommand extends Command {
    
    public static final String COMMAND_WORD = "redo";
    
    public RedoCommand(){
        
    }

    @Override
    public CommandResult  execute(EventsCenter eventsCenter, UiLogic uiLogic, Model model) {
        assert model != null;
        
        if (model.redoToDoList()) {
            return new CommandResult(Messages.REDID_COMMAND);
        } else {
            return new CommandResult(Messages.REDID_COMMAND_FAIL, true);
        }
    }

}
