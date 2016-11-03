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
 * Shows all finished to-dos, optionally filtering by keywords and tags.
 */
public class RecallCommand extends Command {
    public static final String COMMAND_WORD = "recall";

    private Set<String> keywords = Collections.emptySet();
    private Set<Tag> tags = Collections.emptySet();

    /**
     * Initializes a recall command.
     */
    public RecallCommand() {}

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        if (keywords.isEmpty() && tags.isEmpty()) {
            // if no keywords or tags are provided, show all finished to-dos
            model.clearUiToDoListFilter(UiModel.FILTER_MODE.FINISHED);
            return new CommandResult(Messages.RECALL_COMMAND_CLEAR);
        } else {
            model.setUiToDoListFilter(keywords, tags, UiModel.FILTER_MODE.FINISHED);
            return new CommandResult(String.format(Messages.RECALL_COMMAND, new TreeSet<>(keywords), new TreeSet<>(tags)));
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
