package seedu.commando.model.todo;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a to-do
 * Guarantees: details are present and not null, field values are validated.
 */
public class ToDo implements ReadOnlyToDo {
    private Title title;
    private DueDate dueDate;
    private DateRange dateRange;
    private Set<Tag> tags;
    private boolean isFinished;
    private StringProperty value;
    {
        value = new ReadOnlyStringWrapper();
        isFinished = false;
    }


    /**
     * Asserts that title is non-null
     */
    public ToDo(Title title) {
        assert title != null;

        this.title = title;
        updateValue();
    }

    /**
     * Copy constructor
     */
    public ToDo(ReadOnlyToDo toDo) {
        assert toDo != null;

        this.title = new Title(toDo.getTitle());

        if (toDo.getDueDate().isPresent()) {
            this.dueDate = new DueDate(toDo.getDueDate().get());
        }

        if (toDo.getDateRange().isPresent()) {
            this.dateRange = new DateRange(toDo.getDateRange().get());
        }

        if (toDo.getTags().size() > 0) {
            this.tags = toDo.getTags().stream().map(Tag::new).collect(Collectors.toSet());
        }

        this.isFinished = toDo.isFinished();
        updateValue();
    }
    
    public void setTitle(Title title) {
        assert title != null;
        
        this.title = title;
        updateValue();
    }

    public void setDueDate(DueDate dueDate) {
        assert dueDate != null;
        
        this.dueDate = dueDate;
        updateValue();
    }

    public void setDateRange(DateRange dateRange) {
        assert dateRange != null;
        
        this.dateRange = dateRange;
        updateValue();
    }

    public void setTags(Set<Tag> tags) {
        assert tags != null;
        
        this.tags = tags;
        updateValue();
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;

        updateValue();
    }
    
    public Optional<DueDate> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public Optional<DateRange> getDateRange() {
        return Optional.ofNullable(dateRange);
    }

    public Set<Tag> getTags() {
        if (tags == null) {
            return new HashSet<>();
        } else {
            return tags;
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public ObservableStringValue getObservableValue() {
        return value;
    }

    @Override
    public Title getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyToDo // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyToDo) other));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.getValue());
    }

    @Override
    public String toString() {
        return value.getValue();
    }

    /**
     * Called when any of its fields are updated
     */
    private void updateValue() {
        value.getValue(); // Reset "invalidated" state of observable value
        value.setValue(getText());
    }
}
