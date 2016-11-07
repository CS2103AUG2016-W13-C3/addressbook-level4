package seedu.commando.ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.commando.commons.core.DateTimePrettifier;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.Recurrence;
import seedu.commando.model.todo.Tag;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * A "card" that represents a single event. This will be shown in the
 * eventListViewPanel
 */
public class EventCard extends UiPart {

    private static final String FXML = "Card.fxml";
    private static final int MIN_FONT_SIZE = 10;
    private static final int TITLE_LENGTH_BREAKPOINT = 40;
    private static final int TITLE_PREF_HEIGHT = 30;
    private static final int TITLE_FONT_SIZE = 14;
    private boolean isFinished;

    @FXML
    private HBox cardPane;
    @FXML
    private VBox dateTagsPane;
    @FXML
    private FlowPane tagsPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label indexLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label recurrenceLabel;

    private ReadOnlyToDo toDo;
    private int index;

    private boolean containsTags = true;
    private boolean containsDates = true;
    private boolean containsRecurrence = true;

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

        resizeTitleLabelIfTooLong();

        setDateTimesLabels();
        setRecurrenceLabel();
        createTagLabels();
        checkContainsTagsAndDates();
    }

    public void resizeTitleLabelIfTooLong() {
        // Resize title if too long
        if (toDo.getTitle().value.length() > TITLE_LENGTH_BREAKPOINT) {
            titleLabel.setStyle(
                "-fx-font-size: " + Math.max(MIN_FONT_SIZE, (TITLE_LENGTH_BREAKPOINT * TITLE_FONT_SIZE / toDo.getTitle().value.length())) + "pt;"
                    + "-fx-pref-height: " + (TITLE_PREF_HEIGHT + (toDo.getTitle().value.length() - TITLE_LENGTH_BREAKPOINT) / 2) + "pt;"
            );
        } else {
            titleLabel.setStyle("-fx-font-size: " + TITLE_FONT_SIZE + "pt;");
        }
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
            containsRecurrence = false;
            // Hides the recurrence label
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

            dateLabel.setText(DateTimePrettifier.prettifyDateTimeRange(dateRange.startDate, dateRange.endDate));
            dateLabel.setStyle("-fx-text-fill: " + CardStyleManager.getDateProximityBlue((int) startDayDifference));
        } else {
            containsDates = false;
            // hides the date label
            dateLabel.setManaged(false);
        }
    }

    /**
     * Hides the pane containing tags and recurrence if both are not present
     */
    private void checkContainsTagsAndDates() {
        if (!containsTags && !containsRecurrence) {
            if (!containsDates) {
                dateTagsPane.setManaged(false);
            }
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
        return cardPane;
    }

    private void setRecentlyModifiedState() {
        CardStyleManager.addStyleAll("recently-modified", cardPane);
    }

    private void setFinishedState() {
        CardStyleManager.addStyleAll("finished", cardPane, dateTagsPane, indexLabel);
    }

    @FXML
    private void activateHoverState() {
        if (!isFinished) {
            CardStyleManager.addStyleAll("hover", cardPane, indexLabel);
        }
    }

    @FXML
    private void deactivateHoverState() {
        if (!isFinished) {
            CardStyleManager.removeStyleAll("hover", cardPane, indexLabel);
        }
    }
    // @@author

    @Override
    public void setNode(Node node) {
        cardPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
