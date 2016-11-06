package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.commando.commons.core.DateTimePrettifier;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.Recurrence;
import seedu.commando.model.todo.Tag;

public class EventCard extends UiPart {

    private static final String FXML = "EventCard.fxml";
    private boolean isFinished;

    @FXML
    private HBox eventPane;
    @FXML
    private VBox dateTagsPane;
    @FXML
    private FlowPane tagsPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label indexLabel;
    @FXML
    private Label dateIntervalLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label recurrenceLabel;

    private ReadOnlyToDo toDo;
    private int index;
    
    private boolean containsTags = true;
    private boolean containsDates = true;

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
        createTagLabels();
        checkContainsTagsAndDates();
    }

    // @@author A0139080J
    /**
     * Creates a variable number of labels and put them in a flowpane
     */
    private void createTagLabels() {
        if (!toDo.getTags().isEmpty()) {
            for (Tag tag : toDo.getTags()) {
                Label label = new Label();
                label.setText("#" + tag.value);
                label.setId("tagsLabel");
                label.setMaxWidth(200);
                label.getStyleClass().add("cell_big_label");
                label.setAlignment(Pos.CENTER);
                label.setPadding(new Insets(0, 3, 0, 3));

                tagsPane.getChildren().add(label);
            }
        } else {
            containsTags = false;
        }
    }

    private void setRecurrenceLabel() {
        if (toDo.getDateRange().isPresent() && toDo.getDateRange().get().recurrence != Recurrence.None) {
            recurrenceLabel.setText(toDo.getDateRange().get().recurrence.toString());
        } else {
            recurrenceLabel.setManaged(false);
        }
    }

    /**
     * The colour of the dates will become "more red" as the days pass. Also,
     * when the date is over, the colour will be red.
     */
    private void setDateTimesLabels() {
        if (toDo.getDateRange().isPresent()) {
            final DateRange dateRange = toDo.getDateRange().get();
            final long startDayDifference = ChronoUnit.DAYS.between(LocalDateTime.now(), dateRange.startDate);

            dateIntervalLabel.setText(DateTimePrettifier.prettifyDateTimeRange(dateRange.startDate, dateRange.endDate));
            dateIntervalLabel
                    .setStyle("-fx-text-fill: " + CardStyleManager.getDateProximityBlue((int) startDayDifference));
        } else {
            containsDates = false;
            dateIntervalLabel.setManaged(false);
            endLabel.setManaged(false);
        }
    }
    
    private void checkContainsTagsAndDates() {
        if (!containsTags && !containsDates) {
            tagsPane.setManaged(false);
            dateTagsPane.setManaged(false);
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
        CardStyleManager.addStyleAll("recently-modified", eventPane);
    }

    /**
     * Tints a finished event gray
     */
    private void setFinishedState() {
        CardStyleManager.addStyleAll("finished", eventPane, dateTagsPane, indexLabel);
    }

    /**
     * Tints a hovered event a slight gray
     */
    @FXML
    private void activateHoverState() {
        if (!isFinished) {
            CardStyleManager.addStyleAll("hover", eventPane, indexLabel);
        }
    }

    @FXML
    private void deactivateHoverState() {
        if (!isFinished) {
            CardStyleManager.removeStyleAll("hover", eventPane, indexLabel);
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
