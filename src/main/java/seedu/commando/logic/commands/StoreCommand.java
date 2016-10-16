package seedu.commando.logic.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.util.ConfigUtil;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.storage.XmlToDoListStorage;


public class StoreCommand extends Command {

	public static final String COMMAND_WORD = "store";

	private String path;

	public StoreCommand(String inputPath) {
		assert inputPath != null;
		this.path = inputPath;
	}

	/**
	 * Asserts that {@code eventsCenter} and {@code model} are non-null
	 */
	@Override
	public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
		assert model != null;
		assert eventsCenter != null;
		Config config;
		File file = new File(path);
		//Check if the path has a file name to save
		if(path.endsWith("\\")){
			return new CommandResult(Messages.MISSING_STORE_FILE,true);
		}
		//Check if the destination file already exists (avoid overwriting important data);
		if(file.exists()){
			return new CommandResult(String.format(Messages.STORE_COMMAND_FILE_EXIST, path),true);
		}
		
		//Read config.json
        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(Config.DefaultConfigFilePath);
            config= configOptional.get();
        } catch (DataConversionException e) {
            config = new Config();
        }
		//Export the current toDolist to the destination file
		XmlToDoListStorage export = new XmlToDoListStorage(path);
		try {
			export.saveToDoList(model.getToDoList(),path);
		} catch (IOException e) {
			return new CommandResult(e.getMessage(), true);
		}
		
		//Set the default saving path in the config.json file;
		config.setToDoListFilePath(path);
		try {
			ConfigUtil.saveConfig(config, Config.DefaultConfigFilePath);
		} catch (IOException e) {
			return new CommandResult(e.getMessage(),true);
		}
		//Debugging System.exit(0);
		return new CommandResult(String.format(Messages.STORE_COMMAND, path));

	}

}
