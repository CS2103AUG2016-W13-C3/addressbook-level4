package guitests;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.CommandBuilder;
import seedu.commando.commons.core.Messages;

import static org.junit.Assert.assertTrue;

//@@author A0122001M

public class ClearCommandTest extends CommanDoGuiTest {

    @Test
    public void clearCommand_validCommand_listCleared() {
        //verify a non-empty list can be cleared
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, td.getTypicalToDos()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.testToDoItem1));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, td.testToDoItem1));
        commandBox.runCommand("delete 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }
    
    @Test
    public void clearCommand_invalidCommand_reportErrorMessage() {
        //verify clear command does not take in extra params
        commandBox.runCommand("clear 1");
        assertResultMessage(String.format(Messages.INVALID_COMMAND_FORMAT, "clear"));
        
        //verify clear command does not execute on similar bur wrong command name
        commandBox.runCommand("clears");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "clears"));
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertResultMessage(Messages.TODO_LIST_CLEARED);
    }
}
