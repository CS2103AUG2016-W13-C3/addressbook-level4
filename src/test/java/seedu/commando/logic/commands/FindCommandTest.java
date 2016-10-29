package seedu.commando.logic.commands;

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

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.*;

//@@author A0139697H
public class FindCommandTest {
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
    public void execute_findKeywords_filtered() throws IllegalValueException {
        logic.execute("add Title"); // case insensitivity
        logic.execute("add title2"); // superstrings
        logic.execute("add title 3");
        logic.execute("add somethingelse");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("find title");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoExistsFiltered(logic,
            new ToDoBuilder("Title")
                .build());
        assertToDoExistsFiltered(logic,
            new ToDoBuilder("title2")
                .build());
        assertToDoExistsFiltered(logic,
            new ToDoBuilder("title 3")
                .build());
        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("somethingelse")
                .build());
    }

    @Test
    public void execute_findTagsWithKeywords_filtered() throws IllegalValueException {
        logic.execute("add title #tag");
        logic.execute("add somethingelse");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("find tag");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoExistsFiltered(logic,
            new ToDoBuilder("title")
                .withTags("tag")
                .build());
        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("somethingelse")
                .build());
    }

    @Test
    public void execute_findTags_filtered() throws IllegalValueException {
        logic.execute("add Title #Tag"); // case insensitivity
        logic.execute("add title2 #tag2"); // superstrings
        logic.execute("add title 3 #tag #tag2"); // multiple tags
        logic.execute("add tag"); // title with tag
        logic.execute("add somethingelse");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("find #tag");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoExistsFiltered(logic,
            new ToDoBuilder("Title")
                .withTags("Tag")
                .build());
        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("Title2")
                .withTags("tag2")
                .build());
        assertToDoExistsFiltered(logic,
            new ToDoBuilder("title 3")
                .withTags("tag", "tag2")
                .build());
        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("tag")
                .build());
        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("somethingelse")
                .build());
    }


    @Test
    public void execute_findMultipleTags_filtered() throws IllegalValueException {
        logic.execute("add title #tag");
        logic.execute("add title2 #tag #tag2");
        logic.execute("add title3 #tag #tag2 #tag3");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("find #tag #tag2");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("title")
                .withTags("tag")
                .build());
        assertToDoExistsFiltered(logic,
            new ToDoBuilder("title2")
                .withTags("tag", "tag2")
                .build());
        assertToDoExistsFiltered(logic,
            new ToDoBuilder("title3")
                .withTags("tag", "tag2", "tag3")
                .build());
    }

    @Test
    public void execute_findKeywordsWithTags_filtered() throws IllegalValueException {
        logic.execute("add title #tag");
        logic.execute("add title3 #tag #tag2");
        logic.execute("add title3");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        CommandResult result = logic.execute("find title 3 #tag");
        assertFalse(result.hasError());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("title")
                .withTags("tag")
                .build());
        assertToDoExistsFiltered(logic,
            new ToDoBuilder("title3")
                .withTags("tag", "tag2")
                .build());
        assertToDoNotExistsFiltered(logic,
            new ToDoBuilder("title3")
                .build());
    }
}