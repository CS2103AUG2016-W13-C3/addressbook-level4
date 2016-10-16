package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.UiLogic;
import seedu.commando.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed
 * All commands should only run its validation of fields and logic and throw errors when executed
 * {@link #setContext(Context)} should be called before {@link #execute()}
 */
public abstract class Command {

    private Context context;

    /**
     * Sets the context for the command
     * {@param context} must be non-null
     */
    public void setContext(Context context) {
        assert context != null;

        this.context = context;
    }

    /** Error thrown when there is no context during execution */
    public static class NoContextException extends Exception {}

    protected EventsCenter getEventsCenter() throws NoContextException {
        if (context == null) {
            throw new NoContextException();
        }

        return context.eventsCenter;
    }

    protected Model getModel() throws NoContextException {
        if (context == null) {
            throw new NoContextException();
        }

        return context.model;
    }

    protected UiLogic getUiLogic() throws NoContextException {
        if (context == null) {
            throw new NoContextException();
        }

        return context.uiLogic;
    }


    /**
     * Represents a context for the execution of the command
     */
    public static class Context {
        private EventsCenter eventsCenter;
        private UiLogic uiLogic;
        private Model model;

        public Context(EventsCenter eventsCenter, UiLogic uiLogic, Model model) {
            assert eventsCenter != null;
            assert uiLogic != null;
            assert model != null;

            this.eventsCenter = eventsCenter;
            this.uiLogic = uiLogic;
            this.model = model;
        }
    }

    /**
     * Executes the command
     * @return result of the command
     */
    public abstract CommandResult execute()
        throws IllegalValueException, NoContextException;
}
