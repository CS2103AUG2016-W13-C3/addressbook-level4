package seedu.commando.model.ui;

import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListManager;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ToDoList;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static seedu.commando.testutil.ToDoBuilder.*;

//@@author A0139697H
public class UiModelTest {
    private UiModel uiModel;
    private ToDoListManager toDoListManager;

    @Before
    public void setUp() throws IllegalValueException {
        ReadOnlyToDoList toDoList = new ToDoList()
            .add(TaskOldDueUnfinishedNewCreated)
            .add(TaskOldDueUnfinishedOldCreated)
            .add(TaskNewDueUnfinishedNewCreated)
            .add(TaskNewDueUnfinishedOldCreated)
            .add(TaskUnfinishedNewCreated)
            .add(TaskUnfinishedOldCreated)
            .add(TaskOldFinished)
            .add(TaskOlderFinished)
            .add(EventOldRangeUnfinishedNewCreated)
            .add(EventOldRangeUnfinishedOldCreated)
            .add(EventNewRangeUnfinishedNewCreated)
            .add(EventNewRangeUnfinishedOldCreated)
            .add(EventNewFinished)
            .add(EventOldFinished);

        toDoListManager = new ToDoListManager(toDoList);

        uiModel = new UiModel(toDoListManager);
    }

    // Check if the order of events for ui is correct
    @Test
    public void getUiEvents_setup_orderCorrect() {
        List<UiToDo> events = uiModel.getEvents();

        assertEquals(4, events.size());

        assertEquals(EventOldRangeUnfinishedNewCreated, events.get(0));
        assertEquals(EventOldRangeUnfinishedOldCreated, events.get(1));
        assertEquals(EventNewRangeUnfinishedNewCreated, events.get(2));
        assertEquals(EventNewRangeUnfinishedOldCreated, events.get(3));
    }

    // Check if the order of events for ui is correct for history mode
    @Test
    public void getUiEvents_setupFinishedMode_orderCorrect() {
        uiModel.setToDoListFilter(Collections.emptySet(), Collections.emptySet(), Model.FILTER_MODE.FINISHED);
        List<UiToDo> events = uiModel.getEvents();

        assertEquals(2, events.size());

        assertEquals(EventNewFinished, events.get(0));
        assertEquals(EventOldFinished, events.get(1));
    }

    // Check if the order of tasks for ui is correct
    @Test
    public void getUiTasks_setup_orderCorrect() {
        List<UiToDo> tasks = uiModel.getTasks();

        assertEquals(6, tasks.size());

        assertEquals(TaskOldDueUnfinishedNewCreated, tasks.get(0));
        assertEquals(TaskOldDueUnfinishedOldCreated, tasks.get(1));
        assertEquals(TaskNewDueUnfinishedNewCreated, tasks.get(2));
        assertEquals(TaskNewDueUnfinishedOldCreated, tasks.get(3));
        assertEquals(TaskUnfinishedNewCreated, tasks.get(4));
        assertEquals(TaskUnfinishedOldCreated, tasks.get(5));
    }

    // Check if the order of tasks for ui is correct for history
    @Test
    public void getUiTasks_setupFinishedMode_orderCorrect() {
        uiModel.setToDoListFilter(Collections.emptySet(), Collections.emptySet(), Model.FILTER_MODE.FINISHED);

        List<UiToDo> tasks = uiModel.getTasks();

        assertEquals(2, tasks.size());

        assertEquals(TaskOldFinished, tasks.get(0));
        assertEquals(TaskOlderFinished, tasks.get(1));
    }
}