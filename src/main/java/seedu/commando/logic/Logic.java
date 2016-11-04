package seedu.commando.logic;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.logic.commands.CommandResult;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.ui.UiToDo;

//@@author A0139697H

/**
 * API of the Logic component.
 */
public interface Logic {

    /**
     * Executes an input string as a command and returns the result.
     *
     * @param commandText input string as entered by the user to be parsed as a command
     * @return result of the command packaged in {@link CommandResult}
     */
    CommandResult execute(String commandText);

    /**
     * Returns observable read-only list of UI to-dos considered as events by {@link UiToDo#isEvent()}
     * to be displayed on the UI.
     * The ordering of the list is to be respected.
     *
     * @return an observable read-only list of {@link UiToDo} that are events
     */
    UnmodifiableObservableList<UiToDo> getUiEvents();

    /**
     * Returns observable read-only list of UI to-dos considered as tasks by {@link UiToDo#isTask()}
     * to be displayed on the UI.
     * The ordering of the list is to be respected.
     *
     * @return an observable read-only list of {@link UiToDo} that are tasks
     */
    UnmodifiableObservableList<UiToDo> getUiTasks();

    /**
     * Gets the full list of to-dos, read-only.
     *
     * @return the full list of to-dos, read-only
     */
    ReadOnlyToDoList getToDoList();
}
