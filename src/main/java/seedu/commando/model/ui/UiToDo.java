package seedu.commando.model.ui;

import javafx.beans.value.ObservableValue;
import seedu.commando.model.todo.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a to-do on the UI
 */
public class UiToDo implements ReadOnlyToDo {

    private final ReadOnlyToDo toDo;
    private final int index;

    public UiToDo(ReadOnlyToDo toDo, int index) {
        this.toDo = toDo;
        this.index = index;
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
    public ObservableValue getObservableValue() {
        return toDo.getObservableValue();
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof UiToDo // instanceof handles nulls
            && this.isSameStateAs((UiToDo) other)
            && index == ((UiToDo) other).index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toDo, index);
    }

    @Override
    public String toString() {
        return toDo + " with index of " + index;
    }
}