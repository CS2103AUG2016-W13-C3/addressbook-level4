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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.*;

//@@author A0139697H
public class DeleteCommandTest {
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
    public void execute_delete_noSuchIndex() {
        CommandResult result = logic.execute("delete 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_delete_invalidIndex() {
        CommandResult result = logic.execute("delete 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }

    @Test
    public void execute_delete_invalidIndex2() {
        CommandResult result = logic.execute("delete -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_delete_invalidFormat() {
        CommandResult result = logic.execute("delete 1 #troll");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, DeleteCommand.COMMAND_WORD)
                + "\n" + Messages.getCommandFormatMessage("delete").get(), result.getFeedback());
    }

    @Test
    public void execute_delete_missingIndex() {
        CommandResult result = logic.execute("delete missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX
                + "\n" + Messages.getCommandFormatMessage("delete").get(), result.getFeedback());
    }

    @Test
    public void execute_delete_index() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title2")
                .build()));

        CommandResult result = logic.execute("delete 2");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertFalse(ifToDoExists(logic,
            new ToDoBuilder("title2")
                .build()));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .build()));
    }

    @Test
    public void execute_delete_tags() throws IllegalValueException {
        logic.execute("add title from 22 Oct 2014 1300h to 23 Oct 2016 1400h #tag1 #tag2");
        logic.execute("recall");
        
        eventsCollector.reset();

        CommandResult result = logic.execute("delete 1 tag");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(2014, 10, 22, 13, 0),
                    LocalDateTime.of(2016, 10, 23, 14, 0)
                )
                .build()));
    }

    @Test
    public void execute_delete_tagsButNoTags() throws IllegalValueException {
        logic.execute("add title by 10 Nov 2015 1200h");

        eventsCollector.reset();

        CommandResult result = logic.execute("delete 1 tag");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.DELETE_COMMAND_NO_TAGS, "1"), result.getFeedback());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDueDate(LocalDateTime.of(2015, 11, 10, 12, 0))
                .build()));
    }

    @Test
    public void execute_delete_timeButNoTime() throws IllegalValueException {
        logic.execute("add title #tag1");

        eventsCollector.reset();

        CommandResult result = logic.execute("delete 1 time");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.DELETE_COMMAND_NO_TIME_CONSTRAINTS, "1"), result.getFeedback());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withTags("tag1")
                .build()));
    }

    @Test
    public void execute_delete_timeDueDate() throws IllegalValueException {
        logic.execute("add title by 23 Oct 2016");

        eventsCollector.reset();

        CommandResult result = logic.execute("delete 1 time");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .build()));
    }

    @Test
    public void execute_delete_timeDateRange() throws IllegalValueException {
        logic.execute("add title from 22 Oct 2014 to 23 Oct 2016 #tag1");
        logic.execute("recall");

        eventsCollector.reset();

        CommandResult result = logic.execute("delete 1 time");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withTags("tag1")
                .build()));
    }

    @Test
    public void execute_delete_invalidFields() throws IllegalValueException {
        logic.execute("add title");

        eventsCollector.reset();

        CommandResult result = logic.execute("delete 1 invalid field");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, DeleteCommand.COMMAND_WORD)
                + "\n" + Messages.getCommandFormatMessage("delete").get(), result.getFeedback() );
    }


    @Test
    public void execute_delete_tagsAndTime() throws IllegalValueException {
        logic.execute("add title from 22 Oct 2014 to 23 Oct 2016 #tag1");
        logic.execute("recall");
        
        eventsCollector.reset();

        CommandResult result = logic.execute("delete 1 time tag");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .build()));
    }
}