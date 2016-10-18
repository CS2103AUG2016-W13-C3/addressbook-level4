package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ReadOnlyToDo;

public class TaskCard extends UiPart{

    private DateTimeFormatter formatter;
    private static final String FXML = "TaskCard.fxml";
    private boolean isFinished;
    private boolean isNew;

    @FXML
    private HBox taskPane;
    @FXML
    private HBox taskPaneInner;
    @FXML
    private Label titleLabel;
    @FXML
    private Label indexLabel;
    @FXML
    private Label dueLabel;
    @FXML
    private Label tagsLabel;

    private ReadOnlyToDo toDo;
    private int index;

    public TaskCard(){ }

    public static TaskCard load(ReadOnlyToDo toDo, int index){
        TaskCard card = new TaskCard();
        card.toDo = toDo;
        card.index = index;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        formatter = DateTimeFormatter.ofPattern("HH:mma dd/MM/yyyy");
        
        titleLabel.setText(toDo.getTitle().value);
        indexLabel.setText(String.valueOf(index) + ". ");

        setLabelContent();
        setLabelStyle();
        setLabelTags();
    }

    private void setLabelTags() {
        if (!toDo.getTags().isEmpty()) {
            String tags = "";
            for (Tag tag : toDo.getTags()) {
                tags += "#" + tag.value + " ";
            }
            tagsLabel.setText(tags);
        } else {
            tagsLabel.setText("");
        }
    }

    private void setLabelStyle() {
        dueLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-family: Microsoft Yahei Light");
    }

    private void setLabelContent() {
        if (toDo.getDueDate().isPresent()) {
            final LocalDateTime due = toDo.getDueDate().get().value;
            dueLabel.setText("by " + prettifyDateTime(due));
        } else {
            dueLabel.setText("");
        }
    }
    
    private String prettifyDateTime(LocalDateTime ldt) {
        String dateTimePattern = "d MMM HH:mm";
        if (ldt.getYear() != LocalDateTime.now().getYear()) {
            dateTimePattern = "d MMM yyyy HH:mm";
        }
        formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return formatter.format(ldt);
    }

    public HBox getLayoutState(boolean isNew, boolean isFinished) {
        this.isNew = isNew;
        this.isFinished = isFinished;
        if (isNew) {
            setRecentlyModifiedState();
        }
        if (isFinished) {
            setFinishedState();
        }
        return taskPane;
    }
    
    /**
     * Every recently modified event will have a red border
     * This includes modification via undo, edit, add
     */
    private void setRecentlyModifiedState() {
        taskPaneInner.setStyle("-fx-border-color: red");
    }
    
    /**
     * Tints a finished event gray
     */
    private void setFinishedState() {
        taskPaneInner.setStyle("-fx-background-color: derive(#1d1d1d, 95%);");
    }
    
    /**
     * Tints a hovered event a slight gray
     */
    @FXML
    private void activateHoverState() {
        if (!isFinished) {
            taskPaneInner.setStyle("-fx-background-color: derive(#DCDCDC, 50%);");
        }
    }
    
    @FXML
    private void deactivateHoverState() {
        if (!isFinished) {
            taskPaneInner.setStyle("-fx-background-color: #DCDCDC;");
        }
    }

    @Override
    public void setNode(Node node) {
        taskPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
