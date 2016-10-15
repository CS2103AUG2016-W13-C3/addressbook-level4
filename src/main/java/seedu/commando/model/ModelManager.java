package seedu.commando.model;

import javafx.collections.transformation.FilteredList;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.model.todo.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the application's data
 * All changes to any model should be synchronized
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ToDoList toDoList;
    private final FilteredList<ReadOnlyToDo> filteredToDos;
    private final UnmodifiableObservableList<ReadOnlyToDo> protectedFilteredToDos;
    private final ArrayList<ToDoListChange> toDoListChanges; // excludes undo and redo changes
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
        filteredToDos = new FilteredList<>(this.toDoList.getToDos());
        protectedFilteredToDos = new UnmodifiableObservableList<>(filteredToDos);
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

        clearToDoListFilter();
        indicateToDoListChanged();
    }

    private void applyToDoListChange(ToDoListChange change) throws IllegalValueException {
        for (ReadOnlyToDo toDoToDelete : change.getDeletedToDos()) {
            toDoList.remove(toDoToDelete);
            logger.info("[Model][ToDoList changed][Deleted]: " + toDoToDelete);
        }

        for (ReadOnlyToDo toDoToAdd : change.getAddedToDos()) {
            toDoList.add(toDoToAdd);
            logger.info("[Model][ToDoList changed][Added]: " + toDoToAdd);
        }
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

        clearToDoListFilter();
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

        clearToDoListFilter();
        indicateToDoListChanged();

        return true;
    }

    @Override
    public Optional<ToDoListChange> getLastToDoListChange() {
        return Optional.ofNullable(lastToDoListChange);
    }

    /** Raises an event to indicate the model has changed */
    private void indicateToDoListChanged() {
        raise(new ToDoListChangedEvent(toDoList));
    }

    //================================================================================
    // Filtering to-do list operations
    //================================================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyToDo> getFilteredToDoList() {
        return protectedFilteredToDos;
    }

    @Override
    public void clearToDoListFilter() {
        filteredToDos.setPredicate(null);
    }

    @Override
    public void updateToDoListFilter(Set<String> keywords){
        updateFilteredToDoList(new PredicateExpression(new TitleQualifier(keywords)));
    }

    private void updateFilteredToDoList(Expression expression) {
        filteredToDos.setPredicate(expression::satisfies);
    }
    
    //================================================================================
    //  Inner classes/interfaces used for filtering
    //================================================================================

    interface Expression {
        boolean satisfies(ReadOnlyToDo toDo);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyToDo toDo) {
            return qualifier.run(toDo);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyToDo toDo);
        String toString();
    }

    private class TitleQualifier implements Qualifier {
        private Set<String> titleKeyWords;

        TitleQualifier(Set<String> titleKeyWords) {
            this.titleKeyWords = titleKeyWords;
        }

        @Override
        public boolean run(ReadOnlyToDo toDo) {
            return titleKeyWords.stream()
                    .filter(keyword -> checkForKeyword(toDo, keyword))
                    .count() == titleKeyWords.size();
        }
        
        private boolean checkForKeyword(ReadOnlyToDo toDo, String keyword){
            return checkForTagKeyword(toDo, keyword) || checkForTitleKeyword(toDo, keyword);
        }

        private boolean checkForTitleKeyword(ReadOnlyToDo toDo, String keyword) {
            return StringUtil.substringIgnoreCase(toDo.getTitle().value, keyword);
        }

        private boolean checkForTagKeyword(ReadOnlyToDo toDo, String keyword) {
            Iterator<Tag> itr = toDo.getTags().iterator();
            boolean flag = false;
            while (itr.hasNext()){
                Tag element = itr.next();
                if (StringUtil.substringIgnoreCase(element.value, keyword)) {
                    flag = true;
                }
            }
            return flag;
        }
        
        @Override
        public String toString() {
            return "Title = " + String.join(", ", titleKeyWords);
        }
    }

}
