package seedu.commando.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ToDo;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.testutil.ToDoBuilder;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ModelManagerTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private ToDoList toDoList;
    private ToDo toDoListItem1;
    private ToDo toDoListItem2;
    private ToDoList toDoList2;
    private ToDo toDoList2Item1;
    private ModelManager modelManager;

    private ToDoListChange toDoListChangeAdd1;
    private ToDoListChange toDoListChangeDelete1;
    private ToDoListChange toDoListChangeEdit1;

    private boolean hasReached;

    @Before
    public void setup() throws IllegalValueException {
        toDoList = new ToDoList();
        toDoListItem1 = new ToDoBuilder("title").build();
        toDoListItem2 = new ToDoBuilder("title 2")
            .withTags(
                "tag1", "tag2"
            )
            .withDueDate(
                LocalDateTime.of(2016, 5, 1, 20, 1)
            )
            .withDateRange(
                LocalDateTime.of(2016, 3, 1, 20, 1),
                LocalDateTime.of(2016, 4, 1, 20, 1)
            )
            .isFinished(true)
            .build();
        toDoList.add(toDoListItem1);
        toDoList.add(toDoListItem2);

        toDoList2Item1 = new ToDoBuilder("title 3")
            .withDateRange(
                LocalDateTime.of(2016, 3, 1, 20, 1),
                LocalDateTime.of(2016, 4, 1, 20, 1)
            )
            .build();
        toDoList2 = new ToDoList().add(toDoList2Item1);

        modelManager = new ModelManager(toDoList, new UserPrefs());

        toDoListChangeAdd1 = new ToDoListChange(
            new ToDoList().add(toDoList2Item1),
            new ToDoList()
        );

        toDoListChangeDelete1 = new ToDoListChange(
            new ToDoList(),
            new ToDoList().add(toDoListItem1)
        );

        toDoListChangeEdit1 = new ToDoListChange(
            new ToDoList().add(toDoList2Item1),
            new ToDoList().add(toDoListItem1)
        );

        hasReached = false;
    }

    @Test
    public void getToDoList() {
        assertEquals(modelManager.getToDoList(), toDoList);
        assertTrue(modelManager.getToDoList().contains(toDoListItem1));
        assertTrue(modelManager.getToDoList().contains(toDoListItem1));
        assertFalse(modelManager.getToDoList().contains(toDoList2Item1));
    }

    @Test
    public void changeToDoList_add() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);

        assertTrue(modelManager.getToDoList().contains(toDoListItem1));
    }

    @Test
    public void changeToDoList_delete() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeDelete1);

        assertFalse(modelManager.getToDoList().contains(toDoListItem1));
    }

    @Test
    public void changeToDoList_edit() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeEdit1);

        assertFalse(modelManager.getToDoList().contains(toDoListItem1));
        assertTrue(modelManager.getToDoList().contains(toDoList2Item1));
    }

    @Test
    public void changeToDoList_addDelete() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);
        modelManager.changeToDoList(toDoListChangeDelete1);

        assertTrue(modelManager.getToDoList().contains(toDoList2Item1));
        assertFalse(modelManager.getToDoList().contains(toDoListItem1));
    }

    @Test
    public void changeToDoList_clear() throws IllegalValueException {
        ToDoListChange clearChange = new ToDoListChange(
            new ToDoList(),
            modelManager.getToDoList()
        );
        modelManager.changeToDoList(clearChange);
        assertTrue(modelManager.getToDoList().getToDos().size() == 0);
    }

    @Test
    public void undoToDoList_once() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);
        modelManager.changeToDoList(toDoListChangeDelete1);

        assertTrue(modelManager.undoToDoList());

        assertTrue(modelManager.getToDoList().contains(toDoList2Item1));
        assertTrue(modelManager.getToDoList().contains(toDoListItem1));
    }

    @Test
    public void undoToDoList_multiple() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);
        modelManager.changeToDoList(toDoListChangeDelete1);

        // try to undo 3x
        assertTrue(modelManager.undoToDoList());
        assertTrue(modelManager.undoToDoList());
        assertFalse(modelManager.undoToDoList()); // fails

        assertFalse(modelManager.getToDoList().contains(toDoList2Item1));
        assertTrue(modelManager.getToDoList().contains(toDoListItem1));
    }

    @Test
    public void undoToDoList_undoChangeUndo() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);

        assertTrue(modelManager.undoToDoList());

        modelManager.changeToDoList(toDoListChangeDelete1);

        assertTrue(modelManager.undoToDoList());

        assertFalse(modelManager.getToDoList().contains(toDoList2Item1));
        assertTrue(modelManager.getToDoList().contains(toDoListItem1));
    }

    @Test
    public void redoToDoList_noUndos() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);
        modelManager.changeToDoList(toDoListChangeDelete1);

        assertFalse(modelManager.redoToDoList()); // fails

        assertTrue(modelManager.getToDoList().contains(toDoList2Item1));
        assertFalse(modelManager.getToDoList().contains(toDoListItem1));
    }

    @Test
    public void redoToDoList_once() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);
        modelManager.changeToDoList(toDoListChangeDelete1);

        assertTrue(modelManager.undoToDoList());
        assertTrue(modelManager.undoToDoList());
        assertTrue(modelManager.redoToDoList());

        assertTrue(modelManager.getToDoList().contains(toDoList2Item1));
    }

    @Test
    public void redoToDoList_undoChangeRedo() throws IllegalValueException {
        modelManager.changeToDoList(toDoListChangeAdd1);

        assertTrue(modelManager.undoToDoList());

        modelManager.changeToDoList(toDoListChangeDelete1);

        assertFalse(modelManager.redoToDoList()); // fails

        assertFalse(modelManager.getToDoList().contains(toDoList2Item1));
        assertFalse(modelManager.getToDoList().contains(toDoListItem1));
    }
}