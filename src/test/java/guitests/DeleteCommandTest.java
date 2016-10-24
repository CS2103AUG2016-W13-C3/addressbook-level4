package guitests;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.TestUtil;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

public class DeleteCommandTest extends CommanDoGuiTest {

    @Test
    public void delete_one_index() {

        //delete the first in the list
        ToDo[] currentList = td.getTypicalToDos();
        int targetIndex = 1;
        assertDeleteSuccess(targetIndex, currentList);

       //delete the last in the list
        currentList = TestUtil.removeToDoFromList(currentList, targetIndex);
        targetIndex = currentList.length;
        assertDeleteSuccess(targetIndex, currentList);

       //delete from the middle of the list
        currentList = TestUtil.removeToDoFromList(currentList, targetIndex);
        targetIndex = currentList.length/2;
        assertDeleteSuccess(targetIndex, currentList);

       //invalid index
        targetIndex = currentList.length + 1;
        commandBox.runCommand("delete " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, "[" + targetIndex + "]"));
        
        targetIndex = 0;
        commandBox.runCommand("delete " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, "[" + targetIndex + "]"));
        
        targetIndex = -1;
        commandBox.runCommand("delete " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, "[" + targetIndex + "]"));
        
        //invalid params  or empty index
        commandBox.runCommand("delete abc 1");
        assertResultMessage(Messages.MISSING_TODO_ITEM_INDEX);
        
        commandBox.runCommand("delete   ");
        assertResultMessage(Messages.MISSING_TODO_ITEM_INDEX);
        
        //invalid command name
        commandBox.runCommand("deleted 1");
        assertResultMessage(Messages.UNKNOWN_COMMAND);
    }
    
    @Test
    public void delete_one_field_time() {
        ToDo[] currentList = td.getTypicalToDos();
        //delete the time
        commandBox.runCommand("delete 1 time");
        currentList = TestUtil.removeToDoFromList(currentList, 1);
        currentList = TestUtil.addToDosToList(currentList, currentList.length, td.toDoItem2.clearTimeConstraint());
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        assertResultMessage(String.format(Messages.TODO_EDITED, "[1]"));
    }
    
    @Test
    public void delete_one_field_tag() {
        ToDo[] currentList = td.getTypicalToDos();
        //delete the tag
        commandBox.runCommand("delete 1 tag");
        currentList = TestUtil.removeToDoFromList(currentList, 1);
        currentList = TestUtil.addToDosToList(currentList, 0, td.toDoItem2.setTags(Collections.emptySet()));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        assertResultMessage(String.format(Messages.TODO_EDITED, "[1]"));
    }
    
    @Test
    public void delete_multiple_index() {
        ToDo[] currentList = td.getTypicalToDos();
        assertDeleteConsectiveSuccess(2, 3, currentList);
    }

    /**
     * Runs the delete command to delete the single Todo at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first Todos in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of Todos (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final ToDo[] currentList) {
        
        ToDo TodosToDelete = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        ToDo[] expectedRemainder = TestUtil.removeToDoFromList(currentList, targetIndexOneIndexed);
        
        commandBox.runCommand("delete " + targetIndexOneIndexed);

        //confirm the list now contains all previous Todos except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(Messages.TODO_DELETED, "[" + targetIndexOneIndexed + "]"));
    }
    
    private void assertDeleteConsectiveSuccess(int startIndex, int endIndex, final ToDo[] currentList) {
        ToDo TodosToDelete = null;
        ToDo[] expectedRemainder = currentList;
        String range = "[";
        for (int i = startIndex; i<= endIndex; i++){
            TodosToDelete = expectedRemainder[startIndex-1]; //-1 because array uses zero indexing
            expectedRemainder = TestUtil.removeToDoFromList(expectedRemainder, startIndex);
            range += i + ", ";
        }
        commandBox.runCommand("delete " + startIndex + " to " + endIndex);
        
        //confirm the list now contains all previous Todoss except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        range = range.substring(0, range.length()-2) + "]";
        
        //confirm the result message is correct
        assertResultMessage(String.format(Messages.TODO_DELETED, range));
    }

}
