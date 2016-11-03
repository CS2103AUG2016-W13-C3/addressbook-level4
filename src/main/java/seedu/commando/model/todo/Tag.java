package seedu.commando.model.todo;

//@@author A0122001M

import seedu.commando.commons.util.CollectionUtil;

/**
 * Represents a tag of a to-do, immutable.
 */
public class Tag implements Comparable<Tag> {
    public final String value;

    /**
     * Constructor for a tag.
     * Asserts parameters to be non-null.
     */
    public Tag(String value) {
        assert !CollectionUtil.isAnyNull(value);

        this.value = value.trim();
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

    @Override
    public int compareTo(Tag o) {
        return value.compareTo(o.value);
    }
}
