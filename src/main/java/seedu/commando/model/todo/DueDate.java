package seedu.commando.model.todo;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;

//@@author A0122001M

/**
 * Represents the due date of a to-do.
 * Ignores the seconds and nano-seconds field of its datetimes.
 */
public class DueDate {
    public final LocalDateTime value;
    public final Recurrence recurrence;

    /**
     * @see #DueDate(LocalDateTime, Recurrence), but with no recurrence.
     */
    public DueDate(LocalDateTime value) {
        this(value, Recurrence.None);
    }

    /**
     * Constructor for a due date.
     * Asserts parameters are non-null.
     */
    public DueDate(LocalDateTime value, Recurrence recurrence){
        assert !CollectionUtil.isAnyNull(value, recurrence);

        this.value = value.withSecond(0).withNano(0);
        this.recurrence = recurrence;
    }

    /**
     * Copy constructor
     */
    public DueDate(DueDate dueDate) {
        this.value = dueDate.value;
        this.recurrence = dueDate.recurrence;
    }

    @Override
    public String toString() {
        return value
            + ((recurrence == Recurrence.None) ? "" : " " + recurrence.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        // instanceof handles nulls
        return other == this
                || (other instanceof DueDate
                && value.equals(((DueDate) other).value)
                && recurrence.equals(((DueDate) other).recurrence));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, recurrence);
    }

}
