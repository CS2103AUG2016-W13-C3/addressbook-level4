package seedu.commando.model.todo;

//@@author A0122001M

import seedu.commando.commons.util.CollectionUtil;

/**
 * Represents the title of a to-do
 */
public class Title {
    public final String value;

    /**
     * Constructor for a title.
     * Asserts parameters to be non-null.
     */
    public Title(String value) {
        assert !CollectionUtil.isAnyNull(value);

        this.value = value.trim();
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
        //check if same object, then check if values are equal
        return other == this
                || (other instanceof Title 
                && value.equals(((Title) other).value)); 
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
