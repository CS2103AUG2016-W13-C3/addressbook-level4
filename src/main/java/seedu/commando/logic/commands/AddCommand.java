package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.Set;

//@@author A0139697H

/**
 * Adds a to-do to the to-do list.
 */
public class AddCommand extends Command {
    public static final String COMMAND_WORD = "add";

    // To-do that is going to be added
    private final ToDo toDo;

    /**
     * Initializes an add command.
     *
     * @param title title of to-do to add, non-null
     */
    public AddCommand(Title title) {
        assert title != null;

        toDo = new ToDo(title);
    }

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        try {
            ensureOnlyDueDateOrDateRangePresent();

            model.changeToDoList(new ToDoListChange(
                new ToDoList().add(toDo),
                new ToDoList()
            ));

            return new CommandResult(getFeedback());

        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }
    }

    private String getFeedback() {
        String feedback = String.format(Messages.ADD_COMMAND, toDo.getTitle().toString());

        // If event already over, warn user
        if (toDo.getDateRange().isPresent()
            && toDo.getDateRange().get().endDate.isBefore(LocalDateTime.now())) {
            feedback += "\n" + Messages.ADD_COMMAND_EVENT_OVER_WARNING;
        }

        return feedback;
    }

    private void ensureOnlyDueDateOrDateRangePresent() throws IllegalValueException {
        // Ensure to-do doesn't have both duedate and daterange
        if (toDo.getDateRange().isPresent() && toDo.getDueDate().isPresent()) {
            throw new IllegalValueException(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE);
        }
    }

    /**
     * Sets the date range of the to-do to be added, must be non-null.
     *
     * @param dateRange date range to set
     */
    public void setDateRange(DateRange dateRange) {
        assert dateRange != null;

        toDo.setDateRange(dateRange);
    }

    /**
     * Sets the due date of the to-do to be added, must be non-null.
     *
     * @param dueDate due date to set
     */
    public void setDueDate(DueDate dueDate) {
        assert dueDate != null;

        toDo.setDueDate(dueDate);
    }

    /**
     * Gets the title of the to-do to be added.
     *
     * @return optional of title of to-do to be added, empty if none
     */
    public Title getTitle() {
        return toDo.getTitle();
    }

    /**
     * Sets the tags of the to-do to be added, must be non-null.
     *
     * @param tags set of tags to set
     */
    public void setTags(Set<Tag> tags) {
        assert tags != null;

        toDo.setTags(tags);
    }
}
