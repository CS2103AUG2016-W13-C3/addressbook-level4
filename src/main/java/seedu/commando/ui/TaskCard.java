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
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.Recurrence;

public class TaskCard extends UiPart {

    private static final String FXML = "TaskCard.fxml";
    private boolean isFinished;

    @FXML
    private HBox taskPane;
    @FXML
    private HBox taskPaneInner;
    @FXML
    private VBox tagsPane;
    @FXML
    private VBox datePane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label indexLabel;
    @FXML
    private Label dueLabel;
    @FXML
    private Label tagsLabel;
    @FXML
    private Label recurrenceLabel;

    private ReadOnlyToDo toDo;
    private int index;
    private boolean containsTags = true;
    private boolean containsDate = true;

    public TaskCard() {
    }

    public static TaskCard load(ReadOnlyToDo toDo, int index) {
        TaskCard card = new TaskCard();
        card.toDo = toDo;
        card.index = index;

        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        titleLabel.setText(toDo.getTitle().value);
        indexLabel.setText(String.valueOf(index));

        setDateTimeLabel();
        setRecurrenceLabel();
        checkTagsPane();
        setTagLabel();
    }

    // @@author A0139080J
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

    private void setRecurrenceLabel() {
        if (toDo.getDueDate().isPresent() && toDo.getDueDate().get().recurrence != Recurrence.None) {
            recurrenceLabel.setText(toDo.getDueDate().get().recurrence.toString());
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

    /**
     * Sets value for the date time label and colours it according to the
     * proximity of the date to today with red being the closest, and green
     * being the furthest
     */
    private void setDateTimeLabel() {
        if (toDo.getDueDate().isPresent()) {
            final LocalDateTime due = toDo.getDueDate().get().value;
            final long dayDifference = ChronoUnit.DAYS.between(LocalDateTime.now(), due);

            dueLabel.setText("by " + DateTimePrettifier.prettifyDateTime(due));
            dueLabel.setStyle("-fx-text-fill: " + ToDoCardStyleManager.getDateProximityGreen((int) dayDifference));
        } else {
            dueLabel.setManaged(false);
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
        return taskPane;
    }

    /**
     * Every recently modified event will have a red border This includes
     * modification via undo, edit, add
     */
    private void setRecentlyModifiedState() {
        taskPaneInner.setStyle(ToDoCardStyleManager.recentlyModifiedStateCSS);
    }

    /**
     * Tints a finished event gray
     */
    private void setFinishedState() {
        taskPaneInner.setStyle(ToDoCardStyleManager.finishedStateContentCSS);
        datePane.setStyle(ToDoCardStyleManager.finishedStateDateCSS);
        indexLabel.setStyle(ToDoCardStyleManager.finishedStateIndexCSS);
    }

    /**
     * Tints a hovered event a slight gray
     */
    @FXML
    private void activateHoverState() {
        if (!isFinished) {
            taskPaneInner.setStyle(ToDoCardStyleManager.activateHoverStateContentCSS);
            indexLabel.setStyle(ToDoCardStyleManager.activateHoverStateIndexCSS);
        }
    }

    @FXML
    private void deactivateHoverState() {
        if (!isFinished) {
            taskPaneInner.setStyle(ToDoCardStyleManager.deactivateHoverStateContentCSS);
            indexLabel.setStyle(ToDoCardStyleManager.deactivateHoverStateIndexCSS);
        }
    }
    // @@author

    @Override
    public void setNode(Node node) {
        taskPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
