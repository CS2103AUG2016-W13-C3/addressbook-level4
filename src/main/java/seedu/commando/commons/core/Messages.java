package seedu.commando.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {
    public static final String UNKNOWN_COMMAND = "Unknown command.";
    public static final String UNKNOWN_COMMAND_FOR_HELP = "Unknown parameter for help.";
    public static final String MISSING_COMMAND_WORD = "Missing command word.";
    public static final String INVALID_COMMAND_FORMAT = "Invalid format for '%1$s' command!";
    public static final String MISSING_TODO_TITLE = "Missing title for to-do.";
    public static final String INVALID_TODO_DATERANGE_START = "Invalid start date for date range of to-do.";
    public static final String INVALID_TODO_DATERANGE_END = "Invalid end date for date range of to-do.";
    public static final String MISSING_TODO_ITEM_INDEX = "Missing to-do item index.";
    public static final String TODO_DATERANGE_CONSTRAINTS = "The end date of the date range must be after start date.";
    public static final String MISSING_STORE_PATH = "Missing the path to store.";
    public static final String MISSING_STORE_FILE = "Missing the file name to store.";
    public static final String MISSING_EXPORT_PATH = "Missing the path to export.";
    public static final String MISSING_EXPORT_FILE = "Missing the file name to export.";
    public static final String MISSING_IMPORT_FILE = "Missing the file name to import.";
    public static final String MISSING_IMPORT_PATH = "Missing the path to import.";
    public static final String TODO_DUEDATE_CONSTRAINTS = "Due date can be any date.";
    public static final String TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE = "To-do cannot have both due date and date range!";
    public static final String TODO_ADDED = "To-do added: %1$s.";
    public static final String TODO_DELETED = "To-do deleted: %1$s.";
    public static final String TODO_FINISHED = "To-do finished: %1$s.";
    public static final String TODO_UNFINISHED = "To-do unfinished: %1$s.";
    public static final String TODO_EDITED = "To-do edited: %1$s.";
    public static final String TODO_NO_EDITS = "Nothing interesting happens.";
    public static final String TODO_ALREADY_FINISHED = "To-do already finished: %1$s";
    public static final String TODO_ALREADY_ONGOING = "To-do already ongoing: %1$s";
    public static final String TODO_NOT_FOUND = "To-do not found: %1$s";
    public static final String FIND = "Found %1$d events and %2$d tasks.";
    public static final String RECALL = "Found %1$d events and %2$d tasks in the past.";
    public static final String CLEAR_FIND = "Listed all to-do items.";
    public static final String CLEAR_RECALL = "Listed all to-do items in the past.";
    public static final String TODO_ITEM_INDEX_INVALID = "Invalid to-do index: %1$d.";
    public static final String EXIT_APPLICATION = "Exiting application...";
    public static final String TODO_LIST_CLEARED = "Cleared to-do list!";
    public static final String HELP_WINDOW_SHOWN = "Opened help window.";
    public static final String UNDO_COMMAND = "Undid the last command.";
    public static final String UNDO_COMMAND_FAIL = "Nothing more to undo!";
    public static final String REDO_COMMAND = "Redid the last undo command.";
    public static final String REDO_COMMAND_FAIL = "Nothing more to redo!";
    public static final String STORE_COMMAND = "Changed the storage path to: %1$s.";
    public static final String STORE_COMMAND_FILE_EXIST = "Failed to store the storage file to %1$s: destination file exists";
    public static final String EXPORT_COMMAND = "Export the storage file to: %1$s.";
    public static final String EXPORT_COMMAND_FILE_EXIST = "Failed to export the storage file to %1$s: destination file exists";
    public static final String IMPORT_COMMAND = "Import the storage file from: %1$s.";
    public static final String IMPORT_COMMAND_FILE_NOT_EXIST = "Invalid import file: the file does not exist.";
    public static final String IMPORT_COMMAND_INVALID_DATA = "Invalid import file: the file is of an invalid format.";
    public static final String TODO_ALREADY_EXISTS = "To-do already exists!";
    public static final String DELETE_COMMAND_NO_TAGS = "To-do has no tags to delete!";
    public static final String DELETE_COMMAND_NO_TIME_CONSTRAINTS = "To-do has no time constraints to delete!";
}
