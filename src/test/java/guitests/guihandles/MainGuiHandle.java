package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.commando.commons.core.Config;

/**
 * Provides a handle for the main GUI.
 */
public class MainGuiHandle extends GuiHandle {

    public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, Config.ApplicationTitle);
    }

    public EventListPanelHandle getEventListPanel() {
        return new EventListPanelHandle(guiRobot, primaryStage);
    }
    
    public TaskListPanelHandle getTaskListPanel() {
        return new TaskListPanelHandle(guiRobot, primaryStage);
    }

    public ResultDisplayHandle getResultDisplay() {
        return new ResultDisplayHandle(guiRobot, primaryStage);
    }

    public CommandBoxHandle getCommandBox() {
        return new CommandBoxHandle(guiRobot, primaryStage, Config.ApplicationTitle);
    }

    public MainMenuHandle getMainMenu() {
        return new MainMenuHandle(guiRobot, primaryStage);
    }

}
