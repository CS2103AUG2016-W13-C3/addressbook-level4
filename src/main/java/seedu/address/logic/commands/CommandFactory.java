package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.Parser;

import java.util.Optional;
import java.util.Set;

/**
 * Maps and builds commands from input strings, using {@link Parser}
 */
public class CommandFactory {

    private Parser parser;
    {
        parser = new Parser();
    }

    /**
     * Interprets an input string as a command, initializes it, and returns it
     * @return instance of a command based on {@param parsable}
     */
    public Command build(String inputString){
        parser.setInput(inputString);

        // Check if command word exists
        Optional<String> commandWord = parser.extractCommandWord();

        if (!commandWord.isPresent()) {
            return new InvalidCommand(Messages.MESSAGE_MISSING_COMMAND_WORD);
        }

        switch (commandWord.get()) {
            case AddCommand.COMMAND_WORD:
                return buildAddCommand();
            case DeleteCommand.COMMAND_WORD:
                return buildDeleteCommand();
            case FindCommand.COMMAND_WORD:
                return buildFindCommand();
            case ExitCommand.COMMAND_WORD:
                return buildExitCommand();
            case ClearCommand.COMMAND_WORD:
                return buildClearCommand();
            case HelpCommand.COMMAND_WORD:
                return buildHelpCommand();
            default:
                return new InvalidCommand(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
    }

    private Command buildExitCommand() {
        return new ExitCommand();
    }

    private Command buildAddCommand() {
        // Try to find title
        Optional<String> title = parser.extractText();
        if (!title.isPresent()) {
            return new InvalidCommand(Messages.MESSAGE_MISSING_TODO_TITLE);
        }

        try {
            return new AddCommand(title.get());
        } catch (IllegalValueException exception) {
            return new InvalidCommand(Messages.MESSAGE_TODO_TITLE_CONSTRAINTS);
        }
    }

    private Command buildDeleteCommand() {
        // Try to find index
        Optional<Integer> index = parser.extractItemIndex();
        if (!index.isPresent()) {
            return new InvalidCommand(Messages.MESSAGE_MISSING_TODO_ITEM_INDEX);
        }

        return new DeleteCommand(index.get());
    }

    private Command buildFindCommand() {
        // Try to find keywords
        Set<String> words = parser.extractWords();

        return new FindCommand(words);
    }

    private Command buildClearCommand() {
        return new ClearCommand();
    }

    private Command buildHelpCommand() {
        // Try to find command word
        Optional<String> word = parser.extractText();

        if (word.isPresent()) {
            return new HelpCommand(word.get());
        } else {
            return new HelpCommand();
        }
    }
}