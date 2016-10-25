package seedu.commando.logic;

import com.google.common.eventbus.Subscribe;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.events.logic.ToDoListFilePathChangeRequestEvent;
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
        } catch (Command.NoEventsCenterException | Command.NoModelException exception) {
            assert false; // There should always be EventsCenter or Model
            return new CommandResult(exception.getMessage(), true);
        } catch (CommandFactory.InvalidCommandFormatException e) {

            // If invalid command format, check if Messages has sample commands for that command
            // Append to exception message if there is
            Optional<String> commandFormatMessage = Messages.getInvalidCommandFormatMessage(e.command);
            if (commandFormatMessage.isPresent()) {
                return new CommandResult(e.getMessage() + "\n" + commandFormatMessage.get(), true);
            } else {
                return new CommandResult(e.getMessage(), true);
            }

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
