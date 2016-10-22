package seedu.commando.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.ifToDoExistsFiltered;
import static seedu.commando.testutil.TestHelper.wasToDoListChangedEventPosted;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.Logic;
import seedu.commando.testutil.EventsCollector;
import seedu.commando.testutil.ToDoBuilder;

public class RecallCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();

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
    public void execute_recall_keywords() throws IllegalValueException {
        logic.execute("add Title"); // case insensitivity
        logic.execute("add title2"); // superstrings
        logic.execute("add title 3"); // spaces
        logic.execute("add somethingelse");
        
        logic.execute("finish 1");
        logic.execute("finish 1");
        logic.execute("finish 1");
        logic.execute("finish 1");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("recall title");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        
        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("Title")
                .build()));
        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title2")
                .build()));
        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title 3")
                .build()));
        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("somethingelse")
                .build()));
    }
    
    @Test
    public void execute_find_tagsWithKeywords() throws IllegalValueException {
        logic.execute("add title #tag");
        logic.execute("add somethingelse");
        
        logic.execute("finish 1");
        logic.execute("finish 1");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("recall tag");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title")
                .withTags("tag")
                .build()));
        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("somethingelse")
                .build()));
    }

    @Test
    public void execute_find_tags() throws IllegalValueException {
        logic.execute("add Title #Tag"); // case insensitivity
        logic.execute("add title2 #tag2"); // superstrings
        logic.execute("add title 3 #tag #tag2"); // multiple tags
        logic.execute("add tag"); // title with tag
        logic.execute("add somethingelse");
        
        logic.execute("finish 1");
        logic.execute("finish 1");
        logic.execute("finish 1");
        logic.execute("finish 1");
        logic.execute("finish 1");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("recall #tag");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("Title")
                .withTags("Tag")
                .build()));
        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("Title2")
                .withTags("tag2")
                .build()));
        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title 3")
                .withTags("tag", "tag2")
                .build()));
        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("tag")
                .build()));
        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("somethingelse")
                .build()));
    }


    @Test
    public void execute_find_multipleTags() throws IllegalValueException {
        logic.execute("add title #tag");
        logic.execute("add title2 #tag #tag2");
        logic.execute("add title3 #tag #tag2 #tag3");
        
        logic.execute("finish 1");
        logic.execute("finish 1");
        logic.execute("finish 1");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("recall #tag #tag2");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title")
                .withTags("tag")
                .build()));
        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title2")
                .withTags("tag", "tag2")
                .build()));
        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title3")
                .withTags("tag", "tag2", "tag3")
                .build()));
    }

    @Test
    public void execute_find_keywordsWithTags() throws IllegalValueException {
        logic.execute("add title #tag");
        logic.execute("add title3 #tag #tag2");
        logic.execute("add title3");
        
        logic.execute("finish 1");
        logic.execute("finish 1");
        logic.execute("finish 1");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("recall title 3 #tag");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title")
                .withTags("tag")
                .build()));
        assertTrue(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title3")
                .withTags("tag", "tag2")
                .build()));
        assertFalse(ifToDoExistsFiltered(logic,
            new ToDoBuilder("title3")
                .build()));
    }
}
