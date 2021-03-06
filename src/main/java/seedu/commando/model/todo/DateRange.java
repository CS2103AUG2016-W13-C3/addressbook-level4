package seedu.commando.model.todo;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

//@@author A0122001M

/**
 * Represents a date range of a to-do, immutable.
 * Ignores the seconds and nano-seconds field of its datetimes, unless it is MIN or MAX.
 */
public class DateRange {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public final LocalDateTime startDate, endDate;
    public final Recurrence recurrence;

    /**
     * @see #DateRange(LocalDateTime, LocalDateTime, Recurrence), but with no recurrence.
     */
    public DateRange(LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        this(startDate, endDate, Recurrence.None);
    }

    /**
     * Constructor for a date range.
     * Asserts parameters are non-null.
     * Conditions for validity:
     * - {@param endDate} must not be before {@param startDate}
     * - gap between {@param startDate} and {@param endDate} must not be more than the recurrence interval
     *
     * @throws IllegalValueException if given set of arguments is invalid
     */
    public DateRange(LocalDateTime startDate, LocalDateTime endDate, Recurrence recurrence)
        throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(startDate, endDate, recurrence);

        checkIfValid(startDate, endDate, recurrence);

        this.startDate = processDateTime(startDate);
        this.endDate = processDateTime(endDate);
        this.recurrence = recurrence;
    }

    /**
     * Copy constructor
     */
    public DateRange(DateRange dateRange) {
        this.startDate = dateRange.startDate;
        this.endDate = dateRange.endDate;
        this.recurrence = dateRange.recurrence;
    }

    /**
     * Checks if a set of fields for the class is valid.
     *
     * @throws IllegalValueException if some set of fields is invalid
     */
    private static void checkIfValid(LocalDateTime startDate, LocalDateTime endDate, Recurrence recurrence)
        throws IllegalValueException {

        // Checks if start date is before end date
        if (endDate.isBefore(startDate)) {
            throw new IllegalValueException(Messages.TODO_DATERANGE_END_MUST_AFTER_START + "\n" + Messages.DATE_FORMAT);
        }

        // Checks gap between dates must not be more than the recurrence interval
        if (recurrence != Recurrence.None
            && recurrence.getNextDate(startDate).isBefore(endDate)) {
            throw new IllegalValueException(Messages.TODO_DATERANGE_RECURRENCE_INVALID + "\n" + Messages.DATE_FORMAT);
        }
    }

    @Override
    public String toString() {
        String dateString;
        if (startDate.equals(LocalDateTime.MIN) && endDate.equals(LocalDateTime.MAX)) {
            dateString = "forever";
        } else if (startDate.equals(LocalDateTime.MIN)) {
            dateString = "until " + endDate.format(dateFormatter);
        } else if (endDate.equals(LocalDateTime.MAX)) {
            dateString = "from " + startDate.format(dateFormatter) + " onwards";
        } else {
            dateString = "from " + startDate.format(dateFormatter) + " to " + endDate.format(dateFormatter);
        }
        return dateString
            + ((recurrence == Recurrence.None) ? "" : " " + recurrence.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        // instanceof handles nulls
        return other == this
            || (other instanceof DateRange
            && (startDate.equals(((DateRange) other).startDate)
            && endDate.equals(((DateRange) other).endDate)
            && recurrence.equals(((DateRange) other).recurrence))); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, recurrence);
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
