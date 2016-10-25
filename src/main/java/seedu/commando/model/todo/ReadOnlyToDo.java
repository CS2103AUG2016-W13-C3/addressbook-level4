package seedu.commando.model.todo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ObservableValue;

/**
 * A read-only immutable interface for a to-do
 */
public interface ReadOnlyToDo {

    Title getTitle();

    Optional<DateRange> getDateRange();

    Optional<DueDate> getDueDate();

    Set<Tag> getTags();

    LocalDateTime getDateCreated();

    default boolean hasTimeConstraint() {
        return getDateRange().isPresent()
            || getDueDate().isPresent();
    }

    /**
     * If a date range is set and current datetime is after the end of the range
     * returns end of date range regardless of whether date finished was set
     * Else, returns date finished if it is set, empty otherwise
     */
    Optional<LocalDateTime> getDateFinished();

    /**
     * If a date range is set and current datetime is after the end of the range
     * returns true regardless of whether date finished is set.
     * Else, returns if date finished is set and date finished is after current datetime
     */
    default boolean isFinished() {
        if (getDateFinished().isPresent()) {
            return LocalDateTime.now().isAfter(getDateFinished().get());
        } else {
            return getDateRange().isPresent() &&
                LocalDateTime.now().isAfter(getDateRange().get().endDate);
        }
    }

    /**
     * An observable value that changes when any of its fields are updated
     */
    ObservableValue getObservableValue();

    /**
     * Returns true if both have the same state
     * All fields must be equal
     */
    default boolean isSameStateAs(ReadOnlyToDo other) {
        return other == this // short circuit if same object
            || (other != null // this is first to avoid NPE below
            && other.getTitle().equals(getTitle())
            && other.getDateRange().equals(getDateRange())
            && other.getDueDate().equals(getDueDate())
            && other.getTags().equals(getTags())
            && other.getDateFinished().equals(getDateFinished())
            && other.getDateCreated().equals(getDateCreated())); // state checks here onwards
    }

    /**
     * Returns true if both are considered "similar", which means these fields must be equal:
     * - title
     * - due date
     * - date range
     * - tags
     */
    default boolean isSimilar(ReadOnlyToDo other) {
        return other == this // short circuit if same object
            || (other != null // this is first to avoid NPE below
            && other.getTitle().equals(getTitle())
            && other.getDateRange().equals(getDateRange())
            && other.getDueDate().equals(getDueDate())
            && other.getTags().equals(getTags()));
    }

    /**
     * Updates and returns its value, based on the current value of its fields
     */
    default String getText() {
        return String.join(", ",
            "Title: " + getTitle(),
            "Date Range: " + (getDateRange().isPresent() ? getDateRange().get() : "none"),
            "Due Date: " + (getDueDate().isPresent() ? getDueDate().get() : "none"),
            "Tags: " + getTags(),
            "Date Created: " + getDateCreated(),
            "Date Finished: " + (getDateFinished().isPresent() ? getDateFinished().get() : "none"));
    }
}
