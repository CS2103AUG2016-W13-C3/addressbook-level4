package seedu.commando.logic;

import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.commands.Command;
import seedu.commando.logic.commands.CommandFactory;
import seedu.commando.logic.commands.CommandResult;
import seedu.commando.model.Model;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.storage.Storage;

import java.util.logging.Logger;

/**
 * Underlying logic in application
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final CommandFactory commandFactory;
    {
        commandFactory = new CommandFactory();
    }

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
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
    public UnmodifiableObservableList<UiToDo> getUiEventList() {
        return model.getUiEventList();
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getUiTaskList() {
        return model.getUiTaskList();
    }
}
