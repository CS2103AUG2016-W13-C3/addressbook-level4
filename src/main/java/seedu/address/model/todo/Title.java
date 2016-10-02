package seedu.address.model.todo;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Title {

    public static final String MESSAGE_NAME_CONSTRAINTS = "Title names should be spaces or alphanumeric characters";
    public static final String TITLE_VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String fullTitle;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Title(String title) throws IllegalValueException {
        assert title != null;
        title = title.trim();
        if (!isValidTitle(title)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.fullTitle = title;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidTitle(String test) {
        return test.matches(TITLE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullTitle;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && this.fullTitle.equals(((Title) other).fullTitle)); // state check
    }

    @Override
    public int hashCode() {
        return fullTitle.hashCode();
    }

}
