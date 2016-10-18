package seedu.commando.logic.commands;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javafx.scene.shape.Path;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.Logic;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;

public class ExportCommandTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private Model model;
	private Logic logic;
	private EventsCollector eventsCollector;
	private File toDoListFile;
	private File userPrefsFile;

	@Before
	public void setup() throws IOException {
		model = new ModelManager();

		toDoListFile = folder.newFile();
		userPrefsFile = folder.newFile();
		logic = new LogicManager(model,
				new StorageManager(toDoListFile.getAbsolutePath(), userPrefsFile.getAbsolutePath()));

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
    	assertEquals(Messages.MISSING_EXPORT_PATH, result.getFeedback());
    	logic.execute("export    ");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_EXPORT_PATH, result.getFeedback());
    }
	
	@Test
	public void execute_export_invalidPath() {
		CommandResult result = logic.execute("export test\\");
    	assertTrue(result.hasError());
    	assertEquals(Messages.MISSING_EXPORT_FILE, result.getFeedback());
	}
    
	@Test
	public void execute_export_validPath() {
	    logic.execute("add test 1");
	    CommandResult result = logic.execute("export test");
	    assertFalse(result.hasError());
	    try {
            assertTrue(Arrays.equals(Files.readAllBytes(toDoListFile.toPath()), Files.readAllBytes(Paths.get("test"))));
            Files.delete(Paths.get("test"));
        } catch (IOException e) {
        }
	}
}
