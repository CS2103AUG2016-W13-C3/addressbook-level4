package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.todo.ToDo;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
//@@author A0142230B
/**
 * Marks a to-do item as not done.
 */
public class UnfinishCommand extends Command {

    public static final String COMMAND_WORD = "unfinish";

	public final List<Integer> toDoIndices;

    /**
     * Initializes a unfinish command.
     * @param toDoIndices list of indices of to-dos that to unfinish
     */
	public UnfinishCommand(List<Integer> toDoIndices) {
		this.toDoIndices = toDoIndices;
	}

	@Override
	public CommandResult execute() throws NoModelException {
		Model model = getModel();
		ToDoList listToUnfinish = new ToDoList();
		ToDoList unfinishedToDos = new ToDoList();

		// If to-do with the index is valid and finished, mark it as unfinished,
		// else throw error message and return
		for (int index : toDoIndices) {
			Optional<UiToDo> toDoToUnfinish = model.getUiToDoAtIndex(index);

			CommandResult errorResult = checkToDoUnfinishable(index, toDoToUnfinish);

			if (errorResult != null) {
				return errorResult;
			} else {
				try {
					listToUnfinish.add(toDoToUnfinish.get());

					// Mark as unfinished
					unfinishedToDos.add(new ToDo(toDoToUnfinish.get()).setIsFinished(false));
				} catch (IllegalValueException e) {
					return new CommandResult(e.getMessage(), true);
				}
			}
		}
			return updateToDoListChange(model, listToUnfinish, unfinishedToDos);
	}

    private String getToDoTitlesString(Model model) {
        return toDoIndices.stream().map(
            toDoIndex -> model.getUiToDoAtIndex(toDoIndex).get().getTitle().toString()
        ).collect(Collectors.joining(", "));
    }
    
	/**
	 * Update the to-do list after changes to the to-dos
	 * @return CommandResult with error message, if no error returns null
	 */
	private CommandResult updateToDoListChange(Model model, ToDoList listToUnfinish, ToDoList unfinishedToDos) {
		try {
			// Form comma-separated list of to-dos to be unfinished
	        String toDoTitles = getToDoTitlesString(model);
			model.changeToDoList(new ToDoListChange(unfinishedToDos, listToUnfinish));
			return new CommandResult(String.format(Messages.UNFINISHED_COMMAND, toDoTitles));
		} catch (IllegalValueException exception) {
			return new CommandResult(exception.getMessage(), true);
		}
	}

	/**
	 * Check if the given to-do is valid and can be unfinished.
	 * @param toDoToUnfinish the given to-do
	 * @return CommandResult with error message, if no error returns null
	 */
	private CommandResult checkToDoUnfinishable(int index, Optional<UiToDo> toDoToUnfinish) {
		if (!toDoToUnfinish.isPresent()) {
			return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, index), true);
		}

		else if (toDoToUnfinish.get().isEvent()) {
			return new CommandResult(String.format(Messages.UNFINISH_COMMAND_CANNOT_UNFINISH_EVENT,
					toDoToUnfinish.get().getTitle().toString()), true);
		}

		else if (!toDoToUnfinish.get().isFinished()) {
			return new CommandResult(String.format(Messages.UNFINISH_COMMAND_ALREADY_ONGOING,
					toDoToUnfinish.get().getTitle().toString()), true);
		}
		return null;
	}
}
