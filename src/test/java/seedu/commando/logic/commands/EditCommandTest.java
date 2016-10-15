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

    private Model model;
    private Logic logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();
    private File toDoListFile;
    private File userPrefsFile;

    @Before
    public void setup() throws IOException {
        model = new ModelManager();

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
        logic.execute("add title");

        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .build()));

        String command = "edit 1 new title";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("new title")
                .build()));
    }

    @Test
    public void execute_edit_tags() throws IllegalValueException {
        logic.execute("add title #tag1 #tag2");

        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .withTags("tag1", "tag2")
                .build()));

        String command = "edit 1 #tag2 #tag3 #tag4";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .withTags("tag2", "tag3", "tag4")
                .build()));
    }

    @Test
    public void execute_edit_dueDate() throws IllegalValueException {
        logic.execute("add title by 10 Jan 1994 12:00");

        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .withDueDate(LocalDateTime.of(1994, 1, 10, 12, 0))
                .build()));

        String command = "edit 1 by 11 Apr 1995 00:12";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .withDueDate(LocalDateTime.of(1995, 4, 11, 0, 12))
                .build()));
    }

    @Test
    public void execute_edit_dateRange() throws IllegalValueException {
        logic.execute("add title from 10 Jan 1994 12:00 to 21 Jan 1994 13:00");

        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(1994, 1, 10, 12, 0),
                    LocalDateTime.of(1994, 1, 21, 13, 0)
                )
                .build()));

        String command = "edit 1 from 10 Sep 1984 12:00 to 21 Sep 1984 13:00";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .withDateRange(
                    LocalDateTime.of(1984, 9, 10, 12, 0),
                    LocalDateTime.of(1984, 9, 21, 13, 0)
                )
                .build()));
    }

}