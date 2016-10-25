package seedu.commando.model.todo;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToDoTest {
    private ToDo toDo;

    @Before
    public void setup() throws IllegalValueException {
        toDo = new ToDo(new Title("title"));
    }

    @Test
    public void ToDo() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("title"));
        assertEquals(toDo.getTitle(), new Title("title"));
        assertTrue(toDo.getDateCreated().toLocalDate().equals(LocalDate.now()));
    }

    @Test
    public void ToDoCopy() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("title"))
            .setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")))
            .setIsFinished(true);

        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2001, 10, 8, 15, 59),
            Recurrence.Weekly
        ));
        toDo.setDueDate(new DueDate(
            LocalDateTime.of(2001, 10, 8, 12, 59)
        ));

        ToDo newToDo = new ToDo(toDo);
        assertEquals(newToDo, toDo);
    }

    @Test
    public void setTitle() throws IllegalValueException {
        toDo.setTitle(new Title("set title"));
        assertEquals(toDo.getTitle(), new Title("set title"));
    }

    @Test
    public void setTags() throws IllegalValueException {
        assertTrue(toDo.getTags().size() == 0);
        toDo.setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
        assertEquals(toDo.getTags(), Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
    }

    @Test
    public void setDueDate() throws IllegalValueException {
        assertFalse(toDo.getDueDate().isPresent());
        toDo.setDueDate(new DueDate(LocalDateTime.of(2001, 10, 8, 12, 59)));
        assertTrue(toDo.getDueDate().isPresent());
        assertEquals(toDo.getDueDate().get(),
            new DueDate(LocalDateTime.of(2001, 10, 8, 12, 59))
        );
    }

    @Test
    public void setDateRange() throws IllegalValueException {
        assertFalse(toDo.getDateRange().isPresent());
        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2001, 10, 8, 13, 59)
        ));
        assertTrue(toDo.getDateRange().isPresent());
        assertEquals(toDo.getDateRange().get(), new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2001, 10, 8, 13, 59)
        ));
    }

    @Test
    public void setIsFinished() throws IllegalValueException {
        assertFalse(toDo.isFinished());
        assertFalse(toDo.getDateFinished().isPresent());
        toDo.setIsFinished(true);
        assertTrue(toDo.getDateFinished().isPresent());
        assertTrue(toDo.getDateFinished().get().toLocalDate().equals(LocalDate.now()));
        toDo.setIsFinished(false);
        assertFalse(toDo.isFinished());
        assertFalse(toDo.getDateFinished().isPresent());
    }

    @Test
    public void setDateFinished() throws IllegalValueException {
        LocalDateTime datetime = LocalDateTime.of(2011, 11, 2, 1, 23);
        toDo.setDateFinished(datetime);
        assertEquals(datetime, toDo.getDateFinished().orElse(null));
    }

    @Test
    public void setDateCreated() throws IllegalValueException {
        LocalDateTime datetime = LocalDateTime.of(2011, 11, 2, 1, 23);
        toDo.setDateCreated(datetime);
        assertEquals(datetime, toDo.getDateCreated());
    }

    @Test
    public void getObservableValue_allFields() throws IllegalValueException {
        List<String> changes = new LinkedList<>();
        toDo.getObservableValue().addListener((observable, oldValue, newValue) -> {
            changes.add(newValue);
        });

        toDo.setTitle(new Title("new title"));
        assertTrue(changes.size() == 1);

        toDo.setIsFinished(true);
        assertTrue(changes.size() == 2);

        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
        assertTrue(changes.size() == 3);

        toDo.setDueDate(new DueDate(
            LocalDateTime.of(2001, 10, 8, 12, 59)
        ));
        assertTrue(changes.size() == 4);

        toDo.setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
        assertTrue(changes.size() == 5);

        toDo.setDateCreated(LocalDateTime.of(2011, 11, 11, 12, 12));
        assertTrue(changes.size() == 6);

        toDo.clearTimeConstraint(); // with date range, date finished won't change
        assertTrue(changes.size() == 7);

        toDo.setDateFinished(LocalDateTime.now().plusYears(1));
        assertTrue(changes.size() == 8);
    }
}