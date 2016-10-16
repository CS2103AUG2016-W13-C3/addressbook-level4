package seedu.commando.logic;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.commands.Command;
import seedu.commando.logic.commands.CommandFactory;
import seedu.commando.logic.commands.CommandResult;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.storage.Storage;

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
     private final UiLogic uiLogic;
    {
        commandFactory = new CommandFactory();
    }

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        uiLogic = new UiLogic(model.getToDoList());
    }


    @Override
    public CommandResult execute(String commandText) {
        logger.info("User command: " + commandText + "");

        try {
            Command command = commandFactory.build(commandText);
            command.setContext(new Command.Context(eventsCenter, uiLogic, model));
            CommandResult result = command.execute();
            return result;
        } catch (IllegalValueException exception) {
            // Something went wrong in command execution
            return new CommandResult(exception.getMessage(), true);
        } catch (Command.NoContextException exception) {
            assert false; // There should always be a context
            return new CommandResult(exception.getMessage(), true);
        }
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getObservableEventList() {
        return uiLogic.getObservableEvents();
    }

    @Override
    public UnmodifiableObservableList<UiToDo> getObservableTaskList() {
        return uiLogic.getObservableTasks();
    }
}
