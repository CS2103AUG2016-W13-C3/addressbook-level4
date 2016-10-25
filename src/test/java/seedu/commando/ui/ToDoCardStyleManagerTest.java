package seedu.commando.ui;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ToDoCardStyleManagerTest {
    
    private DateTimeFormatter formatDayOfWeek = DateTimeFormatter.ofPattern("EEE");
    private DateTimeFormatter formatMonth = DateTimeFormatter.ofPattern("MMM");
    private LocalDateTime now = LocalDateTime.now();
    private final int currentYear = now.getYear();
    private final int currentMonth = now.getMonthValue();
    private final int currentDay = now.getDayOfMonth();
    
    @Before
    public void setup() {
    }

    @After
    public void teardown() {
    }
    
    @Test
    public void prettifyDateTime_currentYear() {
        // Should not display year
        final LocalDateTime date = LocalDateTime.of(currentYear, 2, 28, 23, 59);
        assertEquals("23:59 " + formatDayOfWeek.format(date) + " 28 Feb",
                ToDoCardStyleManager.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_nonCurrentYear() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(currentYear - 1, 2, 28, 23, 59);
        assertEquals("23:59 " + formatDayOfWeek.format(date) + " 28 Feb " + (currentYear - 1),
                ToDoCardStyleManager.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_today() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(LocalDate.now(), LocalTime.NOON);
        assertEquals("12:00 Today " + formatMonth.format(LocalDate.now()),
                ToDoCardStyleManager.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_tomorrow() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON);
        assertEquals("12:00 Tomorrow " + formatMonth.format(LocalDate.now().plusDays(1)),
                ToDoCardStyleManager.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTime_yesterday() {
        // Should display year
        final LocalDateTime date = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.NOON);
        assertEquals("12:00 Yesterday " + formatMonth.format(LocalDate.now().minusDays(1)),
                ToDoCardStyleManager.prettifyDateTime(date));
    }
    
    @Test
    public void prettifyDateTimeRange_currentYear_to_currentYear() {
        // Should not display any year
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 3, 1, 12, 30);
        assertEquals("12:30 " + formatDayOfWeek.format(dateFrom) + " 28 Feb to\n" + 
                     "12:30 " + formatDayOfWeek.format(dateTo) + " 1 Mar",
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameNonCurrentYear() {
        // Should display one year
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear - 1, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear - 1, 3, 1, 12, 30);
        assertEquals("12:30 " + formatDayOfWeek.format(dateFrom) + " 28 Feb to\n" + 
                     "12:30 " + formatDayOfWeek.format(dateTo) + " 1 Mar " + (currentYear - 1),
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_currentYear_to_nonCurrentYear() {
        // Should display both years
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear + 1, 3, 1, 12, 30);
        assertEquals("12:30 " + formatDayOfWeek.format(dateFrom) + " 28 Feb " + (currentYear) + " to\n" + 
                     "12:30 " + formatDayOfWeek.format(dateTo) + " 1 Mar " + (currentYear + 1),
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_nonCurrentYear_to_diffNonCurrentYear() {
        // Should display both years
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear - 1, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear + 1, 3, 1, 12, 30);
        assertEquals("12:30 " + formatDayOfWeek.format(dateFrom) + " 28 Feb " + (currentYear - 1) + " to\n" + 
                     "12:30 " + formatDayOfWeek.format(dateTo) + " 1 Mar " + (currentYear + 1),
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameYear_differenMonth() {
        // Should display one year and both months
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 3, 1, 12, 30);
        assertEquals("12:30 " + formatDayOfWeek.format(dateFrom) + " 28 Feb to\n" + 
                     "12:30 " + formatDayOfWeek.format(dateTo) + " 1 Mar",
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameYear_sameMonth_differentDay() {
        // Should display one year and both month
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 27, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 2, 28, 12, 30);
        assertEquals("12:30 " + formatDayOfWeek.format(dateFrom) + " 27 to\n" + 
                     "12:30 " + formatDayOfWeek.format(dateTo) + " 28 Feb",
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameYear_sameMonth_sameDay() {
        // Should display one year, one month and two times
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, 2, 27, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, 2, 27, 13, 30);
        assertEquals("12:30 to 13:30 " + formatDayOfWeek.format(dateTo) + " 27 Feb",
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
    
    @Test
    public void prettifyDateTimeRange_sameDate_differentTime_butIsToday() {
        // Should display time and either 'Today'
        
        System.out.println(currentMonth);
        System.out.println(currentDay);
        final LocalDateTime dateFrom = LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 30);
        final LocalDateTime dateTo = LocalDateTime.of(currentYear, currentMonth, currentDay, 13, 30);
        assertEquals("12:30 to 13:30 Today",
                ToDoCardStyleManager.prettifyDateTimeRange(dateFrom, dateTo));
    }
}
