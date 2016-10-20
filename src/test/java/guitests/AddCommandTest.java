package guitests;

import seedu.commando.commons.core.Messages;
import seedu.commando.logic.commands.AddCommand;
import seedu.commando.model.todo.ToDo;

import org.junit.Test;

import guitests.guihandles.EventCardHandle;
import guitests.guihandles.ToDoCardHandle;
import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.CommandBuilder;
import guitests.utils.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends CommanDoGuiTest {

    @Test
    public void add() {
        commandBox.runCommand("clear");
        //add one todo
        ToDo[] currentList = td.getEmptyToDos();
        ToDo toDoToAdd = td.toDoItem2;
        assertAddSuccess(toDoToAdd, currentList);
        currentList = TestUtil.addToDosToList(currentList, toDoToAdd);

        //add another todo
        toDoToAdd = td.toDoItem1;
        assertAddSuccess(toDoToAdd, currentList);
        currentList = TestUtil.addToDosToList(currentList, toDoToAdd);

        //add duplicate person
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.toDoItem1));
        assertResultMessage(Messages.TODO_ALREADY_EXISTS);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.toDoItem3);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(ToDo todoToAdd, ToDo... currentList) {
        commandBox.runCommand(CommandBuilder.buildAddCommand(todoToAdd));

        //confirm the new card contains the right data
        ToDoCardHandle eventListResult = eventListPanel.navigateToTodo(todoToAdd.getTitle().value);
        ToDoCardHandle taskListResult = taskListPanel.navigateToTodo(todoToAdd.getTitle().value);
        ToDoCardHandle addedCard = null;
        if (eventListResult != null)
            addedCard = eventListResult;
        else if (taskListResult != null)
            addedCard = taskListResult;
        assertMatching(todoToAdd, addedCard);

        //confirm the list now contains all previous persons plus the new person
        ToDo[] expectedList = TestUtil.addToDosToList(currentList, todoToAdd);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedList));
    }

}
