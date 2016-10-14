package seedu.commando.logic;

import javafx.collections.ObservableList;
import seedu.commando.commons.core.IndexedItem;
import seedu.commando.logic.commands.CommandResult;
import seedu.commando.model.todo.ReadOnlyToDo;

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
     * */
    ObservableList<IndexedItem<ReadOnlyToDo>> getObservableEventList();

    /**
     *  Return observable list of tasks
     *  Tasks are in chronological order, with those with DueDate on top
     * */
    ObservableList<IndexedItem<ReadOnlyToDo>> getObservableTaskList();
}
