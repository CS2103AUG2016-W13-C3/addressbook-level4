package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.ui.UiModel;
import seedu.commando.model.Model;

//@@author A0139697H

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    private EventsCenter eventsCenter;
    private Model model;

    /**
     * Sets the EventsCenter for the command.
     * {@param eventsCenter} must be non-null.
     */
    public void setEventsCenter(EventsCenter eventsCenter) {
        assert eventsCenter != null;

        this.eventsCenter = eventsCenter;
    }

    /**
     * Sets the Model for the command.
     * {@param model} must be non-null.
     */
    public void setModel(Model model) {
        assert model != null;

        this.model = model;
    }

    public static class NoEventsCenterException extends Exception {
    }

    public static class NoModelException extends Exception {
    }

    /**
     * Gets the events center set by {@link #setEventsCenter(EventsCenter)}.
     *
     * @return events center of this command
     * @throws NoEventsCenterException if {@link #setEventsCenter(EventsCenter)} hasn't been called
     */
    protected EventsCenter getEventsCenter() throws NoEventsCenterException {
        if (eventsCenter == null) {
            throw new NoEventsCenterException();
        }

        return eventsCenter;
    }

    /**
     * Gets the model set by {@link #setModel(Model)}}.
     *
     * @return model of this command
     * @throws NoModelException if {@link #setModel(Model)} hasn't been called
     */
    protected Model getModel() throws NoModelException {
        if (model == null) {
            throw new NoModelException();
        }

        return model;
    }

    /**
     * Executes the command.
     *
     * @return result of the command packaged in {@link CommandResult}
     */
    public abstract CommandResult execute() throws NoModelException, NoEventsCenterException;
}
