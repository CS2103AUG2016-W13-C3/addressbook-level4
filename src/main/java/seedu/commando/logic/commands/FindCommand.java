package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
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

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;

        if (keywords.size() > 0) {
            model.updateToDoListFilter(keywords);
            return new CommandResult(String.format(Messages.MESSAGE_FIND, model.getFilteredToDoList().size()));
        } else {
            model.clearToDoListFilter();
            return new CommandResult(Messages.MESSAGE_CLEAR_FIND);
        }
    }
}
