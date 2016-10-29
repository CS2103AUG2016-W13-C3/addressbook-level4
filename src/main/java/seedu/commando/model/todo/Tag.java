package seedu.commando.model.todo;

//@@author A0122001M

/**
 * Represents a tag of a to-do item
 */
public class Tag {
    public final String value;

    /**
     * Validates given tag name.
     */
    public Tag(String value) {
        value = value.trim();
        this.value = value;
    }

    /**
     * Copy constructor
     */
    public Tag(Tag tag) {
        this.value = tag.value.trim();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        // instanceof handles nulls
        return other == this
                || (other instanceof Tag
                && this.value.equals(((Tag) other).value));
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
