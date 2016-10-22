package seedu.commando.model;

import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;
import seedu.commando.model.ui.UiModel;
import seedu.commando.model.ui.UiToDo;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the application's data
 * All changes to model should be synchronized
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ToDoListManager toDoListManager;
    private final UiModel uiModel;
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

        this.userPrefs = userPrefs;

        toDoListManager = new ToDoListManager(toDoList);
        uiModel = new UiModel(toDoListManager);
    }

    public ModelManager() {
        this(new ToDoList(), new UserPrefs());
    }

    @Override
    public ReadOnlyToDoList getToDoList() {
        return toDoListManager.getToDoList();
    }

    @Override
    public synchronized void changeToDoList(ToDoListChange change) throws IllegalValueException {
        toDoListManager.changeToDoList(change);
        indicateToDoListChanged();
    }

    @Override
    public boolean undoToDoList() {
        boolean hasChanged = toDoListManager.undoToDoList();

        if (hasChanged) {
            indicateToDoListChanged();
        }

        return hasChanged;
    }

    @Override
    public boolean redoToDoList() {
        boolean hasChanged = toDoListManager.redoToDoList();

        if (hasChanged) {
            indicateToDoListChanged();
        }

        return hasChanged;
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiEvents() {
        return uiModel.getEvents();
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiTasks() {
        return uiModel.getTasks();
    }

    @Override
    public Optional<UiToDo> getUiToDoAtIndex(int index) {
        return uiModel.getToDoAtIndex(index);
    }

    @Override
    public void clearUiToDoListFilter() {
        uiModel.clearToDoListFilter();
    }

    @Override
    public void setUiToDoListFilter(Set<String> keywords, Set<Tag> tags) {
        uiModel.setToDoListFilter(keywords, tags);
    }

    /** Raises an event to indicate the model has changed */
    private void indicateToDoListChanged() {
        raise(new ToDoListChangedEvent(toDoListManager.getToDoList()));
    }

}
