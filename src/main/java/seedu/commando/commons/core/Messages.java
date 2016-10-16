package seedu.commando.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {
    public static final String UNKNOWN_COMMAND = "Unknown command.";
    public static final String MISSING_COMMAND_WORD = "Missing command word.";
    public static final String MISSING_TODO_TITLE = "Missing value for to-do.";
    public static final String MISSING_TODO_DATERANGE_START = "Missing start date for date range of to-do.";
    public static final String MISSING_TODO_DATERANGE_END = "Missing end date for date range of to-do.";
    public static final String MISSING_TODO_ITEM_INDEX = "Missing to-do item index.";
    public static final String MISSING_EXPORT_PATH = "Missing the path to export.";
    public static final String MISSING_EXPORT_FILE = "Missing the file name to export.";
    public static final String MISSING_IMPORT_FILE = "Missing the file name to import.";
    public static final String MISSING_IMPORT_PATH = "Missing the path to import.";
    public static final String TODO_DATERANGE_CONSTRAINTS = "For date range, end date must be after start date.";
    public static final String TODO_DUEDATE_CONSTRAINTS = "Due date can be any date.";
    public static final String TODO_DUEDATE_INVALID_FORMAT = "Due date is an invalid date.";
    public static final String TODO_DATERANGE_START_INVALID_FORMAT = "Start of date range is an invalid date.";
    public static final String TODO_DATERANGE_END_INVALID_FORMAT = "End of date range is an invalid date.";
    public static final String TODO_ADDED = "To-do added: %1$s.";
    public static final String TODO_DELETED = "To-do deleted: %1$s.";
    public static final String TODO_FINISHED = "To-do finished: %1$s.";
    public static final String TODO_EDITED = "To-do edited: %1$s.";
    public static final String TODO_NO_EDITS = "Nothing interesting happens.";
    public static final String TODO_ALREADY_FINISHED = "To-do already finished: %1$s";
    public static final String TODO_NOT_FOUND = "To-do not found: %1$s";
    public static final String FIND = "Found %1$d to-do item(s).";
    public static final String CLEAR_FIND = "Listed all to-do items.";
    public static final String TODO_TITLE_CONSTRAINTS = "Title cannot be an empty string.";
    public static final String TODO_TAG_CONSTRAINTS = "Tags cannot contain spaces.";
    public static final String TODO_ITEM_INDEX_INVALID = "Invalid to-do index: %1$d.";
    public static final String EXIT_APPLICATION = "Exiting application...";
    public static final String TODO_LIST_CLEARED = "Cleared to-do list!";
    public static final String HELP_WINDOW_SHOWN = "Opened help window.";
    public static final String UNDID_COMMAND = "Undid the last command.";
    public static final String UNDID_COMMAND_FAIL = "Nothing more to undo!";
    public static final String REDID_COMMAND = "Redid the last undo command.";
    public static final String REDID_COMMAND_FAIL = "Nothing more to redo!";
    public static final String EXPORT_COMMAND = "Export the storage file to: %1$s.";
    public static final String EXPORT_COMMAND_FILE_EXIST = "Failed to Export the storage file to %1$s : destination file exits";
    public static final String IMPORT_COMMAND = "Import the storage file from: %1$s.";
    public static final String IMPORT_COMMAND_TYPE_ERROR = "Invalid import file, it must be a xml file.";
    public static final String IMPORT_COMMAND_FILE_NOT_EXIST = "Invalid import file, the file does not exist";
    public static final String IMPORT_COMMAND_INVALID_DATA = "Invalid import file, CommanDo cannot read data from it";
}
