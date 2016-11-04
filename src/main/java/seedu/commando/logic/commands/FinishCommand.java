package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.todo.ToDo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@@author A0142230B
/**
 * Marks a to-do item as done.
 */
public class FinishCommand extends Command {

    public static final String COMMAND_WORD = "finish";

    public final List<Integer> toDoIndices;

    /**
     * Initializes a finish command.
     * @param toDoIndices list of indices of to-dos that to finish
     */
    public FinishCommand(List<Integer> toDoIndices) {
        this.toDoIndices = toDoIndices;
    }

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();
        ToDoList listToFinish = new ToDoList();
        ToDoList finishedToDos = new ToDoList();

        // If to-do with the index is valid and not finished, mark it as finished, else throw error message and return
		for (int index : toDoIndices) {
			Optional<UiToDo> toDoToFinish = model.getUiToDoAtIndex(index);

			CommandResult errorResult = checkToDoFinishable(index, toDoToFinish);

			if (errorResult != null) {
				return errorResult;
			} else {
				try {
					listToFinish.add(toDoToFinish.get());
					// Mark as finished
					finishedToDos.add(new ToDo(toDoToFinish.get()).setIsFinished(true));
				} catch (IllegalValueException exception) {
					return new CommandResult(exception.getMessage(), true);
				}
			}
		}
        
        return updateToDoListChange(model, listToFinish, finishedToDos);
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
	private CommandResult updateToDoListChange(Model model, ToDoList listToFinish, ToDoList finishedToDos) {
		try {
			// Form comma-separated list of to-dos to be finished
	        String toDoTitles = getToDoTitlesString(model);
			model.changeToDoList(new ToDoListChange(finishedToDos, listToFinish));
			return new CommandResult(String.format(Messages.FINISH_COMMAND, toDoTitles));
		} catch (IllegalValueException exception) {
			return new CommandResult(exception.getMessage(), true);
		}
	}
	/**
	 * Check if the given to-do is valid and can be finished.
	 * @param toDoToFinish the given to-do
	 * @return CommandResult with error message, if no error returns null
	 */
	private CommandResult checkToDoFinishable(int index, Optional<UiToDo> toDoToFinish) {
		if (!toDoToFinish.isPresent()) {
			return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, index), true);
		}

		else if (toDoToFinish.get().isEvent()) {
			return new CommandResult(String.format(Messages.FINISH_COMMAND_CANNOT_FINISH_EVENT,
					toDoToFinish.get().getTitle().toString()), true);
		}

		else if (toDoToFinish.get().isFinished()) {
			return new CommandResult(
					String.format(Messages.FINISH_COMMAND_ALREADY_FINISHED, toDoToFinish.get().getTitle().toString()),
					true);
		}
		return null;
	}
}
