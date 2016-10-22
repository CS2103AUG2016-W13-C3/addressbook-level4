package seedu.commando.testutil;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * Helps build a to-do in 1 line
 */
public class ToDoBuilder {
    private ToDo toDo;

    public ToDoBuilder(String title) throws IllegalValueException {
        toDo = new ToDo(new Title(title));
    }

    public ToDoBuilder withDateRange(LocalDateTime startDate, LocalDateTime endDate)
        throws IllegalValueException {
        toDo.setDateRange(new DateRange(
            startDate, endDate
        ));

        return this;
    }

    public ToDoBuilder withDueDate(LocalDateTime dueDate) throws IllegalValueException {
        toDo.setDueDate(
            new DueDate(dueDate)
        );

        return this;
    }

    public ToDoBuilder withTags(String... tags) throws IllegalValueException {
        Set<Tag> tagsSet = new HashSet<>();
        for (String tag : tags) {
            tagsSet.add(new Tag(tag));
        }

        toDo.setTags(tagsSet);

        return this;
    }

    public ToDoBuilder finish(LocalDateTime date) {
        toDo.setDateFinished(date);

        return this;
    }

    public ToDoBuilder created(LocalDateTime date) {
        toDo.setDateCreated(date);

        return this;
    }

    public ToDoBuilder withRecurrence(Recurrence recurrence) {
        toDo.setRecurrence(recurrence);

        return this;
    }

    public ToDo build() {
        return toDo;
    }
}
