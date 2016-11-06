package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.storage.XmlFileStorage;
import seedu.commando.storage.XmlSerializableToDoList;

import java.io.File;
import java.io.FileNotFoundException;

//@@author A0142230B

/**
* Imports the to-do list from a destination file path.
*/
public class ImportCommand extends Command {

	public static final String COMMAND_WORD = "import";

	private String path;

  /**
   * Initializes a import command.
   * @param inputPath file path of source data, non-null
   */
	public ImportCommand(String inputPath) {
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
			return new CommandResult(Messages.MISSING_IMPORT_FILE, true);
		}

		// Check if the source file exists (should not);
		else if (!file.exists()) {
			return new CommandResult(String.format(Messages.IMPORT_COMMAND_FILE_NOT_EXIST, path), true);
		}

		else {
			// Read the toDoList from the import path
			try {
				XmlSerializableToDoList newXmlToDoList = XmlFileStorage.loadDataFromSaveFile(file);
				updateToDoList(model, newXmlToDoList);
			} catch (FileNotFoundException e) {
				return new CommandResult(String.format(Messages.IMPORT_COMMAND_FILE_NOT_EXIST, path), true);
			} catch (DataConversionException e) {
				return new CommandResult(String.format(Messages.IMPORT_COMMAND_INVALID_DATA, path), true);
			} catch (IllegalValueException e) {
				return new CommandResult(e.getMessage(), true);
			}

			return new CommandResult(String.format(Messages.IMPORT_COMMAND, path));
		}
	}

	private void updateToDoList(Model model, XmlSerializableToDoList newXmlToDoList) throws IllegalValueException {
		model.changeToDoList(new ToDoListChange(newXmlToDoList.getToDos(), model.getToDoList()));
	}

}
