package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.List;
import java.util.Optional;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command, in the context of {@param model}and {@param eventsCenter}
     * with {@param toDoAtIndices} as the mapping of index -> to-dos
     * @returns result of the command
     */
    public abstract CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter);

    protected Optional<ReadOnlyToDo> getToDoAtIndex(List<ReadOnlyToDo> toDoAtIndices, int toDoIndex) {
        if (toDoIndex - 1 < 0 || toDoIndex - 1 >= toDoAtIndices.size()) {
            return Optional.empty();
        } else {
            return Optional.of(toDoAtIndices.get(toDoIndex - 1));
        }
    }
}
