package guitests.utils;

import java.time.LocalDateTime;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;
import seedu.commando.testutil.ToDoBuilder;

//@@author A0122001M

/**
 *  To do items only for test
 */
public class TypicalTestToDos {

    public ToDo toDoItem1, toDoItem2, toDoItem3, toDoItem4, toDoItem5, testToDoItem1, testToDoItem2, testToDoItem3;

    public TypicalTestToDos() {
        resetToDos();
    }

    private void resetToDos() {
        toDoItem1 = new ToDoBuilder("title").finish(null).build();
        toDoItem2 = new ToDoBuilder("title 2").withTags("tag1", "tag2")
            .withDateRange(LocalDateTime.of(2016, 11, 1, 20, 1), LocalDateTime.of(2016, 12, 1, 20, 1))
            .finish(null).build();
        toDoItem3 = new ToDoBuilder("title 3").withTags("tag1", "tag3")
            .withDateRange(LocalDateTime.of(2017, 1, 1, 20, 1), LocalDateTime.of(2017, 2, 1, 20, 1))
            .finish(null).build();
        toDoItem4 = new ToDoBuilder("title 4").withDueDate(LocalDateTime.of(2016, 12, 1, 20, 1)).finish(null)
            .build();
        toDoItem5 = new ToDoBuilder("title 5").withTags("tag1", "tag2")
            .withDueDate(LocalDateTime.of(2017, 11, 1, 20, 1)).finish(null).build();
        testToDoItem1 = new ToDoBuilder("test floating task").build();
        testToDoItem2 = new ToDoBuilder("test event").withTags("tag8", "tag3")
            .withDateRange(LocalDateTime.of(2016, 12, 3, 20, 1), LocalDateTime.of(2017, 2, 4, 20, 1))
            .finish(null).build();
        testToDoItem3 = new ToDoBuilder("test deadline").withTags("abcc", "tag2")
            .withDueDate(LocalDateTime.of(2016, 11, 14, 20, 1)).finish(null).build();
    }

    public void loadToDoListWithSampleData(ToDoList todoList) {
        try {
            resetToDos();
            todoList.add(new ToDo(toDoItem1)).add(new ToDo(toDoItem2)).add(new ToDo(toDoItem3)).add(new ToDo(toDoItem4))
                    .add(new ToDo(toDoItem5));
        } catch (IllegalValueException e) {
            assert false : "impossible";
        }

    }

    public ToDo[] getTypicalToDos() {
        return new ToDo[] { toDoItem2, toDoItem3,  toDoItem4, toDoItem5, toDoItem1 };
    }

    public ToDoList getTypicalToDoList() {
        ToDoList todoList = new ToDoList();
        loadToDoListWithSampleData(todoList);
        return todoList;
    }

    public ToDo[] getEmptyToDos() {
        return new ToDo[] {};
    }
}
