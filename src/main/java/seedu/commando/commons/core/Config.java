package seedu.commando.commons.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Collection of static, application-specific config constants used globally
 */
public class Config {
    public static final String ApplicationTitle = "CommanDo";
    public static final String ApplicationName = "CommanDo";
    public static final Level LogLevel = Level.INFO;
    public static String DefaultUserPrefsFilePath = "preferences.json";
    public static String DefaultToDoListFilePath = "data/todos.xml";
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

    /**
     * Returns anchor name for heading of {@param commandWord}on user guide at {@param UserGuideUrl}
     * If no mapping exists, the original value will be returned
     */
    public static String getUserGuideAnchorForCommandWord(String commandWord) {
        return CommandWordsToUserGuideAnchors.entrySet().stream()
            .filter(e -> e.getKey().equals(commandWord))
            .map(Map.Entry::getValue)
            .findFirst().orElse(commandWord);
    }
}
