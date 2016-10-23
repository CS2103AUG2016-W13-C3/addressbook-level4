package guitests;

import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.TestUtil;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

import static org.junit.Assert.assertTrue;

public class DeleteCommandTest extends CommanDoGuiTest {

    @Test
    public void delete() {

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
        commandBox.runCommand("delete " + currentList.length + 1);
        assertResultMessage("Invalid to-do index: " + currentList.length + 1 + ".");
        
        commandBox.runCommand("delete " + "0");
        assertResultMessage("Invalid to-do index: 0.");
        
        commandBox.runCommand("delete " + "-1");
        assertResultMessage("Invalid to-do index: -1.");
        
        //invalid params  or empty index
        commandBox.runCommand("delete abc" + "1");
        assertResultMessage(Messages.MISSING_TODO_ITEM_INDEX);
        
        commandBox.runCommand("delete   ");
        assertResultMessage(Messages.MISSING_TODO_ITEM_INDEX);
        
        //invalid command name
        commandBox.runCommand("deleted 1");
        assertResultMessage(Messages.UNKNOWN_COMMAND);
        
        
    }

    /**
     * Runs the delete command to delete the Todos at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first Todos in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of Todoss (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final ToDo[] currentList) {
        ToDo TodosToDelete = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        ToDo[] expectedRemainder = TestUtil.removeToDoFromList(currentList, targetIndexOneIndexed);
        for (int i=0; i<expectedRemainder.length; i++)
            System.out.println(expectedRemainder[i].getText());
        
        
        commandBox.runCommand("delete " + targetIndexOneIndexed);

        //confirm the list now contains all previous Todoss except the deleted Todos
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(Messages.TODO_DELETED, TodosToDelete.getTitle().value));
    }

}
