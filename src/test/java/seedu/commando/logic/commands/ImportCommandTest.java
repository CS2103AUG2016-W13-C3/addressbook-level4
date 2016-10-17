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

public class ImportCommandTest {
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
    public void execute_import_emptyPath() {
    	CommandResult result = logic.execute("import");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_IMPORT_PATH, result.getFeedback());
    	result = logic.execute("import    ");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_IMPORT_PATH, result.getFeedback());
    }
    
    @Test
    public void execute_import_invalidPath() {
    	CommandResult result = logic.execute("import 2\\");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_IMPORT_FILE, result.getFeedback());
    	result = logic.execute("import awe@#$\\");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_IMPORT_FILE, result.getFeedback());
    	result = logic.execute("import 3");
    	assertTrue(result.hasError());
    	assertEquals(Messages.IMPORT_COMMAND_TYPE_ERROR, result.getFeedback());
    	result = logic.execute("import 3.doc");
    	assertTrue(result.hasError());
    	assertEquals(Messages.IMPORT_COMMAND_TYPE_ERROR, result.getFeedback());
    	result = logic.execute("import this cant be there.XMl");
    	assertTrue(result.hasError());
    	assertEquals(Messages.IMPORT_COMMAND_FILE_NOT_EXIST, result.getFeedback());
    }
    
}
