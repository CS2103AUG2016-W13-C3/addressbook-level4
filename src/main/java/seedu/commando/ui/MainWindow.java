package seedu.commando.ui;

import java.io.IOException;
import java.util.HashMap;

import com.google.common.eventbus.Subscribe;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.GuiSettings;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.events.storage.ChangeToDoListFilePathEvent;
import seedu.commando.commons.events.ui.ExitAppRequestEvent;
import seedu.commando.commons.events.ui.UpdateFilePathEvent;
import seedu.commando.commons.util.ConfigUtil;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.logic.Logic;
import seedu.commando.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 740;
    private static double X_OFFSET = 0;
    private static double Y_OFFSET = 0;
    
    KeyCombination altE = KeyCodeCombination.keyCombination("Alt+E");
    KeyCombination altH = KeyCodeCombination.keyCombination("Alt+H");
    KeyCombination altC = KeyCodeCombination.keyCombination("Alt+C");

    private Logic logic;

    // Independent Ui parts residing in this Ui container

    private EventListPanel eventPanel;
    private TaskListPanel taskPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private CommandBox commandBox;
    private UserPrefs userPrefs;
    private Config config;
    private HelpWindow helpWindow;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    private String appName;

    @FXML
    private HBox titleBar;
    
    @FXML
    private AnchorPane browserPlaceholder;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private Menu exitMenu;
    
    @FXML
    private Menu helpMenu;
    
    @FXML
    private Menu creditMenu;

    @FXML
    private AnchorPane eventListPanelPlaceholder;
    
    @FXML
    private AnchorPane taskListPanelPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;


    public MainWindow() {
        super();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(Config.ApplicationTitle, Config.ApplicationName, config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String appName, Config config, UserPrefs prefs, Logic logic) {
        // Set dependencies
        this.logic = logic;
        this.appName = appName;
        this.userPrefs = prefs;
        this.config = config;

        // Configure the UI
        setTitle(appTitle);
        
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        
        setDraggable(titleBar);
        scene = new Scene(rootLayout);
        setKeyBindings();
        
        primaryStage.setScene(scene);
        helpWindow = HelpWindow.load(primaryStage, Config.UserGuideUrl);
    }

    /**
     * Sets the 3 keyboard shortcuts that trigger their respective functions:
     * Alt + E = Exit the app
     * Alt + H = Open help in window
     * Alt + C = Open credits in window
     */
    private void setKeyBindings() {
        scene.getAccelerators().put(altE, new Runnable() {
            @Override
            public void run() {
                handleExit();
            }
        });
        scene.getAccelerators().put(altH, new Runnable() {
            @Override
            public void run() {
                handleHelp();
            }
        });
        scene.getAccelerators().put(altC, new Runnable() {
            @Override
            public void run() {
                handleCredits();
            }
        });
    }

    private void setDraggable(HBox bar) {
        bar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                X_OFFSET = event.getSceneX();
                Y_OFFSET = event.getSceneY();
            }
        });
        bar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - X_OFFSET);
                primaryStage.setY(event.getScreenY() - Y_OFFSET);
            }
        });
    }

    void fillInnerParts() {
        eventPanel = EventListPanel.load(primaryStage, getEventListPlaceholder(), logic.getObservableEventList());
        taskPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getObservableTaskList());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getToDoListFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }
    
    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getEventListPlaceholder() {
        return eventListPanelPlaceholder;
    }

    public AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleCredits() {
        helpWindow.visit(Config.AboutUsUrl);
    }
    
    @FXML
    public void handleHelp() {
        showHelpForCommand("");
    }

    public void showHelpForCommand(String commandWord) {
        // Search through map
        helpWindow.show(Config.getUserGuideAnchorForCommandWord(commandWord));
    }

    public void show() {
        primaryStage.show();
        initEventsCenter();
    }
    
    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }
    
    @Subscribe
    public void handleUpdateFilePathEvent(UpdateFilePathEvent event){
    	statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getToDoListFilePath());
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public EventListPanel getEventListPanel() {
        return this.eventPanel;
    }
    
    public TaskListPanel getTaskListPanel() {
        return this.taskPanel;
    }
    
}
