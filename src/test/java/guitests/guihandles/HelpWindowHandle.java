package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.commando.commons.core.Config;

//@@author A0122001M

/**
 * Provides a handle to the help window of the app.
 */
public class HelpWindowHandle extends GuiHandle {

    private static final String HELP_WINDOW_TITLE = "Help";
    private static final String HELP_WINDOW_ROOT_FIELD_ID = "#helpWindowRoot";

    public HelpWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, HELP_WINDOW_TITLE);
        guiRobot.sleep(1000);
    }

    public boolean isWindowOpen() {
        return getNode(HELP_WINDOW_ROOT_FIELD_ID) != null;
    }
    
    public boolean isWindowShowing(String commandName) {
        WebView node = (WebView) ((AnchorPane) getNode(HELP_WINDOW_ROOT_FIELD_ID)).getChildren().get(0);
        return node.getEngine().getLocation().contains(Config.UserGuideUrl);
    }
    
    public void closeWindow() {
        super.closeWindow();
        guiRobot.sleep(500);
    }

}
