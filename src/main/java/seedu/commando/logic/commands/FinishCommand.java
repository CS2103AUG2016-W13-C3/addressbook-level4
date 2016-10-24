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
 * Marks a to-do item as done
 */
public class FinishCommand extends Command {

	public static final String COMMAND_WORD = "finish";

	public final List<Integer> toDoIndices;

	public FinishCommand(List<Integer> toDoIndices) {
		this.toDoIndices = toDoIndices;
	}

	@Override
	public CommandResult execute() throws NoModelException {
		Model model = getModel();
		int index;
		ToDoList listToFinish = new ToDoList();
		ToDoList finishedToDos = new ToDoList();
		Iterator<Integer> iterator = toDoIndices.iterator();

		// If to-do with the index is valid and not finished, mark it as finished, else throw error message and return
		while (iterator.hasNext()) {
			index = iterator.next();
			Optional<UiToDo> toDoToFinish = model.getUiToDoAtIndex(index);

			if (!toDoToFinish.isPresent()) {
				return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, index), true);
			}

			if (toDoToFinish.get().isEvent()) {
				return new CommandResult(String.format(Messages.FINISH_COMMAND_CANNOT_FINISH_EVENT, toDoToFinish.get().getTitle().toString()), true);
			}

			if (toDoToFinish.get().isFinished()) {
				return new CommandResult(
						String.format(Messages.FINISH_COMMAND_ALREADY_FINISHED, toDoToFinish.get().getTitle().toString()), true);
			}

            try {
                listToFinish.add(toDoToFinish.get());

                // Mark as finished
                finishedToDos.add(new ToDo(toDoToFinish.get()).setIsFinished(true));
            } catch (IllegalValueException exception) {
                return new CommandResult(exception.getMessage(), true);
            }
		}

		try {
			model.changeToDoList(new ToDoListChange(finishedToDos, listToFinish));
		} catch (IllegalValueException exception) {
			return new CommandResult(exception.getMessage(), true);
		}

		return new CommandResult(String.format(Messages.FINISH_COMMAND, toDoIndices.toString()));
	}
}
