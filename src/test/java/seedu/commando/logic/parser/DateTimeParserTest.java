package seedu.commando.logic.parser;

import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

//@@author A0139697H
public class DateTimeParserTest {
    private DateTimeParser dateTimeParser;
    private LocalDateTime now = LocalDateTime.now();

    @Before
    public void setup() {
        dateTimeParser = new DateTimeParser();
    }

    @Test
    public void parseDateTime_emptyString_empty() {
        assertFalse(dateTimeParser.parseDateTime("").isPresent());
    }

    @Test
    public void parseDateTime_ddMMMyyyy_colonHHmm_parsed() {
        assertEquals(
            LocalDateTime.of(2016, 2, 28, 23, 59),
            dateTimeParser.parseDateTime("28 Feb 2016 23:59").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ddMMM_HHmmPm_parsed() {
        assertEquals(
            LocalDateTime.of(now.getYear(), 2, 28, 21, 30),
            dateTimeParser.parseDateTime("28 Feb 9.30pm").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ddMMMyyh_parsed() {
        assertEquals(
            LocalDateTime.of(2016, 2, 28, 23, 59),
            dateTimeParser.parseDateTime("28 Feb 16 2359h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ddMMM_parsed() {
        assertEquals(
            LocalDateTime.of(now.getYear(), 2, 28,
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("28 Feb").orElse(null)
        );
    }

    @Test
    public void parseDateTime_dateWithSlashes_parsed() {
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
    public void parseDateTime_HHmmh_parsed() {
        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 19, 19),
            dateTimeParser.parseDateTime("1919h").orElse(null)
        );
    }

    // Underlying natty doesn't parse this properly
    @Test
    public void parseDateTime_DateWithoutYearAndHHmmh_parsed() {
        assertEquals(
            LocalDateTime.of(now.getYear(), 10, 31, 19, 19),
            dateTimeParser.parseDateTime("31 Oct 1919h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_ordinalFullMonthHHAm_parsed() {
        assertEquals(
            LocalDateTime.of(now.getYear(), 1, 1, 11, 0),
            dateTimeParser.parseDateTime("1st January 11am").orElse(null)
        );
    }

    @Test
    public void parseDateTime_DateWithSlashesHHmmh_parsed() {
        assertEquals(
            LocalDateTime.of(2016, 11, 14, 20, 1),
            dateTimeParser.parseDateTime("14/11/2016 2001h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_DateWithSlashesNoYearHHmmWithColon_parsed() {
        assertEquals(
            LocalDateTime.of(2016, 11, 26, 13, 0),
            dateTimeParser.parseDateTime("26/11 13:00").orElse(null)
        );
    }

    @Test
    public void parseDateTime_presets_parsed() {
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

        assertEquals(
            LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("tmr").orElse(null)
        );

        // yesterday
        LocalDateTime yesterday = now.minusDays(1);
        assertEquals(
            LocalDateTime.of(yesterday.getYear(), yesterday.getMonth(), yesterday.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("yesterday").orElse(null)
        );
    }

    @Test
    public void parseDateTime_extraSpaces_parsed() {
        assertEquals(
            LocalDateTime.of(2016, 4, 22, 8, 0),
            dateTimeParser.parseDateTime("22     April   2016  0800h ").orElse(null)
        );
    }

    @Test
    public void parseDateTime_reversedMonthWord_parsed() {
        assertEquals(
            LocalDateTime.of(2011, 6, 21, 13, 13),
            dateTimeParser.parseDateTime("June 21 2011 1.13pm").orElse(null)
        );
    }

    @Test
    public void parseDateTime_inferDateFromPreviousParse_inferred() {
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
    public void parseDateTime_invalidDates_empty() {
        assertFalse(dateTimeParser.parseDateTime("No date").isPresent());

        // Use of Boundary value analysis
        // Close to date with slashes format
        assertFalse(dateTimeParser.parseDateTime("10/2016").isPresent());
        assertFalse(dateTimeParser.parseDateTime("1/2/3/4").isPresent());

        // Close to date with month word formats
        assertFalse(dateTimeParser.parseDateTime("1 May be").isPresent());
        assertFalse(dateTimeParser.parseDateTime("10 Marching").isPresent());
        assertFalse(dateTimeParser.parseDateTime("Feb20").isPresent());

        // Close to relative date formats
        assertFalse(dateTimeParser.parseDateTime("wed thurs").isPresent());
        assertFalse(dateTimeParser.parseDateTime("coming dayum").isPresent());
        assertFalse(dateTimeParser.parseDateTime("next weekday").isPresent());

        // No such dates
        assertFalse(dateTimeParser.parseDateTime("32 Feb").isPresent());
        assertFalse(dateTimeParser.parseDateTime("-1/1/2016").isPresent());
    }

    @Test
    public void parseDateTime_invalidTimes_empty() {
        assertFalse(dateTimeParser.parseDateTime("0").isPresent());
        assertFalse(dateTimeParser.parseDateTime("31").isPresent());

        // Use of Boundary value analysis
        // Close to "pm" format
        assertFalse(dateTimeParser.parseDateTime("10cm").isPresent());

        // Close to "hhmm"h format
        assertFalse(dateTimeParser.parseDateTime("2359").isPresent());
        assertFalse(dateTimeParser.parseDateTime("h").isPresent());

        // Close to colon format
        assertFalse(dateTimeParser.parseDateTime("23::10").isPresent());
        assertFalse(dateTimeParser.parseDateTime("23:110").isPresent());

        // No such times
        assertFalse(dateTimeParser.parseDateTime("10059h").isPresent());
        assertFalse(dateTimeParser.parseDateTime("44:44").isPresent());
    }

    @Test
    public void resetContext_afterParseDateTime_noInferrence() {
        dateTimeParser.parseDateTime("10 Jan 2016 10am");

        dateTimeParser.resetContext();

        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 0),
            dateTimeParser.parseDateTime("11pm").orElse(null)
        );
    }

    //@@author A0122001M

    @Test
    public void parseDateTime_relativeDayOfWeek_parsed() {
        LocalDateTime ldt = now.plusDays(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

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

        LocalDateTime nextLdt = now.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        assertEquals(
            LocalDateTime.of(nextLdt.getYear(), nextLdt.getMonthValue(), nextLdt.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("next sun").orElse(null)
        );

        // With time
        assertEquals(
            LocalDateTime.of(nextLdt.getYear(), nextLdt.getMonthValue(), nextLdt.getDayOfMonth(),
                13, 13),
            dateTimeParser.parseDateTime("next sun 1313h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_relativeDaysLaterAgo_parsed() {
        LocalDateTime plus12Days = now.plusDays(12);
        assertEquals(
            LocalDateTime.of(plus12Days.getYear(), plus12Days.getMonthValue(), plus12Days.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("12 days later").orElse(null)
        );

        LocalDateTime minus8Days = now.minusDays(8);
        assertEquals(
            LocalDateTime.of(minus8Days.getYear(), minus8Days.getMonthValue(), minus8Days.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("8 days ago").orElse(null)
        );

        // 0 and 1 days invalid
        assertFalse(dateTimeParser.parseDateTime("1 day later").isPresent());
        assertFalse(dateTimeParser.parseDateTime("0 days later").isPresent());
        assertFalse(dateTimeParser.parseDateTime("0 days ago").isPresent());
    }

    @Test
    public void parseDateTime_relativeWeeksMonthsYearsLaterAgo_parsed() {
        LocalDateTime ldt = now.plusWeeks(2);
        assertEquals(
            LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("2 weeks later").orElse(null)
        );

        ldt = now.plusMonths(2);
        assertEquals(
            LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("2 months later").orElse(null)
        );

        ldt = now.minusYears(2);
        assertEquals(
            LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("2 years ago").orElse(null)
        );

        // 0 and 1 weeks invalid
        assertFalse(dateTimeParser.parseDateTime("1 week later").isPresent());
        assertFalse(dateTimeParser.parseDateTime("0 weeks later").isPresent());
        assertFalse(dateTimeParser.parseDateTime("0 weeks ago").isPresent());

        // With time
        ldt = now.plusWeeks(3);
        assertEquals(
            LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                10, 23),
            dateTimeParser.parseDateTime("3 weeks later 1023h").orElse(null)
        );
    }

    @Test
    public void parseDateTime_nextWeekMonthYear_parsed() {
        LocalDateTime ldt = now.plusWeeks(1);
        assertEquals(
            LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("next week").orElse(null)
        );

        ldt = now.plusMonths(1);
        assertEquals(
            LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("next month").orElse(null)
        );

        ldt = now.plusYears(1);
        assertEquals(
            LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                DateTimeParser.DefaultLocalTime.getHour(),
                DateTimeParser.DefaultLocalTime.getMinute()),
            dateTimeParser.parseDateTime("next year").orElse(null)
        );
    }

    @Test
    public void parseDateTime_timePresets_parsed() {
        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                DateTimeParser.MorningLocalTime.getHour(),
                DateTimeParser.MorningLocalTime.getMinute()),
            dateTimeParser.parseDateTime("morning").orElse(null)
        );

        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                DateTimeParser.AfternoonLocalTime.getHour(),
                DateTimeParser.AfternoonLocalTime.getMinute()),
            dateTimeParser.parseDateTime("this afternoon").orElse(null)
        );

        assertEquals(
            LocalDateTime.of(2016, 10, 10,
                DateTimeParser.EveningLocalTime.getHour(),
                DateTimeParser.EveningLocalTime.getMinute()),
            dateTimeParser.parseDateTime("10 Oct 2016 evening").orElse(null)
        );

        dateTimeParser.resetContext();

        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                DateTimeParser.NightLocalTime.getHour(),
                DateTimeParser.NightLocalTime.getMinute()),
            dateTimeParser.parseDateTime("night").orElse(null)
        );

        assertEquals(
            LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                0, 0),
            dateTimeParser.parseDateTime("midnight").orElse(null)
        );
    }
}
