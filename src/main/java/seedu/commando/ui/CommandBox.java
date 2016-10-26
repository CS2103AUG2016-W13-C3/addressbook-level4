package seedu.commando.ui;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.util.FxViewUtil;
import seedu.commando.logic.Logic;
import seedu.commando.logic.commands.CommandFactory;
import seedu.commando.logic.commands.CommandResult;

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
            commandTextField.positionCaret(commandTextField.getLength());
        }
        
        changeResultDisplayMessage(mostRecentResult.getFeedback());
        logger.info("Result: " + mostRecentResult.getFeedback());
    }
    
    /**
     * If there is a keyword in the command,
     * and there must be only once space
     */
    protected void checkForKeywordsInInput() {
        // Early termination condition: 
        if (commandTextField.getText().length() <= CommandFactory.getLongestKeywordLength() + 1) {
            final int firstOccurrence = commandTextField.getText().indexOf(' ');
            // Second termination condition: Occurrence of space. Assumes that everything
            // before the space is the keyword
            if (firstOccurrence > -1) {
                final String keyword = commandTextField.getText().substring(0, firstOccurrence);
                // Checks if keyword is an actual command keyword
                if (CommandFactory.getCommandKeywords().contains(keyword)) {
                    final Optional<String> result = Messages.getCommandFormatMessage(keyword);
                    if (result.isPresent()) {
                        changeResultDisplayMessage(result.get());
                        return;
                    }
                }
            }
        } 
    }
    
    protected void changeResultDisplayMessage(String message) {
        resultDisplay.postMessage(message);
    }
    
    /**
     * This and the next method: Switches through a list of commands, invalid or valid.
     * If the boundary of the list is reached, display nothing.
     */
    protected void goUpCommandHistory() {
        if (commandHistoryPointer <= 0) {
            commandTextField.setText("");
        } else {
            commandTextField.setText(commandHistory.get(--commandHistoryPointer));
        }
    }
    
    protected void goDownCommandHistory() {
        if (commandHistoryPointer >= commandHistory.size()) {
            commandTextField.setText("");
        } else {
            commandTextField.setText(commandHistory.get(commandHistoryPointer++));
        }
    }
    
    /**
     * Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error");
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
        commandTextField.getStyleClass().add("error");
    }
    
    protected TextField getCommandField() {
        return commandTextField;
    }

}
