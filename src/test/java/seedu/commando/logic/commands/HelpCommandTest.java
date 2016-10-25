package seedu.commando.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.wasShowHelpRequestEventPosted;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.logic.Logic;
import seedu.commando.testutil.EventsCollector;

//@@author A0139697H
public class HelpCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private Logic logic;
    private EventsCollector eventsCollector;

    @Before
    public void setup() throws IOException {
        logic = initLogic(folder);
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
