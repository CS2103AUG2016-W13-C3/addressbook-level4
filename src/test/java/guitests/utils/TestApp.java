 package guitests.utils;

import java.io.IOException;
import java.util.function.Supplier;

import javafx.stage.Stage;
import seedu.commando.MainApp;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.util.StringUtil;
import seedu.commando.model.Model;
import seedu.commando.model.UserPrefs;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.storage.Storage;
import seedu.commando.storage.XmlSerializableToDoList;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {
    protected Supplier<ReadOnlyToDoList> initialDataSupplier = () -> null;

    public static final String PREF_SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("pref_testing.json");

    protected String toDoListFilePath = Config.DefaultToDoListFilePath;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyToDoList> initialDataSupplier, String toDoListFilePath) {
        this.initialDataSupplier = initialDataSupplier;
        this.toDoListFilePath = toDoListFilePath;

        // Override user pref's file path to testing environment's
        userPrefsFilePath = PREF_SAVE_LOCATION_FOR_TESTING;
    }

    // Override to-do list file path to testing environment's
    @Override
    public void init() throws Exception {
        super.init();
        saveUserPrefs();
    }

    @Override
    protected UserPrefs initPrefs(Storage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        userPrefs.setToDoListFilePath(toDoListFilePath);
        return userPrefs;
    }

    @Override
    protected Model initModelManager(Storage storage, UserPrefs userPrefs) {
        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            TestUtil.createDataFileWithData(
                new XmlSerializableToDoList(this.initialDataSupplier.get()),
                userPrefs.getToDoListFilePath().getValue());
        }

        return super.initModelManager(storage, userPrefs);
    }

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
