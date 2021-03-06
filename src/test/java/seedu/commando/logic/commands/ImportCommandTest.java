package seedu.commando.logic.commands;

import static org.junit.Assert.*;
import static seedu.commando.testutil.TestHelper.wasToDoListChangedEventPosted;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

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
public class ImportCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private File toDoListFile;

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
    public void execute_importEmptyPath_error() {
        CommandResult result = logic.execute("import");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_PATH
                + "\n" + Messages.getCommandFormatMessage("import").get(), result.getFeedback());
        result = logic.execute("import    ");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_PATH
                + "\n" + Messages.getCommandFormatMessage("import").get(), result.getFeedback());
    }

    @Test
    public void execute_importInvalidPath_error() {
        CommandResult result = logic.execute("import 2\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_FILE, result.getFeedback());
        result = logic.execute("import awe@#$\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_FILE, result.getFeedback());
        result = logic.execute("import this cant be there.XMl");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.IMPORT_COMMAND_FILE_NOT_EXIST, "this cant be there.XMl"), result.getFeedback());
    }

    @Test
    public void execute_importInvalidData_error() throws IOException {
        File temp = folder.newFile();
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("somewrongdata");
        Files.write(temp.toPath(), lines, Charset.forName("UTF-8"));
        
        CommandResult result = logic.execute("import " + temp.getPath());
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.IMPORT_COMMAND_INVALID_DATA, temp.getPath()), result.getFeedback());
    }

    @Test
    public void execute_importValidPath_imported() throws IOException {
        String exportFilePath = folder.getRoot() + "/test.xml";

        logic.execute("add test1");
        logic.execute("add test2");
        logic.execute("export " + exportFilePath);
        logic.execute("clear");
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(logic.getToDoList().getToDos().size() == 0);

        CommandResult result = logic.execute("import " + exportFilePath);
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(logic.getToDoList().getToDos().size() == 2);
    }
}
