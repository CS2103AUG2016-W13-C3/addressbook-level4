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
import seedu.commando.model.todo.Recurrence;
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
public class EditCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private int nextYear = LocalDateTime.now().getYear() + 1;

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
    public void execute_editNoSuchIndex_error() {
        CommandResult result = logic.execute("edit 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_editInvalidIndex_error() {
        CommandResult result = logic.execute("edit 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }

    @Test
    public void execute_editInvalidIndex2_error() {
        CommandResult result = logic.execute("edit -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_editMissingIndex_error() {
        CommandResult result = logic.execute("edit missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX
                + "\n" + Messages.getCommandFormatMessage("edit").get(), result.getFeedback());
    }

    @Test
    public void execute_editTitle_editedTitle() throws IllegalValueException {
        logic.execute("add title #tag");

        eventsCollector.reset();

        String command = "edit 1 new title";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("new title")
                .withTags("tag")
                .build());
    }

    @Test
    public void execute_editTags_editedTags() throws IllegalValueException {
        logic.execute("add title #tag1 #tag2");

        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withTags("tag1", "tag2")
                .build());

        String command = "edit 1 #tag2 #tag3 #tag4";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withTags("tag2", "tag3", "tag4")
                .build());
    }

    @Test
    public void execute_editDueDate_editedDueDate() throws IllegalValueException {
        logic.execute("add title by 10 Jan " + nextYear + " 12:00");

        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withDueDate(LocalDateTime.of(nextYear, 1, 10, 12, 0))
                .build());

        String command = "edit 1 by 11 Apr " + (nextYear + 1) + " 00:12";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withDueDate(LocalDateTime.of(nextYear + 1, 4, 11, 0, 12))
                .build());
    }

    @Test
    public void execute_editDateRange_editedDateRange() throws IllegalValueException {
        logic.execute("add title from 10 Jan " + nextYear + " 12:00 to 21 Jan " + nextYear + " 13:00");

        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(nextYear, 1, 10, 12, 0),
                    LocalDateTime.of(nextYear, 1, 21, 13, 0)
                )
                .build());

        String command = "edit 1 from 10 Sep " + nextYear + " 12:15 to 21 Sep " + nextYear + " 13:45";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(nextYear, 9, 10, 12, 15),
                    LocalDateTime.of(nextYear, 9, 21, 13, 45)
                )
                .build());
    }

    @Test
    public void execute_editInvalidDateRange_error() throws IllegalValueException {
        logic.execute("add title from 11 Dec " + nextYear + " to 12 Dec " + nextYear);
        eventsCollector.reset();
        String command = "edit 1 from 10 Dec " + nextYear + " 11:59 to 11 Apr " + nextYear + " 23:10";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_DATERANGE_END_MUST_AFTER_START + "\n" + Messages.DATE_FORMAT + "\n" + 
                Messages.getCommandFormatMessage("edit").get(), result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_editInferredDateFromStart_editedDateRange() throws IllegalValueException {
        logic.execute("add title from 11 Dec " + nextYear + " to 12 Dec " + nextYear + " ");
        String command = "edit 1 from 10 Dec " + nextYear + " 3pm to 7pm";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(nextYear, 12, 10, 15, 0),
                    LocalDateTime.of(nextYear, 12, 10, 19, 0)
                )
                .build());
    }

    @Test
    public void execute_editAddDueDateToEvent_error() throws IllegalValueException {
        logic.execute("add title from 11 Dec " + nextYear + " to 12 Dec " + nextYear);
        eventsCollector.reset();
        String command = "edit 1 by 13 Dec " + nextYear;
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, result.getFeedback());
    }

    @Test
    public void execute_editAddDateRangeToTaskWithDueDate_error() throws IllegalValueException {
        logic.execute("add title by 13 Dec " + nextYear);
        eventsCollector.reset();
        String command = "edit 1 from 11 Dec " + nextYear + " to 12 Dec " + nextYear;
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, result.getFeedback());
    }

    @Test
    public void execute_editDuplicatedTitle_error() throws IllegalValueException {
        logic.execute("add task");
        logic.execute("add task2");
        eventsCollector.reset();

        CommandResult result = logic.execute("edit 2 task2");
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_ALREADY_EXISTS, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoExists(logic,
            new ToDoBuilder("task")
                .build());
        assertToDoExists(logic,
            new ToDoBuilder("task2")
                .build());
    }

    @Test
    public void execute_editEventWith2DateRanges_editedTitleAndDateRange() throws IllegalValueException {
        logic.execute("add title from 10 Jan " + nextYear + " 12:00 to 21 Jan " + nextYear + " 13:00");

        logic.execute("edit 1 from today to tomorrow from 10 Nov 2011 1200h to 11 Dec 2012 1300h");
        assertToDoExists(logic,
            new ToDoBuilder("from today to tomorrow")
                .withDateRange(
                    LocalDateTime.of(2011, 11, 10, 12, 0),
                    LocalDateTime.of(2012, 12, 11, 13, 0)
                )
                .build());
    }


    @Test
    public void execute_editQuotedTitleWithBy_editedTitle() throws IllegalValueException {
        logic.execute("add title #tag");

        eventsCollector.reset();

        CommandResult result = logic.execute("edit 1 `by tomorrow`");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("by tomorrow")
                .withTags("tag")
                .build());
    }

    @Test
    public void execute_editQuotedTitleWithFromTo_editedTitle() throws IllegalValueException {
        logic.execute("add title from 10 Jan " + nextYear + " 12:00 to 21 Jan " + nextYear + " 13:00");

        eventsCollector.reset();

        String command = "edit 1 `from today to tomorrow`"
            + "from 10 Feb " + nextYear + " 12:15 to 21 Feb " + nextYear + " 13:45";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("from today to tomorrow")
                .withDateRange(
                    LocalDateTime.of(nextYear, 2, 10, 12, 15),
                    LocalDateTime.of(nextYear, 2, 21, 13, 45)
                )
                .build());
    }

    @Test
    public void execute_editWithTrailingTextAfterQuotedTitle_error() throws IllegalValueException {
        logic.execute("add title");

        eventsCollector.reset();

        String command = "edit 1 `from today to tomorrow` 13:45";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_editEmptyQuotedTitle_error() throws IllegalValueException {
        logic.execute("add title");

        eventsCollector.reset();

        CommandResult result = logic.execute("edit 1 ``");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(Messages.MISSING_TODO_TITLE + "\n" + Messages.EDIT_COMMAND_FORMAT, result.getFeedback());
    }


    @Test
    public void execute_editNoEdits_error() throws IllegalValueException {
        logic.execute("add task");

        eventsCollector.reset();

        CommandResult result = logic.execute("edit 1");
        assertTrue(result.hasError());
        assertEquals(Messages.EDIT_COMMAND_NO_EDITS, result.getFeedback());
    }

    @Test
    public void execute_editOnDateTime_edited() throws IllegalValueException {
        logic.execute("add event on 10 Jan " + nextYear);

        eventsCollector.reset();

        String command = "edit 1 on 10 Sep " + nextYear + " 11:33am";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("event")
                .withDateRange(
                    LocalDateTime.of(nextYear, 9, 10, 11, 33),
                    LocalDateTime.of(nextYear, 9, 10, 23, 59)
                )
                .build());
    }

    @Test
    public void execute_addTaskWithRecurringDeadline_added() throws IllegalValueException {
        logic.execute("add task by 10 Jan " + nextYear);

        eventsCollector.reset();

        CommandResult result = logic.execute("add task by 11 April " + nextYear + " weekly");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("task")
                .withDueDate(
                    LocalDateTime.of(nextYear, 4, 11, 0, 0),
                    Recurrence.Weekly
                )
                .build()
        );
    }
}