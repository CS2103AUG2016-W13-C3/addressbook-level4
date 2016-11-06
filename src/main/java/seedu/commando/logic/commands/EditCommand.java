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
 * Edits a to-do in the current to-do list.
 */
public class EditCommand extends Command {
    public static final String COMMAND_WORD = "edit";

    private final int toDoIndex;
    private Title title = null;
    private DateRange dateRange = null;
    private DueDate dueDate = null;
    private Set<Tag> tags = null;

    /**
     * Initializes an edit command.
     *
     * @param toDoIndex index of UI to-do to edit
     */
    public EditCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        try {
            ReadOnlyToDo toDoToEdit = getToDoAtIndex(model, toDoIndex);
            ToDo editedToDo = getEditedToDo(toDoToEdit);
            ensureToDoEdited(toDoToEdit, editedToDo);
            ensureOnlyDueDateOrDateRangePresent(editedToDo);

            model.changeToDoList(new ToDoListChange(
                new ToDoList().add(editedToDo),
                new ToDoList().add(toDoToEdit)
            ));

            return new CommandResult(getFeedback(editedToDo));

        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }
    }

    private String getFeedback(ToDo editedToDo) {
        String feedback = String.format(Messages.EDIT_COMMAND, editedToDo.getTitle().toString());

        // If event already over, warn user
        if (editedToDo.getDateRange().isPresent()
            && editedToDo.getDateRange().get().endDate.isBefore(LocalDateTime.now())) {
            feedback += "\n" + Messages.EDIT_COMMAND_EVENT_OVER_WARNING;
        }
        return feedback;
    }

    private void ensureToDoEdited(ReadOnlyToDo toDoToEdit, ToDo editedToDo) throws IllegalValueException {
        // Check if to-do has changed
        if (editedToDo.isSameStateAs(toDoToEdit)) {
            throw new IllegalValueException(Messages.EDIT_COMMAND_NO_EDITS);
        }
    }

    private ToDo getEditedToDo(ReadOnlyToDo toDoToEdit) {
        // Copy original to-do
        ToDo newToDo = new ToDo(toDoToEdit);

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

        return newToDo;
    }

    private void ensureOnlyDueDateOrDateRangePresent(ReadOnlyToDo toDo) throws IllegalValueException {
        // Ensure to-do doesn't have both duedate and daterange
        if (toDo.getDateRange().isPresent() && toDo.getDueDate().isPresent()) {
            throw new IllegalValueException(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE);
        }
    }

    private ReadOnlyToDo getToDoAtIndex(Model model, int toDoIndex) throws IllegalValueException {
        Optional<UiToDo> toDoToEdit = model.getUiToDoAtIndex(toDoIndex);

        if (!toDoToEdit.isPresent()) {
            throw new IllegalValueException(String.format(Messages.TODO_ITEM_INDEX_INVALID, toDoIndex));
        }

        return toDoToEdit.get();
    }

    /**
     * Sets the title for the target to-do, must be non-null.
     *
     * @param title title to set
     */
    public void setTitle(Title title) {
        assert title != null;

        this.title = title;
    }

    /**
     * Sets the date range for the target to-do, must be non-null.
     *
     * @param dateRange date range to set
     */
    public void setDateRange(DateRange dateRange) {
        assert dateRange != null;

        this.dateRange = dateRange;
    }

    /**
     * Sets the due date for the target to-do, must be non-null.
     *
     * @param dueDate due date to set
     */
    public void setDueDate(DueDate dueDate) {
        assert dueDate != null;

        this.dueDate = dueDate;
    }

    /**
     * Sets the tags to replace for the target to-do, must be non-null.
     *
     * @param tags tags to set
     */
    public void setTags(Set<Tag> tags) {
        assert tags != null;

        this.tags = tags;
    }
}