package guitests;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

import static org.junit.Assert.assertTrue;

//@@author A0122001M

public class RecallCommandTest extends CommanDoGuiTest {

    @Test
    public void recall_nonEmptyList() {
        //recall when no finished items
        commandBox.runCommand("recall");
        assertListSize(0);
        commandBox.runCommand("find"); //go back to current list

        //recall when some items are finished
        commandBox.runCommand("finish 4 5");
        assertRecallResult("recall titles"); //no results
        assertRecallResult("recall title", td.toDoItem5.setIsFinished(true), td.toDoItem1.setIsFinished(true)); //multiple results
        assertRecallResult("recall #tag2", td.toDoItem5.setIsFinished(true));
    }

    @Test
    public void recall_emptyList() {
        commandBox.runCommand("clear");
        assertRecallResult("recall title"); //no results
    }

    @Test
    public void recall_invalidCommand() {
        commandBox.runCommand("recalltest");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "recalltest"));
    }

    private void assertRecallResult(String command, ToDo... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);  //number of expected todos = number of listed todos
        
        assertResultMessage(String.format(Messages.RECALL_COMMAND, eventListPanel.getNumberOfToDo(), taskListPanel.getNumberOfToDo()));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedHits));
    }
}
