package seedu.commando.model.todo;

import java.time.LocalDateTime;

//@@author A0139697H

/**
 * Represents a recurrence in a to-do.
 */
public enum Recurrence {
    Daily,
    Weekly,
    Monthly,
    Yearly,
    None;

    /**
     * Gets the next datetime for this recurrence, based on a starting datetime.
     *
     * @param date starting datetime for the recurrence
     * @return the next datetime based on this recurrence
     */
    public LocalDateTime getNextDate(LocalDateTime date) {
        switch (this) {
            case Daily:
                return date.plusDays(1);
            case Weekly:
                return date.plusWeeks(1);
            case Monthly:
                return date.plusMonths(1);
            case Yearly:
                return date.plusYears(1);
            case None:
                return date;
        }

        return date;
    }
}