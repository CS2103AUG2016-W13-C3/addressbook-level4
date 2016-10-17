package seedu.commando.model;

import javafx.collections.transformation.FilteredList;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.model.todo.*;
import seedu.commando.model.ui.UiModel;
import seedu.commando.model.ui.UiToDo;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the application's data
 * All changes to any model should be synchronized
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ToDoList toDoList;
    private final ArrayList<ToDoListChange> toDoListChanges; // excludes undo and redo changes
    private final UiModel uiModel;
    private int undoChangeIndex;
    private ToDoListChange lastToDoListChange;
    {
        toDoListChanges = new ArrayList<>();
        undoChangeIndex = -1; // invariant: changes[index] is the next change to undo
    }

    private final UserPrefs userPrefs;

    /**
     * Initializes a ModelManager with the given to-do list
     * Parameters should be non-null
     * @param toDoList is copied during initialization
     */
    public ModelManager(ReadOnlyToDoList toDoList, UserPrefs userPrefs) {
        super();
        assert toDoList != null;
        assert userPrefs != null;

        logger.fine("Initializing with to-do list: " + toDoList + " and user prefs: " + userPrefs);

        this.toDoList = new ToDoList(toDoList.getToDos());
        this.userPrefs = userPrefs;
        uiModel = new UiModel(this.toDoList);
    }

    public ModelManager() {
        this(new ToDoList(), new UserPrefs());
    }

    //================================================================================
    // CRUD to-do list operations
    //================================================================================

    @Override
    public ReadOnlyToDoList getToDoList() {
        return toDoList;
    }

    @Override
    public synchronized void changeToDoList(ToDoListChange change) throws IllegalValueException {
        applyToDoListChange(change);
        toDoListChanges.add(change);
        undoChangeIndex = toDoListChanges.size() - 1; // reset undo index to this change

        lastToDoListChange = change;

        indicateToDoListChanged();
    }

    private void applyToDoListChange(ToDoListChange change) throws IllegalValueException {
        toDoList.remove(change.getDeletedToDos());
        toDoList.add(change.getAddedToDos());

        logger.info(change.toString());
    }

    @Override
    public boolean undoToDoList() {
        if (undoChangeIndex == -1) {
            return false; // Nothing else to undo
        }

        assert undoChangeIndex >= 0 && undoChangeIndex < toDoListChanges.size();

        // Undo change = reverse of change
        ToDoListChange undoChange = toDoListChanges.get(undoChangeIndex).getReverseChange();
        try {
            applyToDoListChange(undoChange);
        } catch (IllegalValueException exception) {
            assert false; // Undo should always work
            return false; // Undo failed
        }

        // Decrement undo index
        undoChangeIndex --;

        lastToDoListChange = undoChange;

        indicateToDoListChanged();

        return true;
    }

    @Override
    public boolean redoToDoList() {
        if (undoChangeIndex == toDoListChanges.size() - 1) {
            return false; // No undos to redo
        }
        assert undoChangeIndex >= -1 && undoChangeIndex < toDoListChanges.size() - 1;

        // Redo change = change
        ToDoListChange redoChange = toDoListChanges.get(undoChangeIndex + 1);
        try {
            applyToDoListChange(redoChange);
        } catch (IllegalValueException exception) {
            assert false; // Redo should always work
            return false; // Redo failed
        }

        // Increment undo index
        undoChangeIndex ++;

        lastToDoListChange = redoChange;

        indicateToDoListChanged();

        return true;
    }

    @Override
    public Optional<ToDoListChange> getLastToDoListChange() {
        return Optional.ofNullable(lastToDoListChange);
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiEventList() {
        return uiModel.getObservableEvents();
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiTaskList() {
        return uiModel.getObservableTasks();
    }

    @Override
    public Optional<UiToDo> getUiToDoAtIndex(int toDoIndex) {
        return uiModel.getToDoAtIndex(toDoIndex);
    }

    @Override
    public void clearUiToDoListFilter() {
        uiModel.clearToDoListFilter();
    }

    @Override
    public void setUiToDoListFilter(Set<String> keywords, Set<String> tags) {
        uiModel.setToDoListFilter(keywords, tags);
    }

    /** Raises an event to indicate the model has changed */
    private void indicateToDoListChanged() {
        raise(new ToDoListChangedEvent(toDoList));
    }

}
