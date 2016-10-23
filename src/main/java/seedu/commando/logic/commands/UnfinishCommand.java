package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.ToDo;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Marks a to-do item as not done
 */
public class UnfinishCommand extends Command {

    public static final String COMMAND_WORD = "unfinish";

	public final List<Integer> toDoIndices;

    public UnfinishCommand(List<Integer> toDoIndices) {
    	this.toDoIndices = toDoIndices;
    }


	@Override
	public CommandResult execute() throws IllegalValueException, NoModelException {
		Model model = getModel();
		int index;
		ToDoList listToUnfinish = new ToDoList();
		ToDoList unfinishedToDos = new ToDoList();
		Iterator<Integer> iterator = toDoIndices.iterator();
		//If to-do with the index is valid and finished, mark it as unfinished, else throw error message and return
		while (iterator.hasNext()) {
			index = iterator.next();
			Optional<UiToDo> toDoToUnfinish = model.getUiToDoAtIndex(index);
			if (!toDoToUnfinish.isPresent()) {
				return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, index), true);
			}
			if (!toDoToUnfinish.get().isFinished()) {
				return new CommandResult(
						String.format(Messages.TODO_ALREADY_ONGOING, toDoToUnfinish.get().getTitle().toString()), true);
			}
			listToUnfinish.add(toDoToUnfinish.get());
			// Mark as unfinished
			unfinishedToDos.add(new ToDo(toDoToUnfinish.get()).setIsFinished(false));
		}

		try {
			model.changeToDoList(
					new ToDoListChange(unfinishedToDos, listToUnfinish));
		} catch (IllegalValueException exception) {
			return new CommandResult(exception.getMessage(), true);
		}

		return new CommandResult(String.format(Messages.TODO_UNFINISHED, toDoIndices.toString()));
	}

}
