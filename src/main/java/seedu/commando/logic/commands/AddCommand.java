package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    public Set<String> tags;

    public AddCommand(String title) {
        assert title != null;

        this.title = title;
    }

    @Override
    public CommandResult execute(EventsCenter eventsCenter, UiLogic uiLogic, Model model)
        throws IllegalValueException {

        assert model != null;

        // Create the to-do to add
        ToDo toDo = new ToDo(new Title(title));

        // Set fields if exist
        if (dueDate != null) {
            toDo.setDueDate(new DueDate(dueDate));
        }
        if (!CollectionUtil.isAnyNull(dateRangeStart, dateRangeEnd)) {
            toDo.setDateRange(new DateRange(dateRangeStart, dateRangeEnd));
        }
        if (tags != null) {
            toDo.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
        }

        try {
            model.changeToDoList(new ToDoListChange(
                Collections.singletonList(toDo),
                Collections.emptyList()
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.TODO_ADDED, toDo.getTitle().toString()));
    }
}
