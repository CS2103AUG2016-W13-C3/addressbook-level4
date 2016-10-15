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
    
    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;
        
        if (!model.undoToDoList())
            return new CommandResult(Messages.UNDID_COMMAND_FAIL, true);
        
        return new CommandResult(Messages.UNDID_COMMAND);
    }
    

}
