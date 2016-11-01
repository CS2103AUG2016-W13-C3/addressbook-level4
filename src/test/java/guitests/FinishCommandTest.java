package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.TestUtil;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

//@@author A0122001M

public class FinishCommandTest extends CommanDoGuiTest {
        
    @Test
    public void finishCommand_oneIndex() {
        //finish one item
        ToDo[] currentList = td.getTypicalToDos();
        int targetIndex = 4;
        assertFinishSuccess(targetIndex, currentList);
           
    }
    
    @Test
    public void finishCommand_multipleIndices() {
        //finish multiple items
        ToDo[] currentList = td.getTypicalToDos();
        assertFinishConsectiveSuccess(3, 4, currentList);
    }
    
    @Test
    public void finishCommand_invalidIndex_reportError() {
        ToDo[] currentList = td.getTypicalToDos();
        int targetIndex = 4;
        
        //finish invalid index
        targetIndex = 7;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, targetIndex ));
        
        targetIndex = 0;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, targetIndex ));
        
        //finish event
        targetIndex = 1;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.FINISH_COMMAND_CANNOT_FINISH_EVENT, currentList[targetIndex-1].getTitle().toString()));
     
    }
    
    /**
     * Runs the finish command to mark the single Todo at specified index and
     * confirms the result is correct.
     * 
     * @param targetIndex   The indices Todos to be marked as done in the list.
     * @param currentList   A copy of the current list of Todos (before finish command).
     */
    
    private void assertFinishSuccess(int targetIndex, ToDo[] currentList) {
        ToDo TodosToFinish = currentList[targetIndex-1]; //-1 because array uses zero indexing
        TodosToFinish.setIsFinished(true);
        ToDo[] expectedRemainder = TestUtil.removeToDoFromList(currentList, targetIndex);
        expectedRemainder = TestUtil.addToDosToList(expectedRemainder, expectedRemainder.length, TodosToFinish);
        commandBox.runCommand("finish " + targetIndex);

        //confirm the list now contains all previous Todos with one marked as done
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(Messages.FINISH_COMMAND, "[" + targetIndex + "]"));
        
        //finish finished task
        targetIndex = 5;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.FINISH_COMMAND_ALREADY_FINISHED, expectedRemainder[targetIndex-1].getTitle().toString()));
        
    }
    
    /**
     * Runs the finish command to mark the consective Todos (e.g. from index 2 to 4) and
     * confirms the result is correct.
     * 
     * @param startIndex    The starting index Todo to be marked as done in the list.
     * @param endIndex      The ending index Todo to be marked as done in the list.
     * @param currentList   A copy of the current list of Todos (before finish command).
     */
    private void assertFinishConsectiveSuccess(int startIndex, int endIndex, ToDo[] currentList) {
        ToDo[] expectedRemainder = currentList;
        String finishedIndices = "[";
        
        //setfinish for all target todos and reinsert them into the list
        for (int i = startIndex; i<= endIndex; i++){
            ToDo TodosToFinish = expectedRemainder[startIndex-1]; //-1 because array uses zero indexing
            TodosToFinish.setIsFinished(true);
            expectedRemainder = TestUtil.removeToDoFromList(expectedRemainder, startIndex);
            expectedRemainder = TestUtil.addToDosToList(expectedRemainder, expectedRemainder.length, TodosToFinish);
            finishedIndices += i + ", ";
        }
        commandBox.runCommand("finish " + startIndex + " to " + endIndex);
        
        //confirm the list now contains all previous Todoss except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));
        finishedIndices = finishedIndices.substring(0, finishedIndices.length()-2) + "]";
        
        //confirm the result message is correct
        assertResultMessage(String.format(Messages.FINISH_COMMAND, finishedIndices));
        

    }
}
