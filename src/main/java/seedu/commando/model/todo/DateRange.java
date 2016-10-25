package seedu.commando.model.todo;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;

//@@author A0139697H
/**
 * Represents the Date Range of a to-do
 */
public class DateRange {

    public final LocalDateTime startDate, endDate;
    public final Recurrence recurrence;

    /**
     * @see #DateRange(LocalDateTime, LocalDateTime, Recurrence), but with Recurrence.None
     */
    public DateRange(LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        this(startDate, endDate, Recurrence.None);
    }

    /**
     * Constructor for a date range
     * Asserts parameters are non-null
     * Conditions for validity:
     * - {@param endDate} must not be before {@param startDate}
     * - gap between {@param startDate} and {@param endDate} must not be more than the recurrence interval
     * @throws IllegalValueException if given set of values is invalid
     */
    public DateRange(LocalDateTime startDate, LocalDateTime endDate, Recurrence recurrence) 
            throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(startDate,endDate, recurrence);

        checkIfValid(startDate, endDate, recurrence);

        this.startDate = startDate;
        this.endDate = endDate;
        this.recurrence = recurrence;
    }

    /**
     * Copy constructor
     */
    public DateRange(DateRange dateRange){
        this.startDate = dateRange.startDate;
        this.endDate = dateRange.endDate;
        this.recurrence = dateRange.recurrence;
    }

    private static void checkIfValid(LocalDateTime startDate, LocalDateTime endDate, Recurrence recurrence)
        throws IllegalValueException {

        if (endDate.isBefore(startDate)) {
            throw new IllegalValueException(Messages.TODO_DATERANGE_END_MUST_AFTER_START + "\n" + Messages.DATE_FORMAT);
        }

        boolean isRecurrenceValid = true;
        switch (recurrence) {
            case None: break;
            case Daily: isRecurrenceValid = !startDate.plusDays(1).isBefore(endDate); break;
            case Weekly: isRecurrenceValid = !startDate.plusWeeks(1).isBefore(endDate); break;
            case Monthly: isRecurrenceValid = !startDate.plusMonths(1).isBefore(endDate); break;
            case Yearly: isRecurrenceValid = !startDate.plusYears(1).isBefore(endDate); break;
            default:
                assert false: "Should cover all recurrences";
        }

        if (!isRecurrenceValid) {
            throw new IllegalValueException(Messages.TODO_DATERANGE_RECURRENCE_INVALID + "\n" + Messages.DATE_FORMAT);
        }
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate
            + ((recurrence == Recurrence.None) ? "" : " " + recurrence.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateRange // instanceof handles nulls
                && (startDate.equals(((DateRange) other).startDate) 
                && endDate.equals(((DateRange) other).endDate)
                && recurrence.equals(((DateRange) other).recurrence))); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, recurrence);
    }

}
