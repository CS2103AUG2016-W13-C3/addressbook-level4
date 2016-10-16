package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.List;
import java.util.Set;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        assert keywords != null;

        this.keywords = keywords;
    }

    /**
     * Asserts that {@code uiLogic} is non-null
     */
    @Override
    public CommandResult execute(EventsCenter eventsCenter, UiLogic uiLogic, Model model) {
        assert uiLogic != null;

        if (keywords.size() > 0) {
            uiLogic.setToDoListFilter(keywords);
            return new CommandResult(String.format(Messages.FIND, uiLogic.getObservableEvents().size(), uiLogic.getObservableTasks().size()));
        } else {
            uiLogic.clearToDoListFilter();
            return new CommandResult(Messages.CLEAR_FIND);
        }
    }
}
