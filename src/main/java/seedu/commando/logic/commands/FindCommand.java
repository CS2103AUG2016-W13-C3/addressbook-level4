package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.ui.UiModel;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

//@@author A0139697H

/**
 * Shows all unfinished to-dos, optionally filtering by keywords and tags.
 */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";

    private Set<String> keywords = Collections.emptySet();
    private Set<Tag> tags = Collections.emptySet();

    /**
     * Initializes a find command.
     */
    public FindCommand() {}

    /**
     * Asserts that {@code uiLogic} is non-null
     */
    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        if (keywords.isEmpty() && tags.isEmpty()) {
            // if no keywords or tags are provided, show all unfinished to-dos
            model.clearUiToDoListFilter(Model.FILTER_MODE.UNFINISHED);
            return new CommandResult(Messages.FIND_COMMAND_CLEAR);
        } else {
            model.setUiToDoListFilter(keywords, tags, Model.FILTER_MODE.UNFINISHED);
            return new CommandResult(String.format(Messages.FIND_COMMAND, new TreeSet<>(keywords), new TreeSet<>(tags)));
        }
    }

    /**
     * Sets the keywords for the command, must be non-null.
     *
     * @param keywords set of keywords to set
     */
    public void setKeywords(Set<String> keywords) {
        assert keywords != null;

        this.keywords = keywords;
    }

    /**
     * Sets the tags for the command, must be non-null.
     *
     * @param tags set of tags to set
     */
    public void setTags(Set<Tag> tags) {
        assert tags != null;

        this.tags = tags;
    }
}
