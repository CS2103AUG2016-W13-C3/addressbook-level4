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
public class UnfinishCommand extends Command {

    public static final String COMMAND_WORD = "unfinish";

    public final int toDoIndex;

    public UnfinishCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    @Override
    public CommandResult execute()
            throws IllegalValueException, NoModelException {
        Model model = getModel();

        Optional<UiToDo> toDoToUnfinish = model.getUiToDoAtIndex(toDoIndex);

        if (!toDoToUnfinish.isPresent()) {
            return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        if (!toDoToUnfinish.get().isFinished()) {
            return new CommandResult(String.format(Messages.TODO_ALREADY_ONGOING, toDoToUnfinish.get().getTitle().toString()), true);
        }

        // Mark as unfinished
        ToDo unfinishedToDo = new ToDo(toDoToUnfinish.get());
        unfinishedToDo.setIsFinished(false);

        try {
            model.changeToDoList(new ToDoListChange(
                new ToDoList().add(unfinishedToDo),
                new ToDoList().add(toDoToUnfinish.get())
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.TODO_UNFINISHED, toDoToUnfinish.get().getTitle().toString()));
    }

}
