package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDo;

public class EventCard extends UiPart{

    private DateTimeFormatter formatter;
    private static final String FXML = "EventCard.fxml";
    private boolean isFinished;
    private boolean isNew;

    @FXML
    private HBox eventPane;
    @FXML
    private HBox eventPaneInner;
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
        titleLabel.setText(toDo.getTitle().value);
        indexLabel.setText(String.valueOf(index) + ". ");

        setLabelContent();
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
    
    private void setLabelContent() {
        if (toDo.getDateRange().isPresent()) {
            final DateRange dateRange = toDo.getDateRange().get();
            startLabel.setText(prettifyDateTime(dateRange.startDate) + " To ");
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
    
    public HBox getLayoutState(boolean isNew, boolean isFinished) {
        this.isNew = isNew;
        this.isFinished = isFinished;
        if (isNew) {
            setRecentlyModifiedState();
        }
        if (isFinished) {
            setFinishedState();
        }
        return eventPane;
    }

    /**
     * Every recently modified event will have a red border
     * This includes modification via undo, edit, add
     */
    private void setRecentlyModifiedState() {
        eventPaneInner.setStyle("-fx-border-color: red");
    }
    
    /**
     * Tints a finished event gray
     */
    private void setFinishedState() {
        ColorAdjust ca = new ColorAdjust();
        ca.setBrightness(-0.3);
        eventPaneInner.setEffect(ca);
    }

    /**
     * Tints a hovered event a slight gray
     */
    @FXML
    private void activateHoverState() {
        if (!isFinished) {
            ColorAdjust ca = new ColorAdjust();
            ca.setBrightness(-0.1);
            eventPaneInner.setEffect(ca);
        }
    }
    
    @FXML
    private void deactivateHoverState() {
        if (!isFinished) {
            ColorAdjust ca = new ColorAdjust();
            ca.setBrightness(0);
            eventPaneInner.setEffect(ca);
        }
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
