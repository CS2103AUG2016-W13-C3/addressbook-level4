package seedu.commando.logic.commands;

import static org.junit.Assert.*;
import static seedu.commando.logic.LogicManagerTest.initLogic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;
//@@author A0142230B
public class ExportCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private File toDoListFile;
    private EventsCollector eventsCollector;

    @Before
    public void setup() throws IOException {
        toDoListFile = folder.newFile();
        File userPrefsFile  = folder.newFile();
        Model model = new ModelManager();

        logic = new LogicManager(model, new StorageManager(
            toDoListFile.getAbsolutePath(),
            userPrefsFile.getAbsolutePath()
        ), new UserPrefs());

        eventsCollector = new EventsCollector();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_export_emptyPath() {
        CommandResult result = logic.execute("export");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_EXPORT_PATH
                + "\n" + Messages.getCommandFormatMessage("export").get(), result.getFeedback());
        logic.execute("export    ");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_EXPORT_PATH
                + "\n" + Messages.getCommandFormatMessage("export").get(), result.getFeedback());
    }

    @Test
    public void execute_export_invalidPath() {
        CommandResult result = logic.execute("export test\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_EXPORT_FILE, result.getFeedback());
    }

    @Test
    public void execute_export_validPath() throws IOException {
        logic.execute("add test 1");
        CommandResult result = logic.execute("export test");
        assertFalse(result.hasError());

        assertTrue(Arrays.equals(Files.readAllBytes(toDoListFile.toPath()), Files.readAllBytes(Paths.get("test"))));
        Files.delete(Paths.get("test"));
    }
}
