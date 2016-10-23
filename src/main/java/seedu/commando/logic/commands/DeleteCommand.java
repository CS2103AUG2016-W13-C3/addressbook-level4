package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.Recurrence;
import seedu.commando.model.todo.ToDo;
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
    public boolean ifDeleteTime = false;
    public boolean ifDeleteTags = false;

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

        // if no deletion of fields, delete the whole to-do
        if (!ifDeleteTags && !ifDeleteTime) {
            model.changeToDoList(new ToDoListChange(
                new ToDoList(),
                new ToDoList().add(toDoToDelete.get())
            ));

            return new CommandResult(String.format(Messages.TODO_DELETED, toDoToDelete.get().getTitle().toString()));
        } else {
            ToDo toDoToEdit = new ToDo(toDoToDelete.get());

            if (ifDeleteTags) {
                if (toDoToEdit.getTags().size() > 0) {
                    toDoToEdit.setTags(Collections.emptySet());
                } else {
                    throw new IllegalValueException(Messages.DELETE_COMMAND_NO_TAGS);
                }
            }

            if (ifDeleteTime) {
                if (toDoToEdit.hasTimeConstraint()) {
                    toDoToEdit.clearTimeConstraint();
                } else {
                    throw new IllegalValueException(Messages.DELETE_COMMAND_NO_TIME_CONSTRAINTS);
                }
            }

            model.changeToDoList(new ToDoListChange(
                new ToDoList().add(toDoToEdit),
                new ToDoList().add(toDoToDelete.get())
            ));

            return new CommandResult(String.format(Messages.TODO_EDITED, toDoToDelete.get().getTitle().toString()));
        }
    }

}
