package seedu.commando.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.ui.ExitAppRequestEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.commands.*;
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
        logic.execute("add value from 10 Jan 1994 12:00 to 21 Jan 1994 13:00");
        logic.execute("add title2 #tag1 #tag2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDos().size() == 2);

        CommandResult result = logic.execute("clear");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDos().size() == 0);
    }

    @Test
    public void execute_help() {
        logic.execute("help");
        assertTrue(wasShowHelpRequestEventPosted(eventsCollector));
    }

    @Test
    public void execute_finish_invalidIndex() {
        CommandResult result = logic.execute("finish 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
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
    public void execute_undo() {
        logic.execute("add title");
        logic.execute("add title 2");
        logic.execute("add test 3");
        logic.execute("delete 2");
        logic.execute("edit 1 titlereplaced");
        
        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDos().size() == 2);
        
        CommandResult result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDos().size() == 2);
        
        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDos().size() == 3);
        
        result = logic.execute("undo");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(model.getToDoList().getToDos().size() == 2);


    }

    @Test
    public void execute_exit()  {
        logic.execute("exit");
        assertTrue(eventsCollector.hasCollectedEvent(ExitAppRequestEvent.class));
    }
}
