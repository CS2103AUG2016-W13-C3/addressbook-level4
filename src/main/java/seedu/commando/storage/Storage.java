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
    /**
     * Saves the current version of the to-do list to the hard disk
     * at the default filepath
     * Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleToDoListChangedEvent(ToDoListChangedEvent event);
}
