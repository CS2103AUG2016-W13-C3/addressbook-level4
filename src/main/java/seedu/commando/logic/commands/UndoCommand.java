package seedu.commando.logic.commands;

import java.util.List;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

/**
 * Undo the last command
 */
public class UndoCommand extends Command{

    public static final String COMMAND_WORD = "undo";

    public UndoCommand(){
        
    }
    
    /**
     * Asserts that {@code eventsCenter} and {@code model} are non-null
     */
    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;
        assert eventsCenter != null;
        
        //check if there is undo action taken
        if (model.undoToDoList()) {
            return new CommandResult(Messages.UNDID_COMMAND);
        } else {
            return new CommandResult(Messages.UNDID_COMMAND_FAIL, true);
        }
    }
    

}
