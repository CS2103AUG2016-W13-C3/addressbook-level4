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

//@@author A0139697H
public class UndoRedoCommandsTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;

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
    public void execute_undoAndRedoInvalidFormat() {
        CommandResult result = logic.execute("undo 2");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, UndoCommand.COMMAND_WORD), result.getFeedback());

        result = logic.execute("redo 2");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, RedoCommand.COMMAND_WORD), result.getFeedback());
    }

    @Test
    public void execute_undo() {
        logic.execute("add title");
        logic.execute("add test 3");
        logic.execute("delete 2");
        logic.execute("edit 1 titlereplaced");

        assertTrue(logic.getUiTasks().size() == 1);

        CommandResult result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 2);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 0);

        result = logic.execute("undo");
        assertTrue(result.hasError());
        assertEquals(Messages.UNDO_COMMAND_FAIL, result.getFeedback());
    }

    @Test
    public void execute_redo() {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("undo");

        assertTrue(logic.getUiTasks().size() == 1);

        CommandResult result = logic.execute("redo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 2);

        result = logic.execute("redo");
        assertTrue(result.hasError());
        assertEquals(Messages.REDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTasks().size() == 2);
    }

    @Test
    public void execute_undoEditUndoRedo() {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("undo");
        logic.execute("add title3");

        assertTrue(logic.getUiTasks().size() == 2);

        eventsCollector.reset();

        CommandResult result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 0);

        result = logic.execute("undo");
        assertTrue(result.hasError());
        assertEquals(Messages.UNDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTasks().size() == 0);

        result = logic.execute("redo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("redo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 2);

        result = logic.execute("redo");
        assertTrue(result.hasError());
        assertEquals(Messages.REDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTasks().size() == 2);
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
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("edit 1 title4");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("redo");
        assertTrue(result.hasError());
        assertEquals(Messages.REDO_COMMAND_FAIL, result.getFeedback());
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(ifToDoExists(logic,
            new ToDoBuilder("title3")
                .build()));

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 0);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 2);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 1);

        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(logic.getUiTasks().size() == 0);
    }
}