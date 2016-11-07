package seedu.commando.storage;

import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.events.storage.ToDoListSavedEvent;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.todo.ReadOnlyToDoList;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
//@@author A0142230B
/**
 * Concrete implementation of {@link Storage} for the Storage component.
 * Reads data from and writes data to the file system
 */
public class StorageManager extends ComponentManager implements Storage {
    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ToDoListStorage toDoListStorage;
    private UserPrefsStorage userPrefsStorage;

    public StorageManager(String toDoListFilePath, String userPrefsFilePath) {
        this(new XmlToDoListStorage(toDoListFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    public StorageManager(ToDoListStorage toDoListStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.toDoListStorage = toDoListStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    //================================================================================
    // CRUD user prefs operations
    //================================================================================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    @Override
    public void setUserPrefsFilePath(String filePath) {
        userPrefsStorage.setUserPrefsFilePath(filePath);
    }

    //================================================================================
    // CRUD to-do list operations
    //================================================================================

    @Override
    public String getToDoListFilePath() {
        return toDoListStorage.getToDoListFilePath();
    }

    @Override
    public Optional<ReadOnlyToDoList> readToDoList() throws DataConversionException, IOException {
        return readToDoList(toDoListStorage.getToDoListFilePath());
    }

    @Override
    public Optional<ReadOnlyToDoList> readToDoList(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return toDoListStorage.readToDoList(filePath);
    }

    @Override
    public void saveToDoList(ReadOnlyToDoList toDoList) throws IOException {
        saveToDoList(toDoList, toDoListStorage.getToDoListFilePath());
    }

    @Override
    public void saveToDoList(ReadOnlyToDoList toDoList, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        toDoListStorage.saveToDoList(toDoList, filePath);
        raise(new ToDoListSavedEvent(filePath)); // post event that to-do list has been saved to the file system
    }

	@Override
	public void setToDoListFilePath(String path) {
		logger.info("Change the to-do list file path to " + path);
		toDoListStorage.setToDoListFilePath(path);
	}
}
