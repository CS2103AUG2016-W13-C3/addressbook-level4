package seedu.commando.model.todo;

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

//@@author A0122001M

/**
 * Represents a to-do.
 */
public class ToDo implements ReadOnlyToDo {
    private Title title;
    private DueDate dueDate;
    private DateRange dateRange;
    private Set<Tag> tags;
    private LocalDateTime dateFinished; // null if to-do is not finished
    private LocalDateTime dateCreated;
    private StringProperty value = new ReadOnlyStringWrapper();

    /**
     * Asserts that title is non-null
     */
    public ToDo(Title title) {
        assert title != null;

        this.title = title;
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

    public ToDo clearTimeConstraint() {
        dateRange = null;
        dueDate = null;
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

    public Optional<DueDate> getDueDate() {
        updateDueDate();

        return Optional.ofNullable(dueDate);
    }

    public Optional<DateRange> getDateRange() {
        updateDateRange();

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
    public Optional<LocalDateTime> getDateFinished() {
        // we need to use the latest date range which considers recurrence
        updateDateRange();

        // If date range exists and currently it is after its end date
        // return its end date as date finished automatically
        if (dateRange != null && LocalDateTime.now().isAfter(dateRange.endDate)) {
            return Optional.of(dateRange.endDate);
        } else {
            return Optional.ofNullable(dateFinished);
        }
    }

    @Override
    public LocalDateTime getDateCreated() {
        return dateCreated;
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
        // short circuit if same object
        // instanceof handles nulls
        return other == this
                || (other instanceof ReadOnlyToDo
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

    //@@author A0139697H
    /**
     * Called when a to-do should update its date range.
     * Will only have an effect if to-do has a date range with a recurrence,
     *   and the date range is outdated based on the current time.
     */
    private void updateDateRange() {
        if (dateRange == null
            || !LocalDateTime.now().isAfter(dateRange.endDate)
            || dateRange.recurrence == Recurrence.None) {
            return;
        }

        // The initial if-else should cover this
        assert dateRange != null && dateRange.recurrence != Recurrence.None;

        // Keep moving date forward based on recurrence interval
        // until it is not before the current date
        LocalDateTime startDate = dateRange.startDate;
        LocalDateTime endDate = dateRange.endDate;
        while (startDate.isBefore(LocalDateTime.now())) {
            startDate = dateRange.recurrence.getNextDate(startDate);
            endDate = dateRange.recurrence.getNextDate(endDate);
        }
        try {
            dateRange = new DateRange(startDate, endDate, dateRange.recurrence);
        } catch (IllegalValueException exception) {
            assert false : "new date range should be valid";
        }

        updateValue();
    }

    /**
     * Called when a to-do should update its due date
     * Will only have an effect if to-do has a due date with a recurrence,
     *   and the due date is outdated based on the current time.
     */
    private void updateDueDate() {
        if (dueDate == null
            || !LocalDateTime.now().isAfter(dueDate.value)
            || dueDate.recurrence == Recurrence.None) {
            return;
        }

        // The initial if-else should cover this
        assert dueDate != null && dueDate.recurrence != Recurrence.None;

        // Keep moving date forward based on recurrence interval
        // until it is not before the current date
        LocalDateTime date = dueDate.value;
        while (date.isBefore(LocalDateTime.now())) {
            date = dueDate.recurrence.getNextDate(date);
        }
        dueDate = new DueDate(date, dateRange.recurrence);

        updateValue();
    }
}
