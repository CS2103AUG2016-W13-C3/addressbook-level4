package guitests;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.TestUtil;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//@@author A0122001M

public class DeleteCommandTest extends CommanDoGuiTest {

    @Test
    public void deleteCommand_oneIndex_deleteFromList() {
        // delete the first in the list
        ToDo[] currentList = td.getTypicalToDos();
        int targetIndex = 1;
        assertDeleteSuccess(targetIndex, currentList);

        // delete the last in the list
        currentList = TestUtil.removeToDoFromList(currentList, targetIndex);
        targetIndex = currentList.length;
        assertDeleteSuccess(targetIndex, currentList);

        // delete from the middle of the list
        currentList = TestUtil.removeToDoFromList(currentList, targetIndex);
        targetIndex = currentList.length / 2;
        assertDeleteSuccess(targetIndex, currentList);
    }
    
    @Test
    public void deleteCommand_consectiveIndex_deleteFromList() {
        // delete consective indices i.e. delete todo with index 2 to 3
        ToDo[] currentList = td.getTypicalToDos();
        assertDeleteConsectiveSuccess(2, 3, currentList);
    }
    
    @Test
    public void deleteCommand_multipleIndex_deleteFromList() {
        // delete multiple indices
        ToDo[] currentList = td.getTypicalToDos();
        assertDeleteMultipleSuccess(currentList, 2, 4, 5);
    }
    
    @Test
    public void deleteCommand_deleteTime_removeTimeAndChangeToTask() {
        ToDo[] currentList = td.getTypicalToDos();
        // delete the time window
        commandBox.runCommand("delete 1 time");
        currentList = TestUtil.removeToDoFromList(currentList, 1);
        currentList = TestUtil.addToDosToList(currentList, currentList.length, td.toDoItem2.clearTimeConstraint());
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        assertResultMessage(String.format(Messages.EDIT_COMMAND, "[1]"));
    }

    @Test
    public void deleteCommand_deleteTag_allTagsRemoved() {
        ToDo[] currentList = td.getTypicalToDos();
        // delete the tag
        commandBox.runCommand("delete 1 tag");
        currentList = TestUtil.removeToDoFromList(currentList, 1);
        currentList = TestUtil.addToDosToList(currentList, 0, td.toDoItem2.setTags(Collections.emptySet()));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        assertResultMessage(String.format(Messages.EDIT_COMMAND, "[1]"));
    }
    
    
    @Test
    public void deleteCommand_deleteDeadline_MoveToBottomOfList() {
        ToDo[] currentList = td.getTypicalToDos();
        // delete the deadline
        commandBox.runCommand("delete 3 time");
        currentList = TestUtil.removeToDoFromList(currentList, 3);
        currentList = TestUtil.addToDosToList(currentList, currentList.length, td.toDoItem4.clearTimeConstraint());
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        assertResultMessage(String.format(Messages.EDIT_COMMAND, "[3]"));
    }

    @Test
    public void deleteCommand_invalidIndex_reportErrorMessage() {
        // invalid index
        ToDo[] currentList = td.getTypicalToDos();
        int targetIndex = currentList.length + 1;
        commandBox.runCommand("delete " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, targetIndex));
        
        targetIndex = 0;
        commandBox.runCommand("delete " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, targetIndex));

        targetIndex = -1;
        commandBox.runCommand("delete " + targetIndex);
        assertResultMessage(String.format(Messages.TODO_ITEM_INDEX_INVALID, targetIndex));
        
        //empty index      
        commandBox.runCommand("delete   ");
        assertResultMessage(Messages.MISSING_TODO_ITEM_INDEX + "\n" + Messages.DELETE_COMMAND_FORMAT);

    }
    
    @Test
    public void deleteCommand_invalidParams_reportErrorMessage() {
        commandBox.runCommand("delete abc 1");
        assertResultMessage(Messages.MISSING_TODO_ITEM_INDEX + "\n" + Messages.DELETE_COMMAND_FORMAT);
        
        commandBox.runCommand("delete 1 abc");
        assertResultMessage(String.format(Messages.INVALID_COMMAND_FORMAT, "delete") + "\n" + Messages.DELETE_COMMAND_FORMAT);
    }
    
    @Test
    public void deleteCommand_invalidCommandName_reportErrorMessage() {
        // invalid command name
        commandBox.runCommand("deleted 1");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "deleted"));
    }

    /**
     * Runs the delete command to delete the single Todo at specified index and
     * confirms the result is correct.
     * 
     * @param targetIndex   The indexes Todos to be deleted in the list.
     * @param currentList   A copy of the current list of Todos (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final ToDo[] currentList) {

        ToDo TodosToDelete = currentList[targetIndexOneIndexed - 1]; 
        ToDo[] expectedRemainder = TestUtil.removeToDoFromList(currentList, targetIndexOneIndexed);

        commandBox.runCommand("delete " + targetIndexOneIndexed);

        // confirm the list now contains all previous Todos except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        // confirm the result message is correct
        assertResultMessage(String.format(Messages.DELETE_COMMAND, "[" + targetIndexOneIndexed + "]"));
    }

    private void assertDeleteConsectiveSuccess(int startIndex, int endIndex, final ToDo[] currentList) {
        ToDo[] expectedRemainder = currentList;
        String deletedIndices = "[";

        // delete all indices from the list
        for (int i = startIndex; i <= endIndex; i++) {
            ToDo TodosToDelete = expectedRemainder[startIndex - 1]; 
            expectedRemainder = TestUtil.removeToDoFromList(expectedRemainder, startIndex);
            deletedIndices += i + ", ";
        }
        commandBox.runCommand("delete " + startIndex + " to " + endIndex);

        // confirm the list now contains all previous Todoss except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        deletedIndices = deletedIndices.substring(0, deletedIndices.length() - 2) + "]";

        // confirm the result message is correct
        assertResultMessage(String.format(Messages.DELETE_COMMAND, deletedIndices));
    }
    
    private void assertDeleteMultipleSuccess(final ToDo[] currentList, int... indices) {
        ToDo[] expectedRemainder = currentList;
        String deletedIndices = "";
        
        // delete all indices from the list
        List<ToDo> expectedList = new ArrayList<ToDo>();
        for (int i=0; i<indices.length; i++) {
            expectedList.add(expectedRemainder[indices[i]-1]);
            deletedIndices += indices[i] + " ";
        }
        
        expectedRemainder = TestUtil.removeToDosFromList(currentList, expectedList.toArray(new ToDo[0]));
        
        commandBox.runCommand("delete " + deletedIndices);

        // confirm the list now contains all previous Todoss except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        // confirm the result message is correct
        assertResultMessage(String.format(Messages.DELETE_COMMAND, Arrays.toString(indices)));
    }

}
