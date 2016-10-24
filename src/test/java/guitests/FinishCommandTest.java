package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.TestUtil;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

public class FinishCommandTest extends CommanDoGuiTest {
    
    
    @Test
    public void finish_one_index() {
        //finish one item
        ToDo[] currentList = td.getTypicalToDos();
        int targetIndex = 4;
        assertFinishSuccess(targetIndex, currentList);
        
        //finish invalid index
        targetIndex = 7;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, "[" + targetIndex + "]"));
        
        targetIndex = 0;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, "[" + targetIndex + "]"));
        
        //finish event
        targetIndex = 1;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.FINISH_COMMAND_CANNOT_FINISH_EVENT, "[" + targetIndex + "]"));
        
        //finish finished task
        targetIndex = 5;
        commandBox.runCommand("finish " + targetIndex);
        assertResultMessage(String.format(Messages.FINISH_COMMAND_ALREADY_FINISHED, "[" + targetIndex + "]"));
        
        
    }
    
    @Test
    public void finish_multiple_task() {
        //finish multiple items
        ToDo[] currentList = td.getTypicalToDos();
        assertFinishConsectiveSuccess(3, 4, currentList);
    }
    
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
        
    }
    
    private void assertFinishConsectiveSuccess(int startIndex, int endIndex, ToDo[] currentList) {
        ToDo TodosToFinish = null;
        ToDo[] expectedRemainder = currentList;
        String range = "[";
        for (int i = startIndex; i<= endIndex; i++){
            TodosToFinish = expectedRemainder[startIndex-1]; //-1 because array uses zero indexing
            TodosToFinish.setIsFinished(true);
            expectedRemainder = TestUtil.removeToDoFromList(expectedRemainder, startIndex);
            expectedRemainder = TestUtil.addToDosToList(expectedRemainder, expectedRemainder.length, TodosToFinish);
            range += i + ", ";
        }
        commandBox.runCommand("finish " + startIndex + " to " + endIndex);
        
        //confirm the list now contains all previous Todoss except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        range = range.substring(0, range.length()-2) + "]";
        
        //confirm the result message is correct
        assertResultMessage(String.format(Messages.FINISH_COMMAND, range));
        
    }
}
