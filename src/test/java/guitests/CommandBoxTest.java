package guitests;

import org.junit.Test;

import guitests.utils.CommandBuilder;
import seedu.commando.model.todo.ToDo;
import seedu.commando.testutil.ToDoBuilder;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

public class CommandBoxTest extends CommanDoGuiTest {
    
    @Test
    public void commandBox_commandFails_textStays() {
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.toDoItem5));
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.toDoItem5));
    }
    
    @Test
    public void commandBox_commandSucceeds_textClears() {
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.toDoItem6));
        assertEquals(commandBox.getCommandInput(), "");
    }
    
//    @Test
//    public void commandBox_navigateCommandHistory() {
//        commandBox.runCommand(CommandBuilder.buildAddCommand(td.toDoItem6));
//        assertEquals(commandBox.getCommandInput(), "");
//    }

}
