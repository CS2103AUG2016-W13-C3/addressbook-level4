package seedu.commando.model.todo;



import java.time.LocalDateTime;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;

//@@author A0122001M

/**
 * Represents the Due date of a to-do
 * is valid as declared in {@link #isValid(LocalDateTime)}
 */
public class DueDate {
    public final LocalDateTime value;

    /**
     * Constructor for a due date
     * @throws IllegalValueException if given value is invalid
     */
    public DueDate(LocalDateTime value) throws IllegalValueException {
        if (!isValid(value)) {
            throw new IllegalValueException(Messages.TODO_DUEDATE_CONSTRAINTS + 
                    "\n" + Messages.getCommandFormatMessage("finish").get());
        }

        this.value = value;
    }

    /**
     * Copy constructor
     */
    public DueDate(DueDate dueDate) {
        assert isValid(dueDate.value); // should already have been checked

        this.value = dueDate.value;
    }

    private static boolean isValid(LocalDateTime value) {
        return value != null;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DueDate // instanceof handles nulls
                && (value.equals(((DueDate) other).value))); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
