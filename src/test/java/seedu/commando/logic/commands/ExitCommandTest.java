package seedu.commando.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.commando.logic.LogicManagerTest.initLogic;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.events.logic.ExitAppRequestEvent;
import seedu.commando.logic.Logic;
import seedu.commando.testutil.EventsCollector;

public class ExitCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private Logic logic;
    private EventsCollector eventsCollector;

    @Before
    public void setUp() throws IOException {
        logic = initLogic();
        eventsCollector = new EventsCollector();
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }
    
    @Test
    public void execute_exit_eventPosted()  {
        logic.execute("exit");
        assertTrue(eventsCollector.hasCollectedEvent(ExitAppRequestEvent.class));
    }
}
