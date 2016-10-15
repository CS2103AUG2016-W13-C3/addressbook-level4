package seedu.commando.model.todo;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;

/**
 * Represents the value of a to-do
 * Guarantees: immutable; is valid as declared in {@link #isValid(String)}
 */
public class Title {

    private static final String TITLE_VALIDATION_REGEX = ".+";

    public final String value;

    /**
     * Constructor for a value
     * @throws IllegalValueException if given value is invalid
     */
    public Title(String value) throws IllegalValueException {
        if (!isValid(value)) {
            throw new IllegalValueException(Messages.MESSAGE_TODO_TITLE_CONSTRAINTS);
        }

        value = value.trim();
        this.value = value;
    }

    /**
     * Copy constructor
     */
    public Title(Title title) {
        assert isValid(title.value); // should already have been checked

        this.value = title.value;
    }

    /**
     * Returns true if a given string is a valid value
     */
    private static boolean isValid(String title) {
        return title != null && title.matches(TITLE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && value.equals(((Title) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
