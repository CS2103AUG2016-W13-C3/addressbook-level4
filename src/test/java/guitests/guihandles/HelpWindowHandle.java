package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Provides a handle to the help window of the app.
 */
public class HelpWindowHandle extends GuiHandle {

    private static final String HELP_WINDOW_TITLE = "Help";
    private static final String HELP_WINDOW_ROOT_FIELD_ID = "#helpWindowRoot";
    private static final String USER_GUIDE_URL = "https://cs2103aug2016-w13-c3.github.io/main/user";

    public HelpWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, HELP_WINDOW_TITLE);
        guiRobot.sleep(1000);
    }

    public boolean isWindowOpen() {
        return getNode(HELP_WINDOW_ROOT_FIELD_ID) != null;
    }
    
    public boolean isWindowShowing(String commandName) {
        WebView node = (WebView) ((AnchorPane) getNode(HELP_WINDOW_ROOT_FIELD_ID)).getChildren().get(0);
        return node.getEngine().getLocation().equals(USER_GUIDE_URL + "#");
    }
    
    public void closeWindow() {
        super.closeWindow();
        guiRobot.sleep(500);
    }

}
