package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.todo.ReadOnlyToDo;

import java.util.List;
import java.util.Optional;

/**
 * Marks a to-do item as done
 */
public class FinishCommand extends Command {

    public static final String COMMAND_WORD = "finish";

    public final int toDoIndex;

    public FinishCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;
        assert toDoAtIndices != null;

        Optional<ReadOnlyToDo> toDoToFinish = getToDoAtIndex(toDoAtIndices, toDoIndex);

        if (!toDoToFinish.isPresent()) {
            return new CommandResult(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        try {
            model.deleteToDo(toDoToFinish.get());
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.MESSAGE_TODO_FINISHED, toDoToFinish.get().getTitle().toString()));
    }

}
