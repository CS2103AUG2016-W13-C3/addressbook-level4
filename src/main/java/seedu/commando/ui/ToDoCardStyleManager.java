package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToDoCardStyleManager {
    
    public static String recentlyModifiedStateCSS = "-fx-border-color: red";
    public static String finishedStateCSS = "-fx-background-color: derive(#1d1d1d, 95%);";
    public static String activateHoverStateCSS = "-fx-background-color: derive(#DCDCDC, 50%);";
    public static String deactivateHoverStateCSS = "-fx-background-color: #DCDCDC;";
    
    private static DateTimeFormatter formatter;
    private static String dateTimePatternEventsNoYear = "d MMM HH:mm";
    private static String dateTimePatternEvents = "d MMM yyyy HH:mm";
    
    /**
     * The general idea is that if date is in the current year, there is no need
     * to state the year.
     */
    public static String prettifyDateTime(LocalDateTime ldt) {
        if (ldt.getYear() != LocalDateTime.now().getYear()) {
            formatter = DateTimeFormatter.ofPattern(dateTimePatternEvents);
        } else {
            formatter = DateTimeFormatter.ofPattern(dateTimePatternEventsNoYear);
        }
        return formatter.format(ldt);
    }
}
