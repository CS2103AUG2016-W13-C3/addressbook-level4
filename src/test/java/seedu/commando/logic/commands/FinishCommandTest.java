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
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.*;

//@@author A0139697H
public class FinishCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();

    @Before
    public void setup() throws IOException {
        logic = initLogic();
        eventsCollector = new EventsCollector();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_finishNoSuchIndex_error() {
        CommandResult result = logic.execute("delete 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_finishInvalidIndex_error() {
        CommandResult result = logic.execute("finish 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }
    
    @Test
    public void execute_finishInvalidIndex2_error() {
        CommandResult result = logic.execute("finish -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_finishMissingIndex_error() {
        CommandResult result = logic.execute("finish missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX + "\n" + Messages.getCommandFormatMessage("finish").get(), result.getFeedback());
    }

    @Test
    public void execute_finishInvalidFormat_error() {
        CommandResult result = logic.execute("finish 1 #troll");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, FinishCommand.COMMAND_WORD)
                + "\n" + Messages.getCommandFormatMessage("finish").get(), result.getFeedback());
    }

    @Test
    public void execute_finish_finished() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoExists(logic,
            new ToDoBuilder("title2")
                .build());

        CommandResult result = logic.execute("finish 2");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("title2")
                .build());
        assertToDoExists(logic,
            new ToDoBuilder("title")
                .build());
    }
}