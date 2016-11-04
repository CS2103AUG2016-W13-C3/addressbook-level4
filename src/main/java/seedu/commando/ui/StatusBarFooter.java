package seedu.commando.ui;


import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;


import seedu.commando.commons.util.FxViewUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart {
    private static final String FXML = "StatusBarFooter.fxml";
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private StatusBar syncStatus;
    private StatusBar saveLocationStatus;
    private GridPane mainPane;
    private AnchorPane placeHolder;

    @FXML
    private AnchorPane saveLocStatusBarPane;
    @FXML
    private AnchorPane syncStatusBarPane;

    public static StatusBarFooter load(Stage stage, AnchorPane placeHolder, ObservableValue<String> saveLocation) {
        StatusBarFooter statusBarFooter = UiPartLoader.loadUiPart(stage, placeHolder, new StatusBarFooter());
        statusBarFooter.configure(saveLocation);
        return statusBarFooter;
    }

    public void configure(ObservableValue<String> saveLocation) {
        addMainPane();
        addSyncStatus();
        syncStatus.setText("Not updated yet in this session");
        addSaveLocation(saveLocation);
        registerAsAnEventHandler(this);
    }

    private void addMainPane() {
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
    }

    private void addSaveLocation(ObservableValue<String> saveLocation) {
        this.saveLocationStatus = new StatusBar();

        FxViewUtil.applyAnchorBoundaryParameters(saveLocationStatus, 0.0, 0.0, 0.0, 0.0);
        saveLocStatusBarPane.getChildren().add(saveLocationStatus);

        // Update save location when required
        updateSaveLocation(saveLocation.getValue());
        saveLocation.addListener((observable, oldValue, newValue) -> updateSaveLocation(saveLocation.getValue()));
    }

    private void updateSaveLocation(String saveLocation) {
        saveLocationStatus.setText("./" + saveLocation);
    }

    public void setSyncStatus(LocalDateTime dateTime) {
        this.syncStatus.setText("Last Updated: " + dateFormatter.format(dateTime));
    }

    private void addSyncStatus() {
        this.syncStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(syncStatus, 0.0, 0.0, 0.0, 0.0);
        syncStatusBarPane.getChildren().add(syncStatus);
    }

    @Override
    public void setNode(Node node) {
        mainPane = (GridPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
