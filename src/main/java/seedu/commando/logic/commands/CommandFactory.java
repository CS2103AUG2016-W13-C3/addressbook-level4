package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.parser.SequentialParser;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.DueDate;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.Title;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps and builds commands from input strings, using {@link SequentialParser}
 * In charge of splitting up input strings to required parts for commands
 * Doesn't set context for commands
 */
public class CommandFactory {

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
        Optional<String> commandWord = sequentialParser.extractWord();

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
            case StoreCommand.COMMAND_WORD:
            	return buildStoreCommand();
            case ExportCommand.COMMAND_WORD:
            	return buildExportCommand();
            case ImportCommand.COMMAND_WORD:
            	return buildImportCommand();
            case RecallCommand.COMMAND_WORD:
                return buildRecallCommand();
            default:
                throw new IllegalValueException(Messages.UNKNOWN_COMMAND);
        }
    }

    private Command buildRecallCommand() {
        RecallCommand command = new RecallCommand();

        // Extract tags
        Set<Tag> tags = sequentialParser.extractTrailingTags();
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        // Try to find keywords
        command.keywords = sequentialParser.extractWords().stream().collect(Collectors.toSet());

        return command;
    }


    private Command buildExitCommand() throws IllegalValueException {
        if (!sequentialParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, ExitCommand.COMMAND_WORD));
        }

        return new ExitCommand();
    }

    private Command buildImportCommand() throws IllegalValueException {
    	// Extract the file path
    	String path = sequentialParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_IMPORT_PATH)
            );

		return new ImportCommand(path);
	}

	private Command buildExportCommand() throws IllegalValueException {
    	// Extract the file path
        String path = sequentialParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_EXPORT_PATH)
            );

        return new ExportCommand(path);
	}

	private Command buildStoreCommand() throws IllegalValueException {
    	// Extract the file path
        String path = sequentialParser.extractText()
            .orElseThrow(
                () -> new IllegalValueException(Messages.MISSING_STORE_PATH)
            );

    	return new StoreCommand(path);
	}

    private Command buildAddCommand() throws IllegalValueException {
        // Extract tags
        Set<Tag> tags = sequentialParser.extractTrailingTags();

        // Extract date range, if exists
        Optional<DateRange> dateRange = sequentialParser.extractTrailingDateRange();

        // Extract due date, if exists
        Optional<DueDate> dueDate = sequentialParser.extractTrailingDueDate();

        // Extract title
        String title = sequentialParser.extractText().orElseThrow(() -> new IllegalValueException(Messages.MISSING_TODO_TITLE));

        AddCommand command = new AddCommand(new Title(title));

        // Put in fields
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        dueDate.ifPresent(x -> command.dueDate = x);
        dateRange.ifPresent(x -> command.dateRange = x);

        if (!sequentialParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, AddCommand.COMMAND_WORD));
        }

        return command;
    }

    private Command buildDeleteCommand() throws IllegalValueException {
    	List<Integer> indices = sequentialParser.extractIndicesList();
    	if(indices.isEmpty()){
    		throw new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX);
    	}
        if (!sequentialParser.isInputEmpty()) {
           throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, DeleteCommand.COMMAND_WORD));
        }

        return new DeleteCommand(indices);
    }
    
    private Command buildFinishCommand() throws IllegalValueException {
    	List<Integer> indices = sequentialParser.extractIndicesList();
    	if (indices.isEmpty()){
    		throw new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX);
    	}
        if (!sequentialParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, FinishCommand.COMMAND_WORD));

        }

        return new FinishCommand(indices);
    }

    private Command buildFindCommand() {
        FindCommand command = new FindCommand();

        // Extract tags
        Set<Tag> tags = sequentialParser.extractTrailingTags();
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        // Try to find keywords
        command.keywords = sequentialParser.extractWords().stream().collect(Collectors.toSet());

        return command;
    }

    private Command buildClearCommand() throws IllegalValueException {
        if (!sequentialParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, ClearCommand.COMMAND_WORD));
        }

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
        int index = sequentialParser.extractInteger().orElseThrow(
            () -> new IllegalValueException(Messages.MISSING_TODO_ITEM_INDEX)
        );

        EditCommand command = new EditCommand(index);

        // Extract tags
        Set<Tag> tags = sequentialParser.extractTrailingTags();

        // Put in tags
        if (!tags.isEmpty()) {
            command.tags = tags;
        }

        // Extract date range, if exists
        sequentialParser.extractTrailingDateRange().ifPresent(
            x -> command.dateRange = x
        );

        // Extract due date, if exists
        sequentialParser.extractTrailingDueDate().ifPresent(
            x -> command.dueDate = x
        );

        // Extract title
        sequentialParser.extractText().ifPresent(title -> {
            command.title = new Title(title);
        });

        return command;
    }
    
    private Command buildUndoCommand() throws IllegalValueException {
        if (!sequentialParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, UndoCommand.COMMAND_WORD));

        }

        return new UndoCommand();
    }

    private Command buildRedoCommand() throws IllegalValueException {
        if (!sequentialParser.isInputEmpty()) {
            throw new IllegalValueException(String.format(Messages.INVALID_COMMAND_FORMAT, RedoCommand.COMMAND_WORD));
        }

        return new RedoCommand();
    }
}
