package seedu.commando.model.todo;


import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;

/**
 * Represents a tag of a to-do item
 * Guarantees: immutable; tag is valid as declared in {@link #isValid(String)}
 */
public class Tag {

    private static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";

    public final String value;

    /**
     * Validates given tag name.
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String value) throws IllegalValueException {
        if (!isValid(value)) {
            throw new IllegalValueException(Messages.MESSAGE_TODO_TAG_CONSTRAINTS);
        }

        value = value.trim();
        this.value = value;
    }

    /**
     * Copy constructor
     */
    public Tag(Tag tag) {
        assert isValid(tag.value); // should already have been checked

        this.value = tag.value;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValid(String value) {
        return value != null && value.matches(TAG_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.value.equals(((Tag) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + value + ']';
    }

}
