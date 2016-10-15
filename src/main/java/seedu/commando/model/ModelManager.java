package seedu.commando.model;

import javafx.collections.transformation.FilteredList;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.model.todo.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the application's data
 * All changes to any model should be synchronized
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ToDoList toDoList;
    private final FilteredList<ReadOnlyToDo> filteredToDos;
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

        this.toDoList = new ToDoList(toDoList);
        this.userPrefs = userPrefs;
        filteredToDos = new FilteredList<>(this.toDoList.getToDos());
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
        for (ReadOnlyToDo toDoToDelete : change.getDeletedToDos()) {
            toDoList.remove(toDoToDelete);
            logger.info("[Model][ToDoList changed][Deleted]: " + toDoToDelete);
        }

        for (ReadOnlyToDo toDoToAdd : change.getAddedToDos()) {
            toDoList.add(toDoToAdd);
            logger.info("[Model][ToDoList changed][Added]: " + toDoToAdd);
        }

        updateFilteredListToShowAll();
        indicateToDoListChanged();
    }

    @Override
    public boolean undoToDoList() {
        return false;
    }

    @Override
    public boolean redoToDoList() {
        return false;
    }

    @Override
    public List<ToDoListChange> getToDoListChanges() {
        return null;
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
        return new UnmodifiableObservableList<>(filteredToDos);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredToDos.setPredicate(null);
    }

    @Override
    public void updateFilteredToDoList(Set<String> keywords){
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
