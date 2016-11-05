package guitests.guihandles;

import seedu.commando.model.todo.ToDo;

//@@author A0122001M

public interface ToDoListPanelHandle {
    
    /**
     * Compare the ToDoList and current ToDos in the panels 
     * 
     * @param eventListPanel current event panel
     * @param taskListPanel current task panel
     * @param currentList current ToDoList to be compared
     * @return true if ToDoList are matched in both panel, else return false
     */
    public static boolean isBothListMatching(EventListPanelHandle eventListPanel, TaskListPanelHandle taskListPanel, ToDo... currentList){
        return eventListPanel.isListMatching(currentList) && 
                taskListPanel.isListMatching(eventListPanel.getNumberOfToDo(), currentList);
    }
}
