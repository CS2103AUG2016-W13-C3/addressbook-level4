package seedu.commando.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import seedu.commando.commons.core.Messages;
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
        logic = initLogic();
        eventsCollector = new EventsCollector();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }
    

    @Test
    public void execute_help_eventPosted() {
        logic.execute("help");
        assertTrue(wasShowHelpRequestEventPosted(eventsCollector));
    }

    @Test
    public void execute_helpWithWord_eventPosted() {
        logic.execute("help add");
        assertTrue(wasShowHelpRequestEventPosted(eventsCollector));
    }

    @Test
    public void execute_helpWithInvalidWord_error() {
        CommandResult result = logic.execute("help invalid word");
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.UNKNOWN_COMMAND_FOR_HELP, "invalid word"), result.getFeedback());
        assertFalse(wasShowHelpRequestEventPosted(eventsCollector));
    }
}
