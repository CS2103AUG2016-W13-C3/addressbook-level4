package seedu.commando.commons.core;

import java.util.Optional;

import seedu.commando.logic.commands.AddCommand;
import seedu.commando.logic.commands.DeleteCommand;
import seedu.commando.logic.commands.EditCommand;
import seedu.commando.logic.commands.ExportCommand;
import seedu.commando.logic.commands.FindCommand;
import seedu.commando.logic.commands.FinishCommand;
import seedu.commando.logic.commands.ImportCommand;
import seedu.commando.logic.commands.RecallCommand;
import seedu.commando.logic.commands.RedoCommand;
import seedu.commando.logic.commands.StoreCommand;
import seedu.commando.logic.commands.UnfinishCommand;

/**
 * Container for user visible messages.
 */
public class Messages {
    
    public static final String UNKNOWN_COMMAND = "Unknown command: '%1$s'. Input 'help' for instructions on how to use this application.";
    public static final String UNKNOWN_COMMAND_FOR_HELP = "Unknown topic for help: '%1$s'.\n" + 
                                                          "Available Topics: add, edit, delete, find, clear, finish, unfinish, recall, undo, redo, faq, search logic, datetime formats, cheatsheet";
    public static final String MISSING_COMMAND_WORD = "Missing command word.";
    public static final String INVALID_COMMAND_FORMAT = "Invalid format for '%1$s' command!";
    public static final String MISSING_TODO_TITLE = "Missing title for to-do.";
    
    public static final String MISSING_TODO_DATERANGE_START = "Missing start date for date range of to-do.";
    public static final String MISSING_TODO_DATERANGE_END = "Missing end date for date range of to-do.";
    public static final String INVALID_TODO_DATERANGE_START = "Invalid start date for date range of to-do.";
    public static final String INVALID_TODO_DATERANGE_END = "Invalid end date for date range of to-do.";
    public static final String TODO_DATERANGE_END_MUST_AFTER_START = "The end date of the date range must be after start date.";
    public static final String TODO_DATERANGE_RECURRENCE_INVALID = "The recurrence for the given date range is not valid.";
    
    public static final String MISSING_TODO_ITEM_INDEX = "Missing to-do item index.";
    
    public static final String INDEXRANGE_CONSTRAINTS = "The end index must be equal or larger than the start index.";
    
    public static final String MISSING_STORE_PATH = "Missing the path to store.";
    public static final String MISSING_IMPORT_PATH = "Missing the path to import.";
    public static final String MISSING_EXPORT_PATH = "Missing the path to export.";
    
    public static final String MISSING_STORE_FILE = "Missing the file name to store.";
    public static final String MISSING_EXPORT_FILE = "Missing the file name to export.";
    public static final String MISSING_IMPORT_FILE = "Missing the file name to import.";
    
    public static final String TODO_DUEDATE_CONSTRAINTS = "Due date can be any date.";
    public static final String TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE = "To-do cannot have both due date and date range!";
    public static final String TODO_ADDED = "To-do added: %1$s.";
    public static final String TODO_DELETED = "To-do deleted: %1$s.";
    public static final String TODO_EDITED = "To-do edited: %1$s.";
    public static final String TODO_NO_EDITS = "But nothing happened.";
    public static final String TODO_NOT_FOUND = "To-do not found: %1$s";
    public static final String TODO_ITEM_INDEX_INVALID = "Invalid to-do index: %1$d.";
    
    public static final String EXIT_APPLICATION = "Exiting application...";
    public static final String TODO_LIST_CLEARED = "Boom. Cleared to-do list!";
    public static final String HELP_WINDOW_SHOWN = "Opened help window.";
    public static final String FIND_COMMAND = "Found %1$d events and %2$d tasks.";
    public static final String RECALL_COMMAND = "Found %1$d past events and %2$d completed tasks.";
    public static final String FIND_COMMAND_CLEAR = "Listed all to-do items.";
    public static final String RECALL_COMMAND_CLEAR = "Listed all past to-do items.";
    public static final String FINISH_COMMAND = "Task marked as done: %1$s.";
    public static final String FINISH_COMMAND_ALREADY_FINISHED = "Task already marked done: %1$s";
    public static final String FINISH_COMMAND_CANNOT_FINISH_EVENT = "To-do must be a task to be marked done: %1$s.";
    public static final String UNFINISHED_COMMAND = "Task marked as undone: %1$s.";
    public static final String UNFINISH_COMMAND_ALREADY_ONGOING = "Task already marked undone: %1$s.";
    public static final String UNFINISH_COMMAND_CANNOT_UNFINISH_EVENT = "To-do must be a task to be marked undone: %1$s.";
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
    public static final String DELETE_COMMAND_NO_TAGS = "To-do with index '%1$s' has no tags to delete!";
    public static final String DELETE_COMMAND_NO_TIME_CONSTRAINTS = "To-do with index '%1$s' has no time constraints to delete!";

    public static final String STORE_COMMAND_FORMAT = ">> store ../../Dropbox/work/todolist.xml.";
    public static final String IMPORT_COMMAND_FORMAT = ">> import ../../Dropbox/work/todolist.xml.";
    public static final String EXPORT_COMMAND_FORMAT = ">> export ../../Dropbox/work/todolist.xml.";
    public static final String ADD_COMMAND_FORMAT = ">> add <task description> #tag1 #tag2...\n" + 
                                                    ">> add <task description> by <deadline>\n" + 
                                                    ">> add <event description> from <start time> to <end time>";
    public static final String DELETE_COMMAND_FORMAT = ">> delete <index>";
    public static final String FIND_COMMAND_FORMAT = ">> find\n" + ">> find <keyword(s)> #tag1 #tag2...";
    public static final String RECALL_COMMAND_FORMAT = ">> recall\n" + ">> recall <keyword(s)> #tag1 #tag2...";
    public static final String EDIT_COMMAND_FORMAT = ">> edit <index> <change in description>\n" + 
                                                     ">> edit <index> `<change in description`\n" + 
                                                     "(If you want to start your description by keywords such as `by`, `from` and `to`";
    public static final String FINISH_COMMAND_FORMAT = ">> finish <index>";
    public static final String UNFINISH_COMMAND_FORMAT = ">> unfinish <index>";
    
    public static final String DATE_FORMAT = "Date Time Formats: 9 jan 2018 23:59 | Jan 9 2019 1900h | coming friday morning | today 23:59";

    /**
     * Returns an additional invalid command format message to be appended
     * for a {@param commandWord}.
     */
    public static Optional<String> getInvalidCommandFormatMessage(String commandWord) {
        switch (commandWord) {
            case StoreCommand.COMMAND_WORD:
                return Optional.of(STORE_COMMAND_FORMAT);
            case ImportCommand.COMMAND_WORD:
                return Optional.of(IMPORT_COMMAND_FORMAT);
            case ExportCommand.COMMAND_WORD:
                return Optional.of(EXPORT_COMMAND_FORMAT);
            case AddCommand.COMMAND_WORD:
                return Optional.of(ADD_COMMAND_FORMAT);
            case DeleteCommand.COMMAND_WORD:
                return Optional.of(DELETE_COMMAND_FORMAT);
            case EditCommand.COMMAND_WORD:
                return Optional.of(EDIT_COMMAND_FORMAT);
            case FinishCommand.COMMAND_WORD:
                return Optional.of(FINISH_COMMAND_FORMAT);
            case UnfinishCommand.COMMAND_WORD:
                return Optional.of(UNFINISH_COMMAND_FORMAT);
            default: break; 
        }
        return Optional.empty();
    }
}
