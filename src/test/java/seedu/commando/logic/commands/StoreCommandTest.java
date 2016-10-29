package seedu.commando.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import seedu.commando.model.UserPrefs;
import seedu.commando.storage.Storage;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;
//@@author A0142230B
public class StoreCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Model model;
    private Logic logic;
    private Storage storage;
    private EventsCollector eventsCollector;
    private File toDoListFile;

    @Before
    public void setup() throws IOException {
        model = new ModelManager();

        toDoListFile = folder.newFile();
        File userPrefsFile = folder.newFile();
        storage = new StorageManager(
                toDoListFile.getAbsolutePath(),
                userPrefsFile.getAbsolutePath()
                );
        logic = new LogicManager(model, storage, new UserPrefs());

        eventsCollector = new EventsCollector();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_storeEmptyPath_error() {
        CommandResult result = logic.execute("store");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_PATH
                + "\n" + Messages.getCommandFormatMessage("store").get(), result.getFeedback());
        result = logic.execute("store    ");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_PATH
                + "\n" + Messages.getCommandFormatMessage("store").get(), result.getFeedback());
    }

    @Test
    public void execute_storeInvalidPath_error() {
        CommandResult result = logic.execute("store 2\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_FILE, result.getFeedback());
        result = logic.execute("store awe@#$\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_FILE, result.getFeedback());
    }
    
    @Test
    public void execute_storeValidPath_filePathChanged() throws IOException {
        Files.deleteIfExists(Paths.get("test.xml"));
        CommandResult result = logic.execute("store test.xml");
        assertFalse(result.hasError());
        assertTrue(storage.getToDoListFilePath().equals("test.xml"));
        Files.delete(Paths.get("test.xml"));
    }

    @Test
    public void execute_storeValidPath_fileSaved() {
        CommandResult result = logic.execute("store test");
        assertFalse(result.hasError());
        assertEquals(String.format(Messages.STORE_COMMAND, "test"), result.getFeedback());
    }

}
