package seedu.commando.logic.commands;

import java.util.List;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

public class RedoCommand extends Command {
    
    public static final String COMMAND_WORD = "redo";
    
    public RedoCommand(){
        
    }

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;
        
        if (model.redoToDoList()) {
            return new CommandResult(Messages.REDID_COMMAND);
        } else {
            return new CommandResult(Messages.REDID_COMMAND_FAIL, true);
        }
    }

}
