package seedu.commando.commons.core;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seedu.commando.commons.core.DateTimePrettifier;

//@@author A0139080J
public class DateTimePrettifierTest {
    
    private DateTimeFormatter formatDayOfWeek = DateTimeFormatter.ofPattern("EEE");
    private LocalDateTime now = LocalDateTime.now();
    private LocalDateTime untilNever = LocalDateTime.MAX;
    private LocalDateTime sinceForever = LocalDateTime.MIN;
    
    private final int currentYear = now.getYear();
    private final int currentMonth = now.getMonthValue();
    private final int currentDay = now.getDayOfMonth();
    
    private final int tomorrowYear = now.plusDays(1).getYear();
    private final int tomorrowMonth = now.plusDays(1).getMonthValue();
    private final int tomorrowDay = now.plusDays(1).getDayOfMonth();
    
    @Test
    public void prettifyDateTime_currentYear_dontShowYear() {
        // Should not display year
        final LocalDateTime date = LocalDateTime.of(currentYear, 2, 28, 23, 59);
        assertEquals("23:59 on " + formatDayOfWeek.format(date) + " 28 Feb",
                DateTimePrettifier.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_nonCurrentYear_showYear() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(currentYear - 1, 2, 28, 23, 59);
        assertEquals("23:59 on " + formatDayOfWeek.format(date) + " 28 Feb " + (currentYear - 1),
                DateTimePrettifier.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_today_showToday() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(LocalDate.now(), LocalTime.NOON);
        assertEquals("12:00 Today",
                DateTimePrettifier.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_tomorrow_showTomorrow() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON);
        assertEquals("12:00 Tomorrow",
                DateTimePrettifier.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_yesterday_showYesterday() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.NOON);
        assertEquals("12:00 Yesterday",
                DateTimePrettifier.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTimeRange_currentYearToCurrentYear_dontShowYear() {
        // Should not display any year
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 3, 1, 12, 30);
        assertEquals("12:30 on " + formatDayOfWeek.format(dateFrom) + " 28 Feb to " +
                     "12:30 on " + formatDayOfWeek.format(dateTo) + " 1 Mar",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameNonCurrentYear_showOneYear() {
        // Should display one year
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear - 1, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear - 1, 3, 1, 12, 30);
        assertEquals("12:30 on " + formatDayOfWeek.format(dateFrom) + " 28 Feb to " +
                     "12:30 on " + formatDayOfWeek.format(dateTo) + " 1 Mar " + (currentYear - 1),
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_currentYearToNonCurrentYear_showBothYears() {
        // Should display both years
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear + 1, 3, 1, 12, 30);
        assertEquals("12:30 on " + formatDayOfWeek.format(dateFrom) + " 28 Feb " + (currentYear) + " to " +
                     "12:30 on " + formatDayOfWeek.format(dateTo) + " 1 Mar " + (currentYear + 1),
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_nonCurrentYearToDiffNonCurrentYear_showBothYears() {
        // Should display both years
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear - 1, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear + 1, 3, 1, 12, 30);
        assertEquals("12:30 on " + formatDayOfWeek.format(dateFrom) + " 28 Feb " + (currentYear - 1) + " to " +
                     "12:30 on " + formatDayOfWeek.format(dateTo) + " 1 Mar " + (currentYear + 1),
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameYearDifferentMonth_showOneYearAndBothMonths() {
        // Should display one year and both months
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 3, 1, 12, 30);
        assertEquals("12:30 on " + formatDayOfWeek.format(dateFrom) + " 28 Feb to " +
                     "12:30 on " + formatDayOfWeek.format(dateTo) + " 1 Mar",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameYearSameMonthDifferentDay_showOneYearBothMonths() {
        // Should display one year and both month
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 27, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        assertEquals("12:30 on " + formatDayOfWeek.format(dateFrom) + " 27 Feb to " +
                     "12:30 on " + formatDayOfWeek.format(dateTo) + " 28 Feb",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameYearSameMonthSameDay_showOneYearOneMonthAndTwoTimes() {
        // Should display one year, one month and two times
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 27, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 2, 27, 13, 30);
        assertEquals("12:30 to 13:30 on " + formatDayOfWeek.format(dateTo) + " 27 Feb",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_diffYearSameMonthSameDay_showTwoYearTwoMonthAndTwoTimes() {
        // Should display one year, one month and two times
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 27, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear + 1, 2, 27, 13, 30);
        assertEquals("12:30 on " + formatDayOfWeek.format(dateFrom) + " 27 Feb " + currentYear + " to " +
                     "13:30 on " + formatDayOfWeek.format(dateTo) + " 27 Feb " + (currentYear + 1),
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_todayToToday_showTimeAndToday() {
        // Should display time and date
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, currentMonth, currentDay, 13, 30);
        assertEquals("12:30 to 13:30 Today",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_todayToTomorrow_showTimeTodayToTimeTomorrow() {
        // Should display time and date
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(tomorrowYear, tomorrowMonth, tomorrowDay, 13, 30);
        assertEquals("12:30 Today to 13:30 Tomorrow",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_todayToSomeDate_showTimeTodayToTimeDate() {
        // Should display time and date
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear + 1, 2, 27, 13, 30);
        assertEquals("12:30 Today to 13:30 on " + formatDayOfWeek.format(dateTo) + " 27 Feb " + (currentYear + 1),
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_untilTomorrow_showUntilTomorrow() {
        // Should display time and date with words Until and Tomorrow
        final LocalDateTime dateFrom = sinceForever;
        final LocalDateTime dateTo = LocalDateTime.of(tomorrowYear, tomorrowMonth, tomorrowDay, 13, 30);
        assertEquals("Until 13:30 Tomorrow",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_nowUntilForever_showSinceForever() {
        // Should display time and date and Onwards
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 30);
        final LocalDateTime dateTo = untilNever;
        assertEquals("From 12:30 Today onwards",
                DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo));
    }

    @Test
    public void prettifyDateTimeRange_todayToNextYearToday_showTodayToNextYearToday() {
        final LocalDateTime dateFrom = LocalDateTime.now().withHour(1).withMinute(0);
        final LocalDateTime dateTo = LocalDateTime.now().withHour(1).withMinute(0).plusYears(1);
        assertEquals(
            "01:00 Today to 01:00 on " + DateTimeFormatter.ofPattern("EEE d MMM YYYY").format(dateTo),
            DateTimePrettifier.prettifyDateTimeRange(dateFrom, dateTo)
        );
    }
}
