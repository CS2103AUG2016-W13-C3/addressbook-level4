package seedu.commando.model.todo;


import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;

/**
 * Represents a tag of a to-do item
 * Guarantees: immutable; tag is valid as declared in {@link #isValid(String)}
 */
public class Tag {

    private static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";

    public String tagName;

    /**
     * Validates given tag name.
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String name) throws IllegalValueException {
        if (!isValid(name)) {
            throw new IllegalValueException(Messages.MESSAGE_TODO_TAG_CONSTRAINTS);
        }

        name = name.trim();
        this.tagName = name;
    }

    /**
     * Copy constructor
     */
    public Tag(Tag tag) {
        assert isValid(tag.tagName); // should already have been checked

        this.tagName = tag.tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValid(String tag) {
        return tag != null && tag.matches(TAG_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
