package seedu.commando.logic;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.logic.ToDoListFilePathChangeRequestEvent;
import seedu.commando.commons.events.model.ToDoListChangedEvent;
import seedu.commando.commons.events.storage.DataSavingExceptionEvent;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.logic.commands.Command;
import seedu.commando.logic.commands.CommandFactory;
import seedu.commando.logic.commands.CommandResult;
import seedu.commando.model.Model;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.storage.Storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

//@@author A0139697H

/**
 * Concrete implementation of {@link Logic} for the Logic component.
 * Executes commands from the UI, using the API provided by Model and Storage.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final UserPrefs userPrefs;
    private final CommandFactory commandFactory = new CommandFactory();

    public LogicManager(Model model, Storage storage, UserPrefs userPrefs) {
        this.model = model;
        this.storage = storage;
        this.userPrefs = userPrefs;

        saveToDoListToStorage(model.getToDoList());
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("User command: " + commandText + "");

        try {
            return executeCommand(commandText);
        } catch (CommandFactory.InvalidCommandFormatException e) {
            return getCommandResultForInvalidFormat(e);
        } catch (CommandFactory.UnknownCommandWordException e) {
            return new CommandResult(String.format(Messages.UNKNOWN_COMMAND, e.commandWord), true);
        } catch (CommandFactory.MissingCommandWordException e) {
            return new CommandResult(Messages.MISSING_COMMAND_WORD, true);
        }
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiEvents() {
        return model.getUiEvents();
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiTasks() {
        return model.getUiTasks();
    }

    @Override
    public ReadOnlyToDoList getToDoList() {
        return model.getToDoList();
    }

    /**
     * Saves the to-do list to the file system with Storage.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     *
     * @param toDoList to-do list to save
     */
    public void saveToDoListToStorage(ReadOnlyToDoList toDoList) {
        try {
            storage.saveToDoList(toDoList);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    /**
     * Called upon an event that the Model's to-do list has changed.
     *
     * In a separate thread, it saves the current version of the to-do list to the hard disk
     * at the default to-do list filepath with Storage.
     */
    @Subscribe
    public void handleToDoListChangedEvent(ToDoListChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));

        // Try to run on JavaFX UI thread to prevent lag. If not, just run on current thread.
        try {
            Platform.runLater(() -> saveToDoListToStorage(event.toDoList));
        } catch (IllegalStateException e) {
           saveToDoListToStorage(event.toDoList);
        }
    }

    /**
     * Called upon an event that the to-do list file path change has been requested.
     *
     * It changes to-do list file path in user prefs and storage.
     * Then, in a separate thread, it saves the to-do data to that new file path with Storage.
     */
    @Subscribe
    public void handleToDoListFilePathRequestEvent(ToDoListFilePathChangeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));

        storage.setToDoListFilePath(event.path);
        userPrefs.setToDoListFilePath(event.path);

        // Try to run on JavaFX UI thread to prevent lag. If not, just run on current thread.
        try {
            Platform.runLater(() -> saveToDoListToStorage(model.getToDoList()));
        } catch (IllegalStateException e) {
            saveToDoListToStorage(model.getToDoList());
        }
    }

    private CommandResult getCommandResultForInvalidFormat(CommandFactory.InvalidCommandFormatException e) {
        // If invalid command format, check if Messages has sample commands for that command
        // Append to exception message if there is
        Optional<String> commandFormatMessage = Messages.getCommandFormatMessage(e.command);
        if (commandFormatMessage.isPresent()) {
            return new CommandResult(e.getMessage() + "\n" + commandFormatMessage.get(), true);
        } else {
            return new CommandResult(e.getMessage(), true);
        }
    }

    private CommandResult executeCommand(String commandText)
        throws CommandFactory.InvalidCommandFormatException,
        CommandFactory.UnknownCommandWordException,
        CommandFactory.MissingCommandWordException {

        Command command = commandFactory.build(commandText);

        command.setEventsCenter(eventsCenter);
        command.setModel(model);

        try {
            return command.execute();
        } catch (Command.NoEventsCenterException | Command.NoModelException exception) {
            assert false : "there should always be EventsCenter or Model";
            return new CommandResult(exception.getMessage(), true);
        }
    }
}
