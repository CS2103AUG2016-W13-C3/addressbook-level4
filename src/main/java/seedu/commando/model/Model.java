package seedu.commando.model;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Returns the to-do list */
    ReadOnlyToDoList getToDoList();

    /** Changes the to-do list */
    void changeToDoList(ToDoListChange change) throws IllegalValueException;

    /** Undos the last change to the to-do list, returns true if successful */
    boolean undoToDoList();

    /** Redos the last undo to the to-do list, returns true if successful */
    boolean redoToDoList();

    /** Gets the last to-do list change, considering undos and redos, if it exists */
    Optional<ToDoListChange> getLastToDoListChange();

    /** Returns the filtered to-do list as an {@code UnmodifiableObservableList<ReadOnlyToDo>} */
    UnmodifiableObservableList<ReadOnlyToDo> getFilteredToDoList();

    /** Updates the filter of the filtered to-do list to show all to-dos */
    void clearToDoListFilter();

    /** Updates the filter of the filtered to-do list to filter by the given keywords */
    void updateToDoListFilter(Set<String> keywords);

}
