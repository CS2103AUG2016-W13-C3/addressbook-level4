package seedu.commando.model.todo;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.testutil.TestHelper.*;

public class ToDoTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private ToDo defaultToDo;

    @Before
    public void setup() throws IllegalValueException {
        defaultToDo = new ToDo(new Title("value"));
    }

    @Test
    public void ToDo() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("value"));
        assertEquals(toDo.getTitle(), new Title("value"));
    }

    @Test
    public void ToDoCopy() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("value"));
        toDo.setTags(SetOf(new Tag("tag1"), new Tag("tag2")));
        toDo.setIsFinished(true);
        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
        toDo.setDueDate(new DueDate(
            LocalDateTime.of(2001, 10, 8, 12, 59)
        ));

        ToDo newToDo = new ToDo(toDo);
        assertEquals(newToDo, toDo);
    }

    @Test
    public void setTitle() throws IllegalValueException {
        defaultToDo.setTitle(new Title("set value"));
        assertEquals(defaultToDo.getTitle(), new Title("set value"));
    }

    @Test
    public void setTags() throws IllegalValueException {
        assertTrue(defaultToDo.getTags().size() == 0);
        defaultToDo.setTags(SetOf(new Tag("tag1"), new Tag("tag2")));
        assertEquals(defaultToDo.getTags(), SetOf(new Tag("tag1"), new Tag("tag2")));
    }

    @Test
    public void setDueDate() throws IllegalValueException {
        assertFalse(defaultToDo.getDueDate().isPresent());
        defaultToDo.setDueDate(new DueDate(LocalDateTime.of(2001, 10, 8, 12, 59)));
        assertTrue(defaultToDo.getDueDate().isPresent());
        assertEquals(defaultToDo.getDueDate().get(),
            new DueDate(LocalDateTime.of(2001, 10, 8, 12, 59))
        );
    }

    @Test
    public void setDateRange() throws IllegalValueException {
        assertFalse(defaultToDo.getDateRange().isPresent());
        defaultToDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
        assertTrue(defaultToDo.getDateRange().isPresent());
        assertEquals(defaultToDo.getDateRange().get(), new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
    }

    @Test
    public void setIsFinished() throws IllegalValueException {
        assertFalse(defaultToDo.isFinished());
        defaultToDo.setIsFinished(true);
        assertTrue(defaultToDo.isFinished());
    }

    @Test
    public void getObservableValue_allFields() throws IllegalValueException {
        List<String> changes = new LinkedList<>();
        defaultToDo.getObservableValue().addListener((observable, oldValue, newValue) -> {
            changes.add(newValue);
        });

        defaultToDo.setTitle(new Title("new value"));
        assertTrue(changes.size() == 1);

        defaultToDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
        assertTrue(changes.size() == 2);

        defaultToDo.setDueDate(new DueDate(
            LocalDateTime.of(2001, 10, 8, 12, 59)
        ));
        assertTrue(changes.size() == 3);

        defaultToDo.setTags(SetOf(new Tag("tag1"), new Tag("tag2")));
        assertTrue(changes.size() == 4);

        defaultToDo.setIsFinished(true);
        assertTrue(changes.size() == 5);
    }
}