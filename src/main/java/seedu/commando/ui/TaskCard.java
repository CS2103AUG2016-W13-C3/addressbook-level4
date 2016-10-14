package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ReadOnlyToDo;

public class TaskCard extends UiPart{

    private DateTimeFormatter formatter;
    private static final String FXML = "TaskCard.fxml";

    @FXML
    private HBox taskPane;
    @FXML
    private GridPane taskPaneInner;
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
        
        titleLabel.setText(toDo.getTitle().title);
        indexLabel.setText(String.valueOf(index) + ". ");

        setLabelContent();
        setLabelStyle();
        setLabelTags();
    }

    private void setLabelTags() {
        if (!toDo.getTags().isEmpty()) {
            String tags = "";
            for (Tag tag : toDo.getTags()) {
                tags += "#" + tag.tagName + " ";
            }
            tagsLabel.setText(tags);
        } else {
            tagsLabel.setText("");
        }
    }

    private void setLabelStyle() {
        taskPane.setStyle("-fx-background-color: #FFFFFF");
        taskPaneInner.setStyle("-fx-background-color: #DCDCDC");
        dueLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-family: Microsoft Yahei Light");
        indexLabel.setStyle("-fx-font-family: Lucida Sans Unicode");
    }

    private void setLabelContent() {
        if (toDo.getDueDate().isPresent()) {
            final LocalDateTime due = toDo.getDueDate().get().dueDate;
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

    public HBox getLayout() {
        return taskPane;
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
