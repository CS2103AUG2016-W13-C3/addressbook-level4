package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.parser.CommandParser;
import seedu.commando.model.todo.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//@@author A0139697H

/**
 * Maps and builds commands from input strings, using {@link CommandParser}.
 * In charge of splitting up input strings to required parts for commands.
 */
public class CommandFactory {
    private static final String KEYWORD_DELETE_TIME = "time";
    private static final String KEYWORD_DELETE_TAG = "tag";
    private static final String KEYWORD_DELETE_RECURRENCE = "recurrence";

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
     * Interprets an input string as a command, initializes it, and returns it.
     *
     * @return instance of a command
     * @throws InvalidCommandFormatException if command format is invalid
     * @throws UnknownCommandWordException if command word is unknown
     * @throws MissingCommandWordException if command word is missing
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
                case AddCommand.COMMAND_WORD: return buildAddCommand();
                case DeleteCommand.COMMAND_WORD: return buildDeleteCommand();
                case FinishCommand.COMMAND_WORD: return buildFinishCommand();
                case UnfinishCommand.COMMAND_WORD: return buildUnfinishCommand();
                case FindCommand.COMMAND_WORD: return buildFindCommand();
                case ExitCommand.COMMAND_WORD: return buildExitCommand();
                case ClearCommand.COMMAND_WORD: return buildClearCommand();
                case HelpCommand.COMMAND_WORD: return buildHelpCommand();
                case EditCommand.COMMAND_WORD: return buildEditCommand();
                case UndoCommand.COMMAND_WORD: return buildUndoCommand();
                case RedoCommand.COMMAND_WORD: return buildRedoCommand();
                case StoreCommand.COMMAND_WORD: return buildStoreCommand();
                case ExportCommand.COMMAND_WORD: return buildExportCommand();
                case ImportCommand.COMMAND_WORD: return buildImportCommand();
                case RecallCommand.COMMAND_WORD: return buildRecallCommand();
                case ListCommand.COMMAND_WORD: return buildListCommand();
                default: throw new UnknownCommandWordException(processedCommandWord);
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
            command.setTags(tags);
        }

        // Try to find keywords
        Set<String> keywords = commandParser.extractWords().stream().collect(Collectors.toSet());
        command.setKeywords(keywords);

        return command;
    }


    private Command buildExitCommand() throws IllegalValueException {
        ensureInputIsEmpty(ExitCommand.COMMAND_WORD);

        return new ExitCommand();
    }

    //@@author A0142230B
    private Command buildImportCommand() throws IllegalValueException {
        // Extract the file path
        String path = commandParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_IMPORT_PATH)
            );

        return new ImportCommand(path);
    }

    //@@author A0142230B
    private Command buildExportCommand() throws IllegalValueException {
        // Extract the file path
        String path = commandParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_EXPORT_PATH)
            );

        return new ExportCommand(path);
    }

    //@@author A0142230B
    private Command buildStoreCommand() throws IllegalValueException {
        // Extract the file path
        String path = commandParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_STORE_PATH)
            );

        return new StoreCommand(path);
    }

    //@@author A0142230B
    private Command buildListCommand() throws IllegalValueException {
        // Extract the date range, if exists
        Optional<DateRange> dateRange = commandParser.extractTrailingDateRange();

        // Wrong format
        ensureInputIsEmpty(ListCommand.COMMAND_WORD);

        // Should not have recurrence info in the input
        if (dateRange.isPresent() && dateRange.get().recurrence != Recurrence.None) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, ListCommand.COMMAND_WORD));
        } else {
            return new ListCommand(dateRange);
        }
    }

    //@@author A0139697H
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
            command.setTags(tags);
        }

        dueDate.ifPresent(command::setDueDate);
        dateRange.ifPresent(command::setDateRange);

        ensureInputIsEmpty(AddCommand.COMMAND_WORD);

        return command;
    }

    private Command buildDeleteCommand() throws IllegalValueException {
        List<Integer> indices = extractIndices();

        DeleteCommand deleteCommand = new DeleteCommand(indices);

        // check for fields
        List<String> words = commandParser.extractWords();

        int fieldsCount = 0;

        if (words.contains(KEYWORD_DELETE_TAG)) {
            deleteCommand.deletesTags();
            fieldsCount++;
        }

        if (words.contains(KEYWORD_DELETE_TIME)) {
            deleteCommand.deletesTime();
            fieldsCount++;
        }

        if (words.contains(KEYWORD_DELETE_RECURRENCE)) {
            deleteCommand.deletesRecurrence();
            fieldsCount++;
        }

        // If there were extra words besides the fields, invalid command format
        if (fieldsCount != words.size()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, DeleteCommand.COMMAND_WORD));
        }

        return deleteCommand;
    }

    private Command buildFinishCommand() throws IllegalValueException {
        List<Integer> indices = extractIndices();
        ensureInputIsEmpty(FinishCommand.COMMAND_WORD);

        return new FinishCommand(indices);
    }

    private Command buildUnfinishCommand() throws IllegalValueException {
        List<Integer> indices = extractIndices();
        ensureInputIsEmpty(UnfinishCommand.COMMAND_WORD);

        return new UnfinishCommand(indices);
    }

    private Command buildFindCommand() {
        FindCommand command = new FindCommand();

        // Extract tags
        Set<Tag> tags = commandParser.extractTrailingTags();
        if (!tags.isEmpty()) {
            command.setTags(tags);
        }

        // Try to find keywords
        Set<String> keywords = commandParser.extractWords().stream().collect(Collectors.toSet());
        command.setKeywords(keywords);

        return command;
    }

    private Command buildClearCommand() throws IllegalValueException {
        ensureInputIsEmpty(ClearCommand.COMMAND_WORD);

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

        // Put in fields
        if (!tags.isEmpty()) {
            command.setTags(tags);
        }
        commandParser.extractTrailingDateRange().ifPresent(command::setDateRange);
        commandParser.extractTrailingDueDate().ifPresent(command::setDueDate);

        // Try to extract title, if there was no quoted title
        // Otherwise, use the quoted title
        if (quotedTitle.isPresent()) {
            command.setTitle(new Title(quotedTitle.get()));
        } else {
            commandParser.extractText().ifPresent(
                title -> command.setTitle(new Title(title))
            );
        }

        ensureInputIsEmpty(EditCommand.COMMAND_WORD);

        return command;
    }

    //@@author A0122001M

    private Command buildUndoCommand() throws IllegalValueException {
        ensureInputIsEmpty(UndoCommand.COMMAND_WORD);

        return new UndoCommand();
    }

    private Command buildRedoCommand() throws IllegalValueException {
        ensureInputIsEmpty(RedoCommand.COMMAND_WORD);

        return new RedoCommand();
    }

    private void ensureInputIsEmpty(String commandWord) throws IllegalValueException {
        if (!commandParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, commandWord));
        }
    }

    private List<Integer> extractIndices() throws IllegalValueException {
        List<Integer> indices = commandParser.extractIndicesList();
        if (indices.isEmpty()) {
            throw new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX);
        }
        return indices;
    }
}
