package seedu.commando.logic.commands;

import seedu.commando.commons.core.DateTimePrettifier;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.model.todo.DateRange;

import java.util.Optional;

//@@author A0142230B

/**
 * Lists all to-dos within a specified date range.
 */
public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";
    private Optional<DateRange> dateRange;

    /**
     * Initializes a list command.
     * @param dateRange requested date range to list
     */
    public ListCommand(Optional<DateRange> dateRange) {
        this.dateRange = dateRange;
    }

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        // if no time provided, show all to-dos
        if (!dateRange.isPresent()) {
            model.clearUiToDoListFilter(Model.FILTER_MODE.ALL);
            return new CommandResult(Messages.LIST_COMMAND_CLEAR);
        }

        // Search the toDoList and filter with the dateRange
        model.setUiToDoListFilter(dateRange.get());

        // No to-dos found in search
        if (model.getUiEvents().isEmpty() && model.getUiTasks().isEmpty()) {
            return new CommandResult(String.format(Messages.LIST_COMMAND_NO_TODOS, getDateRangeString()));
        }

        return new CommandResult(String.format(Messages.LIST_COMMAND, getDateRangeString()));
    }

    private String getDateRangeString() {
        return DateTimePrettifier.prettifyDateTimeRange(
            dateRange.get().startDate, dateRange.get().endDate
        ).replaceAll("\\n", " ");
    }
}
