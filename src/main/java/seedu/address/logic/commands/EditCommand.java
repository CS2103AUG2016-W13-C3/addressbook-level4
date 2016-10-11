package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.model.todo.DateRange;
import seedu.address.model.todo.DueDate;
import seedu.address.model.todo.ReadOnlyToDo;
import seedu.address.model.todo.Title;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        try {
            if (title != null) {
                model.editTodoTitle(toDoToEdit.get(), title);
            }

            if (dueDate != null) {
                model.editTodoDueDate(toDoToEdit.get(), dueDate);
            }

            if (dateRange != null) {
                model.editTodoDateRange(toDoToEdit.get(), dateRange);
            }

            if (!tags.isEmpty()) {
                model.editTodoTags(toDoToEdit.get(), tags);
            }
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.toString(), true);
        }

        return new CommandResult(String.format(Messages.MESSAGE_TODO_EDITED, toDoToEdit.get().getTitle().toString()));
    }
}
