package seedu.address.ui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    private static final String FXML = "eventCard.fxml";

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

        if (toDo.getDateRange().isPresent()) {
            final DateRange dateRange = toDo.getDateRange().get();
            
            startLabel.setText(prettifyDateTime(dateRange.startDate));
            endLabel.setText(prettifyDateTime(dateRange.endDate));
        } else {
            startLabel.setText("");
            endLabel.setText("");
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
        return ldt.getHour() + ":" + 
               ldt.getMinute() + " " + 
               ldt.getDayOfMonth() + " " + 
               ldt.getMonth().getDisplayName(TextStyle.SHORT , Locale.ENGLISH) + " " + 
               ldt.getYear();
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
