package seedu.commando.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.wasToDoListChangedEventPosted;

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
public class ClearCommandTest {

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
    public void execute_clear() {
        logic.execute("add value from 10 Jan 1994 12:00 to 21 Jan 1994 13:00");
        logic.execute("add title2 #tag1 #tag2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(logic.getToDoList().getToDos().size() == 2);

        CommandResult result = logic.execute("clear");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(logic.getToDoList().getToDos().size() == 0);
    }
}
