package seedu.commando.logic.commands;

import com.google.common.collect.Lists;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Edits a to-do in the current to-do list
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    private final int toDoIndex;
    private Title title;
    private DateRange dateRange;
    private DueDate dueDate;
    private Set<Tag> tags = new HashSet<>();

    public EditCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    public void setTitle(String title) throws IllegalValueException {
        assert title != null;

        this.title = new Title(title);
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

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;
        assert toDoAtIndices != null;

        Optional<ReadOnlyToDo> toDoToEdit = getToDoAtIndex(toDoAtIndices, toDoIndex);

        if (!toDoToEdit.isPresent()) {
            return new CommandResult(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        // Copy original to-do
        ToDo newToDo = new ToDo(toDoToEdit.get());

        if (title != null) {
            newToDo.setTitle(title);
        }

        if (dueDate != null) {
            newToDo.setDueDate(dueDate);
        }

        if (dateRange != null) {
            newToDo.setDateRange(dateRange);
        }

        if (!tags.isEmpty()) {
            newToDo.setTags(tags);
        }

        if (newToDo.equals(toDoToEdit.get())) {
            return new CommandResult(Messages.MESSAGE_TODO_NO_EDITS, true);
        }

        try {
            model.changeToDoList(new ToDoListChange(
                Collections.singletonList(newToDo),
                Collections.singletonList(toDoToEdit.get())
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.MESSAGE_TODO_EDITED, newToDo.getTitle().toString()));
    }
}
