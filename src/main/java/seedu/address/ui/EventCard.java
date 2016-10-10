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
            
            final LocalDateTime start = convertToLocalDateTime(dateRange.startDate);
            final LocalDateTime end = convertToLocalDateTime(dateRange.endDate);
            
            startLabel.setText(prettifyDateTime(start));
            endLabel.setText(prettifyDateTime(end));
        } else {
            startLabel.setText("");
            endLabel.setText("");
        }
    }
    
    private String prettifyDateTime(LocalDateTime ldt) {
        return ldt.getHour() + ":" + 
               ldt.getMinute() + " " + 
               ldt.getDayOfMonth() + " " + 
               ldt.getMonth().getDisplayName(TextStyle.SHORT , Locale.ENGLISH) + " " + 
               ldt.getYear();
    }
    
    /**
     * @param Date date
     * @return LocalDateTime date
     */
    private LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.systemDefault());
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
