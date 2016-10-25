package guitests.guihandles;

import seedu.commando.model.todo.ToDo;

//@@author A0122001M

public interface ToDoListPanelHandle {
    public static boolean isBothListMatching(EventListPanelHandle eventListPanel, TaskListPanelHandle taskListPanel, ToDo... currentList){
        return eventListPanel.isListMatching(currentList) && 
                taskListPanel.isListMatching(eventListPanel.getNumberOfToDo(), currentList);
    }
}
