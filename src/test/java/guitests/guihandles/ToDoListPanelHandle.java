package guitests.guihandles;

import seedu.commando.model.todo.ToDo;

public interface ToDoListPanelHandle {
    public static boolean isBothListMatching(EventListPanelHandle eventListPanel, TaskListPanelHandle taskListPanel, ToDo... currentList){
        return eventListPanel.isListMatching(currentList) && 
                taskListPanel.isListMatching(eventListPanel.getListViewSize(), currentList);
    }
}
