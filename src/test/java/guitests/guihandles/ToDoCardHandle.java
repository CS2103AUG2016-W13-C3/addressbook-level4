package guitests.guihandles;

import seedu.commando.model.todo.ReadOnlyToDo;

//@@author A0122001M

public interface ToDoCardHandle {
    
    
    /**
     * Compare the target ToDo with the ToDo in the panel
     * 
     * @param todo
     * @return true if todos are equal, else return false
     */
    boolean isSameToDo(ReadOnlyToDo todo);

}
