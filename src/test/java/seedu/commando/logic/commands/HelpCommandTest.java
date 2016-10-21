package seedu.commando.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.commando.testutil.TestHelper.wasShowHelpRequestEventPosted;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.logic.Logic;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;

public class HelpCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private Logic logic;
    private EventsCollector eventsCollector;
    private File toDoListFile;
    private File userPrefsFile;

    @Before
    public void setup() throws IOException {
        Model model = new ModelManager();

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
    public void execute_help() {
        logic.execute("help");
        assertTrue(wasShowHelpRequestEventPosted(eventsCollector));
    }
}
