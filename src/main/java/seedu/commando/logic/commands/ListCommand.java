package seedu.commando.logic.commands;

import java.util.Optional;

import seedu.commando.commons.core.Messages;
import seedu.commando.logic.commands.Command.NoModelException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.ui.UiModel;

//@@author A0142230B
public class ListCommand extends Command {
	public static final String COMMAND_WORD = "list";
	private Optional<DateRange> dateRange;

	public ListCommand(Optional<DateRange> dateRange) {
		this.dateRange = dateRange;
	}

	@Override
	public CommandResult execute() throws NoModelException {
		Model model = getModel();

		// if no time provided, show all to-dos
		if (!dateRange.isPresent()) {
			model.clearUiToDoListFilter(UiModel.FILTER_MODE.ALL);
			return new CommandResult(Messages.LIST_COMMAND_CLEAR);
		}

		// Search the toDoList and filter with the dateRange
		else {
			model.setUiToDoListFilter(dateRange.get());
			return new CommandResult(
                String.format(Messages.LIST_COMMAND, dateRange.get())
			);
		}
	}
}
