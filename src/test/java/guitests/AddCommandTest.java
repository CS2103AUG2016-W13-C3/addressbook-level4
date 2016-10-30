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

//@@author A0122001M

public class AddCommandTest extends CommanDoGuiTest {

    @Test
    public void addCommand_nonEmptyList_addSuccessful() throws IllegalValueException {
        // add one todo to existing list
        // add floating task
        ToDo[] currentList = td.getTypicalToDos();
        ToDo toDoToAdd = td.testToDoItem1;
        assertAddSuccess(toDoToAdd, 4, currentList);
        currentList = TestUtil.addToDosToList(currentList, 4, toDoToAdd);

        // add event
        toDoToAdd = td.testToDoItem2;
        assertAddSuccess(toDoToAdd, 1, currentList);
        currentList = TestUtil.addToDosToList(currentList, 1, toDoToAdd);

        // add deadline
        toDoToAdd = td.testToDoItem3;
        assertAddSuccess(toDoToAdd, 3, currentList);
        currentList = TestUtil.addToDosToList(currentList, 3, toDoToAdd);
    }
    
    @Test
    public void addCommand_emptyList_addSuccessful() {
        //add one todo to empty list
        commandBox.runCommand("clear");
        ToDo[] currentList = td.getEmptyToDos();
        ToDo toDoToAdd = td.toDoItem2;
        assertAddSuccess(toDoToAdd, currentList.length, currentList);
        currentList = TestUtil.addToDosToList(currentList, currentList.length, toDoToAdd);

        //add another todo
        toDoToAdd = td.toDoItem1;
        assertAddSuccess(toDoToAdd, currentList.length, currentList);
        currentList = TestUtil.addToDosToList(currentList, currentList.length, toDoToAdd);
    }
    
    @Test
    public void addCommand_invalidCommand_reportErrorMessage() {
        //Invalid add command:
        ToDo[] currentList = td.getTypicalToDos();

        //add duplicate todo
        commandBox.runCommand(CommandBuilder.buildAddCommand(td.toDoItem1));
        assertResultMessage(Messages.TODO_ALREADY_EXISTS);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));
        
        //add missing title
        commandBox.runCommand("add ");
        assertResultMessage(Messages.MISSING_TODO_TITLE + "\n" + Messages.ADD_COMMAND_FORMAT);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "adds"));
    }
    
    @Test
    public void addCommand_invalidTimeWindow_reportErrorMessage() {
        ToDo[] currentList = td.getTypicalToDos();
        
        //enddate is earlier than start date
        commandBox.runCommand("add test from tomorrow to today");
        assertResultMessage(Messages.TODO_DATERANGE_END_MUST_AFTER_START + "\n" + Messages.DATE_FORMAT + "\n" + 
                Messages.ADD_COMMAND_FORMAT);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));
    }
    
    @Test
    public void addCommand_invalidDate_reportErrorMessage() {
        ToDo[] currentList = td.getTypicalToDos();
        
        //invalid start date
        commandBox.runCommand("add test from abcde to 1pm");
        assertResultMessage(Messages.INVALID_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT + "\n" + 
                Messages.ADD_COMMAND_FORMAT);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));
        
        //invalid end date
        commandBox.runCommand("add test from 1pm to abcde");
        assertResultMessage(Messages.INVALID_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT + "\n" + 
                Messages.ADD_COMMAND_FORMAT);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));
    }
    
    @Test
    public void addCommand_emptyDate_reportErrorMessage() {
        ToDo[] currentList = td.getTypicalToDos();
        
        //empty start date
        commandBox.runCommand("add test from to 1pm");
        assertResultMessage(Messages.MISSING_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT + "\n" + 
                Messages.ADD_COMMAND_FORMAT);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));
       
        //empty end date
        commandBox.runCommand("add test from 1pm to");
        assertResultMessage(Messages.MISSING_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT + "\n" + 
                Messages.ADD_COMMAND_FORMAT);
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, currentList));
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
