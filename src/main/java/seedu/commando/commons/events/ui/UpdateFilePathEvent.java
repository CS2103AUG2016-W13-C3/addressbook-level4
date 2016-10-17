package seedu.commando.commons.events.ui;

import seedu.commando.commons.events.BaseEvent;
/**
 * Indicates a request to Ui to update the status bar with new file path.
 */
public class UpdateFilePathEvent extends BaseEvent{
	@Override
	public String toString() {
		return ("Update the status bar with new file path");
	}

}
