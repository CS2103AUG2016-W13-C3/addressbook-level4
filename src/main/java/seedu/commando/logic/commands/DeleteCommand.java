package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.List;
import java.util.Optional;

/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public final int toDoIndex;

    public DeleteCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;
        assert toDoAtIndices != null;

        Optional<ReadOnlyToDo> toDoToDelete = getToDoAtIndex(toDoAtIndices, toDoIndex);

        if (!toDoToDelete.isPresent()) {
            return new CommandResult(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        try {
            model.deleteToDo(toDoToDelete.get());
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.MESSAGE_TODO_DELETED, toDoToDelete.get().getTitle().toString()));
    }

}
