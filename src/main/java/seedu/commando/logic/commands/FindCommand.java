package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Updates the filter for to-do list to show only to-do items containing all
 * of the keywords and tags (case insensitive)
 */
public class FindCommand extends Command {
    public static final String
        COMMAND_WORD = "find";

    public Set<String> keywords = Collections.emptySet();
    public Set<String> tags = Collections.emptySet();

    /**
     * Asserts that {@code uiLogic} is non-null
     */
    @Override
    public CommandResult execute() throws NoContextException {
        UiLogic uiLogic = getUiLogic();

        // if no keywords or tags are provided, clear find
        if (keywords.isEmpty() && tags.isEmpty()) {
            uiLogic.clearToDoListFilter();
            return new CommandResult(Messages.CLEAR_FIND);
        }

        uiLogic.setToDoListFilter(keywords, tags);
        return new CommandResult(String.format(Messages.FIND, uiLogic.getObservableEvents().size(), uiLogic.getObservableTasks().size()));
    }
}
