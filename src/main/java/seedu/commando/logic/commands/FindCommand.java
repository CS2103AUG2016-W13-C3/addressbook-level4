package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.todo.Tag;

import java.util.Collections;
import java.util.Set;

/**
 * Updates the filter for to-do list to show only to-do items containing all
 * of the keywords and tags (case insensitive)
 */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";

    public Set<String> keywords = Collections.emptySet();
    public Set<Tag> tags = Collections.emptySet();

    /**
     * Asserts that {@code uiLogic} is non-null
     */
    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        // if no keywords or tags are provided, clear filter
        if (keywords.isEmpty() && tags.isEmpty()) {
            model.clearUiToDoListFilter(false);
            return new CommandResult(Messages.CLEAR_FIND);
        }

        model.setUiToDoListFilter(keywords, tags, false);
        return new CommandResult(String.format(Messages.FIND, model.getUiEvents().size(), model.getUiTasks().size()));
    }
}
