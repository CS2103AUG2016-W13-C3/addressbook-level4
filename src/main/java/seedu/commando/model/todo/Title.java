package seedu.commando.model.todo;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;

/**
 * Represents the title of a to-do
 */
public class Title {

    public final String value;

    /**
     * Constructor for a value
     */
    public Title(String value) {
        value = value.trim();
        this.value = value;
    }

    /**
     * Copy constructor
     */
    public Title(Title title) {
        this.value = title.value.trim();
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
