package seedu.commando.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {
    public static final String UNKNOWN_COMMAND = "Unknown command.";
    public static final String MISSING_COMMAND_WORD = "Missing command word.";
    public static final String INVALID_COMMAND_FORMAT = "Invalid format for '%1$s' command!";
    public static final String MISSING_TODO_TITLE = "Missing title for to-do.";
    public static final String MISSING_TODO_DATERANGE_START = "Missing start date for date range of to-do.";
    public static final String MISSING_TODO_DATERANGE_END = "Missing end date for date range of to-do.";
    public static final String MISSING_TODO_ITEM_INDEX = "Missing to-do item index.";
    public static final String TODO_DATERANGE_CONSTRAINTS = "The end date of the date range must be after start date.";
    public static final String TODO_DUEDATE_CONSTRAINTS = "Due date can be any date.";
    public static final String TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE = "To-do cannot have both due date and date range!";
    public static final String TODO_ADDED = "To-do added: %1$s.";
    public static final String TODO_DELETED = "To-do deleted: %1$s.";
    public static final String TODO_FINISHED = "To-do finished: %1$s.";
    public static final String TODO_EDITED = "To-do edited: %1$s.";
    public static final String TODO_NO_EDITS = "Nothing interesting happens.";
    public static final String TODO_ALREADY_FINISHED = "To-do already finished: %1$s";
    public static final String TODO_NOT_FOUND = "To-do not found: %1$s";
    public static final String FIND = "Found %1$d events and %2$d tasks.";
    public static final String CLEAR_FIND = "Listed all to-do items.";
    public static final String TODO_ITEM_INDEX_INVALID = "Invalid to-do index: %1$d.";
    public static final String EXIT_APPLICATION = "Exiting application...";
    public static final String TODO_LIST_CLEARED = "Cleared to-do list!";
    public static final String HELP_WINDOW_SHOWN = "Opened help window.";
    public static final String UNDO_COMMAND = "Undid the last command.";
    public static final String UNDO_COMMAND_FAIL = "Nothing more to undo!";
    public static final String REDO_COMMAND = "Redid the last undo command.";
    public static final String REDO_COMMAND_FAIL = "Nothing more to redo!";
}
