package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.parser.SequentialParser;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Maps and builds commands from input strings, using {@link SequentialParser}
 * In charge of splitting up input strings to required parts for commands
 * Doesn't set context for commands
 */
public class CommandFactory {
    public static final String KEYWORD_DATERANGE_START = "from";
    public static final String KEYWORD_DATERANGE_END = "to";
    public static final String KEYWORD_DUEDATE = "by";
    public static final String TAG_PREFIX = "#";

    private SequentialParser sequentialParser;
    {
        sequentialParser = new SequentialParser();
    }

    /**
     * Interprets an input string as a command, initializes it, and returns it
     * @return instance of a command based on {@param parsable}
     * @throws IllegalValueException if the command is invalid
     */
    public Command build(String inputString) throws IllegalValueException {
        sequentialParser.setInput(inputString);

        // Check if command word exists
        Optional<String> commandWord = sequentialParser.extractFirstWord();

        if (!commandWord.isPresent()) {
            throw new IllegalValueException(Messages.MISSING_COMMAND_WORD);
        }

        switch (commandWord.get().toLowerCase()) {
            case AddCommand.COMMAND_WORD:
                return buildAddCommand();
            case DeleteCommand.COMMAND_WORD:
                return buildDeleteCommand();
            case FinishCommand.COMMAND_WORD:
                return buildFinishCommand();
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
            default:
                throw new IllegalValueException(Messages.UNKNOWN_COMMAND);
        }
    }

    private Command buildExitCommand() throws IllegalValueException {
        if (sequentialParser.isInputEmpty()) {
            return new ExitCommand();
        } else {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, ExitCommand.COMMAND_WORD));
        }
    }

    private Command buildAddCommand() throws IllegalValueException {
        // Extract tags
        List<String> tags = sequentialParser.extractPrefixedWords(TAG_PREFIX, true);

        // Extract date range, if exists
        Optional<LocalDateTime> dateRangeStart = sequentialParser.extractDateTimeAfterKeyword(KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        );

        Optional<LocalDateTime> dateRangeEnd = sequentialParser.extractDateTimeAfterKeyword(KEYWORD_DATERANGE_END,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        );

        // Extract due date, if exists
        Optional<LocalDateTime> dueDate = sequentialParser.extractDateTimeAfterKeyword(KEYWORD_DUEDATE,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        );

        // Extract title
        String title = sequentialParser.extractText().orElseThrow(() -> new IllegalValueException(Messages.MISSING_TODO_TITLE));

        AddCommand command = new AddCommand(title);

        // Put in fields
        if (!tags.isEmpty()) {
            command.tags = tags.stream().collect(Collectors.toSet());
        }

        dueDate.ifPresent(date -> command.dueDate = date);
        dateRangeStart.ifPresent(date -> command.dateRangeStart = date);
        dateRangeEnd.ifPresent(date -> command.dateRangeEnd = date);

        return command;
    }

    private Command buildDeleteCommand() throws IllegalValueException {
        int index = sequentialParser.extractFirstInteger().orElseThrow(
            () -> new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX)
        );

        if (sequentialParser.isInputEmpty()) {
            return new DeleteCommand(index);
        } else {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, DeleteCommand.COMMAND_WORD));
        }
    }
    
    private Command buildFinishCommand() throws IllegalValueException {
        int index = sequentialParser.extractFirstInteger().orElseThrow(
            () -> new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX)
        );

        if (sequentialParser.isInputEmpty()) {
            return new FinishCommand(index);
        } else {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, FinishCommand.COMMAND_WORD));
        }
    }

    private Command buildFindCommand() {
        FindCommand command = new FindCommand();

        // Extract tags
        List<String> tags = sequentialParser.extractPrefixedWords(TAG_PREFIX, true);
        if (!tags.isEmpty()) {
            command.tags = tags.stream().collect(Collectors.toSet());
        }

        // Try to find keywords
        List<String> keywords = sequentialParser.extractWords();
        if (!keywords.isEmpty()) {
            command.keywords = keywords.stream().collect(Collectors.toSet());
        }

        return command;
    }

    private Command buildClearCommand() throws IllegalValueException {
        if (sequentialParser.isInputEmpty()) {
            return new ClearCommand();
        } else {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, ClearCommand.COMMAND_WORD));
        }
    }

    private Command buildHelpCommand() {
        // Try to find command word
        Optional<String> word = sequentialParser.extractText();

        if (word.isPresent()) {
            return new HelpCommand(word.get());
        } else {
            return new HelpCommand();
        }
    }
    private Command buildEditCommand() throws IllegalValueException {
        int index = sequentialParser.extractFirstInteger().orElseThrow(
            () -> new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX)
        );

        EditCommand command = new EditCommand(index);

        // Check if tag prefix exists in command
        boolean hasTagPrefix = sequentialParser.getInput().contains(TAG_PREFIX);

        // Extract tags
        List<String> tags = sequentialParser.extractPrefixedWords(TAG_PREFIX, true);

        // Put in tags
        if (!tags.isEmpty()) {
            command.tags = tags.stream().collect(Collectors.toSet());
        } else if (hasTagPrefix) { // If has an empty tag, empty list of tags for to-do
            command.tags = Collections.emptySet();
        }

        // Extract date range, if exists
        sequentialParser.extractDateTimeAfterKeyword(KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        ).ifPresent(date -> command.dateRangeStart = date);

        sequentialParser.extractDateTimeAfterKeyword(KEYWORD_DATERANGE_END,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        ).ifPresent(date -> command.dateRangeEnd = date);

        // Extract due date, if exists
        sequentialParser.extractDateTimeAfterKeyword(KEYWORD_DUEDATE,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        ).ifPresent(date -> command.dueDate = date);

        // Extract title
        sequentialParser.extractText().ifPresent(title -> {
            command.title = title;
        });

        return command;
    }
    
    private Command buildUndoCommand() throws IllegalValueException {
        if (sequentialParser.isInputEmpty()) {
            return new UndoCommand();
        } else {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, UndoCommand.COMMAND_WORD));
        }
    }

    private Command buildRedoCommand() throws IllegalValueException {
        if (sequentialParser.isInputEmpty()) {
            return new RedoCommand();
        } else {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, RedoCommand.COMMAND_WORD));
        }
    }
}
