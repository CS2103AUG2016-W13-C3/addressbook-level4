package guitests;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

import static org.junit.Assert.assertTrue;

public class FindCommandTest extends CommanDoGuiTest {

    @Test
    public void find_nonEmptyList() {
        assertFindResult("find titles"); //no results
        assertFindResult("find title 3", td.toDoItem3); //multiple results
        assertFindResult("find title tag2", td.toDoItem2, td.toDoItem5);
        
        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find title 2", td.toDoItem5);
    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find Jean"); //no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "findgeorge"));
    }

    private void assertFindResult(String command, ToDo... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length); //number of expected todos = number of listed todos in result
        assertResultMessage(String.format(Messages.FIND_COMMAND, eventListPanel.getNumberOfToDo(), taskListPanel.getNumberOfToDo()));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedHits));
    }
}
