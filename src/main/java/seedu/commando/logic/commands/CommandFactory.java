package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.parser.DateTimeParser;
import seedu.commando.logic.parser.SequentialParser;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Maps and builds commands from input strings, using {@link SequentialParser} and {@link DateTimeParser}
 * In charge of splitting up input strings to required parts for commands
 * Doesn't set context for commands
 */
public class CommandFactory {
    public static final String KEYWORD_DATERANGE_START = "from";
    public static final String KEYWORD_DATERANGE_END = "to";
    public static final String KEYWORD_DUEDATE = "by";
    public static final String TAG_PREFIX = "#";

    private SequentialParser sequentialParser;
    private DateTimeParser dateTimeParser;
    {
        sequentialParser = new SequentialParser();
        dateTimeParser = new DateTimeParser();
    }

    /**
     * Interprets an input string as a command, initializes it, and returns it
     * @return instance of a command based on {@param parsable}
     * @throws IllegalValueException if the command is invalid
     */
    public Command build(String inputString) throws IllegalValueException {
        dateTimeParser.resetContext(); // reset any contextual info from last command
        sequentialParser.setInput(inputString);

        // Check if command word exists
        Optional<String> commandWord = sequentialParser.extractFirstWord();

        if (!commandWord.isPresent()) {
            throw new IllegalValueException(Messages.MISSING_COMMAND_WORD);
        }

        switch (commandWord.get()) {
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

    private Command buildExitCommand() {
        return new ExitCommand();
    }

    private Command buildAddCommand() throws IllegalValueException {
        // Extract tags
        List<String> tags = sequentialParser.extractPrefixedWords(TAG_PREFIX, true);

        // Extract title
        String title = sequentialParser.extractText(
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        ).orElseThrow(() -> new IllegalValueException(Messages.MISSING_TODO_TITLE));

        AddCommand command = new AddCommand(title);

        // Put in tags
        if (!tags.isEmpty()) {
            command.tags = tags.stream().collect(Collectors.toSet());
        }

        // Extract due date, if exists
        Optional<String> dueDate = sequentialParser.extractTextAfterKeyword(KEYWORD_DUEDATE,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END
        );

        if (dueDate.isPresent()) {
            Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dueDate.get());

            command.dueDate = date.orElseThrow(
                () -> new IllegalValueException(Messages.TODO_DUEDATE_INVALID_FORMAT)
            );
        }

        // Extract date range, if exists
        Optional<String> dateRangeStart = sequentialParser.extractTextAfterKeyword(KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        );
        if (dateRangeStart.isPresent()) {
            Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dateRangeStart.get());

            command.dateRangeStart = date.orElseThrow(
                () -> new IllegalValueException(Messages.TODO_DATERANGE_START_INVALID_FORMAT)
            );
        }

        Optional<String> dateRangeEnd = sequentialParser.extractTextAfterKeyword(KEYWORD_DATERANGE_END,
            KEYWORD_DATERANGE_START,
            KEYWORD_DUEDATE
        );
        if (dateRangeEnd.isPresent()) {
            Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dateRangeEnd.get());

            command.dateRangeEnd = date.orElseThrow(
                () -> new IllegalValueException(Messages.TODO_DATERANGE_END_INVALID_FORMAT)
            );
        }

        return command;
    }

    private Command buildDeleteCommand() throws IllegalValueException {
        int index = sequentialParser.extractFirstInteger().orElseThrow(
            () -> new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX)
        );

        return new DeleteCommand(index);
    }
    
    private Command buildFinishCommand() throws IllegalValueException {
        int index = sequentialParser.extractFirstInteger().orElseThrow(
            () -> new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX)
        );

        return new FinishCommand(index);
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

    private Command buildClearCommand() {
        return new ClearCommand();
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

        // Extract tags
        List<String> tags = sequentialParser.extractPrefixedWords(TAG_PREFIX, true);

        // Extract title
        sequentialParser.extractText(
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        ).ifPresent(title -> {
            command.title = title;

            // If title is set but no tags, remove tags as well
            if (tags.isEmpty()) {
                command.tags = Collections.emptySet();
            }
        });

        // Put in tags
        if (!tags.isEmpty()) {
            command.tags = tags.stream().collect(Collectors.toSet());
        }

        // Extract due date, if exists
        Optional<String> dueDate = sequentialParser.extractTextAfterKeyword(KEYWORD_DUEDATE,
            KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END
        );

        if (dueDate.isPresent()) {
            Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dueDate.get());

            command.dueDate = date.orElseThrow(
                () -> new IllegalValueException(Messages.TODO_DUEDATE_INVALID_FORMAT)
            );
        }

        // Extract date range, if exists
        Optional<String> dateRangeStart = sequentialParser.extractTextAfterKeyword(KEYWORD_DATERANGE_START,
            KEYWORD_DATERANGE_END,
            KEYWORD_DUEDATE
        );
        if (dateRangeStart.isPresent()) {
            Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dateRangeStart.get());

            command.dateRangeStart = date.orElseThrow(
                () -> new IllegalValueException(Messages.TODO_DATERANGE_START_INVALID_FORMAT)
            );
        }

        Optional<String> dateRangeEnd = sequentialParser.extractTextAfterKeyword(KEYWORD_DATERANGE_END,
            KEYWORD_DATERANGE_START,
            KEYWORD_DUEDATE
        );
        if (dateRangeEnd.isPresent()) {
            Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dateRangeEnd.get());

            command.dateRangeEnd = date.orElseThrow(
                () -> new IllegalValueException(Messages.TODO_DATERANGE_END_INVALID_FORMAT)
            );
        }

        return command;
    }
    
    private Command buildUndoCommand(){
        return new UndoCommand();
    }
    
    private Command buildRedoCommand(){
        return new RedoCommand();
    }
}
