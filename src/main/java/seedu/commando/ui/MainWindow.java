package seedu.commando.ui;

import java.awt.Scrollbar;

import com.google.common.eventbus.Subscribe;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.GuiSettings;
import seedu.commando.commons.events.ui.ExitAppRequestEvent;
import seedu.commando.commons.events.ui.UpdateFilePathEvent;
import seedu.commando.logic.Logic;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.ui.ToDoListViewCell.Card;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {
    
    // Fixed variables
    private final double NON_MAXIMIZED_HEIGHT = 750;
    private final double NON_MAXIMIZED_WIDTH = 1200;
    private final String ICON = "/images/address_book_32.png";
    private final String FXML = "MainWindow.fxml";
    private final String maximize = "⬜";
    private final String unMaximize = "❐";
    
    // Variables that changes while the app is active
    private double currScreenXPos = 0;
    private double currScreenYPos = 0;
    private static boolean isMaximized;
    
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
    
    // Textfield of commandBox
    private TextField commandField;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    private String appName;
    
    private enum FocusPanes {
        COMMANDBOX, EVENTPANEL, TASKPANEL
    }
    
    FocusPanes currentlyFocusedPane = FocusPanes.COMMANDBOX;
    
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

    protected static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
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
        
        // Icon and App size settings
        setIcon(ICON);
        setWindowDefaultSize(prefs);
        
        scene = new Scene(rootLayout);
        
        // Bind events
        setDraggable();
        setKeyBindings();
        
        primaryStage.setScene(scene);
        
        helpWindow = HelpWindow.load(primaryStage, Config.UserGuideUrl);
    }

    protected void fillInnerParts() {
        eventPanel = EventListPanel.load(primaryStage, getEventListPlaceholder(), logic.getUiEventsToday(), logic.getUiEventsUpcoming());
        taskPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getUiTasks());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getToDoListFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }
    
    protected void moreConfigurations() {
        commandField = (TextField) commandBoxPlaceholder.lookup("#commandTextField");
        commandField.requestFocus();
        disableSplitPaneResize();
        
//        eventPanel.getListView().setCellFactory(new Callback<ListView<ToDoListViewCell>, ToDoListViewCell<UiToDo>>() {
//
//            @Override
//            public ListCell<UiToDo> call(ListView<ToDoListViewCell> arg0) {
//                // TODO Auto-generated method stub
//                return null;
//            }
//            
//        });
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
        primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
        primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        isMaximized = prefs.getGuiSettings().getIsMaximized();
        primaryStage.setMaximized(isMaximized);
        setToggleSizeButtonSymbol();
    }

    /**
     * Returns the current position of the main Window.
     */
    protected GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(), 
                (int) primaryStage.getX(), (int) primaryStage.getY(), isMaximized);
    }

    protected void disableSplitPaneResize() {
        splitPane.lookup(".split-pane-divider").setMouseTransparent(true);
    }

    /**
     * Sets the 3 keyboard shortcuts that trigger their respective functions:
     * Alt + E = Exit the app
     * Alt + H = Open help in window
     * Alt + C = Open credits in window
     * 
     * Pressing Tab will cycle between Event Panel, Task Panel and Command Box
     * Typing text will set focus automatically to the Command Box
     */
    private void setKeyBindings() {
        KeyCombination altH = KeyCodeCombination.keyCombination("Alt+H");
        KeyCombination altC = KeyCodeCombination.keyCombination("Alt+C");
        KeyCombination altM = KeyCodeCombination.keyCombination("Alt+M");
        
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
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                switch (key.getCode()) {
                    case UP:
                        // If currently focused on Event Panel or Task Panel, scroll respectively
                        switch (currentlyFocusedPane) {
                            case EVENTPANEL:
                                eventPanel.scrollUp();
                                break;
                            case TASKPANEL:
                                taskPanel.scrollUp();
                                break;
                            default:
                                break;
                        }
                        break;
                    case DOWN:
                        switch (currentlyFocusedPane) {
                            case EVENTPANEL:
                                eventPanel.scrollDown();
                                break;
                            case TASKPANEL:
                                taskPanel.scrollDown();
                                break;
                            default:
                                break;
                        }
                        break;
                    case TAB:
                        // If tab is pressed, cycle through Event Panel, Task Panel and Command Box
                        cycleThroughFocusPanes();
                        key.consume();
                        break;
                    default:
                        // Else, any other sutiable character is considered input to command box
                        // and it will be in focus
                        currentlyFocusedPane = FocusPanes.COMMANDBOX;
                        commandField.requestFocus();
                }
            }
        });
    }
    
    private void cycleThroughFocusPanes() {
        switch (currentlyFocusedPane) {
        case COMMANDBOX:
            currentlyFocusedPane = FocusPanes.EVENTPANEL;
            eventPanel.getEventListView().requestFocus();
            break;
        case EVENTPANEL:
            currentlyFocusedPane = FocusPanes.TASKPANEL;
            taskPanel.getTaskListView().requestFocus();
            break;
        case TASKPANEL:
            currentlyFocusedPane = FocusPanes.COMMANDBOX;
            commandField.requestFocus();
            break;
        }
    }
    
    private <T extends UiPart> T getCurrentlyFocusedPane() {
        if (currentlyFocusedPane == FocusPanes.EVENTPANEL) {
            return (T) eventPanel;
        } else {
            return (T) taskPanel;
        }
    }

    /**
     * Sets the whole app to be draggable
     */
    private void setDraggable() {
        titleBar.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                currScreenXPos = mouseEvent.getSceneX();
                currScreenYPos = mouseEvent.getSceneY();
            }
        });
        titleBar.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
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
            primaryStage.setHeight(NON_MAXIMIZED_HEIGHT);
            primaryStage.setWidth(NON_MAXIMIZED_WIDTH);
        } else {
            primaryStage.setMaximized(true);
        }
        isMaximized = !isMaximized;
        setToggleSizeButtonSymbol();
    }
    
    private void setToggleSizeButtonSymbol() {
        if (isMaximized) {
            toggleSizeButton.setText(unMaximize);
        } else {
            toggleSizeButton.setText(maximize);
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
        helpWindow.show(anchor);
    }

    protected void show() {
        primaryStage.show();
        initEventsCenter();
    }
    
    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }
    
    @Subscribe
    private void handleUpdateFilePathEvent(UpdateFilePathEvent event){
    	statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getToDoListFilePath());
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }
    
}
