package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.ToDo;

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
    public CommandResult execute()
            throws IllegalValueException, NoModelException {
        Model model = getModel();

        Optional<UiToDo> toDoToFinish = model.getUiToDoAtIndex(toDoIndex);

        if (!toDoToFinish.isPresent()) {
            return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        if (toDoToFinish.get().isFinished()) {
            return new CommandResult(String.format(Messages.TODO_ALREADY_FINISHED, toDoToFinish.get().getTitle().toString()), true);
        }

        // Mark as finished
        ToDo finishedToDo = new ToDo(toDoToFinish.get());
        finishedToDo.setIsFinished(true);

        try {
            model.changeToDoList(new ToDoListChange(
                    new ToDoList().add(finishedToDo),
                    new ToDoList().add(toDoToFinish.get())
                    ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.TODO_UNFINISHED, toDoToFinish.get().getTitle().toString()));
    }

}
