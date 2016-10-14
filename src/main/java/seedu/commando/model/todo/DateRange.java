package seedu.commando.model.todo;

import java.time.LocalDateTime;
import java.util.Date;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;

/**
 * Represents the Date Range of a to-do
 * Guarantees: immutable; is valid as declared in {@link #isValid(LocalDateTime, LocalDateTime)}
 */
public class DateRange {

    public final LocalDateTime startDate, endDate;

    /**
     * Constructor for a date range
     * @throws IllegalValueException if given title is invalid
     */
    public DateRange(LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        if (!isValid(startDate, endDate)) {
            throw new IllegalValueException(Messages.MESSAGE_TODO_DATERANGE_CONSTRAINTS);
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public static boolean isValid(LocalDateTime startDate, LocalDateTime endDate) {
        if (CollectionUtil.isAnyNull(startDate, endDate)) {
            return false;
        }

        if (endDate.compareTo(startDate) >= 0) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return this.startDate.toString() + " " + this.endDate.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateRange // instanceof handles nulls
                && (startDate.equals(((DateRange) other).startDate) 
                &&  endDate.equals(((DateRange) other).endDate))); // state check
    }

    @Override
    public int hashCode() {
        return startDate.hashCode() + endDate.hashCode();
    }

}
