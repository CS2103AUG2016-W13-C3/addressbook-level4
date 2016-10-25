package seedu.commando.logic.commands;

import java.io.File;

import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.logic.ToDoListFilePathChangeRequestEvent;
import seedu.commando.model.Model;


public class StoreCommand extends Command {

    public static final String COMMAND_WORD = "store";

    private String path;
  //@@author A0142230B
    public StoreCommand(String inputPath) {
        assert inputPath != null;
        this.path = inputPath;
    }
  //@@author A0142230B
    /**
     * Asserts that {@code eventsCenter} and {@code model} are non-null
     */
    @Override
    public CommandResult execute() throws NoEventsCenterException, NoModelException {
        Model model = getModel();
        EventsCenter eventsCenter = getEventsCenter();

        Config config;

        File file = new File(path);

        // Check if the path has a file name to save
        if (path.endsWith("\\")) {
            return new CommandResult(Messages.MISSING_STORE_FILE, true);
        }

        // Check if the destination file already exists (avoid overwriting important data);
        if (file.exists()) {
            return new CommandResult(String.format(Messages.STORE_COMMAND_FILE_EXIST, path), true);
        }

        // Send a event to change the file path
        eventsCenter.post(new ToDoListFilePathChangeRequestEvent(path));
        return new CommandResult(String.format(Messages.STORE_COMMAND, path));
    }
}
