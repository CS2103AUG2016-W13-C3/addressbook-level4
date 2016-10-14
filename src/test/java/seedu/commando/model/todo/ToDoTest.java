package seedu.commando.model.todo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.time.LocalDateTime;

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
        defaultToDo = new ToDo(new Title("title"));
    }

    @Test
    public void ToDo() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("title"));
        assertEquals(toDo.getTitle(), new Title("title"));
    }

    @Test
    public void ToDoCopy() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("title"));
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
        defaultToDo.setTitle(new Title("set title"));
        assertEquals(defaultToDo.getTitle(), new Title("set title"));
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
}