package seedu.commando.model.todo;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;

/**
 * Represents the title of a to-do
 * Guarantees: immutable; is valid as declared in {@link #isValid(String)}
 */
public class Title {

    private static final String TITLE_VALIDATION_REGEX = ".+";

    public final String title;

    /**
     * Constructor for a title
     * @throws IllegalValueException if given title is invalid
     */
    public Title(String title) throws IllegalValueException {
        if (!isValid(title)) {
            throw new IllegalValueException(Messages.MESSAGE_TODO_TITLE_CONSTRAINTS);
        }

        title = title.trim();
        this.title = title;
    }

    /**
     * Copy constructor
     */
    public Title(Title title) {
        assert isValid(title.title); // should already have been checked

        this.title = title.title;
    }

    /**
     * Returns true if a given string is a valid title
     */
    private static boolean isValid(String title) {
        return title != null && title.matches(TITLE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && title.equals(((Title) other).title)); // state check
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

}
