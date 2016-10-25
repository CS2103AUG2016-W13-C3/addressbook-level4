package seedu.commando.commons.events.storage;

import seedu.commando.commons.events.BaseEvent;

/**
 * Indicates that the to-do list has been saved on the file system
 */
public class ToDoListSavedEvent extends BaseEvent {
    private String filePath; // where to-do list was saved

    public ToDoListSavedEvent(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
}
