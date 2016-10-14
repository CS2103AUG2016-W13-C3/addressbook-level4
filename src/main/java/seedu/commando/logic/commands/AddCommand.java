package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adds a to-do to the to-do list
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    private final ToDo toDo;
    private DateRange dateRange;
    private DueDate dueDate;
    private Set<Tag> tags = new HashSet<>();

    public AddCommand(String titleString) throws IllegalValueException {
        assert titleString != null;

        final Title title = new Title(titleString);
        toDo = new ToDo(title);
    }

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;

        // Set fields if exist
        if (dueDate != null) {
            toDo.setDueDate(dueDate);
        }

        if (dateRange != null) {
            toDo.setDateRange(dateRange);
        }

        if (!tags.isEmpty()) {
            toDo.setTags(tags);
        }

        model.addToDo(toDo);
        return new CommandResult(String.format(Messages.MESSAGE_TODO_ADDED, toDo.getTitle().toString()));
    }

    public void setDueDate(LocalDateTime dueDate) throws IllegalValueException {
        this.dueDate = new DueDate(dueDate);
    }

    public void setDateRange(LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        this.dateRange = new DateRange(startDate, endDate);
    }

    /**
     * Sets tags for edit command
     * Tags will be checked for validity
     */
    public void setTags(Set<String> tags) throws IllegalValueException {
        assert tags != null;

        this.tags.clear();

        for (String tag : tags) {
            this.tags.add(new Tag(tag));
        }
    }
}
