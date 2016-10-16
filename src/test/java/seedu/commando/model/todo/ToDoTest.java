package seedu.commando.model.todo;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.testutil.TestHelper.*;

public class ToDoTest {
    private ToDo toDo;

    @Before
    public void setup() throws IllegalValueException {
        toDo = new ToDo(new Title("value"));
    }

    @Test
    public void ToDo() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("value"));
        assertEquals(toDo.getTitle(), new Title("value"));
    }

    @Test
    public void ToDoCopy() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("value"));
        toDo.setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
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
        toDo.setTitle(new Title("set value"));
        assertEquals(toDo.getTitle(), new Title("set value"));
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
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
        assertTrue(toDo.getDateRange().isPresent());
        assertEquals(toDo.getDateRange().get(), new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
    }

    @Test
    public void setIsFinished() throws IllegalValueException {
        assertFalse(toDo.isFinished());
        toDo.setIsFinished(true);
        assertTrue(toDo.isFinished());
    }

    @Test
    public void getObservableValue_allFields() throws IllegalValueException {
        List<String> changes = new LinkedList<>();
        toDo.getObservableValue().addListener((observable, oldValue, newValue) -> {
            changes.add(newValue);
        });

        toDo.setTitle(new Title("new value"));
        assertTrue(changes.size() == 1);

        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
        assertTrue(changes.size() == 2);

        toDo.setDueDate(new DueDate(
            LocalDateTime.of(2001, 10, 8, 12, 59)
        ));
        assertTrue(changes.size() == 3);

        toDo.setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
        assertTrue(changes.size() == 4);

        toDo.setIsFinished(true);
        assertTrue(changes.size() == 5);
    }
}