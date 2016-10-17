package seedu.commando.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.Logic;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;

public class StroeCommandTest {
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
    public void execute_store_emptyPath() {
    	CommandResult result = logic.execute("store");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_STORE_PATH, result.getFeedback());
    	result = logic.execute("store    ");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_STORE_PATH, result.getFeedback());
    }
    
    @Test
    public void execute_store_invalidPath() {
    	CommandResult result = logic.execute("store 2\\");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_STORE_FILE, result.getFeedback());
    	result = logic.execute("store awe@#$\\");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_STORE_FILE, result.getFeedback());
    }
    
}
