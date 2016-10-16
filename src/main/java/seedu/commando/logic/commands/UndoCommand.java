package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;

/**
 * Undo the last command
 */
public class UndoCommand extends Command{

    public static final String COMMAND_WORD = "undo";

    public UndoCommand(){ }
    
    @Override
    public CommandResult execute(EventsCenter eventsCenter, UiLogic uiLogic, Model model) {
        assert model != null;
        
        if (model.undoToDoList()) {
            return new CommandResult(Messages.UNDID_COMMAND);
        } else {
            return new CommandResult(Messages.UNDID_COMMAND_FAIL, true);
        }
    }
    

}
