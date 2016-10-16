package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;
import seedu.commando.logic.UiLogic;
import seedu.commando.logic.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Edits a to-do in the current to-do list
 * Public fields are initially null and are optional parameters for the command
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    private final int toDoIndex;
    public String title;
    public LocalDateTime dateRangeStart;
    public LocalDateTime dateRangeEnd;
    public LocalDateTime dueDate;
    public Set<String> tags = Collections.emptySet();

    public EditCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    /**
     * Asserts that {@param uiLogic} and {@param model} is non-null
     */
    @Override
    public CommandResult execute(EventsCenter eventsCenter, UiLogic uiLogic, Model model)
        throws IllegalValueException {
        assert model != null;
        assert uiLogic != null;

        Optional<UiToDo> toDoToEdit = uiLogic.getToDoAtIndex(toDoIndex);

        if (!toDoToEdit.isPresent()) {
            return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        // Copy original to-do
        ToDo newToDo = new ToDo(toDoToEdit.get());

        // Set fields if exist
        if (title != null) {
            newToDo.setTitle(new Title(title));
        }
        if (dueDate != null) {
            newToDo.setDueDate(new DueDate(dueDate));
        }
        if (!CollectionUtil.isAnyNull(dateRangeStart, dateRangeEnd)) {
            newToDo.setDateRange(new DateRange(dateRangeStart, dateRangeEnd));
        }
        if (tags != null) {
            newToDo.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
        }

        // Check if to-do has changed
        if (newToDo.isSameStateAs(toDoToEdit.get())) {
            return new CommandResult(Messages.TODO_NO_EDITS, true);
        }

        try {
            model.changeToDoList(new ToDoListChange(
                Collections.singletonList(newToDo),
                Collections.singletonList(toDoToEdit.get())
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.TODO_EDITED, newToDo.getTitle().toString()));
    }
}