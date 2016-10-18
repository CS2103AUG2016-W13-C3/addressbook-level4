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

public class UndoRedoCommandsTest {
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
        userPrefsFile = folder.newFile();
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
    public void execute_undo() {
        logic.execute("add title");
        logic.execute("add test 3");
        logic.execute("delete 2");
        logic.execute("edit 1 titlereplaced");

        assertTrue(logic.getUiTaskList().size() == 1);

        CommandResult result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 2);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 0);

        result = logic.execute("undo");
        assertTrue(result.hasError());
        assertEquals(Messages.UNDO_COMMAND_FAIL, result.getFeedback());
    }

    @Test
    public void execute_redo() {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("undo");

        assertTrue(logic.getUiTaskList().size() == 1);

        CommandResult result = logic.execute("redo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 2);

        result = logic.execute("redo");
        assertTrue(result.hasError());
        assertEquals(Messages.REDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTaskList().size() == 2);
    }

    @Test
    public void execute_undoEditUndoRedo() {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("undo");
        logic.execute("add title3");

        assertTrue(logic.getUiTaskList().size() == 2);

        eventsCollector.reset();

        CommandResult result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 0);

        result = logic.execute("undo");
        assertTrue(result.hasError());
        assertEquals(Messages.UNDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTaskList().size() == 0);

        result = logic.execute("redo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("redo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 2);

        result = logic.execute("redo");
        assertTrue(result.hasError());
        assertEquals(Messages.REDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTaskList().size() == 2);
    }

    @Test
    public void execute_undoClearAndEdit() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("clear");
        logic.execute("add title3");
        logic.execute("clear");

        CommandResult result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("edit 1 title4");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("redo");
        assertTrue(result.hasError());
        assertEquals(Messages.REDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title3")
                .build()));

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 0);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 2);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTaskList().size() == 0);
    }
}