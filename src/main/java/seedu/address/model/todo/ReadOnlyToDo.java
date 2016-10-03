package seedu.address.model.todo;

/**
 * A read-only immutable interface for a to-do
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyToDo {

    Title getTitle();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyToDo other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(this.getTitle())); // state checks here onwards
    }

    /**
     * Formats the to-do as text, showing all details
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle());
        return builder.toString();
    }

}
