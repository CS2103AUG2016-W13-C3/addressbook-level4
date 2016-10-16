package seedu.commando.logic.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.storage.XmlFileStorage;
import seedu.commando.storage.XmlSerializableToDoList;

public class ImportCommand extends Command {

	public static final String COMMAND_WORD = "import";

	private String path;

	public ImportCommand(String inputPath) {
		assert inputPath != null;
		this.path = inputPath;
	}

	/**
	 * Asserts that {@code model} are non-null
	 */
	@Override
	public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
		assert model != null;
		File file = new File(path);
		
		//Check if the path has a file name to save
		if(path.endsWith("\\")){
			return new CommandResult(Messages.MISSING_IMPORT_FILE,true);
		}
		//Check if the file extension is xml
		if(!path.toLowerCase().endsWith(".xml")){
			return new CommandResult(Messages.IMPORT_COMMAND_TYPE_ERROR,true);
		}
		//Check if the source file exists;
		if(!file.exists()){
			return new CommandResult(Messages.IMPORT_COMMAND_FILE_NOT_EXIST,true);
		}
		//Read the toDoList from the import path
		try {
		XmlSerializableToDoList newXmlToDoList = XmlFileStorage.loadDataFromSaveFile(file);
		model.changeToDoList(new ToDoListChange(newXmlToDoList.getToDos(), toDoAtIndices));
		
		} catch (FileNotFoundException e) {
			return new CommandResult(Messages.IMPORT_COMMAND_FILE_NOT_EXIST, true);
		} catch (DataConversionException e) {
			return new CommandResult(Messages.IMPORT_COMMAND_INVALID_DATA, true);
		} catch (IllegalValueException e) {
			return new CommandResult(e.getMessage(),true);
		}
		
		
		return new CommandResult(String.format(Messages.IMPORT_COMMAND, path));
	}

}
