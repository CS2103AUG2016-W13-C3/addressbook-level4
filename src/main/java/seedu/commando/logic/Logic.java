package seedu.commando.logic;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.logic.commands.CommandResult;
import seedu.commando.model.Model;
import seedu.commando.model.ui.UiToDo;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result
     * @param commandText The command as entered by the user
     * @return the result of the command execution
     */
    CommandResult execute(String commandText);

    /**
     * @see Model#getUiEventList()
     * */
    UnmodifiableObservableList<UiToDo> getObservableEventList();

    /**
     * @see Model#getUiTaskList()
     * */
    UnmodifiableObservableList<UiToDo> getObservableTaskList();
}
