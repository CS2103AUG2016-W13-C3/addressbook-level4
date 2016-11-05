package guitests.guihandles;


import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.commando.model.todo.ReadOnlyToDo;

//@@author A0122001M

/**
 * Provides a handle to a task card in the task list panel.
 */
public class TaskCardHandle extends GuiHandle implements ToDoCardHandle {
    private static final String TITLE_FIELD_ID = "#titleLabel";
    private static final String DUEDATE_FIELD_ID = "#dueLabel";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getTitle() {
        return getTextFromLabel(TITLE_FIELD_ID);
    }

    public String getDueDate() {
        return getTextFromLabel(DUEDATE_FIELD_ID);
    }


    public boolean isSameToDo(ReadOnlyToDo todo){
        return getTitle().toString().equals(todo.getTitle().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getTitle().equals(handle.getTitle()); //TODO: compare the rest
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}