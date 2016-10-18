package seedu.commando.commons.events.storage;

import seedu.commando.commons.events.BaseEvent;

/**
 * Indicates a request change of default toDoList file path.
 */

public class ChangeToDoListFilePathEvent extends BaseEvent{
	
	public final String path;
	
	public ChangeToDoListFilePathEvent(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Change the default toDoList file path to "+path;
	}

}
