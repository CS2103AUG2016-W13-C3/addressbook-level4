package guitests;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.commands.AddCommand;
import seedu.commando.model.todo.ToDo;
import seedu.commando.testutil.ToDoBuilder;

import org.junit.Test;

import guitests.guihandles.EventCardHandle;
import guitests.guihandles.ToDoCardHandle;
import guitests.guihandles.ToDoListPanelHandle;
import guitests.utils.CommandBuilder;
import guitests.utils.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends CommanDoGuiTest {

    @Test
    public void add() throws IllegalValueException {
        // add one todo to existing list
        ToDo[] currentList = td.getTypicalToDos();
        ToDo toDoToAdd = td.testToDoItem1;
        assertAddSuccess(toDoToAdd, 5, currentList);
        currentList = TestUtil.addToDosToList(currentList, 5, toDoToAdd);

        toDoToAdd = td.testToDoItem2;
        assertAddSuccess(toDoToAdd, 1, currentList);
        currentList = TestUtil.addToDosToList(currentList, 1, toDoToAdd);

        toDoToAdd = td.testToDoItem3;
        assertAddSuccess(toDoToAdd, 3, currentList);
        currentList = TestUtil.addToDosToList(currentList, 3, toDoToAdd);

        //add one todo to empty list
        commandBox.runCommand("clear");
        currentList = td.getEmptyToDos();
        toDoToAdd = td.toDoItem2;
        assertAddSuccess(toDoToAdd, currentList.length, currentList);
        currentList = TestUtil.addToDosToList(currentList, currentList.length, toDoToAdd);

        //add another todo
        toDoToAdd = td.toDoItem1;
        assertAddSuccess(toDoToAdd, currentList.length, currentList);
        currentList = TestUtil.addToDosToList(currentList, currentList.length, toDoToAdd);

        //Invalid add command:

        //add duplicate todo
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.toDoItem1));
        assertResultMessage(Messages.TODO_ALREADY_EXISTS);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        //add missing title
        commandBox.runCommand("add ");
        assertResultMessage(Messages.MISSING_TODO_TITLE);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        //add missing startdate ,  empty date --> consider as floating task
        commandBox.runCommand("add test from to 1pm");
        toDoToAdd = new ToDoBuilder("test from to 1pm").build();
        ToDo[] expectedList = TestUtil.addToDosToList(currentList, currentList.length, toDoToAdd);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedList));
        currentList = expectedList;
       
        //add missing startdate ,  invalid date
        commandBox.runCommand("add test from abcde to 1pm");
        assertResultMessage(Messages.INVALID_TODO_DATERANGE_START);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        //add missing enddate , empty date --> consider as floating task
        commandBox.runCommand("add test from 1pm to");
        toDoToAdd = new ToDoBuilder("test from 1pm to").build();
        expectedList = TestUtil.addToDosToList(currentList, currentList.length, toDoToAdd);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedList));
        currentList = expectedList;
        
        //add missing enddate ,  invalid date
        commandBox.runCommand("add test from 1pm to abcde");
        assertResultMessage(Messages.INVALID_TODO_DATERANGE_END);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(ToDo todoToAdd, int idx, ToDo... currentList ) {
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
        ToDo[] expectedList = TestUtil.addToDosToList(currentList, idx, todoToAdd);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedList));
    }

}
