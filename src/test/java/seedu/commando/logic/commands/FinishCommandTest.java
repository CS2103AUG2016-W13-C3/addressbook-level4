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

import static org.junit.Assert.*;
import static seedu.commando.testutil.TestHelper.*;

public class FinishCommandTest {
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
    public void execute_finish_noSuchIndex() {
        CommandResult result = logic.execute("delete 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_finish_invalidIndex() {
        CommandResult result = logic.execute("finish 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }

    @Test
    public void execute_finish_invalidIndex2() {
        CommandResult result = logic.execute("finish -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_finish_missingIndex() {
        CommandResult result = logic.execute("finish missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX, result.getFeedback());
    }

    @Test
    public void execute_finish_index() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title2")
                .build()));

        CommandResult result = logic.execute("finish 2");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertFalse(ifToDoExists(model,
            new ToDoBuilder("title2")
                .build()));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title")
                .build()));
    }
}