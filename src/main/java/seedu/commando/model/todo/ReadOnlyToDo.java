package seedu.commando.model.todo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ObservableValue;

//@@author A0139697H
/**
 * A read-only immutable interface for a to-do.
 */
public interface ReadOnlyToDo {

    Title getTitle();

    Optional<DateRange> getDateRange();

    Optional<DueDate> getDueDate();

    Set<Tag> getTags();

    LocalDateTime getDateCreated();

    /**
     * @return if either to-do contains a date range or a due date
     */
    default boolean hasTimeConstraint() {
        return getDateRange().isPresent()
            || getDueDate().isPresent();
    }

    /**
     * Returns the date which the to-do is completed.
     */
    Optional<LocalDateTime> getDateFinished();

    /**
     * Returns true if date finished is set and it is before the current time.
     */
    default boolean isFinished() {
        return getDateFinished().isPresent() && LocalDateTime.now().isAfter(getDateFinished().get());
    }

    /**
     * An observable value that changes when any of its fields are updated.
     */
    ObservableValue getObservableValue();

    /**
     * Returns true if both have the same state.
     * All fields, except date created, must be equal.
     */
    default boolean isSameStateAs(ReadOnlyToDo other) {
        // short circuit if same object
        // other != null to avoid NPE below
        return other == this
            || (other != null
            && other.getTitle().equals(getTitle())
            && other.getDateRange().equals(getDateRange())
            && other.getDueDate().equals(getDueDate())
            && other.getTags().equals(getTags())
            && other.getDateFinished().equals(getDateFinished()));
    }

    /**
     * Returns true if both are considered "similar",
     *   which means these fields must be equal:
     *      - title
     *      - due date
     *      - date range
     *      - tags
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
     * @return a complete textual representation of the to-do as a string
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
