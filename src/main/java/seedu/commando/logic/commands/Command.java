package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.ui.UiModel;
import seedu.commando.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed
 * All commands should only run its validation of fields and logic and throw errors when executed
 * {@link #setContext(Context)} should be called before {@link #execute()}
 */
public abstract class Command {

    private EventsCenter eventsCenter;
    private Model model;

    /**
     * Sets the EventsCenter for the command
     * {@param eventsCenter} must be non-null
     */
    public void setEventsCenter(EventsCenter eventsCenter) {
        assert eventsCenter != null;

        this.eventsCenter = eventsCenter;
    }

    /**
     * Sets the Model for the command
     * {@param model} must be non-null
     */
    public void setModel(Model model) {
        assert model != null;

        this.model = model;
    }

    public static class NoEventsCenterException extends Exception {}

    public static class NoModelException extends Exception {}

    protected EventsCenter getEventsCenter() throws NoEventsCenterException {
        if (eventsCenter == null) {
            throw new NoEventsCenterException();
        }

        return eventsCenter;
    }

    protected Model getModel() throws NoModelException {
        if (model == null) {
            throw new NoModelException();
        }

        return model;
    }

    /**
     * Executes the command
     * @return result of the command
     */
    public abstract CommandResult execute()
        throws IllegalValueException, NoModelException, NoEventsCenterException;
}
