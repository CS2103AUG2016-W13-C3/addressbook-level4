package seedu.commando.commons.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@@author A0139080J
/**
 * Collection of methods to prettify date-times for the GUI.
 */
public class DateTimePrettifier {

    // Formatter for displaying the dates for dueLabel in TaskCard, startLabel
    // and endLabel for EventCard
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
     * @param startDateTime
     *            start date time
     * @param endDateTime
     *            end date time
     * @return format in a way that is intuitive to the user
     *
     *         I.e. Dates that in the current year will not show the years I.e.
     *         Dates that are in the same year will only show the year once I.e.
     *         Dates that are tomorrow show up as "tomorrow" (Similarly,
     *         yesterday and today) I.e. Dates that are in the same month will
     *         only show the month once, provided that the year is the same I.e.
     *         Times will be displayed for both no matter what, even if
     *         datetimes are exactly the same
     */
    public static String prettifyDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String start = "";
        String end = "";

        boolean sameYear = startDateTime.getYear() == endDateTime.getYear();
        boolean sameMonth = startDateTime.getMonthValue() == endDateTime.getMonthValue();
        boolean sameDay = startDateTime.getDayOfMonth() == endDateTime.getDayOfMonth();
        boolean startIsYtdOrTdyOrTmr = isYtdOrTdyOrTmr(startDateTime);
        boolean endIsYtdOrTdyOrTmr = isYtdOrTdyOrTmr(endDateTime);

        // If start date is Yesterday or Today or Tomorrow, shouldn't show month
        // and year
        if (startIsYtdOrTdyOrTmr) {
            // If same day, don't display month and year
            if (sameDay) {
                return getTime(startDateTime) + " " + keywordTo + " " + getTime(endDateTime) + " "
                        + getDay(startDateTime);
            } else if (endIsYtdOrTdyOrTmr) {
                return getTime(startDateTime) + " " + getDay(startDateTime) + " " + keywordTo + "\n"
                        + getTime(endDateTime) + " " + getDay(endDateTime);
            } else if (endDateTime.getYear() == todayDate.getYear()) {
                return getTime(startDateTime) + " " + getDay(startDateTime) + " " + keywordTo + "\n"
                        + getTime(endDateTime) + " " + getDay(endDateTime) + " " + getMonth(endDateTime);
            } else {
                return getTime(startDateTime) + " " + getDay(startDateTime) + " " + keywordTo + "\n"
                        + getTime(endDateTime) + " " + getDay(endDateTime) + " " + getMonth(endDateTime) + " "
                        + endDateTime.getYear();
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
            end = " \n" + getDay(endDateTime) + end;
        } else {
            // Display both months
            start = " " + getDay(startDateTime) + start;
            end = " " + getDay(endDateTime) + end;
        }

        // If exactly the same date, start will be "", hence there is a need to
        // get rid
        // of the extra space
        start = getTime(startDateTime) + start;
        end = getTime(endDateTime) + end;

        // If its the same day, there is no need to display the date on two
        // lines
        // because its comparatively short to other intervals that span over
        // days
        if (!sameYear || !sameMonth || !sameDay) {
            return start + " " + keywordTo + "\n" + end;
        } else {
            return start + " " + keywordTo + " " + end;
        }
    }

    private static boolean isYtdOrTdyOrTmr(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().isEqual(todayDate) || localDateTime.toLocalDate().isEqual(tomorrowDate)
                || localDateTime.toLocalDate().isEqual(yesterdayDate);
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
            return "on " + formatDay.format(ldt);
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
     * @param the
     *            date and time (LocalDateTime)
     * @return format in a way that is intuitive to the user
     *
     *         I.e. Dates that in the current year will not show the years I.e.
     *         Dates that are today show up as "Today" I.e. Dates that are
     *         tomrorow show up as "Tmr"
     */
    public static String prettifyDateTime(LocalDateTime dateTime) {
        boolean isYtdOrTdyOrTmr = isYtdOrTdyOrTmr(dateTime);

        if (isYtdOrTdyOrTmr) {
            return getTime(dateTime) + " " + getDay(dateTime);
        } else if (dateTime.getYear() == todayDate.getYear()) {
            return getTime(dateTime) + " " + getDay(dateTime) + " " + getMonth(dateTime);
        } else {
            return getTime(dateTime) + " " + getDay(dateTime) + " " + getMonth(dateTime) + " " + dateTime.getYear();
        }
    }
}
