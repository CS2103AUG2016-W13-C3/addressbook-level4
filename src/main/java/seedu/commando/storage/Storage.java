package seedu.commando.storage;

import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.events.storage.DataSavingExceptionEvent;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.UserPrefs;

import java.io.IOException;
import java.util.Optional;

/**
 * API of the Storage component
 */
public interface Storage extends ToDoListStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getToDoListFilePath();

    @Override
    Optional<ReadOnlyToDoList> readToDoList() throws DataConversionException, IOException;

    @Override
    void saveToDoList(ReadOnlyToDoList toDoList) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleToDoListChangedEvent(ToDoListChangedEvent abce);
}
