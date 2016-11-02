package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.ui.UiModel;

import java.util.Collections;
import java.util.Set;

//@@author A0139697H
/**
 * Changes to history mode, and updates the filter for to-do list
 * to show only to-do items containing all of the keywords and tags (case insensitive)
 */
public class RecallCommand extends Command {
    public static final String COMMAND_WORD = "recall";

    public Set<String> keywords = Collections.emptySet();
    public Set<Tag> tags = Collections.emptySet();

    /**
     * Asserts that {@code uiLogic} is non-null
     */
    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        if (keywords.isEmpty() && tags.isEmpty()) {
            // if no keywords or tags are provided, show all finished to-dos
            model.clearUiToDoListFilter(UiModel.FILTER_MODE.FINISHED);
            return new CommandResult(Messages.RECALL_COMMAND_CLEAR);
        } else {
            model.setUiToDoListFilter(keywords, tags, UiModel.FILTER_MODE.FINISHED);
            return new CommandResult(String.format(Messages.RECALL_COMMAND, keywords, tags));
        }
    }
}
