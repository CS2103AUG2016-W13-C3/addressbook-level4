package seedu.commando.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.testutil.ToDoBuilder;

public class ToDoListChangeTest {

    @Test
    public void todoListChange_equalsAndHashcode() throws IllegalValueException {
        ReadOnlyToDo toDo1 = new ToDoBuilder("title1")
            .withDateRange(LocalDateTime.MIN, LocalDateTime.MAX)
            .withDueDate(LocalDateTime.MAX)
            .withTags("tag1", "tag2")
            .finish(LocalDateTime.MAX)
            .build();

        ReadOnlyToDo toDo2 = new ToDoBuilder("title2")
            .withDateRange(LocalDateTime.MIN, LocalDateTime.MAX)
            .withDueDate(LocalDateTime.MAX)
            .withTags("tag3", "tag4")
            .finish(LocalDateTime.MAX)
            .build();

        
        ToDoListChange todoListEventTest1 = new ToDoListChange(
                new ToDoList().add(toDo1), new ToDoList().add(toDo2));
        
        ToDoListChange todoListEventTest2 = new ToDoListChange(
                new ToDoList().add(toDo1), new ToDoList().add(toDo2));
        
        assertEquals(todoListEventTest1, todoListEventTest2);
        assertEquals(todoListEventTest1.hashCode(), todoListEventTest2.hashCode());
        assertEquals(todoListEventTest1.toString(), todoListEventTest2.toString());
    }
}
