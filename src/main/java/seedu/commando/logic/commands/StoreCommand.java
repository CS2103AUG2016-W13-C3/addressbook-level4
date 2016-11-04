package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.logic.ToDoListFilePathChangeRequestEvent;

import java.io.File;

//@@author A0142230B
/**
 *  Change the to-do list file path.
 */
public class StoreCommand extends Command {

    public static final String COMMAND_WORD = "store";

    private String path;
    
    /**
     * Initializes a store command.
     * @param inputPath requested file path to store data, non-null
     */
    public StoreCommand(String inputPath) {
        assert inputPath != null;
        this.path = inputPath;
    }

    /**
     * Asserts that {@code eventsCenter} and {@code model} are non-null
     */
    @Override
	public CommandResult execute() throws NoEventsCenterException, NoModelException {
		EventsCenter eventsCenter = getEventsCenter();

		File file = new File(path);

		// Check if the path has a file name to save
		if (path.endsWith("\\")) {
			return new CommandResult(Messages.MISSING_STORE_FILE, true);
		}

		// Check if the destination file already exists (avoid overwriting
		// important data);
		else if (file.exists()) {
			return new CommandResult(String.format(Messages.STORE_COMMAND_FILE_EXIST, path), true);
		}

		// Send a event to change the file path
		else {
			eventsCenter.post(new ToDoListFilePathChangeRequestEvent(path));
			return new CommandResult(String.format(Messages.STORE_COMMAND, path));
		}
	}
}
