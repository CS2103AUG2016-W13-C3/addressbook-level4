package seedu.commando.commons.events.ui;

import seedu.commando.commons.events.BaseEvent;

/**
 * Indicates a request for termination of application
 */
public class ExitAppRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
