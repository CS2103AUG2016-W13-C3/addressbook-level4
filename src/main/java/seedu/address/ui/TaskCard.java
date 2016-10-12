package seedu.address.ui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.tag.Tag;
import seedu.address.model.todo.ReadOnlyToDo;

public class TaskCard extends UiPart{

    private DateTimeFormatter formatter;
    private static final String FXML = "TaskCard.fxml";

    @FXML
    private HBox taskPane;
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
        indexLabel.setText(String.valueOf(index));

        if (toDo.getDueDate().isPresent()) {
            final LocalDateTime due = toDo.getDueDate().get().dueDate;

            dueLabel.setText(prettifyDateTime(due));
        } else {
            dueLabel.setText("");
        }

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
    
    private String prettifyDateTime(LocalDateTime ldt) {
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
