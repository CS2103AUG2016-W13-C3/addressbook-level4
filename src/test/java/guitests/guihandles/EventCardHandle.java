package guitests.guihandles;


import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.commando.model.todo.ReadOnlyToDo;

/**
 * Provides a handle to a person card in the person list panel.
 */
public class EventCardHandle extends GuiHandle implements ToDoCardHandle{
    private static final String TITLE_FIELD_ID = "#titleLabel";
    private static final String STARTDATE_FIELD_ID = "#startLabel";
    private static final String ENDDATE_FIELD_ID = "#endLabel";

    private Node node;

    public EventCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getTitle() {
        return getTextFromLabel(TITLE_FIELD_ID);
    }

    public String getStartDate() {
        return getTextFromLabel(STARTDATE_FIELD_ID);
    }

    public String getEndDate() {
        return getTextFromLabel(ENDDATE_FIELD_ID);
    }

    public boolean isSameToDo(ReadOnlyToDo todo){
        return getTitle().equals(todo.getTitle().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EventCardHandle) {
            EventCardHandle handle = (EventCardHandle) obj;
            return getTitle().equals(handle.getTitle()); //TODO: compare the rest
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
