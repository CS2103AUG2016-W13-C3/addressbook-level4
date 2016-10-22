package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Deletes a person identified using it's last displayed index from the address
 * book.
 */
public class DeleteCommand extends Command {

	public static final String COMMAND_WORD = "delete";

	public final List<Integer> toDoIndices;

	public DeleteCommand(List<Integer> toDoIndices) {
		this.toDoIndices = toDoIndices;
	}

	@Override
	public CommandResult execute() throws IllegalValueException, NoModelException {
		Model model = getModel();
		int index;
		ToDoList listToDelete = new ToDoList();
		Iterator<Integer> iterator = toDoIndices.iterator();
		//If to-do with the index is valid, delete it, else throw error message and return
		while (iterator.hasNext()) {
			index = iterator.next();
			Optional<UiToDo> toDoToDelete = model.getUiToDoAtIndex(index);
			if (!toDoToDelete.isPresent()) {
				return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, index), true);
			}
			listToDelete.add(toDoToDelete.get());
		}

		try {
			model.changeToDoList(new ToDoListChange(new ToDoList(), listToDelete));
		} catch (IllegalValueException exception) {
			return new CommandResult(exception.getMessage(), true);
		}

		return new CommandResult(String.format(Messages.TODO_DELETED, toDoIndices.toString()));
	}

}
