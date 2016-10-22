package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDo;

public class EventCard extends UiPart{

    private static final String FXML = "EventCard.fxml";
    private boolean isFinished;
    
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
        indexLabel.setText(String.valueOf(index));

        setDateTimesLabels();
        setTagLabel();
    }

    private void setTagLabel() {
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
    
    private void setDateTimesLabels() {
        if (toDo.getDateRange().isPresent()) {
            final DateRange dateRange = toDo.getDateRange().get();
            final long startDayDifference = 
                    ChronoUnit.DAYS.between(LocalDateTime.now(), dateRange.startDate);
            final long endDayDifference = 
                    ChronoUnit.DAYS.between(LocalDateTime.now(), dateRange.endDate);
            startLabel.setText(ToDoCardStyleManager.prettifyDateTime(dateRange.startDate) + " to ");
            endLabel.setText(ToDoCardStyleManager.prettifyDateTime(dateRange.endDate));
            startLabel.setStyle("-fx-text-fill: " + 
                    ToDoCardStyleManager.getDateProximityBlue((int) startDayDifference));
            endLabel.setStyle("-fx-text-fill: " + 
                    ToDoCardStyleManager.getDateProximityBlue((int) endDayDifference));
        } else {
            startLabel.setText("");
            endLabel.setText("");
        }
    }
    
    
    /*
     * Different CSS styles for different states
     */
    protected HBox getLayoutState(boolean isNew, boolean isFinished) {
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
        eventPaneInner.setStyle(ToDoCardStyleManager.recentlyModifiedStateCSS);
    }
    
    /**
     * Tints a finished event gray
     */
    private void setFinishedState() {
        eventPaneInner.setStyle(ToDoCardStyleManager.finishedStateContentCSS);
        indexLabel.setStyle(ToDoCardStyleManager.finishedStateIndexCSS);
    }

    /**
     * Tints a hovered event a slight gray
     */
    @FXML
    private void activateHoverState() {
        if (!isFinished) {
            eventPaneInner.setStyle(ToDoCardStyleManager.activateHoverStateContentCSS);
            indexLabel.setStyle(ToDoCardStyleManager.activateHoverStateIndexCSS);
        }
    }
    
    @FXML
    private void deactivateHoverState() {
        if (!isFinished) {
            eventPaneInner.setStyle(ToDoCardStyleManager.deactivateHoverStateContentCSS);
            indexLabel.setStyle(ToDoCardStyleManager.deactivateHoverStateIndexCSS);
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
