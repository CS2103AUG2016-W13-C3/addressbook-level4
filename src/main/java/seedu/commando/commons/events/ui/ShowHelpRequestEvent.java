package seedu.commando.commons.events.ui;

import seedu.commando.commons.events.BaseEvent;

/**
 * An event requesting to view the help page
 */
public class ShowHelpRequestEvent extends BaseEvent {

    private final String anchor;

    public ShowHelpRequestEvent(String anchor) {
        this.anchor = anchor;
    }

    public String getAnchor() {
        return anchor;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " with command word: " + anchor;
    }
}
