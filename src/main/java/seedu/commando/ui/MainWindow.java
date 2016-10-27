package seedu.commando.ui;

import com.google.common.eventbus.Subscribe;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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
import seedu.commando.commons.events.logic.ExitAppRequestEvent;
import seedu.commando.commons.events.storage.ToDoListSavedEvent;
import seedu.commando.logic.Logic;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.ui.UiToDo;

import java.time.LocalDateTime;
import java.util.logging.Logger;

//@@author A0139080J
/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {
    private static final Logger logger = LogsCenter.getLogger(MainWindow.class);
    
    // Fixed variables
    private final String FXML = "MainWindow.fxml";
    private final String maximizeButtonSymbol = "⬜";
    private final String unMaximizeButtonSymbol = "❐";
    
    // Variables that changes while the app is active
    private double currScreenXPos = 0;
    private double currScreenYPos = 0;
    private static boolean isMaximized;
    
    private Logic logic;
    private String appName;

    // Independent Ui parts residing in this Ui container
    private EventListPanel eventPanel;
    private TaskListPanel taskPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private CommandBox commandBox;
    private UserPrefs userPrefs;
    private HelpWindow helpWindow;
    
    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    // The three panes that will take turns to be in focus 
    // when the user presses 'Tab' repeatedly
    private TextField commandField;
    private ListView<UiToDo> eventListView;
    private ListView<UiToDo> taskListView;
    private enum FocusPanes {
        COMMANDFIELD, EVENTPANEL, TASKPANEL
    }
    FocusPanes currentlyFocusedPane = FocusPanes.COMMANDFIELD;
    
    // Key combinations
    KeyCombination altH = KeyCodeCombination.keyCombination("Alt+H");
    KeyCombination altC = KeyCodeCombination.keyCombination("Alt+C");
    KeyCombination altM = KeyCodeCombination.keyCombination("Alt+M");
    
    @FXML
    private HBox titleBar;
    @FXML
    private SplitPane splitPane;
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
    private Menu appNameMenu;
    @FXML
    private Button toggleSizeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button exitButton;
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

    public static MainWindow load(Stage primaryStage, UserPrefs prefs, Logic logic) {
        // Try to remove title bar of window
        try {
            primaryStage.initStyle(StageStyle.UNDECORATED);
        } catch (IllegalStateException exception) {
            // Ignore, window has been made visible already
        }

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(Config.ApplicationTitle, Config.ApplicationName, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String appName, UserPrefs prefs, Logic logic) {
        // Set dependencies
        this.logic = logic;
        this.userPrefs = prefs;
        this.appName = appName;

        // Configure the UI
        setTitle(appTitle);
        
        // Icon and App size settings
        setIcon(Config.ApplicationIcon);
        setWindowDefaultSize(prefs);
        
        scene = new Scene(rootLayout);
        
        // Program is draggable through its titlebar
        setDraggable(titleBar);
        // Set keyboard shortcuts for certain functions
        setKeyboardShortcuts();
        // Programmatically focus certain panes through tab
        // Arrow keys to navigate a listview when either event or task pane is focused
        setTabAndArrowKeysNavigations();
        
        primaryStage.setScene(scene);
        helpWindow = HelpWindow.load(primaryStage, Config.UserGuideUrl);
    }


    void fillInnerParts() {
        eventPanel = EventListPanel.load(primaryStage, getEventListPlaceholder(), logic.getUiEvents());
        taskPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getUiTasks());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), userPrefs.getToDoListFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }
    
    protected void moreConfigurations() {
        commandField = commandBox.getCommandField();
        eventListView = eventPanel.getEventListView();
        taskListView = taskPanel.getTaskListView();
        setFocusTo(commandField);
        disableSplitPaneResize();
        setAppName(appName);
    }
    
    private void setFocusTo(Node node) {
        node.requestFocus();
    }
    
    private void disableSplitPaneResize() {
        splitPane.lookup(".split-pane-divider").setMouseTransparent(true);
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

    private AnchorPane getEventListPlaceholder() {
        return eventListPanelPlaceholder;
    }

    private AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }

    protected void hide() {
        primaryStage.hide();
    }

    private void setAppName(String appName) {
        appNameMenu.setText(appName);
    }
    
    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
        
    }

    /**
     * Sets the default size and coordinates based on user preferences.
     * Also includes whether the app is previously maximized
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());

        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }

        primaryStage.setMaximized(isMaximized = prefs.getGuiSettings().getIsMaximized());
        toggleSizeButtonSymbol();
    }

    /**
     * Returns the current position of the main Window.
     */
    protected GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(), 
                (int) primaryStage.getX(), (int) primaryStage.getY(), isMaximized);
    }

    /**
     * Sets the 3 keyboard shortcuts that trigger their respective functions:
     * Alt + E = Exit the app
     * Alt + H = Open help in window
     * Alt + C = Open credits in window
     */
    private void setKeyboardShortcuts() {
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
        scene.getAccelerators().put(altM, new Runnable() {
            @Override
            public void run() {
                toggleWindowSize();
            }
        });
    }
    
    /**
     * Pressing Tab will cycle between Event Panel, Task Panel and Command Box
     * Typing text will set focus automatically to the Command Box
     */
    private void setTabAndArrowKeysNavigations() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                switch (key.getCode()) {
                case UP:
                    // If currently focused on Event Panel or Task Panel, scroll respectively
                    if (currentlyFocusedPane == FocusPanes.EVENTPANEL) {
                        eventPanel.scrollUp();
                    } else if (currentlyFocusedPane == FocusPanes.TASKPANEL) {
                        taskPanel.scrollUp();
                    } else {
                        commandBox.goUpCommandHistory();
                    }
                    break;
                case DOWN:
                    if (currentlyFocusedPane == FocusPanes.EVENTPANEL) {
                        eventPanel.scrollDown();
                    } else if (currentlyFocusedPane == FocusPanes.TASKPANEL) {
                        taskPanel.scrollDown();
                    } else {
                        commandBox.goDownCommandHistory();
                    }
                    break;
                case TAB:
                    // If tab is pressed, cycle through Event Panel, Task Panel and Command Box
                    cycleThroughFocusPanes();
                    key.consume();
                    break;
                case CONTROL:
                case ALT:
                case SHIFT:
                    // Nome of these keys should trigger the focus of commandfield
                    // because sometimes you need to copy and paste or do other stuff
                    key.consume();
                    break;
                default:
                    // Else, any other sutiable character is considered input to command box
                    // and it will be in focus
                    currentlyFocusedPane = FocusPanes.COMMANDFIELD;
                    commandField.requestFocus();
                }
            }
        });
    }

    private void cycleThroughFocusPanes() {
        switch (currentlyFocusedPane) {
        case COMMANDFIELD:
            currentlyFocusedPane = FocusPanes.EVENTPANEL;
            setFocusTo(eventListView);
            break;
        case EVENTPANEL:
            currentlyFocusedPane = FocusPanes.TASKPANEL;
            setFocusTo(taskListView);
            break;
        case TASKPANEL:
            currentlyFocusedPane = FocusPanes.COMMANDFIELD;
            setFocusTo(commandField);
            break;
        }
    }
    

    
    /**
     * Sets the whole app to be draggable
     */
    private void setDraggable(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                currScreenXPos = mouseEvent.getSceneX();
                currScreenYPos = mouseEvent.getSceneY();
            }
        });
        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                primaryStage.setX(mouseEvent.getScreenX() - currScreenXPos);
                primaryStage.setY(mouseEvent.getScreenY() - currScreenYPos);
            }
        });
    }
    
    /**
     * Toggles the app between two sizes. One being the default size, and the full screen size
     */
    @FXML
    protected void toggleWindowSize() {
        if (isMaximized) {
            primaryStage.setMaximized(false);

            primaryStage.setWidth(Config.DefaultWindowWidth);
            primaryStage.setHeight(Config.DefaultWindowHeight);

        } else {
            primaryStage.setMaximized(true);
        }
        isMaximized = !isMaximized;
        toggleSizeButtonSymbol();
    }
    
    private void toggleSizeButtonSymbol() {
        if (isMaximized) {
            toggleSizeButton.setText(unMaximizeButtonSymbol);
        } else {
            toggleSizeButton.setText(maximizeButtonSymbol);
        }
    }
    
    /**
     * Opens the About Us page
     */
    @FXML
    private void handleCredits() {
        helpWindow.visit(Config.AboutUsUrl);
    }
    
    /**
     * Minimizes the window
     */
    @FXML
    private void setMinimized() {
        primaryStage.setIconified(true);
    }
    
    @FXML
    private void handleHelp() {
        showHelpAtAnchor("");
    }

    protected void showHelpAtAnchor(String anchor) {
        helpWindow.getHelp(anchor);
    }

    protected void show() {
        primaryStage.show();
        initEventsCenter();
    }
    
    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }
    
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    @Subscribe
    public void handleToDoListSavedEvent(ToDoListSavedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Setting last updated status to " + LocalDateTime.now()));
        statusBarFooter.setSyncStatus(LocalDateTime.now());
    }
}
