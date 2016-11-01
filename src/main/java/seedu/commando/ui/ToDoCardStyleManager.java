package seedu.commando.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@@author A0139080J
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
    private static DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEE d");
    private static DateTimeFormatter formatMonth = DateTimeFormatter.ofPattern("MMM");
    
    // In case I want to change them for some reason
    private static String keywordTo = "to";
    private static String keywordToday = "Today";
    private static String keywordTomorrow = "Tomorrow";
    private static String keywordYesterday = "Yesterday";
    
    // Date references
    private static LocalDate todayDate = LocalDate.now();
    private static LocalDate tomorrowDate = todayDate.plusDays(1);
    private static LocalDate yesterdayDate = todayDate.minusDays(1);
    
    
    /**
     * @param startDateTime start date time
     * @param endDateTime end date time
     * @return format in a way that is intuitive to the user
     * 
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
        
        boolean sameYear = startDateTime.getYear() == endDateTime.getYear();
        boolean sameMonth = startDateTime.getMonthValue() == endDateTime.getMonthValue();
        boolean sameDay = startDateTime.getDayOfMonth() == endDateTime.getDayOfMonth();
        boolean startIsYtdOrTdyOrTmr = startDateTime.toLocalDate().isEqual(todayDate) ||
                                         startDateTime.toLocalDate().isEqual(tomorrowDate) ||
                                         startDateTime.toLocalDate().isEqual(yesterdayDate);
        boolean endIsYtdOrTdyOrTmr = endDateTime.toLocalDate().isEqual(todayDate) ||
                                        endDateTime.toLocalDate().isEqual(tomorrowDate) ||
                                        endDateTime.toLocalDate().isEqual(yesterdayDate);
        
        // If start date is Yesterday or Today or Tomorrow, shouldn't show month and year
        if (startIsYtdOrTdyOrTmr) {
            // If same day, don't display month and year
            if (sameDay) {
                return getTime(startDateTime) + " " + keywordTo + " " + getTime(endDateTime) + " " + getDay(startDateTime);
            } else if (endIsYtdOrTdyOrTmr) {
                return getTime(startDateTime) + " " + getDay(startDateTime) + " " + keywordTo + " " + 
                        getTime(endDateTime) + " " + getDay(endDateTime);
            } else if (endDateTime.getYear() == todayDate.getYear()) {
                return getTime(startDateTime) + " " + getDay(startDateTime) + " " + keywordTo + " " + 
                        getTime(endDateTime) + " " + getDay(endDateTime) + " " + getMonth(endDateTime);
            } else {
                return getTime(startDateTime) + " " + getDay(startDateTime) + " " + keywordTo + " " + 
                        getTime(endDateTime) + " " + getDay(endDateTime) + " " + getMonth(endDateTime) + " " + endDateTime.getYear();
            }
        }
        
        if (sameYear) {
            // If start year is equals to end year
            if (endDateTime.getYear() != todayDate.getYear()) {
                // If both start year and end year is not this year
                end += " " + endDateTime.getYear();
                // Else, don't display any year
            }
        } else {
            // Start and end year are not equal
            // Hence display both
            end += " " + endDateTime.getYear();
            start += " " + startDateTime.getYear();
        }
        
        if (sameYear && sameMonth) {
            // If same year and same month
            // Display one month
            end = " " + getMonth(endDateTime) + end;
        } else {
            // Display both months
            start = " " + getMonth(startDateTime) + start;
            end = " " + getMonth(endDateTime) + end;
        }
        
        if (sameYear && sameMonth && sameDay) {
            // If same year and same month and same day
            // Display one month
            end = " " + getDay(endDateTime) + end;
        } else {
            // Display both months
            start = " " + getDay(startDateTime) + start;
            end = " " + getDay(endDateTime) + end;
        }

        // If exactly the same date, start will be "", hence there is a need to get rid 
        // of the extra space
        start = getTime(startDateTime) + start;
        end = getTime(endDateTime) + end;
        
        // If its the same day, there is no need to display the date on two lines
        // because its comparatively short to other intervals that span over days
        if (!sameYear || !sameMonth || !sameDay) {
            return start + " " + keywordTo + "\n" + end;
        } else {
            return start + " " + keywordTo + " " + end;
        }
    }
    
    /**
     * @param ldt
     * @return a date or 'today' or 'yesterday' or 'tomorrow'
     */
    private static String getDay(LocalDateTime ldt) {
        final LocalDate ld = ldt.toLocalDate();
        if (ld.isEqual(yesterdayDate)) {
            return keywordYesterday;
        } else if (ld.isEqual(todayDate)) {
            return keywordToday;
        } else if (ld.isEqual(tomorrowDate)) {
            return keywordTomorrow;
        } else {
            return formatDay.format(ldt);
        }
    }
    
    /**
     * @param ldt
     * @return the month of the date
     */
    private static String getMonth(LocalDateTime ldt) {
        return formatMonth.format(ldt);
    }
    
    /**
     * @param ldt
     * @return the time of the date
     */
    private static String getTime(LocalDateTime ldt) {
        return formatTime.format(ldt);
    }
    
    /**
     * @param the date and time (LocalDateTime)
     * @return format in a way that is intuitive to the user
     * 
     * I.e. Dates that in the current year will not show the years
     * I.e. Dates that are today show up as "Today"
     * I.e. Dates that are tomrorow show up as "Tmr"
     */
    protected static String prettifyDateTime(LocalDateTime dateTime) {
        boolean isYtdOrTdyOrTmr = 
                 dateTime.toLocalDate().isEqual(todayDate) ||
                 dateTime.toLocalDate().isEqual(tomorrowDate) ||
                 dateTime.toLocalDate().isEqual(yesterdayDate);
        
        if (isYtdOrTdyOrTmr) {
            return getTime(dateTime) + " " + getDay(dateTime);
        } else if (dateTime.getYear() == todayDate.getYear()) {
            return getTime(dateTime) + " " + getDay(dateTime) + " " + getMonth(dateTime);
        } else {
            return getTime(dateTime) + " " + getDay(dateTime) + " " + getMonth(dateTime) + " " + dateTime.getYear();
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
            return "#AA1100";
        } else if (dayDifference <= 3) {
            return "#882200";
        } else if (dayDifference <= 7) {
            return "#775500";
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
