package seedu.commando.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contains the common functions and variables that EventCard and TaskCard shares
 */
public class ToDoCardStyleManager {
    
    // Styles for EventCard and TaskCard for their respective states: 
    //      Recently modified
    //      Finished
    //      Hover and non hover
    public static String recentlyModifiedStateCSS = "-fx-border-color: red";
    public static String finishedStateCSS = "-fx-background-color: derive(#1d1d1d, 95%);";
    public static String activateHoverStateCSS = "-fx-background-color: derive(#DCDCDC, 50%);";
    public static String deactivateHoverStateCSS = "-fx-background-color: #DCDCDC;";
    
    // Formatter for displaying the dates for dueLabel in TaskCard, startLabel and endLabel for EventCard
    private static DateTimeFormatter formatter;
    private static String dateTimePatternNoYear = "d MMM HH:mm";
    private static String dateTimePattern = "d MMM yyyy HH:mm";
    private static String timePattern = "HH:mm";
    
    /**
     * Formats the localdatetime in a way that is intuitive to the user
     * I.e. Dates that in the current year will not show the years
     * I.e. Dates that are today show up as "today"
     * I.e. Dates that are tomrorow show up as "tomorrow"
     */
    protected static String prettifyDateTime(LocalDateTime ldt) {
        if ((ldt.toLocalDate()).equals(LocalDate.now())) {
            formatter = DateTimeFormatter.ofPattern(timePattern);
            return "Today " + formatter.format(ldt);
        } else if ((ldt.toLocalDate()).equals(LocalDate.now().plusDays(1))) {
            formatter = DateTimeFormatter.ofPattern(timePattern);
            return "Tomorrow " + formatter.format(ldt);
        }
        
        if (ldt.getYear() != LocalDateTime.now().getYear()) {
            formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        } else {
            formatter = DateTimeFormatter.ofPattern(dateTimePatternNoYear);
        }
        
        return formatter.format(ldt);
    }
}
