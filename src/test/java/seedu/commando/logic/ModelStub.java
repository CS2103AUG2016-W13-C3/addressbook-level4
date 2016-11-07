package seedu.commando.logic;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.ui.UiToDo;

import java.util.Optional;
import java.util.Set;

//@@author A0139697H
public class ModelStub implements Model {
    @Override
    public ReadOnlyToDoList getToDoList() {
        return null;
    }

    @Override
    public void changeToDoList(ToDoListChange change) throws IllegalValueException {}

    @Override
    public boolean undoToDoList() {
        return false;
    }

    @Override
    public boolean redoToDoList() {
        return false;
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiEvents() {
        return null;
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiTasks() {
        return null;
    }

    @Override
    public Optional<UiToDo> getUiToDoAtIndex(int index) {
        return null;
    }

    @Override
    public void clearUiToDoListFilter(FILTER_MODE filterMode) {}

    @Override
    public void setUiToDoListFilter(Set<String> keywords, Set<Tag> tags, FILTER_MODE filterMode) {}

    @Override
    public void setUiToDoListFilter(DateRange dateRange) {}
}