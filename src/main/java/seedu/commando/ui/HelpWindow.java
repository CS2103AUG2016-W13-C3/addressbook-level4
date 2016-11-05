package seedu.commando.ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.util.FxViewUtil;

import java.net.URL;
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
    private String helpUrl = "";
    private String aboutUsUrl = "";

    protected static HelpWindow load(Stage primaryStage, String helpUrl, String aboutUsUrl) {
        logger.fine("Showing help page about the application.");
        HelpWindow helpWindow = UiPartLoader.loadUiPart(primaryStage, new HelpWindow());
        helpWindow.configure(helpUrl, aboutUsUrl);
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

    private void configure(String helpurl, String aboutUsUrl){
        this.helpUrl = helpurl;
        this.aboutUsUrl = aboutUsUrl;

        Scene scene = new Scene(mainPane);
        // Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMaximized(true);
        setIcon(dialogStage, ICON);

        browser = new WebView();
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(browser);
    }

    /**
     * Shows a window that navigates to help url, anchored at `#{@param anchor}`
     */
    protected void getHelp(String anchor) {
        visitUserGuide(anchor);
    }
    
    /**
     * Shows a window that navigates to the user guide
     */
    protected void visitUserGuide(String anchor) {
        URL url = getClass().getResource(helpUrl);
        browser.getEngine().load(url.toExternalForm() + "#" + anchor);
        dialogStage.show();
    }

    /**
     * Shows a window that navigates to the about us page
     */
    protected void visitAboutUs() {
        browser.getEngine().load(aboutUsUrl);
        dialogStage.show();
    }
}
