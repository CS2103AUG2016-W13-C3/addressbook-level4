package seedu.commando;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.core.Version;
import seedu.commando.commons.events.logic.ExitAppRequestEvent;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.logic.Logic;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.*;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.storage.Storage;
import seedu.commando.storage.StorageManager;
import seedu.commando.ui.Ui;
import seedu.commando.ui.UiManager;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(1, 0, 0, true);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected UserPrefs userPrefs;

    public MainApp() {}

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing " + Config.ApplicationTitle + " ]===========================");
        super.init();

        logger.info("Using prefs file: " + Config.DefaultToDoListFilePath);
        storage = new StorageManager(Config.DefaultToDoListFilePath, Config.UserPrefsFilePath);

        userPrefs = initPrefs(storage);

        initLogging();

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage, userPrefs);

        ui = new UiManager(logic, userPrefs);

        initEventsCenter();
    }

    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        // Set to-do list storage file path to user pref's
        logger.info("Using to-do list file: " + userPrefs.getToDoListFilePath());
        storage.setToDoListFilePath(userPrefs.getToDoListFilePath().getValue());

        ReadOnlyToDoList initialToDoList = new ToDoList();

        try {
            Optional<ReadOnlyToDoList> toDoListOptional = storage.readToDoList();
            if(toDoListOptional.isPresent()){
                initialToDoList = toDoListOptional.get();
            } else {
                logger.info("Data file not found. Will be starting with an empty to-do list");
            }
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty to-do list");
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty to-do list");
        }

        return new ModelManager(initialToDoList, userPrefs);
    }

    private void initLogging() {
        LogsCenter.init(Config.LogLevel);
    }
    
    protected UserPrefs initPrefs(Storage storage) {
        UserPrefs initializedPrefs = new UserPrefs();

        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (prefsOptional.isPresent()) {
                initializedPrefs = prefsOptional.get();
            }
        } catch (DataConversionException e) {
            logger.warning("User prefs file is not in the correct format. "
                + "Using default user prefs...");
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Using default user prefs...");
        }

        // Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException exception) {
            logger.warning("Failed to save user prefs file: " + StringUtil.getDetails(exception));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting " + Config.ApplicationTitle + " " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping " + Config.ApplicationTitle + " ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
