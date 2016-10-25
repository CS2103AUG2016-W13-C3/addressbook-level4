package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.parser.CommandParser;
import seedu.commando.model.todo.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps and builds commands from input strings, using {@link CommandParser}
 * In charge of splitting up input strings to required parts for commands
 * Doesn't set context for commands
 */
public class CommandFactory {
    private static final String KEYWORD_DELETE_TIME = "time";
    private static final String KEYWORD_DELETE_TAG = "tag";

    private CommandParser commandParser = new CommandParser();

    public static class InvalidCommandFormatException extends Exception {
        public final String command;

        public InvalidCommandFormatException(String message, String command) {
            super(message);
            this.command = command;
        }
    }

    public static class UnknownCommandWordException extends Exception {
        public final String commandWord;
        UnknownCommandWordException(String commandWord) {
            this.commandWord = commandWord;
        }
    }
    
    public static class MissingCommandWordException extends Exception {}

    /**
     * Interprets an input string as a command, initializes it, and returns it
     * @return instance of a command
     */
    public Command build(String inputString) throws InvalidCommandFormatException,
        UnknownCommandWordException, MissingCommandWordException {
        commandParser.setInput(inputString);

        // Check if command word exists
        Optional<String> commandWord = commandParser.extractWord();

        if (!commandWord.isPresent()) {
            throw new MissingCommandWordException();
        }

        String processedCommandWord = commandWord.get().toLowerCase();
        try {
            switch (processedCommandWord) {
                case AddCommand.COMMAND_WORD:
                    return buildAddCommand();
                case DeleteCommand.COMMAND_WORD:
                    return buildDeleteCommand();
                case FinishCommand.COMMAND_WORD:
                    return buildFinishCommand();
                case UnfinishCommand.COMMAND_WORD:
                    return buildUnfinishCommand();
                case FindCommand.COMMAND_WORD:
                    return buildFindCommand();
                case ExitCommand.COMMAND_WORD:
                    return buildExitCommand();
                case ClearCommand.COMMAND_WORD:
                    return buildClearCommand();
                case HelpCommand.COMMAND_WORD:
                    return buildHelpCommand();
                case EditCommand.COMMAND_WORD:
                    return buildEditCommand();
                case UndoCommand.COMMAND_WORD:
                    return buildUndoCommand();
                case RedoCommand.COMMAND_WORD:
                    return buildRedoCommand();
                case StoreCommand.COMMAND_WORD:
                    return buildStoreCommand();
                case ExportCommand.COMMAND_WORD:
                    return buildExportCommand();
                case ImportCommand.COMMAND_WORD:
                    return buildImportCommand();
                case RecallCommand.COMMAND_WORD:
                    return buildRecallCommand();
                default:
                    throw new UnknownCommandWordException(processedCommandWord);
            }
        } catch (IllegalValueException e) {
            throw new InvalidCommandFormatException(e.getMessage(), processedCommandWord);
        }
    }

    private Command buildRecallCommand() {
        RecallCommand command = new RecallCommand();

        // Extract tags
        Set<Tag> tags = commandParser.extractTrailingTags();
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        // Try to find keywords
        command.keywords = commandParser.extractWords().stream().collect(Collectors.toSet());

        return command;
    }


    private Command buildExitCommand() throws IllegalValueException {
        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, ExitCommand.COMMAND_WORD));
        }

        return new ExitCommand();
    }

    private Command buildImportCommand() throws IllegalValueException {
        // Extract the file path
        String path = commandParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_IMPORT_PATH)
            );

        return new ImportCommand(path);
    }

    private Command buildExportCommand() throws IllegalValueException {
        // Extract the file path
        String path = commandParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_EXPORT_PATH)
            );

        return new ExportCommand(path);
    }

    private Command buildStoreCommand() throws IllegalValueException {
        // Extract the file path
        String path = commandParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_STORE_PATH)
            );

        return new StoreCommand(path);
    }

    private Command buildAddCommand() throws IllegalValueException {
        // Check if quoted title exists
        Optional<String> quotedTitle = commandParser.extractQuotedTitle();

        // Extract tags
        Set<Tag> tags = commandParser.extractTrailingTags();

        // Extract date range, if exists
        Optional<DateRange> dateRange = commandParser.extractTrailingDateRange();

        // Extract due date, if exists
        Optional<DueDate> dueDate = commandParser.extractTrailingDueDate();

        // Initialize command
        // Extract title, if there was no quoted title
        // Otherwise, use the quoted title
        AddCommand command;
        if (quotedTitle.isPresent()) {
            command = new AddCommand(new Title(quotedTitle.get()));
        } else {
            String title = commandParser.extractText().orElseThrow(() -> new IllegalValueException(Messages.MISSING_TODO_TITLE));
            command = new AddCommand(new Title(title));
        }

        // Put in fields
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        dueDate.ifPresent(x -> command.dueDate = x);
        dateRange.ifPresent(x -> command.dateRange = x);

        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, AddCommand.COMMAND_WORD));
        }

        return command;
    }

    private Command buildDeleteCommand() throws IllegalValueException {
    	List<Integer> indices = commandParser.extractIndicesList();
    	if (indices.isEmpty()){
    		throw new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX);
    	}

        DeleteCommand deleteCommand = new DeleteCommand(indices);

        // check for fields
        List<String> words = commandParser.extractWords();

        int fieldsCount = 0;

        if (words.contains(KEYWORD_DELETE_TAG)) {
            deleteCommand.ifDeleteTag = true;
            fieldsCount ++;
        }

        if (words.contains(KEYWORD_DELETE_TIME)) {
            deleteCommand.ifDeleteTime = true;
            fieldsCount++;
        }

        // If there were extra words besides the fields, invalid command format
        if (fieldsCount != words.size()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, DeleteCommand.COMMAND_WORD));
        }

        return deleteCommand;
    }

    private Command buildFinishCommand() throws IllegalValueException {
    	List<Integer> indices = commandParser.extractIndicesList();
    	if (indices.isEmpty()){
    		throw new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX);
    	}
        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, FinishCommand.COMMAND_WORD));

        }

        return new FinishCommand(indices);
    }

    private Command buildUnfinishCommand() throws IllegalValueException {
    	List<Integer> indices = commandParser.extractIndicesList();
    	if (indices.isEmpty()){
    		throw new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX);
    	}
        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, FinishCommand.COMMAND_WORD));
        }

        return new UnfinishCommand(indices);
    }

    private Command buildFindCommand() {
        FindCommand command = new FindCommand();

        // Extract tags
        Set<Tag> tags = commandParser.extractTrailingTags();
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        // Try to find keywords
        command.keywords = commandParser.extractWords().stream().collect(Collectors.toSet());

        return command;
    }

    private Command buildClearCommand() throws IllegalValueException {
        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, ClearCommand.COMMAND_WORD));
        }

        return new ClearCommand();
    }

    private Command buildHelpCommand() {
        // Try to find command word
        Optional<String> word = commandParser.extractText();

        if (word.isPresent()) {
            return new HelpCommand(word.get());
        } else {
            return new HelpCommand();
        }
    }

    private Command buildEditCommand() throws IllegalValueException {
        int index = commandParser.extractInteger().orElseThrow(
            () -> new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX)
        );

        Optional<String> quotedTitle = commandParser.extractQuotedTitle();

        EditCommand command = new EditCommand(index);

        // Extract tags
        Set<Tag> tags = commandParser.extractTrailingTags();

        // Put in tags
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        // Extract date range, if exists
        commandParser.extractTrailingDateRange().ifPresent(
            x -> command.dateRange = x
        );

        // Extract due date, if exists
        commandParser.extractTrailingDueDate().ifPresent(
            x -> command.dueDate = x
        );

        // Try to extract title, if there was no quoted title
        // Otherwise, use the quoted title
        if (quotedTitle.isPresent()) {
            command.title = new Title(quotedTitle.get());
        } else {
            commandParser.extractText().ifPresent(
                title -> command.title = new Title(title)
            );
        }

        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, EditCommand.COMMAND_WORD));
        }

        return command;
    }

    private Command buildUndoCommand() throws IllegalValueException {
        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, UndoCommand.COMMAND_WORD));

        }

        return new UndoCommand();
    }

    private Command buildRedoCommand() throws IllegalValueException {
        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, RedoCommand.COMMAND_WORD));
        }

        return new RedoCommand();
    }
}
