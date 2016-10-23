package seedu.commando.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;

/**
 * Contains the common functions and variables that EventCard and TaskCard shares
 * and additional styling choices of elements that are doen programmatically
 */
public class ToDoCardStyleManager {
    
    // Styles for EventCard and TaskCard for their respective states: 
    //      Recently modified
    //      Finished
    //      Hover and non hover
    public static String recentlyModifiedStateCSS = "-fx-border-color: red";
    
    public static String finishedStateContentCSS = "-fx-background-color: derive(#1d1d1d, 95%);";
    public static String finishedStateIndexCSS = "-fx-background-color: derive(#1d1d1d, 30%);";
    
    public static String activateHoverStateContentCSS = "-fx-background-color: derive(#DCDCDC, 50%);";
    public static String activateHoverStateIndexCSS = "-fx-background-color: derive(#1d1d1d, 40%);";
    
    public static String deactivateHoverStateContentCSS = "-fx-background-color: #DCDCDC;";
    public static String deactivateHoverStateIndexCSS = "-fx-background-color: derive(#1d1d1d, 60%);";
    
    // Formatter for displaying the dates for dueLabel in TaskCard, startLabel and endLabel for EventCard
    private static DateTimeFormatter formatter;
    private static String dateTimePatternNoYear = "d MMM HH:mm";
    private static String dateTimePattern = "d MMM yyyy HH:mm";
    private static String timePattern = "HH:mm";
    
    /**
     * @param the date and time (LocalDateTime)
     * @return format in a way that is intuitive to the user
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
    
    /**
     * @param dayDifference
     * @return colour code for a date label. The closer it is to today, the more red 
     * it will become, otherwise, tends towards green. If it is already over (neg), it is 
     * fully red
     */
    protected static String getDateProximityGreen(int dayDifference) {
        if (dayDifference < 0) {
            return "#FF0000";
        } else if (dayDifference <= 1) {
            return "#DD0000";
        } else if (dayDifference <= 3) {
            return "#AA0000";
        } else if (dayDifference <= 7) {
            return "#882200";
        } else if (dayDifference <= 14) {
            return "#686033";
        } else {
            return "#386D33";
        }
    }
    
    /**
     * @param dayDifference
     * @return colour code for a date label. The closer it is to today, the more red 
     * it will become, otherwise, tends towards blue. If it is already over (neg), it is 
     * fully red
     */
    protected static String getDateProximityBlue(int dayDifference) {
        if (dayDifference < 0) {
            return "#FF0000";
        } else if (dayDifference <= 1) {
            return "#DD0A00";
        } else if (dayDifference <= 3) {
            return "#AA2100";
        } else if (dayDifference <= 7) {
            return "#69326B";
        } else if (dayDifference <= 14) {
            return "#3C26A8";
        } else {
            return "#0011FF";
        }
    }
    
}
