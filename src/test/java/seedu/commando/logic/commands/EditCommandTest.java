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
import seedu.commando.logic.LogicManager;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;
import seedu.commando.testutil.ToDoBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.testutil.TestHelper.*;

public class EditCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private int nextYear = LocalDateTime.now().getYear() + 1;
    private File toDoListFile;
    private File userPrefsFile;

    @Before
    public void setup() throws IOException {
        Model model = new ModelManager();

        toDoListFile = folder.newFile();
        userPrefsFile  = folder.newFile();
        logic = new LogicManager(model, new StorageManager(
            toDoListFile.getAbsolutePath(),
            userPrefsFile.getAbsolutePath()
        ));

        eventsCollector = new EventsCollector();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_edit_noSuchIndex() {
        CommandResult result = logic.execute("edit 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_edit_invalidIndex() {
        CommandResult result = logic.execute("edit 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }

    @Test
    public void execute_edit_invalidIndex2() {
        CommandResult result = logic.execute("edit -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_edit_missingIndex() {
        CommandResult result = logic.execute("edit missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX, result.getFeedback());
    }

    @Test
    public void execute_edit_title() throws IllegalValueException {
        logic.execute("add title #tag");

        String command = "edit 1 new title";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("new title")
                .withTags("tag")
                .build()));
    }

    @Test
    public void execute_edit_tags() throws IllegalValueException {
        logic.execute("add title #tag1 #tag2");

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withTags("tag1", "tag2")
                .build()));

        String command = "edit 1 #tag2 #tag3 #tag4";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withTags("tag2", "tag3", "tag4")
                .build()));
    }

    @Test
    public void execute_edit_titleRemoveTags() throws IllegalValueException {
        logic.execute("add title #tag1 #tag2");

        CommandResult result = logic.execute("edit 1 new title #");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("new title")
                .build()));
    }

    @Test
    public void execute_edit_dueDate() throws IllegalValueException {
        logic.execute("add title by 10 Jan " + nextYear + " 12:00");

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDueDate(LocalDateTime.of(nextYear, 1, 10, 12, 0))
                .build()));

        String command = "edit 1 by 11 Apr " + (nextYear + 1) + " 00:12";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDueDate(LocalDateTime.of(nextYear + 1, 4, 11, 0, 12))
                .build()));
    }

    @Test
    public void execute_edit_dateRange() throws IllegalValueException {
        logic.execute("add title from 10 Jan " + nextYear + " 12:00 to 21 Jan " + nextYear + " 13:00");

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(nextYear, 1, 10, 12, 0),
                    LocalDateTime.of(nextYear, 1, 21, 13, 0)
                )
                .build()));

        String command = "edit 1 from 10 Sep " + nextYear + " 12:00 to 21 Sep " + nextYear + " 13:00";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(nextYear, 9, 10, 12, 0),
                    LocalDateTime.of(nextYear, 9, 21, 13, 0)
                )
                .build()));
    }

    @Test
    public void execute_edit_invalidDateRange() throws IllegalValueException {
        logic.execute("add title from 11 Dec " + nextYear + " to 12 Dec " + nextYear);
        eventsCollector.reset();
        String command = "edit 1 from 10 Dec " + nextYear + " 11:59 to 11 Apr " + nextYear + " 23:10";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_DATERANGE_CONSTRAINTS, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_edit_inferredDateFromStart() throws IllegalValueException {
        logic.execute("add title from 11 Dec " + nextYear + " to 12 Dec " + nextYear + " ");
        String command = "edit 1 from 10 Dec " + nextYear + " 3pm to 7pm";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(nextYear, 12, 10, 15, 0),
                    LocalDateTime.of(nextYear, 12, 10, 19, 0)
                )
                .build()));
    }

    @Test
    public void execute_edit_cannotAddDueDateToEvent() throws IllegalValueException {
        logic.execute("add title from 11 Dec " + nextYear + " to 12 Dec " + nextYear);
        eventsCollector.reset();
        String command = "edit 1 by 13 Dec " + nextYear;
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, result.getFeedback());
    }

    @Test
    public void execute_edit_cannotAddDateRangeToTaskWithDueDate() throws IllegalValueException {
        logic.execute("add title by 13 Dec " + nextYear);
        eventsCollector.reset();
        String command = "edit 1 from 11 Dec " + nextYear + " to 12 Dec " + nextYear;
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, result.getFeedback());
    }

    @Test
    public void execute_edit_cannotHaveDuplicates() throws IllegalValueException {
        logic.execute("add task");
        logic.execute("add task2");
        eventsCollector.reset();

        CommandResult result = logic.execute("edit 1 task2");
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_ALREADY_EXISTS, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("task")
                .build()));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("task2")
                .build()));
    }
}