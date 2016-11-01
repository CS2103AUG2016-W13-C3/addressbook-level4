package guitests;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

import static org.junit.Assert.assertTrue;

//@@author A0122001M

public class FindCommandTest extends CommanDoGuiTest {

    @Test
    public void findCommand_nonEmptyList() {
        //no results
        assertFindResult("find titles"); 
        
        //multiple results
        assertFindResult("find title 3", td.toDoItem3); 
        assertFindResult("find title tag2", td.toDoItem2, td.toDoItem5);
        
        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find title 2", td.toDoItem5);
    }

    @Test
    public void findCommand_emptyList(){
        commandBox.runCommand("clear");
        
        //no results
        assertFindResult("find Jean"); 
    }

    @Test
    public void findCommand_invalidCommand_reportUnknownCommand() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "findgeorge"));
    }
    
    /**
     * Runs the find command to filter ToDoList according to given keywords
     * 
     * @param command        The find command to be executed.
     * @param expectedHits   The expected result list after filtering.
     */
    private void assertFindResult(String command, ToDo... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length); //number of expected todos = number of listed todos in result
        assertResultMessage(String.format(Messages.FIND_COMMAND, eventListPanel.getNumberOfToDo(), taskListPanel.getNumberOfToDo()));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedHits));
    }
}
