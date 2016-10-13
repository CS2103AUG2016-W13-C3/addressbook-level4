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
import seedu.address.model.todo.DateRange;
import seedu.address.model.todo.ReadOnlyToDo;

public class EventCard extends UiPart{

    private DateTimeFormatter formatter;
    private static final String FXML = "EventCard.fxml";

    @FXML
    private HBox eventPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label indexLabel;
    @FXML
    private Label startLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label tagsLabel;

    private ReadOnlyToDo toDo;
    private int index;

    public EventCard(){ }

    public static EventCard load(ReadOnlyToDo toDo, int index){
        EventCard card = new EventCard();
        card.toDo = toDo;
        card.index = index;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        titleLabel.setText(toDo.getTitle().title);
        indexLabel.setText(String.valueOf(index));

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
        startLabel.setStyle("-fx-text-fill: #053a8e; -fx-font-family: Microsoft Yahei Light");
        endLabel.setStyle("-fx-text-fill: #053a8e; -fx-font-family: Microsoft Yahei Light");
    }
    
    private void setLabelContent() {
        if (toDo.getDateRange().isPresent()) {
            final DateRange dateRange = toDo.getDateRange().get();
            
            startLabel.setText(prettifyDateTime(dateRange.startDate) + " - ");
            endLabel.setText(prettifyDateTime(dateRange.endDate));
        } else {
            startLabel.setText("");
            endLabel.setText("");
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
        return eventPane;
    }

    @Override
    public void setNode(Node node) {
        eventPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
