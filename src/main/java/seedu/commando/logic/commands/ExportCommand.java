package seedu.commando.logic.commands;

import java.io.File;
import java.io.IOException;

import seedu.commando.commons.core.Messages;
import seedu.commando.model.Model;
import seedu.commando.storage.XmlToDoListStorage;

//@@author A0142230B
/**
 * Exports the to-do list to a destination file path.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    private String path;

    /**
     * Initializes a export command.
     * @param inputPath requested file path to export data, non-null
     */
    public ExportCommand(String inputPath) {
        assert inputPath != null;
        this.path = inputPath;
    }

    /**
     * Asserts that {@code model} are non-null
     */
	@Override
	public CommandResult execute() throws NoModelException {
		Model model = getModel();

		File file = new File(path);

		// Check if the path has a file name to save
		if (path.endsWith("\\")) {
			return new CommandResult(Messages.MISSING_EXPORT_FILE, true);
		}
		// Check if the destination file already exists (avoid overwriting
		// important data)
		else if (file.exists()) {
			return new CommandResult(String.format(Messages.EXPORT_COMMAND_FILE_EXIST, path), true);
		} 		
		else {

			XmlToDoListStorage export = new XmlToDoListStorage(path);

			try {
				export.saveToDoList(model.getToDoList(), path);
			} catch (IOException e) {
				return new CommandResult(e.getMessage(), true);
			}

			return new CommandResult(String.format(Messages.EXPORT_COMMAND, path));
		}
	}
}
