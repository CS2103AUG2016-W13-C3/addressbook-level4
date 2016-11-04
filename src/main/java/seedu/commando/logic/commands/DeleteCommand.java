package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.*;
import seedu.commando.model.ui.UiToDo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//@@author A0139697H

/**
 * Deletes to-do(s), or their fields.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    private final List<Integer> toDoIndices;
    private boolean ifDeleteTime = false;
    private boolean ifDeleteTags = false;
    private boolean ifDeleteRecurrence = false;

    /**
     * Initializes a delete command.
     *
     * @param toDoIndices list of indices of UI to-dos to target, non-null
     */
    public DeleteCommand(List<Integer> toDoIndices) {
        assert toDoIndices != null;

        this.toDoIndices = toDoIndices;
    }

    //@@author A0142230B
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

            if (ifDeleteTags) {
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
            } else if (ifDeleteRecurrence) {
                // Check if there is a date range and it is recurring
                Optional<DateRange> dateRangeOptional = toDoToEdit.getDateRange();
                Optional<DueDate> dueDateOptional = toDoToEdit.getDueDate();

                if (dateRangeOptional.isPresent() && dateRangeOptional.get().recurrence != Recurrence.None) {
                    try {
                        toDoToEdit.setDateRange(
                            new DateRange(
                                dateRangeOptional.get().startDate,
                                dateRangeOptional.get().endDate,
                                Recurrence.None
                            )
                        );
                    } catch (IllegalValueException e) {
                        assert false : "Deleting recurrence should always be valid";
                    }
                } else if (dueDateOptional.isPresent() && dueDateOptional.get().recurrence != Recurrence.None) {
                    toDoToEdit.setDueDate(
                        new DueDate(
                            dueDateOptional.get().value,
                            Recurrence.None
                        )
                    );
                } else {
                    return new CommandResult(String.format(Messages.DELETE_COMMAND_NO_RECURRENCE, index), true);
                }
            }

            try {
                listToEdit.add(toDoToEdit);
            } catch (IllegalValueException exception) {
                return new CommandResult(exception.getMessage(), true);
            }
        }

        // if no deletion of fields, delete the whole to-do
        if (!ifDeleteTags && !ifDeleteTime && !ifDeleteRecurrence) {
            try {
                // Form comma-separated list of to-dos to be deleted
                String toDoTitles = getToDoTitlesString(model);

                model.changeToDoList(new ToDoListChange(new ToDoList(), listToDelete));

                return new CommandResult(String.format(Messages.DELETE_COMMAND, toDoTitles));
            } catch (IllegalValueException exception) {
                return new CommandResult(exception.getMessage(), true);
            }
        } else {
            // if any deletion of fields, edit the to-do
            try {
                // Form comma-separated list of to-dos to be deleted
                String toDoTitles = getToDoTitlesString(model);

                model.changeToDoList(new ToDoListChange(listToEdit, listToDelete));

                return new CommandResult(String.format(Messages.EDIT_COMMAND, toDoTitles));

            } catch (IllegalValueException exception) {
                return new CommandResult(exception.getMessage(), true);
            }

        }
    }

    private String getToDoTitlesString(Model model) {
        return toDoIndices.stream().map(
            toDoIndex -> model.getUiToDoAtIndex(toDoIndex).get().getTitle().toString()
        ).collect(Collectors.joining(", "));
    }

    /**
     * Sets the delete command to delete the time constraints of the to-dos at indices.
     */
    public void deletesTime() {
        this.ifDeleteTime = true;
    }

    /**
     * Sets the delete command to delete the tags of the to-dos at indices.
     */
    public void deletesTags() {
        this.ifDeleteTags = true;
    }

    /**
     * Sets the delete command to delete the recurrences of the to-dos at indices.
     */
    public void deletesRecurrence() {
        this.ifDeleteRecurrence = true;
    }
}
