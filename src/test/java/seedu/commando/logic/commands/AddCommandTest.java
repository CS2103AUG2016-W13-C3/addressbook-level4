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

public class AddCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();
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
    public void execute_add_missingTitle() {
        CommandResult result = logic.execute("add");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_TODO_TITLE, result.getFeedback());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_add_title() throws IllegalValueException {
        CommandResult result = logic.execute("add valid title");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic, new ToDoBuilder("valid title").build()));
    }

    @Test
    public void execute_add_titleDueDate() throws IllegalValueException {
        CommandResult result = logic.execute("add valid title by 10 Feb 2016 11:59");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("valid title")
                .withDueDate(LocalDateTime.of(2016, 2, 10, 11, 59))
                .build()));
    }

    @Test
    public void execute_add_titleDateRange() throws IllegalValueException {
        String command = "add valid title from 10 Dec 2016 11:59 to 11 Apr 2017 23:10";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("valid title")
                .withDateRange(
                    LocalDateTime.of(2016, 12, 10, 11, 59),
                    LocalDateTime.of(2017, 4, 11, 23, 10)
                )
                .build()));
    }

    @Test
    public void execute_add_illogicalDateRange() throws IllegalValueException {
        String command = "add valid title from 10 Dec 2017 11:59 to 11 Apr 2017 23:10";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_DATERANGE_CONSTRAINTS, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_add_missingDateRangeStart() throws IllegalValueException {
        String command = "add valid title from not date to 11 Apr 2017 23:10";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.INVALID_TODO_DATERANGE_START, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_add_missingDateRangeEnd() throws IllegalValueException {
        String command = "add valid title from 11 Apr 2017 23:10 to not date";
        CommandResult result = logic.execute(command);
        assertTrue(result.hasError());
        assertEquals(Messages.INVALID_TODO_DATERANGE_END, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_add_taskEndingWithBy() throws IllegalValueException {
        String command = "add task ending with by by 10 Oct 2016 10:10";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("task ending with by")
                .withDueDate(
                    LocalDateTime.of(2016, 10, 10, 10, 10)
                )
                .build()));
    }

    @Test
    public void execute_add_inferredDateFromStart() throws IllegalValueException {
        String command = "add title from 10 Dec 2016 3pm to 7pm";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(2016, 12, 10, 15, 0),
                    LocalDateTime.of(2016, 12, 10, 19, 0)
                )
                .build()));
    }

    @Test
    public void execute_add_titleTags() throws IllegalValueException {
        String command = "add valid title #tag1 #tag2";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("valid title")
                .withTags(
                    "tag1", "tag2"
                )
                .build()));
    }

    @Test
    public void execute_add_titleDueDateTags() throws IllegalValueException {
        String command = "add valid title by 1 Mar 2016 20:01 #tag1 #tag2";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("valid title")
                .withTags(
                    "tag1", "tag2"
                )
                .withDueDate(
                    LocalDateTime.of(2016, 3, 1, 20, 1)
                )
                .build()));
    }

    @Test
    public void execute_add_emptyTag() throws IllegalValueException {
        logic.execute("add title #    ");

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title")
                .build()));
    }

    @Test
    public void execute_add_taskWithFromToBy() throws IllegalValueException {
        logic.execute("add walk by the beach from here to there");

        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("walk by the beach from here to there")
                .build()));
    }

    @Test
    public void execute_add_cannotAddDuplicate() throws IllegalValueException {
        logic.execute("add task by 10 Oct 2015");
        eventsCollector.reset();

        CommandResult result = logic.execute("add task by 10 Oct 2015");
        assertTrue(result.hasError());
        assertEquals(Messages.TODO_ALREADY_EXISTS, result.getFeedback());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }
}