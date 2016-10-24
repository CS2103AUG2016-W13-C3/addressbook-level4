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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Deletes to-do(s), or their fields.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public final List<Integer> toDoIndices;
    public boolean ifDeleteTime = false;
    public boolean ifDeleteTag = false;

	public DeleteCommand(List<Integer> toDoIndices) {
		this.toDoIndices = toDoIndices;
	}

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();
        int index;
		ToDoList listToDelete = new ToDoList();
		ToDoList listToEdit = new ToDoList();
		
		Iterator<Integer> iterator = toDoIndices.iterator();
		// If to-do with the index is valid, add it to the listToDelete
		// If delete any fields is required, add it to the listToEdit,too.
		// else throw error message and return
		while (iterator.hasNext()) {
			index = iterator.next();
			Optional<UiToDo> toDoToDelete = model.getUiToDoAtIndex(index);
			if (!toDoToDelete.isPresent()) {
				return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, index), true);
			}
			ToDo toDoToEdit = new ToDo(toDoToDelete.get());

            try {
                listToDelete.add(toDoToDelete.get());
            } catch (IllegalValueException exception) {
                return new CommandResult(exception.getMessage(), true);
            }

            if (ifDeleteTag) {
				if (toDoToEdit.getTags().size() > 0) {
					toDoToEdit.setTags(Collections.emptySet());
				} else {
					return new CommandResult(String.format(Messages.DELETE_COMMAND_NO_TAGS, index), true);
				}
			}
			if (ifDeleteTime) {
				if (toDoToEdit.hasTimeConstraint()) {
					toDoToEdit.clearTimeConstraint();
				} else {
					return new CommandResult(String.format(Messages.DELETE_COMMAND_NO_TIME_CONSTRAINTS, index), true);
				}
			}
            try {
                listToEdit.add(toDoToEdit);
            } catch (IllegalValueException exception) {
                return new CommandResult(exception.getMessage(), true);
            }
        }

        // if no deletion of fields, delete the whole to-do
        if (!ifDeleteTag && !ifDeleteTime) {
    		try {
    			model.changeToDoList(new ToDoListChange(new ToDoList(), listToDelete));
    		} catch (IllegalValueException exception) {
    			return new CommandResult(exception.getMessage(), true);
    		}

    		return new CommandResult(String.format(Messages.TODO_DELETED, toDoIndices.toString()));
        } else {
        // if any deletion of fields, edit the whole to-do
    		try {
    			model.changeToDoList(new ToDoListChange(listToEdit, listToDelete));
    		} catch (IllegalValueException exception) {
    			return new CommandResult(exception.getMessage(), true);
    		}
            return new CommandResult(String.format(Messages.TODO_EDITED, toDoIndices.toString()));
        }
    }

}
