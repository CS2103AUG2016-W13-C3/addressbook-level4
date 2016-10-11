package seedu.address.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.*;
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

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
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
    public void execute_emptyString() {
        CommandResult result = logic.execute("");
        assertTrue(result.hasError());
        assertEquals(Messages.MESSAGE_MISSING_COMMAND_WORD, result.getFeedback());
    }

    @Test
    public void execute_unknownCommand() {
        CommandResult result = logic.execute("unknownCommand");
        assertTrue(result.hasError());
        assertEquals(Messages.MESSAGE_UNKNOWN_COMMAND, result.getFeedback());
    }

    @Test
    public void execute_clear() {
        logic.execute("add title from 10 Jan 1994 12:00 to 21 Jan 1994 13:00");
        logic.execute("add title2 #tag1 #tag2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDoList().size() == 2);

        CommandResult result = logic.execute("clear");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDoList().size() == 0);
    }

    @Test
    public void execute_help() {
        logic.execute("help");
        assertTrue(wasShowHelpRequestEventPosted(eventsCollector));
    }

    @Test
    public void execute_find_keywords() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");
        logic.execute("add title 3");
        logic.execute("add somethingelse");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("find title");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExistsFiltered(model,
            new ToDoBuilder("title")
                .build()));
        assertTrue(ifToDoExistsFiltered(model,
            new ToDoBuilder("title2")
                .build()));
        assertTrue(ifToDoExistsFiltered(model,
            new ToDoBuilder("title 3")
                .build()));
        assertFalse(ifToDoExistsFiltered(model,
            new ToDoBuilder("somethingelse")
                .build()));
    }

    @Test
    public void execute_exit()  {
        logic.execute("exit");
        assertTrue(eventsCollector.hasCollectedEvent(ExitAppRequestEvent.class));
    }
}
