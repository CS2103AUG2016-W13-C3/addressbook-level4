package seedu.commando.logic.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.logic.ToDoListFilePathChangeRequestEvent;
import seedu.commando.commons.events.storage.ToDoListSavedEvent;
import seedu.commando.logic.Logic;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.model.UserPrefs;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;
//@@author A0142230B
public class ExportCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private File toDoListFile;
    private EventsCollector eventsCollector;

    @Before
    public void setUp() throws IOException {
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
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_exportEmptyPath_error() {
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
    public void execute_exportInvalidPath_error() {
        CommandResult result = logic.execute("export test\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_EXPORT_FILE, result.getFeedback());
    }

    @Test
    public void execute_exportValidPath_exported() throws IOException {
        String exportFilePath = folder.getRoot() + "/test";

        logic.execute("add test 1");
        CommandResult result = logic.execute("export " + exportFilePath);
        assertFalse(result.hasError());

        assertTrue(Arrays.equals(Files.readAllBytes(toDoListFile.toPath()), Files.readAllBytes(Paths.get(exportFilePath))));
    }

    @Test
    public void execute_exportFileExists_error() throws IOException {
        File file = folder.newFile();
        logic.execute("add test 1");
        CommandResult result = logic.execute("export " + file.getPath());
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.EXPORT_COMMAND_FILE_EXIST, file.getPath()), result.getFeedback());
    }

    @Test
    public void execute_exportToExistingFile_error() throws IOException, InterruptedException {
        File file = folder.newFile();

        CommandResult result = logic.execute("export " + file.getPath());
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.EXPORT_COMMAND_FILE_EXIST, file.getPath()), result.getFeedback());
    }

    @Test
    public void execute_exportToExistingFileButOverride_fileSaved() throws IOException, InterruptedException {
        String filePath = folder.newFile().getPath();

        CommandResult result = logic.execute("export " + filePath + " override");
        assertFalse(result.hasError());
        assertEquals(String.format(Messages.EXPORT_COMMAND, filePath), result.getFeedback());
        assertTrue(Arrays.equals(Files.readAllBytes(toDoListFile.toPath()), Files.readAllBytes(Paths.get(filePath))));
    }
}
