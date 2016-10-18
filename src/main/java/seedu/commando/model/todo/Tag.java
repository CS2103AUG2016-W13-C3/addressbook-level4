package seedu.commando.model.todo;

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
