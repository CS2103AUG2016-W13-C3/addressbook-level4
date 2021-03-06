package seedu.commando.ui;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seedu.commando.MainApp;
import seedu.commando.commons.core.ComponentManager;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.events.storage.DataSavingExceptionEvent;
import seedu.commando.commons.events.logic.ShowHelpRequestEvent;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.logic.Logic;
import seedu.commando.model.UserPrefs;

import java.util.logging.Logger;

/**
 * The manager of the UI component.
 */
public class UiManager extends ComponentManager implements Ui {
    private static final Logger logger = LogsCenter.getLogger(UiManager.class);

    private Logic logic;
    private UserPrefs prefs;
    private MainWindow mainWindow;

    public UiManager(Logic logic, UserPrefs prefs) {
        super();
        this.logic = logic;
        this.prefs = prefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");

        primaryStage.setTitle(Config.ApplicationTitle);

        //Set the application icon.
        primaryStage.getIcons().add(getImage(Config.ApplicationIcon));

        try {
            mainWindow = MainWindow.load(primaryStage, prefs, logic);
            mainWindow.show(); // This should be called before creating other UI parts
            mainWindow.fillInnerParts();
            mainWindow.moreConfigurations(); // This is to set properties of some components that
                                             // can only be set after loading 
        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    @Override
    public void stop() {
        prefs.setGuiSettings(mainWindow.getCurrentGuiSetting());
        mainWindow.hide();
    }

    private void showFileOperationAlertAndWait(String description, String details, Throwable cause) {
        final String content = details + ":\n" + cause.toString();
        if (mainWindow != null) {
            showAlertDialogAndWait(AlertType.ERROR, "File Op Error", description, content);
        }
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    private void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(mainWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    //==================== Event Handling Code =================================================================

    @Subscribe
    private void handleDataSavingExceptionEvent(DataSavingExceptionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showFileOperationAlertAndWait("Could not save data", "Could not save data to file", event.exception);
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.showHelpAtAnchor(event.getAnchor());
    }
}
