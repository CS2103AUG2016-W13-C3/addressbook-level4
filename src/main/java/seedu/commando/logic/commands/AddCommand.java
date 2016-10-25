package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.*;

import java.util.Collections;
import java.util.Set;

/**
 * Adds a to-do to the to-do list
 * Public fields are initially null and are optional parameters for the command
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    private final Title title;
    public DateRange dateRange;
    public DueDate dueDate;
    public Set<Tag> tags = Collections.emptySet();

    public AddCommand(Title title) {
        assert title != null;

        this.title = title;
    }

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        // Create the to-do to add
        ToDo toDo = new ToDo(title);

        // Set fields if exist
        if (dueDate != null) {
            toDo.setDueDate(dueDate);
        }

        if (dateRange != null) {
            toDo.setDateRange(dateRange);
        }

        if (tags != null) {
            toDo.setTags(tags);
        }

        // Ensure to-do doesn't have both duedate and daterange
        if (toDo.getDateRange().isPresent() && toDo.getDueDate().isPresent()) {
            return new CommandResult(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, true);
        }

        try {
            model.changeToDoList(new ToDoListChange(
                new ToDoList().add(toDo),
                new ToDoList()
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.TODO_ADDED, toDo.getTitle().toString()));
    }
}
