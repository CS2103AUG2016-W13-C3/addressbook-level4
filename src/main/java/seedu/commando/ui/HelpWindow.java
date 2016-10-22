package seedu.commando.ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.util.FxViewUtil;

import java.util.logging.Logger;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    
    // Fixed variables
    private final String ICON = "/images/help_icon.png";
    private final String FXML = "HelpWindow.fxml";
    private final String TITLE = "Help";

    private AnchorPane mainPane;
    private Stage dialogStage;
    private WebView browser;
    private String helpurl = "";

    protected static HelpWindow load(Stage primaryStage, String helpurl) {
        logger.fine("Showing help page about the application.");
        HelpWindow helpWindow = UiPartLoader.loadUiPart(primaryStage, new HelpWindow());
        helpWindow.configure(helpurl);
        return helpWindow;
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    private void configure(String helpurl){
        this.helpurl = helpurl;

        Scene scene = new Scene(mainPane);
        // Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMaximized(true); //TODO: set a more appropriate initial size
        setIcon(dialogStage, ICON);

        browser = new WebView();
        browser.getEngine().load(this.helpurl); // preload page at url
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(browser);
    }

    /**
     * Shows a window that navigates to help url, anchored at `#{@param anchor}`
     */
    protected void getHelp(String anchor) {
        visit(helpurl + "#" + anchor);
    }
    
    /**
     * Shows a window that navigates to a specified url
     */
    protected void visit(String url) {
        browser.getEngine().load(url);
        dialogStage.show();
    }
}
