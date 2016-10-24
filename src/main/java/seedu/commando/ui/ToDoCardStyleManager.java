package seedu.commando.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    private static DateTimeFormatter formatDateTimeNoYear = DateTimeFormatter.ofPattern("EEE d MMM HH:mm");
    private static DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("EEE d MMM yyyy HH:mm");
    private static DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("EEE d MMM");
    
    // In case I want to change them for some reason
    private static String to = "to";
    private static String today = "tdy";
    private static String tomorrow = "tmr";
    private static String yesterday = "ytd";
    
    
    /**
     * @param Start datetime and end datetime
     * @return format in a way that is intuitive to the user
     * I.e. Dates that in the current year will not show the years
     * I.e. Dates that are in the same year will only show the year once
     * I.e. Dates that are tomorrow show up as "tomorrow" (Similarly, yesterday and today)
     * I.e. Dates that are in the same month will only show the month once, provided that 
     *      the year is the same
     * I.e. Times will be displayed for both no matter what, even if datetimes are exactly the same
     */
    protected static String prettifyDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        
        String start = "";
        String end = "";
        
        boolean sameYear = false;
        boolean sameMonth = false;
        
        if (startDateTime.getYear() == endDateTime.getYear()) {
            // If start year is equals to end year
            if (endDateTime.getYear() != LocalDateTime.now().getYear()) {
                // If both start year and end year is not this year
                end += " " + endDateTime.getYear();
                // Else, don't display any year
            }
            sameYear = true;
        } else {
            // Start and end year are not equal
            // Hence display both
            end += " " + endDateTime.getYear();
            start += " " + startDateTime.getYear();
        }
        
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        LocalDate todayDate = LocalDate.now();
        LocalDate tomorrowDate = todayDate.plusDays(1);
        LocalDate yesterdayDate = todayDate.minusDays(1);
        
        // This means that the event is a day event (happening only within one day)
        if (startDate.equals(endDate)) {
            if (startDate.equals(yesterdayDate)) {
                end = formatTime.format(endDateTime) + " " + yesterday + end;
            } else if (startDate.equals(todayDate)) {
                end = formatTime.format(endDateTime) + " " + today + end;
            } else if (startDate.equals(tomorrowDate)) {
                end = formatTime.format(endDateTime) + " " + tomorrow + end;
            } else {
                end = formatDateTime.format(endDateTime) + end;
            }
            start = formatTime.format(startDateTime) + start;
            return start + " " + to + " " + end;
        }

        // If same year and not same month
        if (sameYear && startDateTime.getMonthValue() != endDateTime.getMonthValue()) {
            // Display one month
            end = formatDate.format(endDateTime) + end;
            sameMonth = true;
        } else {
            // Display both months
            start = formatDate.format(startDateTime) + start;
            end = formatDate.format(endDateTime) + end;
        }

        // If exactly the same date, start will be "", hence there is a need to get rid 
        // of the extra space
        start = formatTime.format(startDateTime) + (start.isEmpty() ? "" : " " + start);
        end = formatTime.format(endDateTime) + (start.isEmpty() ? "" : " " + end);
        
        if (!sameYear || !sameMonth) {
            return start + " " + to + "\n" + end;
        } else {
            return start + " " + to + " " + end;
        }
    }
    
    /**
     * @param the date and time (LocalDateTime)
     * @return format in a way that is intuitive to the user
     * I.e. Dates that in the current year will not show the years
     * I.e. Dates that are today show up as "Today"
     * I.e. Dates that are tomrorow show up as "Tmr"
     */
    protected static String prettifyDateTime(LocalDateTime dateTime) {
        final LocalDate date = dateTime.toLocalDate();
        final LocalTime time = dateTime.toLocalTime();
        
        if (date.equals(LocalDate.now())) {
            return "Today " + formatTime.format(time);
        } else if (date.equals(LocalDate.now().plusDays(1))) {
            return "Tmr " + formatTime.format(time);
        }
        
        if (date.getYear() == LocalDateTime.now().getYear()) {
            return formatDateTimeNoYear.format(dateTime);
        } else {
            return formatDateTime.format(dateTime);
        }
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
