package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;

import java.util.Collections;
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
    public CommandResult execute()
        throws IllegalValueException, NoModelException {
        Model model = getModel();

        Optional<UiToDo> toDoToDelete = model.getUiToDoAtIndex(toDoIndex);

        if (!toDoToDelete.isPresent()) {
            return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        try {
            model.changeToDoList(new ToDoListChange(
                new ToDoList(),
                new ToDoList().add(toDoToDelete.get())
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.TODO_DELETED, toDoToDelete.get().getTitle().toString()));
    }

}
