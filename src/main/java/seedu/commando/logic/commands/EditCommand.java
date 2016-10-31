package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.*;

//@@author A0139697H
/**
 * Edits a to-do in the current to-do list
 * Public fields are initially null and are optional parameters for the command
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    private final int toDoIndex;
    public Title title;
    public DateRange dateRange;
    public DueDate dueDate;
    public Set<Tag> tags;

    public EditCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        Optional<UiToDo> toDoToEdit = model.getUiToDoAtIndex(toDoIndex);

        if (!toDoToEdit.isPresent()) {
            return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        // Copy original to-do
        ToDo newToDo = new ToDo(toDoToEdit.get());

        // Set fields if exist
        if (title != null) {
            newToDo.setTitle(title);
        }

        if (dueDate != null) {
            newToDo.setDueDate(dueDate);
        }

        if (dateRange != null) {
            newToDo.setDateRange(dateRange);
        }

        if (tags != null) {
            newToDo.setTags(tags);
        }

        // Check if to-do has changed
        if (newToDo.isSameStateAs(toDoToEdit.get())) {
            return new CommandResult(Messages.EDIT_COMMAND_NO_EDITS, true);
        }

        // Ensure to-do doesn't have both duedate and daterange
        if (newToDo.getDateRange().isPresent() && newToDo.getDueDate().isPresent()) {
            return new CommandResult(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, true);
        }

        try {
            model.changeToDoList(new ToDoListChange(
                new ToDoList().add(newToDo),
                new ToDoList().add(toDoToEdit.get())
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        String feedback = String.format(Messages.EDIT_COMMAND, newToDo.getTitle().toString());

        // If event already over, warn user
        if (newToDo.getDateRange().isPresent()
            && newToDo.getDateRange().get().endDate.isBefore(LocalDateTime.now())) {
            feedback += "\n" + Messages.EDIT_COMMAND_EVENT_OVER;
        }

        return new CommandResult(feedback);
    }
}