package seedu.commando.commons.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Collection of static, application-specific config constants used globally
 */
public class Config {
    public static final String DefaultConfigFilePath = "config.json";
    public static final String ApplicationTitle = "CommanDo";
    public static final String ApplicationName = "CommanDo";
    public static final Level LogLevel = Level.INFO;
    private String userPrefsFilePath = "preferences.json";
    private String toDoListFilePath = "data/todos.xml";
    public static String UserGuideUrl = "https://cs2103aug2016-w13-c3.github.io/main/user";
    public static String AboutUsUrl = "https://github.com/CS2103AUG2016-W13-C3/main/blob/master/docs/AboutUs.md";
    private static Map<String, String> CommandWordsToUserGuideAnchors = new HashMap<String, String>() {{
        put("cheatsheet", "cheatsheet");
        put("add", "adding-to-do-items-add");
        put("edit", "editing-to-do-items-edit");
        put("clear", "clearing-all-to-do-items-clear");
        put("delete", "deleting-to-do-items-delete");
        put("finish", "finishing-to-do-items-finish");
        put("store", "setting-save-location-set");
        put("export", "exporting-export");
        put("import", "importing-import");
        put("undo", "undoing-undo");
        put("redo", "redoing-redo");
        put("exit", "exiting-the-application");
        put("faq", "faq");
        put("about", "about");
        put("commands", "commands");
        put("datetime formats", "supported-date-time-formats");
        put("find", "finding-to-do-items-find");
        put("search logic", "search-logic");
    }};

    public Config() {
    }

    public String getUserPrefsFilePath() {
        return userPrefsFilePath;
    }

    public void setUserPrefsFilePath(String userPrefsFilePath) {
        this.userPrefsFilePath = userPrefsFilePath;
    }

    public String getToDoListFilePath() {
        return toDoListFilePath;
    }

    public void setToDoListFilePath(String toDoListFilePath) {
        this.toDoListFilePath = toDoListFilePath;
    }

    /**
     * Returns anchor name for heading of {@param commandWord}on user guide at {@param UserGuideUrl}
     * If no mapping exists, returns empty
     */
    public static Optional<String> getUserGuideAnchorForCommandWord(String commandWord) {
        for (String word : CommandWordsToUserGuideAnchors.keySet()) {
            if (word.equalsIgnoreCase(commandWord)) {
                return Optional.of(CommandWordsToUserGuideAnchors.get(word));
            }
        }

        return Optional.empty();
    }
}
