package seedu.commando.model.todo;

import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ObservableValue;

/**
 * A read-only immutable interface for a to-do
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyToDo {

    Title getTitle();
    Optional<DateRange> getDateRange();
    Optional<DueDate> getDueDate();
    Set<Tag> getTags();
    boolean isFinished();

    /**
     * An observable value that changes when any of its fields are updated
     */
    ObservableValue getObservableValue();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyToDo other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(getTitle())
                && other.getDateRange().equals(getDateRange())
                && other.getDueDate().equals(getDueDate())
                && other.getTags().equals(getTags())
                && other.isFinished() == isFinished()); // state checks here onwards
    }

    /**
     * Updates and returns its value, based on the current value of its fields
     */
    default String getText() {
        final StringBuilder builder = new StringBuilder();

        builder.append(String.join(", ",
            "Title: " + getTitle().toString(),
            "Date Range: " + (getDateRange().isPresent() ? getDateRange().get().toString() : "none"),
            "Due Date: " + (getDueDate().isPresent() ? getDueDate().get().toString() : "none"),
            "Tags: " + getTags().toString()));

        return builder.toString();
    }
}
