package seedu.commando.storage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ReadOnlyToDoList;
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
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private EventsCollector eventCollector;
    private File toDoListFile;
    private File userPrefsFile;

    @Before
    public void setUp() throws IOException {
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
        assertTrue(original.isSimilar(new ToDoList(retrieved)));
    }

    @Test
    public void getToDoListFilePath(){
        assertTrue(storageManager.getToDoListFilePath() != null);
    }

    private static ToDoList getSample() throws IllegalValueException {
        return new ToDoList()
            .add(new ToDoBuilder("valid title")
                .withTags("tag1", "tag2" )
                .withDueDate(LocalDateTime.of(2016, 5, 1, 20, 1))
                .withDateRange(LocalDateTime.of(2016, 3, 1, 20, 1),
                    LocalDateTime.of(2016, 4, 1, 20, 1))
                .build())
            .add(new ToDoBuilder("valid title 2")
                .finish(LocalDateTime.now())
                .build())
            .add(new ToDoBuilder("valid title 3")
                .withDateRange(LocalDateTime.MIN,  LocalDateTime.of(2016, 4, 1, 20, 1))
                .build())
            .add(new ToDoBuilder("valid title 4")
                .withDateRange(LocalDateTime.of(2016, 4, 1, 20, 1), LocalDateTime.MAX)
                .build());
    }
    
}
