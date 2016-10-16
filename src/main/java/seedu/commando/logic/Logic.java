package seedu.commando.logic;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.logic.commands.CommandResult;

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
     * Returns observable list of events
     * Events are in chronological order
     * */
    UnmodifiableObservableList<UiToDo> getObservableEventList();

    /**
     *  Return observable list of tasks
     *  Tasks are in chronological order, with those with DueDate on top
     * */
    UnmodifiableObservableList<UiToDo> getObservableTaskList();
}
