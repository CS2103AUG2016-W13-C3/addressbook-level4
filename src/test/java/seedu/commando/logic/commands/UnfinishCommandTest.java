package seedu.commando.logic.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.Logic;
import seedu.commando.testutil.EventsCollector;
import seedu.commando.testutil.ToDoBuilder;

import java.io.IOException;

import static org.junit.Assert.*;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.*;

//@@author A0139080J
public class UnfinishCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;

    @Before
    public void setUp() throws IOException {
        logic = initLogic();
        eventsCollector = new EventsCollector();
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_unfinishNoSuchIndex_error() {
        CommandResult result = logic.execute("unfinish 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_unfinishInvalidIndex_error() {
        CommandResult result = logic.execute("unfinish 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }
    
    @Test
    public void execute_unfinishInvalidIndex2_error() {
        CommandResult result = logic.execute("unfinish -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_unfinishMissingIndex_error() {
        CommandResult result = logic.execute("unfinish missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX
                + "\n" + Messages.getCommandFormatMessage("unfinish").get(), result.getFeedback());
    }

    @Test
    public void execute_unfinishInvalidFormat_error() {
        CommandResult result = logic.execute("unfinish 1 #troll");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, UnfinishCommand.COMMAND_WORD)
                + "\n" + Messages.getCommandFormatMessage("unfinish").get(), result.getFeedback());
    }

    @Test
    public void execute_unfinishIndex_unfinished() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("finish 2");
        logic.execute("recall");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoExists(logic,
            new ToDoBuilder("title2")
                .build());

        CommandResult result = logic.execute("unfinish 1");
        assertFalse(result.hasError());
        
        logic.execute("find");

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("title2")
                .build());
        assertToDoExists(logic,
            new ToDoBuilder("title")
                .build());
    }
    
    @Test
    public void execute_unfinishEvent_error() {
        logic.execute("add test from yesterday 1am to 2am");
        logic.execute("recall");
        CommandResult result = logic.execute("unfinish 1");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.UNFINISH_COMMAND_CANNOT_UNFINISH_EVENT, "test"), 
                result.getFeedback());
    }
    
    //@@author A0142230B
    @Test
    public void execute_unfinishMutipleTaskAndEvent_error() throws IllegalValueException {
        logic.execute("add title1");
        logic.execute("finish 1");
        logic.execute("add title2 on yesterday");
        logic.execute("recall");

        eventsCollector.reset();

        CommandResult result = logic.execute("unfinish 1 2");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(String.format(Messages.UNFINISH_COMMAND_CANNOT_UNFINISH_EVENT, "title2"), result.getFeedback());
    }
    
    @Test
    public void execute_deleteMutipleTasks_Finished() throws IllegalValueException {
        logic.execute("add title1");
        logic.execute("add title2");
        logic.execute("finish 1-2");
        logic.execute("recall");

        eventsCollector.reset();

        logic.execute("unfinish 1-2");
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic, new ToDoBuilder("title1").build());
        assertToDoExists(logic, new ToDoBuilder("title2").build());
    }

}