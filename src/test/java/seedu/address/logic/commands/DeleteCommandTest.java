package seedu.address.logic.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.EventsCollector;
import seedu.address.testutil.ToDoBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.testutil.TestHelper.*;

public class DeleteCommandTest {
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
    public void execute_delete_noSuchIndex() {
        CommandResult result = logic.execute("delete 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_delete_invalidIndex() {
        CommandResult result = logic.execute("delete 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }

    @Test
    public void execute_delete_invalidIndex2() {
        CommandResult result = logic.execute("delete -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_delete_missingIndex() {
        CommandResult result = logic.execute("delete missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MESSAGE_MISSING_TODO_ITEM_INDEX, result.getFeedback());
    }

    @Test
    public void execute_delete_index() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExists(model,
            new ToDoBuilder("title2")
                .build()));

        CommandResult result = logic.execute("delete 2");
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