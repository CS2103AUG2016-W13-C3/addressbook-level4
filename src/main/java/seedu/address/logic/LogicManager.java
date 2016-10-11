package seedu.address.logic;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.IndexedItem;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandFactory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.todo.ReadOnlyToDo;
import seedu.address.storage.Storage;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Underlying logic in application
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final CommandFactory commandFactory;
    private final ObservableList<IndexedItem<ReadOnlyToDo>> observableEvents;
    private final ObservableList<IndexedItem<ReadOnlyToDo>> observableTasks;
    private final ArrayList<ReadOnlyToDo> toDoAtIndices;
    {
        observableEvents = FXCollections.observableArrayList();
        observableTasks = FXCollections.observableArrayList();
        toDoAtIndices = new ArrayList<>();
        commandFactory = new CommandFactory();
    }

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;

        // Listen to changes in model's filtered to-do list and update event
        // and task list
        updateEventsAndTasks();
        this.model.getFilteredToDoList().addListener(new ListChangeListener<ReadOnlyToDo>() {
            @Override
            public void onChanged(Change<? extends ReadOnlyToDo> change) {
                updateEventsAndTasks();
            }
        });
    }

    /**
     * Called when model.getFilteredToDoList() changes
     */
    private void updateEventsAndTasks() {
        final ObservableList<ReadOnlyToDo> list = model.getFilteredToDoList();

        List<ReadOnlyToDo> events = list.stream().filter(LogicManager.this::isEvent).collect(Collectors.toList());
        List<ReadOnlyToDo> tasks = list.stream().filter(LogicManager.this::isTask).collect(Collectors.toList());

        // Sort observableEvents and observableTasks for UI

        // For observableEvents, sort by chronological order
        events.sort((event1, event2) -> {
            // Must have date ranges because they are observableEvents
            assert event1.getDateRange().isPresent();
            assert event2.getDateRange().isPresent();

            return event1.getDateRange().get().startDate.compareTo(event2.getDateRange().get().startDate);
        });

        // For observableTasks, sort by whether they have due dates, then chronological order
        tasks.sort((task1, task2) -> {
            if (!task1.getDueDate().isPresent() && !task2.getDueDate().isPresent()) {
                return 0; // both don't have dates, equal
            }

            if (task1.getDueDate().isPresent() && !task2.getDueDate().isPresent()) {
                return -1; // taskl first
            }

            if (task2.getDueDate().isPresent() && !task1.getDueDate().isPresent()) {
                return 1; // task2 first
            }

            // Here, both have duedates
            assert task1.getDueDate().isPresent();
            assert task2.getDueDate().isPresent();

            return task1.getDueDate().get().dueDate.compareTo(task2.getDueDate().get().dueDate);
        });

        toDoAtIndices.clear();

        // Add indices to events first and set to observable events
        observableEvents.setAll(events.stream().map(toDo -> {
            IndexedItem<ReadOnlyToDo> indexed =  new IndexedItem<>(toDo, 1 + toDoAtIndices.size());
            toDoAtIndices.add(toDo);
            return indexed;
        }).collect(Collectors.toList()));

        // Then do the same for tasks
        observableTasks.setAll(tasks.stream().map(toDo -> {
            IndexedItem<ReadOnlyToDo> indexed =  new IndexedItem<>(toDo, 1 + toDoAtIndices.size());
            toDoAtIndices.add(toDo);
            return indexed;
        }).collect(Collectors.toList()));

        // log events and tasks shown
        logger.info("Events: " + observableEvents.stream().map(indexedToDo -> indexedToDo.getIndex() + ") " + indexedToDo.get().getTitle().title)
            .collect(Collectors.joining(",")));

        logger.info("Tasks: " + observableTasks.stream().map(indexedToDo -> indexedToDo.getIndex() + ") " + indexedToDo.get().getTitle().title)
            .collect(Collectors.joining(",")));
    }

    /**
     * Returns whether a to-do item is an event
     */
    private boolean isEvent(ReadOnlyToDo todo) {
        return todo.getDateRange().isPresent();
    }

    /**
     * Returns whether a to-do item is a task
     */
    private boolean isTask(ReadOnlyToDo todo) {
        return !todo.getDateRange().isPresent();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("User command: " + commandText + "");

        Command command = commandFactory.build(commandText);

        return command.execute(toDoAtIndices, model, eventsCenter);
    }

    @Override
    public ObservableList<IndexedItem<ReadOnlyToDo>> getObservableEventList() {
        return observableEvents;
    }

    @Override
    public ObservableList<IndexedItem<ReadOnlyToDo>> getObservableTaskList() {
        return observableTasks;
    }
}
