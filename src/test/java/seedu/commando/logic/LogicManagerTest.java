package seedu.commando.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.events.storage.DataSavingExceptionEvent;
import seedu.commando.logic.commands.*;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.testutil.EventsCollector;
import seedu.commando.testutil.StorageStub;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for LogicManager
 * More tests for specific command words might also be in {@link seedu.commando.logic.commands}
 */
public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    private LogicManager logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();

    @Before
    public void setup() throws IOException {
        eventsCollector = new EventsCollector();
        logic = initLogic();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    public static LogicManager initLogic() throws IOException {
        Model model = new ModelManager();
        return new LogicManager(model, new StorageStub(), new UserPrefs());
    }

    @Test
    public void execute_emptyString() {
        CommandResult result = logic.execute("");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_COMMAND_WORD, result.getFeedback());
    }

    @Test
    public void execute_unknownCommand() {
        CommandResult result = logic.execute("unknownCommand");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.UNKNOWN_COMMAND, "unknowncommand"), result.getFeedback());
    }

    @Test
    public void handleToDoListChangedEvent_exceptionThrown_eventRaised() throws IOException {
        logic.handleToDoListChangedEvent(new ToDoListChangedEvent(new ToDoList()));
        assertTrue(eventsCollector.hasCollectedEvent(StorageStub.ToDoListSavedEvent.class));
    }
}
