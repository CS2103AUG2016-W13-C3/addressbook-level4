package seedu.commando.model.todo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ObservableValue;

//@@author A0122001M

/**
 * A read-only immutable interface for a to-do.
 */
public interface ReadOnlyToDo {

    /**
     * Gets the title of the to-do.
     * A title is compulsory for a to-do.
     *
     * @return title of the to-do
     */
    Title getTitle();

    /**
     * Gets the date range of the to-do.
     *
     * @return optional of the date range, empty if the to-do has none
     */
    Optional<DateRange> getDateRange();

    /**
     * Gets the due date of the to-do.
     *
     * @return optional of the due date, empty if the to-do has none
     */
    Optional<DueDate> getDueDate();

    /**
     * Gets the set of tags of the to-do.
     *
     * @return set of tags of the to-do
     */
    Set<Tag> getTags();

    /**
     * Gets the date created of the to-do.
     * By default, it is set as the datetime when the to-do was initialized.
     *
     * @return date created of the to-do
     */
    LocalDateTime getDateCreated();

    /**
     * Returns true if to-do contains either a date range or a due date.
     *
     * @return whether to-do has a time constraint
     */
    default boolean hasTimeConstraint() {
        return getDateRange().isPresent()
            || getDueDate().isPresent();
    }

    /**
     * Gets the date finished for the to-do.
     * If there is a recurring date range or due date, this would always be empty.
     * If there is a non-recurring date range, this would return the end date
     * if the current time is after the end date, empty otherwise (not over).
     *
     * @return optional of date finished of the to-do, empty if the date has no date finished.
     */
    Optional<LocalDateTime> getDateFinished();

    /**
     * Returns true if date finished is set and it is before the current time.
     *
     * @return whether to-do is considered finished
     */
    default boolean isFinished() {
        return getDateFinished().isPresent() && LocalDateTime.now().isAfter(getDateFinished().get());
    }

    /**
     * An observable value that changes when any of the to-do's fields are updated.
     *
     * @return an {@code ObservableValue} that tracks all fields of the to-do.
     */
    ObservableValue getObservableValue();

    /**
     * Returns true if both to-dos have the same state.
     * All fields, except date created, must be equal.
     *
     * @param other the other to-do to compare with
     * @return whether current to-do has the same state as {@param other}
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
     * which means these fields must be equal:
     * - title
     * - due date
     * - date range
     * - tags
     *
     * @param other the other to-do to compare with
     * @return whether current to-do is similar to {@param other}
     */
    default boolean isSimilar(ReadOnlyToDo other) {
        return other == this
            || (other != null
            && other.getTitle().equals(getTitle())
            && other.getDateRange().equals(getDateRange())
            && other.getDueDate().equals(getDueDate())
            && other.getTags().equals(getTags()));
    }

    /**
     * Returns a complete textual representation of the to-do as a string, displaying
     * all its fields.
     *
     * @return a string that represents the to-do
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
