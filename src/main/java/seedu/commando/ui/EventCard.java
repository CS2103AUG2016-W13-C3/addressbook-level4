package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.commando.commons.core.DateTimePrettifier;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.Recurrence;

public class EventCard extends UiPart {

    private static final String FXML = "EventCard.fxml";
    private boolean isFinished;

    @FXML
    private HBox eventPane;
    @FXML
    private HBox eventPaneInner;
    @FXML
    private VBox tagsPane;
    @FXML
    private HBox datePane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label indexLabel;
    @FXML
    private Label dateIntervalLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label tagsLabel;
    @FXML
    private Label recurrenceLabel;

    private ReadOnlyToDo toDo;
    private int index;
    private boolean containsTags = true;
    private boolean containsDate = true;

    public EventCard() {
    }

    public static EventCard load(ReadOnlyToDo toDo, int index) {
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
        setRecurrenceLabel();
        checkTagsPane();
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
            tagsLabel.setManaged(false);
            containsTags = false;
        }
    }

    // @@author A0139080J
    private void setRecurrenceLabel() {
        if (toDo.getDateRange().isPresent() && toDo.getDateRange().get().recurrence != Recurrence.None) {
            recurrenceLabel.setText(toDo.getDateRange().get().recurrence.toString());
        } else {
            recurrenceLabel.setManaged(false);
            containsDate = false;
        }
    }

    /**
     * If both tags and recurrence are non-existent, hide the pane that contains
     * them
     */
    private void checkTagsPane() {
        if (!containsTags && !containsDate) {
            tagsPane.setManaged(false);
        }
    }

    private void setDateTimesLabels() {
        if (toDo.getDateRange().isPresent()) {
            final DateRange dateRange = toDo.getDateRange().get();
            final long startDayDifference = ChronoUnit.DAYS.between(LocalDateTime.now(), dateRange.startDate);

            dateIntervalLabel.setText(DateTimePrettifier.prettifyDateTimeRange(dateRange.startDate, dateRange.endDate));
            dateIntervalLabel
                    .setStyle("-fx-text-fill: " + ToDoCardStyleManager.getDateProximityBlue((int) startDayDifference));
        } else {
            dateIntervalLabel.setManaged(false);
            endLabel.setManaged(false);
            datePane.setManaged(false);
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
     * Every recently modified event will have a red border This includes
     * modification via undo, edit, add
     */
    private void setRecentlyModifiedState() {
        eventPaneInner.setStyle(ToDoCardStyleManager.recentlyModifiedStateCSS);
    }

    /**
     * Tints a finished event gray
     */
    private void setFinishedState() {
        eventPaneInner.setStyle(ToDoCardStyleManager.finishedStateContentCSS);
        datePane.setStyle(ToDoCardStyleManager.finishedStateDateCSS);
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
    // @@author

    @Override
    public void setNode(Node node) {
        eventPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
