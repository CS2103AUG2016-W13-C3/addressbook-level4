package seedu.commando.model.todo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a to-do
 */
public class ToDo implements ReadOnlyToDo {
    private Title title;
    private DueDate dueDate;
    private DateRange dateRange;
    private Set<Tag> tags;
    private LocalDateTime dateFinished; // null if to-do is not finished
    private LocalDateTime dateCreated;
    private Recurrence recurrence;
    private StringProperty value;
    {
        value = new ReadOnlyStringWrapper();
    }

    /**
     * Asserts that title is non-null
     */
    public ToDo(Title title) {
        assert title != null;

        this.title = title;
        recurrence = Recurrence.None;
        dateCreated = LocalDateTime.now();
        updateValue();
    }

    /**
     * Copy constructor
     */
    public ToDo(ReadOnlyToDo toDo) {
        assert toDo != null;

        this.title = new Title(toDo.getTitle());
        this.dateCreated = toDo.getDateCreated();

        if (toDo.getDueDate().isPresent()) {
            this.dueDate = new DueDate(toDo.getDueDate().get());
        }

        if (toDo.getDateRange().isPresent()) {
            this.dateRange = new DateRange(toDo.getDateRange().get());
        }

        if (toDo.getTags().size() > 0) {
            this.tags = toDo.getTags().stream().map(Tag::new).collect(Collectors.toSet());
        }

        if (toDo.getDateFinished().isPresent()) {
            this.dateFinished = toDo.getDateFinished().get();
        }

        this.recurrence = toDo.getRecurrence();

        updateValue();
    }
    
    public ToDo setTitle(Title title) {
        assert title != null;
        
        this.title = title;
        updateValue();
        
        return this;
    }

    public ToDo setDueDate(DueDate dueDate) {
        assert dueDate != null;

        this.dueDate = dueDate;
        updateValue();
        
        return this;
    }

    public ToDo setDateRange(DateRange dateRange) {
        assert dateRange != null;
        
        this.dateRange = dateRange;
        updateValue();

        return this;
    }

    public ToDo setTags(Set<Tag> tags) {
        assert tags != null;
        
        this.tags = tags;
        updateValue();
        
        return this;
    }

    public ToDo setDateFinished(LocalDateTime date) {
        this.dateFinished = date;

        updateValue();
        
        return this;
    }

    /**
     * If {@param isFinished} is true, sets to-do's date finished to now
     * Else, sets remove to-do's date finished
     */
    public ToDo setIsFinished(boolean isFinished) {
        if (isFinished) {
            dateFinished = LocalDateTime.now();
        } else {
            dateFinished = null; // remove date finished if unfinish
        }

        updateValue();

        return this;
    }

    public ToDo setDateCreated(LocalDateTime date) {
        this.dateCreated = date;

        updateValue();

        return this;
    }

    public ToDo setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;

        updateValue();

        return this;
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
            return new HashSet<>(tags);
        }
    }

    @Override
    public boolean isFinished() {
        return dateFinished != null;
    }

    @Override
    public Optional<LocalDateTime> getDateFinished() {
        return Optional.ofNullable(dateFinished);
    }

    @Override
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public Recurrence getRecurrence() {
        return recurrence;
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
        return Objects.hash(title, dueDate, dateRange, tags);
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
