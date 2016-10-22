package seedu.commando.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.commando.commons.util.FxViewUtil;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart {
    
    // Fixed variables
    private final String FXML = "ResultDisplay.fxml";
    private final String RESULT_DISPLAY_ID = "resultDisplay";
    private final String STATUS_BAR_STYLE_SHEET = "result-display";
    private final StringProperty displayed = new SimpleStringProperty("");
    
    private TextArea resultDisplayArea;
    private AnchorPane placeHolder;
    private AnchorPane mainPane;

    protected static ResultDisplay load(Stage primaryStage, AnchorPane placeHolder) {
        ResultDisplay statusBar = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ResultDisplay());
        statusBar.configure();
        return statusBar;
    }

    protected void configure() {
        resultDisplayArea = new TextArea();
        resultDisplayArea.setEditable(false);
        resultDisplayArea.setId(RESULT_DISPLAY_ID);
        resultDisplayArea.getStyleClass().removeAll();
        resultDisplayArea.getStyleClass().add(STATUS_BAR_STYLE_SHEET);
        resultDisplayArea.setText("");
        resultDisplayArea.textProperty().bind(displayed);
        FxViewUtil.applyAnchorBoundaryParameters(resultDisplayArea, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(resultDisplayArea);
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    protected void postMessage(String message) {
        displayed.setValue(message);
    }

}
