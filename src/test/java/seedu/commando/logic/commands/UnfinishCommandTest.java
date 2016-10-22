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

public class UnfinishCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();

    @Before
    public void setup() throws IOException {
        logic = initLogic(folder);
        eventsCollector = new EventsCollector();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_unfinish_noSuchIndex() {
        CommandResult result = logic.execute("delete 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_unfinish_invalidIndex() {
        CommandResult result = logic.execute("unfinish 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }
    
    @Test
    public void execute_unfinish_invalidIndex2() {
        CommandResult result = logic.execute("unfinish -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_unfinish_missingIndex() {
        CommandResult result = logic.execute("unfinish missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX, result.getFeedback());
    }

    @Test
    public void execute_unfinish_invalidFormat() {
        CommandResult result = logic.execute("unfinish 1 #troll");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, FinishCommand.COMMAND_WORD), result.getFeedback());
    }

    @Test
    public void execute_unfinish_index() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("finish 2");
        logic.execute("recall");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title2")
                .build()));

        CommandResult result = logic.execute("unfinish 1");
        assertFalse(result.hasError());
        
        logic.execute("find");

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title2")
                .build()));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .build()));
    }
}