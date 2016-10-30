package seedu.commando.model.ui;

import com.google.common.collect.Sets;
import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.ToDoListManager;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.testutil.ToDoBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.testutil.ToDoBuilder.*;

//@@author A0139697H
public class UiModelTest {
    private UiModel uiModel;
    private ToDoListManager toDoListManager;

    @Before
    public void setup() throws IllegalValueException {
        ReadOnlyToDoList toDoList = new ToDoList()
            .add(TaskOldDueUnfinishedNewCreated)
            .add(TaskOldDueUnfinishedOldCreated)
            .add(TaskNewDueUnfinishedNewCreated)
            .add(TaskNewDueUnfinishedOldCreated)
            .add(TaskUnfinishedNewCreated)
            .add(TaskUnfinishedOldCreated)
            .add(TaskTodayNewFinished)
            .add(TaskTodayOldFinished)
            .add(TaskOldFinished)
            .add(TaskOlderFinished)
            .add(EventOldRangeUnfinishedNewCreated)
            .add(EventOldRangeUnfinishedOldCreated)
            .add(EventNewRangeUnfinishedNewCreated)
            .add(EventNewRangeUnfinishedOldCreated)
            .add(EventTodayNewFinished)
            .add(EventTodayOldFinished)
            .add(EventNewFinished)
            .add(EventOldFinished);

        toDoListManager = new ToDoListManager(toDoList);

        uiModel = new UiModel(toDoListManager);
    }

    // Check if the order of events for ui is correct
    @Test
    public void getUiEvents() {
        List<UiToDo> events = uiModel.getEvents();

        assertEquals(6, events.size());

        assertEquals(EventOldRangeUnfinishedNewCreated, events.get(0));
        assertEquals(EventOldRangeUnfinishedOldCreated, events.get(1));
        assertEquals(EventNewRangeUnfinishedNewCreated, events.get(2));
        assertEquals(EventNewRangeUnfinishedOldCreated, events.get(3));
        assertEquals(EventTodayNewFinished, events.get(4));
        assertEquals(EventTodayOldFinished, events.get(5));
    }

    // Check if the order of events for ui is correct for history mode
    @Test
    public void getUiEventsHistory() {
        uiModel.setToDoListFilter(Collections.emptySet(), Collections.emptySet(), true);
        List<UiToDo> events = uiModel.getEvents();

        assertEquals(4, events.size());

        assertEquals(EventTodayNewFinished, events.get(0));
        assertEquals(EventTodayOldFinished, events.get(1));
        assertEquals(EventNewFinished, events.get(2));
        assertEquals(EventOldFinished, events.get(3));
    }

    // Check if the order of tasks for ui is correct
    @Test
    public void getUiTasks() {
        List<UiToDo> tasks = uiModel.getTasks();

        assertEquals(8, tasks.size());

        assertEquals(TaskOldDueUnfinishedNewCreated, tasks.get(0));
        assertEquals(TaskOldDueUnfinishedOldCreated, tasks.get(1));
        assertEquals(TaskNewDueUnfinishedNewCreated, tasks.get(2));
        assertEquals(TaskNewDueUnfinishedOldCreated, tasks.get(3));
        assertEquals(TaskUnfinishedNewCreated, tasks.get(4));
        assertEquals(TaskUnfinishedOldCreated, tasks.get(5));
        assertEquals(TaskTodayNewFinished, tasks.get(6));
        assertEquals(TaskTodayOldFinished, tasks.get(7));
    }

    // Check if the order of tasks for ui is correct for history
    @Test
    public void getUiTasksHistory() {
        uiModel.setToDoListFilter(Collections.emptySet(), Collections.emptySet(), true);

        List<UiToDo> tasks = uiModel.getTasks();

        assertEquals(4, tasks.size());

        assertEquals(TaskTodayNewFinished, tasks.get(0));
        assertEquals(TaskTodayOldFinished, tasks.get(1));
        assertEquals(TaskOldFinished, tasks.get(2));
        assertEquals(TaskOlderFinished, tasks.get(3));
    }
}