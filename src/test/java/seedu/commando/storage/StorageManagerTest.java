package seedu.commando.storage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.events.storage.DataSavingExceptionEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.Title;
import seedu.commando.model.todo.ToDo;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.UserPrefs;
import seedu.commando.testutil.EventsCollector;
import seedu.commando.testutil.ToDoBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private EventsCollector eventCollector;
    private File toDoListFile;
    private File userPrefsFile;

    @Before
    public void setup() throws IOException {
        toDoListFile = folder.newFile();
        userPrefsFile = folder.newFile();
        storageManager = new StorageManager(
            toDoListFile.getAbsolutePath(),
            userPrefsFile.getAbsolutePath()
        );
        eventCollector = new EventsCollector();
    }

    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void userPrefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6, false);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void toDoListReadSave() throws Exception {
        ToDoList original = getSample();
        storageManager.saveToDoList(original);
        ReadOnlyToDoList retrieved = storageManager.readToDoList().get();
        assertEquals(original, new ToDoList(retrieved));
    }

    @Test
    public void getToDoListFilePath(){
        assertTrue(storageManager.getToDoListFilePath() != null);
    }

    @Test
    public void handleToDoListChangedEvent_exceptionThrown_eventRaised() throws IOException {
        // Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new ExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"));
        storage.handleToDoListChangedEvent(new ToDoListChangedEvent(new ToDoList()));
        assertTrue(eventCollector.hasCollectedEvent(DataSavingExceptionEvent.class));
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    private static class ExceptionThrowingStub extends XmlToDoListStorage {

        public ExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveToDoList(ReadOnlyToDoList toDoList, String filePath) throws IOException {
            throw new IOException();
        }
    }

    private static ToDoList getSample() throws IllegalValueException {
        ToDoList toDoList = new ToDoList();
        toDoList.add(new ToDoBuilder("valid title")
                    .withTags("tag1", "tag2" )
                    .withDueDate(LocalDateTime.of(2016, 5, 1, 20, 1))
                    .withDateRange(LocalDateTime.of(2016, 3, 1, 20, 1),
                                   LocalDateTime.of(2016, 4, 1, 20, 1))
                    .build());
        toDoList.add(new ToDoBuilder("valid title 2")
                    .isFinished(true)
                    .build());
        return toDoList;
    }
    
}
