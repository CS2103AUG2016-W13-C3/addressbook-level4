package seedu.commando.model.ui;

import javafx.beans.value.ObservableValue;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

//@@author A0139697H
/**
 * Represents a to-do on the UI.
 */
public class UiToDo implements ReadOnlyToDo {

    private final ReadOnlyToDo toDo;
    private final int index;
    private final boolean isNew;

    public UiToDo(ReadOnlyToDo toDo, int index, boolean isNew) {
        this.toDo = toDo;
        this.isNew = isNew;
        this.index = index;
    }

    /**
     * Returns whether a to-do item is an event
     */
    public static boolean isEvent(ReadOnlyToDo todo) {
        return todo.getDateRange().isPresent();
    }

    /**
     * Returns whether a to-do item is a task
     */
    public static boolean isTask(ReadOnlyToDo todo) {
        return !todo.getDateRange().isPresent();
    }

    @Override
    public Title getTitle() {
        return toDo.getTitle();
    }

    @Override
    public Optional<DateRange> getDateRange() {
        return toDo.getDateRange();
    }

    @Override
    public Optional<DueDate> getDueDate() {
        return toDo.getDueDate();
    }

    @Override
    public Set<Tag> getTags() {
        return toDo.getTags();
    }

    @Override
    public boolean isFinished() {
        return toDo.isFinished();
    }

    @Override
    public Optional<LocalDateTime> getDateFinished() {
        return toDo.getDateFinished();
    }

    @Override
    public LocalDateTime getDateCreated() {
        return toDo.getDateCreated();
    }

    @Override
    public ObservableValue getObservableValue() {
        return toDo.getObservableValue();
    }

    /**
     * @return index of the to-do on the UI.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return whether the to-do is newly added or edited.
     */
    public boolean isNew() { return isNew; }

    public boolean isEvent() {
        return isEvent(this);
    }

    public boolean isTask() {
        return isTask(this);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        // instanceof handles nulls
        return other == this
            || (other instanceof UiToDo
            && this.isSameStateAs((UiToDo) other)
            && index == ((UiToDo) other).index
            && isNew == ((UiToDo) other).isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toDo, index, isNew);
    }

    @Override
    public String toString() {
        return toDo + " with index of " + index
            + (isNew ? "(new)" : "");
    }
}
