package seedu.commando.ui;

import org.apache.commons.lang.StringUtils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        
        SimpleIntegerProperty initHeight = new SimpleIntegerProperty(30);
        resultDisplayArea.prefHeightProperty().bindBidirectional(initHeight);
        resultDisplayArea.minHeightProperty().bindBidirectional(initHeight);
        resultDisplayArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                final int count = StringUtils.countMatches(newValue, "\n");
                initHeight.setValue(30 + count * 22);
            }
        });
        
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
