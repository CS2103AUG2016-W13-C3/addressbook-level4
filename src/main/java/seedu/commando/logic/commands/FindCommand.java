package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.ui.UiModel;

import java.util.Collections;
import java.util.Set;

//@@author A0139697H
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

        if (keywords.isEmpty() && tags.isEmpty()) {
            // if no keywords or tags are provided, show all unfinished to-dos
            model.clearUiToDoListFilter(UiModel.FILTER_MODE.UNFINISHED);
            return new CommandResult(Messages.FIND_COMMAND_CLEAR);
        } else {
            model.setUiToDoListFilter(keywords, tags, UiModel.FILTER_MODE.UNFINISHED);
            return new CommandResult(String.format(Messages.FIND_COMMAND, keywords, tags));
        }
    }
}
