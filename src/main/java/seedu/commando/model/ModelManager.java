package seedu.commando.model;

import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.ui.UiModel;
import seedu.commando.model.ui.UiToDo;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//@@author A0122001M

/**
 * Concrete implementation of {@link Model} for the Model component.
 * Defines how data is represented and holds the data of the application in-memory.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ToDoListManager toDoListManager;
    private final UiModel uiModel;

    /**
     * Initializes a ModelManager with the given to-do list.
     * Asserts parameters to be non-null.
     *
     * @param toDoList the internal to-do list will be a deep copy of this
     */
    public ModelManager(ReadOnlyToDoList toDoList) {
        super();
        assert toDoList != null;

        logger.fine("Initializing with to-do list: " + toDoList);

        toDoListManager = new ToDoListManager(toDoList);
        uiModel = new UiModel(toDoListManager);

        logUiToDoList();
    }

    /**
     * @see ModelManager(ReadOnlyToDoList), but with an empty to-do list
     */
    public ModelManager() {
        this(new ToDoList());
    }

    @Override
    public ReadOnlyToDoList getToDoList() {
        return toDoListManager.getToDoList();
    }
    
    /**
     * @see Model#changeToDoList(ToDoListChange)
     */
    @Override
    public synchronized void changeToDoList(ToDoListChange change) throws IllegalValueException {
        logger.info("Applying change to to-do list: " + change);
        toDoListManager.changeToDoList(change);

        // if to-do list has changed, reset any find or history filter
        clearUiToDoListFilter(FILTER_MODE.UNFINISHED);
        indicateToDoListChanged();
        logUiToDoList();
    }
    
    /**
     * @see Model#undoToDoList()
     */
    @Override
    public boolean undoToDoList() {
        logger.info("Undoing to-do list...");
        boolean hasChanged = toDoListManager.undoToDoList();

        if (hasChanged) {
            clearUiToDoListFilter(FILTER_MODE.UNFINISHED);
            indicateToDoListChanged();
            logUiToDoList();
        }

        return hasChanged;
    }

    /**
     * @see Model#redoToDoList()
     */
    @Override
    public boolean redoToDoList() {
        logger.info("Redoing to-do list...");
        boolean hasChanged = toDoListManager.redoToDoList();

        if (hasChanged) {
            clearUiToDoListFilter(FILTER_MODE.UNFINISHED);
            indicateToDoListChanged();
            logUiToDoList();
        }

        return hasChanged;
    }
     
    /**
     * @see Model#getUiEvents()
     */
    @Override
    public UnmodifiableObservableList<UiToDo> getUiEvents() {
        return uiModel.getEvents();
    }

    /**
     * @see Model#getUiTasks()
     */
    @Override
    public UnmodifiableObservableList<UiToDo> getUiTasks() {
        return uiModel.getTasks();
    }
    
    /**
     * @see Model#getUiToDoAtIndex(int)
     */
    @Override
    public Optional<UiToDo> getUiToDoAtIndex(int index) {
        return uiModel.getToDoAtIndex(index);
    }
    
    /**
     * @see Model#clearUiToDoListFilter(seedu.commando.model.Model.FILTER_MODE)
     */
    @Override
    public void clearUiToDoListFilter(FILTER_MODE filterMode) {
        logger.info("Clearing filter on UI to-dos with filter mode: " + filterMode);

        uiModel.clearToDoListFilter(filterMode);
        logUiToDoList();
    }

    
    /**
     * @see Model#setUiToDoListFilter(java.util.Set, java.util.Set, seedu.commando.model.Model.FILTER_MODE)
     */
    @Override
    public void setUiToDoListFilter(Set<String> keywords, Set<Tag> tags, FILTER_MODE filterMode) {
        logger.info("Filtering UI to-dos by keywords " + keywords + " and tags " + tags
            + " with filter mode: " + filterMode);

        uiModel.setToDoListFilter(keywords, tags, filterMode);

        logUiToDoList();
    }

    /**
     * Raises an event to indicate the model has changed.
     */
    private void indicateToDoListChanged() {
        raise(new ToDoListChangedEvent(toDoListManager.getToDoList()));
    }
    
    /**
     * log events and tasks shown
     */
    private void logUiToDoList() {
        logger.info("Events: " + uiModel.getEvents().stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(", ")));

        logger.info("Tasks: " + uiModel.getTasks().stream().map(uiToDo -> uiToDo.getIndex() + ") " + uiToDo.getTitle())
            .collect(Collectors.joining(", ")));
    }

    //@@author A0142230B
    @Override
    public void setUiToDoListFilter(DateRange dateRange) {
        logger.info("Filtering UI to-dos from " + dateRange.startDate.toString() + " to " + dateRange.endDate.toString());

        uiModel.setToDoListFilter(dateRange);

        logUiToDoList();
    }
}
