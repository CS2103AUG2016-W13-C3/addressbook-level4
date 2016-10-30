package seedu.commando.commons.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Collection of static, application-specific config constants used globally
 */
public class Config {
    public static final String ApplicationTitle = "CommanDo";
    public static final String ApplicationName = "CommanDo";
    public static final String ApplicationIcon = "/images/calendar.png";
    public static final Level LogLevel = Level.INFO;
    public static final String UserPrefsFilePath = "preferences.json";
    public static final String DefaultToDoListFilePath = "data/todos.xml";
    public static final double DefaultWindowWidth = 1200;
    public static final double DefaultWindowHeight = 750;
    public static String UserGuideUrl = "https://cs2103aug2016-w13-c3.github.io/main/user";
    public static String AboutUsUrl = "https://github.com/CS2103AUG2016-W13-C3/main/blob/master/docs/AboutUs.md";
    private static Map<String, String> CommandWordsToUserGuideAnchors = new HashMap<String, String>() {{
        put("cheatsheet", "cheatsheet");
        put("add", "add");
        put("edit", "edit");
        put("clear", "clear");
        put("delete", "delete");
        put("finish", "finish");
        put("store", "store");
        put("export", "export");
        put("import", "import");
        put("undo", "undo");
        put("redo", "redo");
        put("exit", "exit");
        put("faq", "faq");
        put("about", "about");
        put("commands", "commands");
        put("datetime formats", "supported-date-time-formats");
        put("find", "find");
        put("search logic", "search-logic");
        put("unfinish", "unfinish");
        put("recall", "recall");
    }};

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
