package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adds a to-do to the to-do list
 * Public fields are initially null and are optional parameters for the command
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    private final String title;
    public LocalDateTime dateRangeStart;
    public LocalDateTime dateRangeEnd;
    public LocalDateTime dueDate;
    public Set<String> tags = Collections.emptySet();

    public AddCommand(String title) {
        assert title != null;

        this.title = title;
    }

    @Override
    public CommandResult execute()
        throws IllegalValueException, NoModelException {
        Model model = getModel();

        // Create the to-do to add
        ToDo toDo = new ToDo(new Title(title));

        // Set fields if exist
        if (dueDate != null) {
            toDo.setDueDate(new DueDate(dueDate));
        }

        if (dateRangeStart != null && dateRangeEnd != null) {
            toDo.setDateRange(new DateRange(dateRangeStart, dateRangeEnd));
        } else if (dateRangeEnd != null) {
            throw new IllegalValueException(Messages.MISSING_TODO_DATERANGE_START);
        } else if (dateRangeStart != null) {
            throw new IllegalValueException(Messages.MISSING_TODO_DATERANGE_END);
        }

        if (tags != null) {
            toDo.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
        }

        // Ensure to-do doesn't have both duedate and daterange
        if (toDo.getDateRange().isPresent() && toDo.getDueDate().isPresent()) {
            throw new IllegalValueException(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE);
        }

        model.changeToDoList(new ToDoListChange(
            Collections.singletonList(toDo),
            Collections.emptyList()
        ));

        return new CommandResult(String.format(Messages.TODO_ADDED, toDo.getTitle().toString()));
    }
}
