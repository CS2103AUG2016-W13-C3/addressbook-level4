package seedu.commando.model.todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import seedu.commando.commons.util.CollectionUtil;

//@@author A0122001M

/**
 * Represents the due date of a to-do, immutable.
 * Ignores the seconds and nano-seconds field of its datetimes, unless it is MIN or MAX.
 */
public class DueDate {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        this.value = processDateTime(value);
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
        return value.format(dateFormatter)
            + ((recurrence == Recurrence.None) ? "" : " " + recurrence.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        //check if same object, if not check if values are equal
        return other == this 
                || (other instanceof DueDate
                && (value.equals(((DueDate) other).value))
                && recurrence.equals(((DueDate) other).recurrence));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, recurrence);
    }
    
    /**
     * Remove the seconds in the dateTime if the dateTime is not set to max or min
     * 
     * @param localDateTime
     * @return processed localDateTime
     */
    private LocalDateTime processDateTime(LocalDateTime localDateTime) {
        if (localDateTime.equals(LocalDateTime.MAX) || localDateTime.equals(LocalDateTime.MIN)) {
            return localDateTime;
        } else {
            return localDateTime.withSecond(0).withNano(0);
        }
    }
}
