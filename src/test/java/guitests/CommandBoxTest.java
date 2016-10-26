package guitests;

import org.junit.Test;

import guitests.utils.CommandBuilder;

import static org.junit.Assert.assertEquals;

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
    
    @Test
    public void commandBox_navigateCommandHistory_invalidLastCommand() {
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.testToDoItem1));
        commandBox.runCommand("edit 4 test");
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.toDoItem1));
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.toDoItem1));
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), "edit 4 test");
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.testToDoItem1));
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.testToDoItem1));
        
        commandBox.navigateCommandDown();
        assertEquals(commandBox.getCommandInput(), "edit 4 test");
        
        commandBox.navigateCommandDown();
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.toDoItem1));
        
        commandBox.navigateCommandDown();
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.toDoItem1));
    }
    
    @Test
    public void commandBox_navigateCommandHistory_validLastCommand() {
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.testToDoItem1));
        commandBox.runCommand("edit 4 test");
        commandBox.runCommand("delete 3");
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), "delete 3");
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), "edit 4 test");
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.testToDoItem1));
        
        commandBox.navigateCommandUp();
        assertEquals(commandBox.getCommandInput(), CommandBuilder.buildAddCommand(td.testToDoItem1));
        
        commandBox.navigateCommandDown();
        assertEquals(commandBox.getCommandInput(), "edit 4 test");
        
        commandBox.navigateCommandDown();
        assertEquals(commandBox.getCommandInput(), "delete 3");
        
        commandBox.navigateCommandDown();
        assertEquals(commandBox.getCommandInput(), "delete 3");
    }

}
