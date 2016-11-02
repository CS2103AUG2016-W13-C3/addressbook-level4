package seedu.commando.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.util.FxViewUtil;
import seedu.commando.logic.Logic;
import seedu.commando.logic.commands.CommandResult;

import java.util.ArrayList;
import java.util.logging.Logger;

public class CommandBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private ResultDisplay resultDisplay;
    private String previousCommandTest;

    private Logic logic;
    private ArrayList<String> commandHistory;
    private int commandHistoryPointer;

    @FXML
    private TextField commandTextField;
    private CommandResult mostRecentResult;

    protected static CommandBox load(Stage primaryStage, AnchorPane commandBoxPlaceholder,
            ResultDisplay resultDisplay, Logic logic) {
        CommandBox commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandBox());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }

    protected void configure(ResultDisplay resultDisplay, Logic logic) {
        this.resultDisplay = resultDisplay;
        this.logic = logic;
        commandHistory = new ArrayList<String>();
        registerAsAnEventHandler(this);
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    @FXML
    private void handleCommandInputChanged() {
        //Take a copy of the command text
        previousCommandTest = commandTextField.getText();
        commandHistory.add(previousCommandTest);
        commandHistoryPointer = commandHistory.size();

        /* We assume the command is correct. If it is incorrect, the command box will be changed accordingly
         * in the event handling code {@link #handleIncorrectCommandAttempted}
         */
        setStyleToIndicateCorrectCommand();
        mostRecentResult = logic.execute(previousCommandTest);

        // If invalid input given, keep the text
        if (mostRecentResult.hasError()) {
            setStyleToIndicateIncorrectCommand();
            restoreCommandText();
            setCaretAtEndOfText();
            commandHistoryPointer = commandHistory.size() - 1;
        }

        changeResultDisplayMessage(mostRecentResult.getFeedback());
        logger.info("Result: " + mostRecentResult.getFeedback());
    }

    protected void changeResultDisplayMessage(String message) {
        resultDisplay.postMessage(message);
    }

    //@@author A0139080J
    /**
     * This and the next method: Switches through a list of commands, invalid or valid.
     * If the boundary of the list is reached, display nothing.
     */
    protected void goUpCommandHistory() {
        if (!commandHistory.isEmpty()) {
            if (commandHistoryPointer > 0) {
                setTextAndPositionCaret(--commandHistoryPointer);
            } else if (commandHistoryPointer == 0) {
                setTextAndPositionCaret(commandHistoryPointer);
            }
        }
    }

    protected void goDownCommandHistory() {
        if (!commandHistory.isEmpty()) {
            if (commandHistoryPointer < commandHistory.size() - 1) {
                setTextAndPositionCaret(++commandHistoryPointer);
            } else if (commandHistoryPointer == commandHistory.size() - 1) {
                commandTextField.setText("");
                commandHistoryPointer++;
            }
        }
    }

    private void setTextAndPositionCaret(int pointer) {
        commandTextField.setText(commandHistory.get(pointer));
        setCaretAtEndOfText();
    }
    
    private void setCaretAtEndOfText() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                commandTextField.positionCaret(commandTextField.getLength());
            }
        });
    }
    //@@author 
    
    /**
     * Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error-command");
        resultDisplay.getResultDisplayArea().getStyleClass().remove("error-result");
        commandTextField.setText("");
    }

    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(previousCommandTest);
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
        commandTextField.getStyleClass().add("error-command");
        resultDisplay.getResultDisplayArea().getStyleClass().add("error-result");
    }

    protected TextField getCommandField() {
        return commandTextField;
    }

}
