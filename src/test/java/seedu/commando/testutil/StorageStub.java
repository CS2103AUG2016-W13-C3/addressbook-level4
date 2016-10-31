package seedu.commando.testutil;

import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.events.BaseEvent;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.storage.Storage;

import java.io.IOException;
import java.util.Optional;

/**
 * A Stub class of storage
 * Posts event {@link ToDoListSavedEvent} when to-do list is saved
 */
public class StorageStub extends ComponentManager implements Storage {

    public class ToDoListSavedEvent extends BaseEvent {
        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }

    @Override
    public String getToDoListFilePath() {
        return null;
    }

    @Override
    public void setToDoListFilePath(String path) {
    }

    @Override
    public Optional<ReadOnlyToDoList> readToDoList() throws DataConversionException, IOException {
        return null;
    }

    @Override
    public Optional<ReadOnlyToDoList> readToDoList(String filePath) throws DataConversionException, IOException {
        return null;
    }

    @Override
    public void saveToDoList(ReadOnlyToDoList toDoList) throws IOException {
        if (toDoList instanceof ToDoListStub) {
            throw new IOException();
        }
            
        raise(new ToDoListSavedEvent());
    }

    @Override
    public void saveToDoList(ReadOnlyToDoList toDoList, String filePath) throws IOException {
        if (toDoList instanceof ToDoListStub) {
            throw new IOException();
        }
        
        raise(new ToDoListSavedEvent());
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return null;
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {

    }

    @Override
    public void setUserPrefsFilePath(String filePath) {

    }
}