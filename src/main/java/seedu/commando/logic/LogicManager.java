package seedu.commando.logic;

import com.google.common.eventbus.Subscribe;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.logic.ToDoListFilePathChangeRequestEvent;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.logic.commands.Command;
import seedu.commando.logic.commands.CommandFactory;
import seedu.commando.logic.commands.CommandResult;
import seedu.commando.model.Model;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.storage.Storage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Underlying logic in application
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final UserPrefs userPrefs;
    private final CommandFactory commandFactory;
    {
        commandFactory = new CommandFactory();
    }

    public LogicManager(Model model, Storage storage, UserPrefs userPrefs) {
        this.model = model;
        this.storage = storage;
        this.userPrefs = userPrefs;
    }


    @Override
    public CommandResult execute(String commandText) {
        logger.info("User command: " + commandText + "");

        try {
            Command command = commandFactory.build(commandText);

            command.setEventsCenter(eventsCenter);
            command.setModel(model);

            return command.execute();
        } catch (IllegalValueException exception) {
            // Something went wrong in command execution
            return new CommandResult(exception.getMessage(), true);
        } catch (Command.NoEventsCenterException | Command.NoModelException exception) {
            assert false; // There should always be EventsCenter or Model
            return new CommandResult(exception.getMessage(), true);
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
     * Changes to-do list file path in user prefs and storage and saves the
     * to-do data to that new file path with storage
     */
    @Subscribe
    public void handleToDoListFilePathRequestEvent(ToDoListFilePathChangeRequestEvent event){
        logger.info(LogsCenter.getEventHandlingLogMessage(event));

        storage.setToDoListFilePath(event.path);

        try {
            storage.saveToDoList(model.getToDoList());
        } catch (IOException exception) {
            logger.warning("Failed to save to-do list data file: " + StringUtil.getDetails(exception));
        }

        userPrefs.setToDoListFilePath(event.path);
    }
}
