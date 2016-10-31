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
    private LocalDateTime dateCreated;
    private StringProperty value = new ReadOnlyStringWrapper();

    // null if to-do is not finished
    private LocalDateTime dateFinished;

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

    /**
     * Sets the date finished for the to-do.
     * If there is a recurring date range, this won't have any effect,
     *   since the recurrence will not allow the to-do to finish.
     * If there is a non-recurring date range, this won't have any effect, since
     *   the date finished will be automatically the end of the date range.
     * If there is a recurring due date, this will advance the due date
     *   by the recurrence interval until it's after {@param dateFinished}
     */
    public ToDo setDateFinished(LocalDateTime dateFinished) {
        if (dateRange != null) {
            return this;
        }

        if (dueDate != null && dueDate.recurrence != Recurrence.None) {
            advanceDueDate(dateFinished);
        } else {
            this.dateFinished = dateFinished;
        }

        updateValue();

        return this;
    }

    /**
     * If {@param isFinished} is true:
     *   - If it has a recurring due date, its due date will be advanced by its recurrence once,
     *   - If it has a date range, it will have no effect.
     *   - Otherwise, its date finished is set to now.
     * Else, sets remove to-do's date finished, if there is.
     */
    public ToDo setIsFinished(boolean isFinished) {
        if (isFinished) {
            if (dueDate != null && dueDate.recurrence != Recurrence.None) {
                setDateFinished(dueDate.value);
            } else {
                setDateFinished(LocalDateTime.now());
            }
        } else {
            // remove date finished if unfinish
            dateFinished = null;
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
        return Optional.ofNullable(dueDate);
    }

    public Optional<DateRange> getDateRange() {
        // advance based on recurring date range, if applicable
        advanceDateRange(LocalDateTime.now());

        return Optional.ofNullable(dateRange);
    }

    public Set<Tag> getTags() {
        if (tags == null) {
            return new HashSet<>();
        } else {
            return new HashSet<>(tags);
        }
    }

    /**
     * Gets the date finished for the to-do.
     * If there is a recurring date range or due date, this would always be empty.
     * If there is a non-recurring date range, this would return the end date
     *   if the current time is after the end date, empty otherwise (not over).
     */
    @Override
    public Optional<LocalDateTime> getDateFinished() {
        if (dateFinished != null) {
            return Optional.of(dateFinished);
        } else if (dateRange != null) {
            // we need to use the latest date range which considers recurrence
            advanceDateRange(LocalDateTime.now());

            // If date range is after its end date
            // return its end date as date finished automatically
            if (LocalDateTime.now().isAfter(dateRange.endDate)) {
                return Optional.of(dateRange.endDate);
            }
        }

        return Optional.empty();
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
     * Called when a to-do is to advance its date range to after {@param dateUntil}
     *   based on its recurrence.
     * Will only have an effect if to-do has a date range with a recurrence,
     */
    private void advanceDateRange(LocalDateTime dateUntil) {
        if (dateRange == null
            || dateUntil.isBefore(dateRange.endDate)
            || dateRange.recurrence == Recurrence.None) {
            return;
        }

        // The initial if-else should cover this
        assert dateRange != null && dateRange.recurrence != Recurrence.None;

        // Keep moving date forward based on recurrence interval
        // until it is not before the current date
        LocalDateTime startDate = dateRange.startDate;
        LocalDateTime endDate = dateRange.endDate;
        while (!startDate.isAfter(dateUntil)) {
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
     * Called when a to-do is to advance its due date to after {@param dateUntil}
     *   based on its recurrence.
     * Will only have an effect if to-do has a due date with a recurrence,
     */
    private void advanceDueDate(LocalDateTime dateUntil) {
        if (dueDate == null
            || dateUntil.isBefore(dueDate.value)
            || dueDate.recurrence == Recurrence.None) {
            return;
        }

        // The initial if-else should cover this
        assert dueDate != null && dueDate.recurrence != Recurrence.None;

        // Keep moving date forward based on recurrence interval
        // until it is not before the current date
        LocalDateTime date = dueDate.value;
        while (!date.isAfter(dateUntil)) {
            date = dueDate.recurrence.getNextDate(date);
        }
        dueDate = new DueDate(date, dueDate.recurrence);

        updateValue();
    }
}
