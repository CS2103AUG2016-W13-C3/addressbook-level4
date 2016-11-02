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
import seedu.commando.model.todo.DateRange;
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
public class AddCommandTest {
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
    public void execute_addWithMissingTitle_error() {
        CommandResult result = logic.execute("add");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_TODO_TITLE + "\n" + Messages.getCommandFormatMessage("add").get(), result.getFeedback());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addWithTitle_added() throws IllegalValueException {
        CommandResult result = logic.execute("add valid title");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic, new ToDoBuilder("valid title").build());
    }

    @Test
    public void execute_addWithTitleDueDate_added() throws IllegalValueException {
        CommandResult result = logic.execute("add valid title by 10 Feb 2016 11:59");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("valid title")
                .withDueDate(LocalDateTime.of(2016, 2, 10, 11, 59))
                .build()
        );
    }

    @Test
    public void execute_addWithTitleDateRange_added() throws IllegalValueException {
        String command = "add valid title from 10 Dec 2016 11:59 to 11 Apr 2017 23:10";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("valid title")
                .withDateRange(
                    LocalDateTime.of(2016, 12, 10, 11, 59),
                    LocalDateTime.of(2017, 4, 11, 23, 10)
                )
                .build()
        );
    }

    @Test
    public void execute_addWithIllogicalDateRange_error() throws IllegalValueException {
        String command = "add valid title from 10 Dec 2017 11:59 to 11 Apr 2017 23:10";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_DATERANGE_END_MUST_AFTER_START + "\n" + Messages.DATE_FORMAT + "\n" +
            Messages.getCommandFormatMessage("add").get(), result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addWithMissingDateRangeStart_error() throws IllegalValueException {
        String command = "add valid title from not date to 11 Apr 2017 23:10";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.INVALID_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT + "\n" +
            Messages.getCommandFormatMessage("add").get(), result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addWithMissingDateRangeEnd_error() throws IllegalValueException {
        String command = "add valid title from 11 Apr 2017 23:10 to not date";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.INVALID_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT + "\n" +
            Messages.getCommandFormatMessage("add").get(), result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addWithTaskEndingWithBy_added() throws IllegalValueException {
        String command = "add task ending with by by 10 Oct 2016 10:10";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("task ending with by")
                .withDueDate(
                    LocalDateTime.of(2016, 10, 10, 10, 10)
                )
                .build()
        );
    }

    @Test
    public void execute_addWithInferredDateFromStart_added() throws IllegalValueException {
        String command = "add title from 10 Dec 2016 3pm to 7pm";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(2016, 12, 10, 15, 0),
                    LocalDateTime.of(2016, 12, 10, 19, 0)
                )
                .build()
        );
    }

    @Test
    public void execute_addWithTitleTags_added() throws IllegalValueException {
        String command = "add valid title #tag1 #tag2";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("valid title")
                .withTags(
                    "tag1", "tag2"
                )
                .build()
        );
    }

    @Test
    public void execute_addWithTitleDueDateTags_added() throws IllegalValueException {
        String command = "add valid title by 1 Mar 2016 20:01 #tag1 #tag2";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("valid title")
                .withTags(
                    "tag1", "tag2"
                )
                .withDueDate(
                    LocalDateTime.of(2016, 3, 1, 20, 1)
                )
                .build()
        );
    }

    @Test
    public void execute_addWithEmptyTag_added() throws IllegalValueException {
        CommandResult result = logic.execute("add title #    ");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic, new ToDoBuilder("title").build());
    }

    @Test
    public void execute_addWithTaskWithFromToBy_added() throws IllegalValueException {
        CommandResult result = logic.execute("add walk by the beach from here to there");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("walk by the beach from here to there")
                .build()
        );
    }

    @Test
    public void execute_addWithEventWith2DateRanges_added() throws IllegalValueException {
        CommandResult result = logic.execute("add walk by the beach from today to tomorrow from 10 Nov 2011 1200h to 11 Dec 2012 1300h");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("walk by the beach from today to tomorrow")
                .withDateRange(
                    LocalDateTime.of(2011, 11, 10, 12, 0),
                    LocalDateTime.of(2012, 12, 11, 13, 0)
                )
                .build()
        );
    }

    @Test
    public void execute_addWithDuplicate_error() throws IllegalValueException {
        logic.execute("add task by 10 Oct 2015");
        eventsCollector.reset();

        CommandResult result = logic.execute("add task by 10 Oct 2015");
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_ALREADY_EXISTS, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addEventWithRecurrence_added() throws IllegalValueException {
        logic.execute("add event from 10 Oct 1200h to 11 Oct 1300h monthly");

        LocalDateTime startDate = LocalDateTime.of(now.getYear(), 10, 10, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(now.getYear(), 10, 11, 13, 0);

        while (startDate.isBefore(LocalDateTime.now())) {
            startDate = startDate.plusMonths(1);
            endDate = endDate.plusMonths(1);
        }

        assertToDoExists(logic,
            new ToDoBuilder("event")
                .withDateRange(
                    startDate, endDate, Recurrence.Monthly
                )
                .build()
        );
    }

    @Test
    public void execute_addEventWithInvalidRecurrence_error() throws IllegalValueException {
        CommandResult result = logic.execute("add event from 10 Oct to 12 Oct daily");
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_DATERANGE_RECURRENCE_INVALID + "\n" + Messages.DATE_FORMAT + "\n" +
            Messages.getCommandFormatMessage("add").get(), result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addWithMissingStartEndDates_error() throws IllegalValueException {
        CommandResult result = logic.execute("add event from to 12 Oct");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT + "\n" +
            Messages.getCommandFormatMessage("add").get(), result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        eventsCollector.reset();

        result = logic.execute("add event from 12 Oct to");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT + "\n" +
            Messages.getCommandFormatMessage("add").get(), result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addWithQuotedTitle_added() throws IllegalValueException {
        CommandResult result = logic.execute("add `event from today to tomorrow`");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("event from today to tomorrow")
                .build()
        );
    }

    @Test
    public void execute_addWithTrailingTextAfterQuotedTitle_error() throws IllegalValueException {
        CommandResult result = logic.execute("add `event from today to` tomorrow");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_addWithEmptyQuotedTitle_error() throws IllegalValueException {
        CommandResult result = logic.execute("add `  ` from today to tomorrow");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(Messages.MISSING_TODO_TITLE + "\n" + Messages.ADD_COMMAND_FORMAT, result.getFeedback());
    }

    @Test
    public void execute_addWithDateRangeAndDueDate_error() throws IllegalValueException {
        CommandResult result = logic.execute("add eventask by tomorrow from today to tomorrow");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, result.getFeedback());
    }

    @Test
    public void execute_addOnDate_added() throws IllegalValueException {
        CommandResult result = logic.execute("add event on 23/11/2015");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("event")
                .withDateRange(
                    LocalDateTime.of(2015, 11, 23, 0, 0),
                    LocalDateTime.of(2015, 11, 23, 23, 59)
                )
                .build()
        );
    }

    @Test
    public void execute_addOnDateTime_added() throws IllegalValueException {
        CommandResult result = logic.execute("add event on today 1.21pm");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("event")
                .withDateRange(
                    LocalDateTime.now().withHour(13).withMinute(21),
                    LocalDateTime.now().withHour(23).withMinute(59)
                )
                .build()
        );
    }

    @Test
    public void execute_addTaskWithRecurringDeadline_added() throws IllegalValueException {
        CommandResult result = logic.execute("add task by tomorrow monthly");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("task")
                .withDueDate(
                    LocalDateTime.now().plusDays(1).withHour(0).withMinute(0),
                    Recurrence.Monthly
                )
                .build()
        );
    }
}