package seedu.commando.logic.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.commando.logic.parser.DateTimeParser;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTimeParserTest {
    private DateTimeParser dateTimeParser;
    private LocalDateTime now = LocalDateTime.now();

    @Before
    public void setup() {
        dateTimeParser = new DateTimeParser();
    }

    @After
    public void teardown() {
    }

    @Test
    public void parseDateTime_emptyString()  {
        assertFalse(dateTimeParser.parseDateTime("").isPresent());
    }

    @Test
    public void parseDateTime_ddMMMyyyy_colonHHmm()  {
        assertEquals(
            LocalDateTime.of(2016, 2, 28, 23, 59),
            dateTimeParser.parseDateTime("28 Feb 2016 23:59").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ddMMM_HHmmPm()  {
        assertEquals(
            LocalDateTime.of(now.getYear(), 2, 28, 21, 30),
            dateTimeParser.parseDateTime("28 Feb 9.30pm").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ddMMMyy_h()  {
        assertEquals(
            LocalDateTime.of(2016, 2, 28, 23, 59),
            dateTimeParser.parseDateTime("28 Feb 16 2359h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ddMMM()  {
        assertEquals(
            LocalDateTime.of(now.getYear(), 2, 28,
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("28 Feb").orElse(null)
        );
    }

    @Test
    public void parseDateTime_dateWithSlashes()  {
        assertEquals(
            LocalDateTime.of(2016, 12, 11,
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("11/12/16").orElse(null)
        );

        assertEquals(
            LocalDateTime.of(2001, 1, 1,
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("1/1/01").orElse(null)
        );
    }

    @Test
    public void parseDateTime_HHmmh()  {
        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 19, 19),
            dateTimeParser.parseDateTime("1919h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ordinalFullMonth_HHAm()  {
        assertEquals(
            LocalDateTime.of(now.getYear(), 1, 1, 11, 0),
            dateTimeParser.parseDateTime("1st January 11am").orElse(null)
        );
    }

    @Test
    public void parseDateTime_presets()  {
        // today
        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("today").orElse(null)
        );

        // tomorrow
        LocalDateTime tomorrow = now.plusDays(1);
        assertEquals(
            LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("tomorrow").orElse(null)
        );
    }

    @Test
    public void parseDateTime_extraSpaces()  {
        assertEquals(
            LocalDateTime.of(2016, 4, 22, 8, 0),
            dateTimeParser.parseDateTime("22     April   2016  0800h ").orElse(null)
        );
    }

    @Test
    public void parseDateTime_reversedMonthWord()  {
        assertEquals(
            LocalDateTime.of(2011, 6, 21, 13, 13),
            dateTimeParser.parseDateTime("June 21 2011 1.13pm").orElse(null)
        );
    }

    @Test
    public void parseDateTime_inferDateFromPreviousParse()  {
        // In multiple parses, date should be inferred from previous parse
        dateTimeParser.parseDateTime("10 Jan 2016 10am");

        assertEquals(
            LocalDateTime.of(2016, 1, 10, 23, 0),
            dateTimeParser.parseDateTime("11pm").orElse(null)
        );

        assertEquals(
            LocalDateTime.of(2016, 1, 10, 11, 11),
            dateTimeParser.parseDateTime("1111h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_invalidDate()  {
        assertFalse(dateTimeParser.parseDateTime("No date").isPresent());
        assertFalse(dateTimeParser.parseDateTime("10/2016").isPresent());
        assertFalse(dateTimeParser.parseDateTime("1/2/3/4").isPresent());
        assertFalse(dateTimeParser.parseDateTime("1 May be").isPresent());
        assertFalse(dateTimeParser.parseDateTime("10 Marching").isPresent());
        assertFalse(dateTimeParser.parseDateTime("Feb20").isPresent());
    }

    @Test
    public void parseDateTime_invalidTime()  {
        assertFalse(dateTimeParser.parseDateTime("10cm").isPresent());
        assertFalse(dateTimeParser.parseDateTime("2459").isPresent());
        assertFalse(dateTimeParser.parseDateTime("0").isPresent());
        assertFalse(dateTimeParser.parseDateTime("31").isPresent());
        assertFalse(dateTimeParser.parseDateTime("h").isPresent());
    }

    @Test
    public void resetContext_inferredDate()  {
        dateTimeParser.parseDateTime("10 Jan 2016 10am");

        dateTimeParser.resetContext();

        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 0),
            dateTimeParser.parseDateTime("11pm").orElse(null)
        );
    }

    // TODO: Add more datetime format tests
    @Test
    public void parseDateTime_relativeDayOfWeek()  {
        dateTimeParser.parseDateTime("10 Jan 2016 10am");
        
        LocalDateTime ldt = now.minusDays(1).with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        assertEquals(
                LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),                 
                    DateTimeParser.DefaultLocalTime.getHour(),
                    DateTimeParser.DefaultLocalTime.getMinute()),
                dateTimeParser.parseDateTime("coming Friday").orElse(null)
        );
        
        assertEquals(
                LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),                 
                    DateTimeParser.DefaultLocalTime.getHour(),
                    DateTimeParser.DefaultLocalTime.getMinute()),
                dateTimeParser.parseDateTime("Friday").orElse(null)
        );
        
        assertEquals(
                LocalDateTime.of(ldt.plusWeeks(1).getYear(), ldt.plusWeeks(1).getMonthValue(), ldt.plusWeeks(1).getDayOfMonth(),                 
                    DateTimeParser.DefaultLocalTime.getHour(),
                    DateTimeParser.DefaultLocalTime.getMinute()),
                dateTimeParser.parseDateTime("next Friday").orElse(null)
        );         
    }
    
    @Test
    public void parseDateTime_realtiveDays()  {
        dateTimeParser.parseDateTime("10 Jan 2016 10am");
        
        assertEquals(
                LocalDateTime.of(now.plusDays(2).getYear(), now.plusDays(2).getMonthValue(), now.plusDays(2).getDayOfMonth(),                 
                    DateTimeParser.DefaultLocalTime.getHour(),
                    DateTimeParser.DefaultLocalTime.getMinute()),
                dateTimeParser.parseDateTime("2 days later").orElse(null)
        );   
    }

}
