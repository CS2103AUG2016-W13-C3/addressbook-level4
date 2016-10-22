package seedu.commando.model.todo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.testutil.ToDoBuilder;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToDoListTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private ToDoList toDoList;
    private ToDo toDoListItem1;
    private ToDo toDoListItem2;
    private ToDoList toDoList2;
    private ToDo toDoList2Item1;

    @Before
    public void setup() throws IllegalValueException {
        toDoList = new ToDoList();
        toDoListItem1 = new ToDoBuilder("title").build();
        toDoListItem2 = new ToDoBuilder("title 2").withTags("tag1", "tag2")
                .withDueDate(LocalDateTime.of(2016, 5, 1, 20, 1))
                .finish(LocalDateTime.of(2016, 6, 3, 20, 20)).build();
        toDoList.add(toDoListItem1);
        toDoList.add(toDoListItem2);
        toDoList2Item1 = new ToDoBuilder("title 3")
                .withDateRange(LocalDateTime.of(2016, 3, 1, 20, 1), LocalDateTime.of(2016, 4, 1, 20, 1)).build();
        toDoList2 = new ToDoList();
        toDoList2.add(toDoList2Item1);
    }

    @Test
    public void ToDoListCopy() {
        ToDoList copied = new ToDoList(toDoList);
        assertEquals(copied, toDoList);
    }

    @Test
    public void getToDos() throws IllegalValueException {
        assertTrue(toDoList.getToDos().size() == 2);
        assertTrue(toDoList.getToDos().contains(toDoListItem1));
        assertTrue(toDoList.getToDos().contains(toDoListItem2));
    }

    @Test
    public void reset() {
        toDoList.reset(toDoList2.getToDos());
        assertEquals(toDoList, toDoList2);
    }

    @Test
    public void add() throws IllegalValueException {
        assertFalse(toDoList.getToDos().contains(toDoList2Item1));
        toDoList.add(toDoList2Item1);
        assertTrue(toDoList.getToDos().contains(toDoList2Item1));
    }

    @Test
    public void remove_valid() throws IllegalValueException {
        assertTrue(toDoList.getToDos().contains(toDoListItem1));
        toDoList.remove(toDoListItem1);
        assertFalse(toDoList.getToDos().contains(toDoListItem1));
    }
}