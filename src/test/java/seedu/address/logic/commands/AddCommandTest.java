package seedu.address.logic.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.EventsCollector;
import seedu.address.testutil.ToDoBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.testutil.TestHelper.*;

public class AddCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Model model;
    private Logic logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();
    private File toDoListFile;
    private File userPrefsFile;

    @Before
    public void setup() throws IOException {
        model = new ModelManager();

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
    public void execute_add_missingTitle() {
        CommandResult result = logic.execute("add");
        assertTrue(result.hasError());
        assertEquals(Messages.MESSAGE_MISSING_TODO_TITLE, result.getFeedback());

        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_add_title() throws IllegalValueException {
        CommandResult result = logic.execute("add valid title");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model, new ToDoBuilder("valid title").build()));
    }

    @Test
    public void execute_add_titleDueDate() throws IllegalValueException {
        CommandResult result = logic.execute("add valid title by 10 Feb 2016 11:59");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("valid title")
                .withDueDate(LocalDateTime.of(2016, 2, 10, 11, 59))
                .build()));
    }

    @Test
    public void execute_add_titleDateRange() throws IllegalValueException {
        String command = "add valid title from 10 Dec 2016 11:59 to 11 Apr 2017 23:10";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("valid title")
                .withDateRange(
                    LocalDateTime.of(2016, 12, 10, 11, 59),
                    LocalDateTime.of(2017, 4, 11, 23, 10)
                )
                .build()));
    }

    @Test
    public void execute_add_titleTags() throws IllegalValueException {
        String command = "add valid title #tag1 #tag2";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("valid title")
                .withTags(
                    "tag1", "tag2"
                )
                .build()));
    }

    @Test
    public void execute_add_titleDueDateTags() throws IllegalValueException {
        String command = "add valid title by 1 Mar 2016 20:01 #tag1 #tag2";
        CommandResult result = logic.execute(command);
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(ifToDoExists(model,
            new ToDoBuilder("valid title")
                .withTags(
                    "tag1", "tag2"
                )
                .withDueDate(
                    LocalDateTime.of(2016, 3, 1, 20, 1)
                )
                .build()));
    }
}