package seedu.commando.logic.parser;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import javafx.util.Pair;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@@author A0139697H

/**
 * Parses datetimes with help of {@link com.joestelmach.natty.Parser}
 */
public class DateTimeParser {
    public static final LocalTime MorningLocalTime = LocalTime.of(8, 0);
    public static final LocalTime AfternoonLocalTime = LocalTime.of(12, 0);
    public static final LocalTime EveningLocalTime = LocalTime.of(19, 0);
    public static final LocalTime NightLocalTime = LocalTime.of(21, 0);

    private static final String MonthWordRegexString = "January|Feburary|March|April|June|July|August|September|October|November|December|" +
        "Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec";
    private static final String DayWordRegexString = "Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|Mon|Tue|Tues|Wed|Thu|Thur|Thurs|Fri|Sat|Sun";
    private static final String YearRegexString = "(?<year>\\d{4}|\\d{2})";
    private static final String TwoDigitYearRegexString = "\\d{2}$";
    private static final String MonthRegexString = "(0?[1-9])|10|11|12";
    private static final String DayOfMonthRegexString = "([12]\\d)|([3][01])|(0?[1-9])";
    private static final String Hours24RegexString = "(1\\d)|20|21|22|23|(0?\\d)";
    private static final String Hours12RegexString = "11|12|(0?[1-9])";
    private static final String MinutesRegexString = "([1234]\\d)|(5[0-9])|(0?\\d)";

    private static final String DateWithSlashesRegexString = "(?<day>" + DayOfMonthRegexString + ")\\/(?<month>" + MonthRegexString + ")(\\/" + YearRegexString + ")?";
    private static final String DateWithMonthWordRegexString = "((" + DayOfMonthRegexString + ")(th|rd|st|nd)?)\\s+" +
        "(" + MonthWordRegexString + ")(\\s+" + YearRegexString + ")?";
    private static final String DateWithMonthWordReversedRegexString = "(" + MonthWordRegexString + ")\\s+" +
        "(" + DayOfMonthRegexString + ")(th|rd|st|nd)?(\\s+" + YearRegexString + ")?";
    private static final String DateWithDayWordRegexString = "((this|coming|next)\\s+)?(" + DayWordRegexString + ")";
    private static final String DateWithLaterAgoRegexString = "((\\d+\\d)|([2-9]))\\s+(days|weeks|months|years)\\s+(later|ago)";
    private static final String DateWithLastNextRegexString = "(last|this|next)\\s+(week|month|year)";
    private static final String DatePresetsRegexString = "today|tomorrow|tmr|yesterday";

    private static final String Time24HourRegexString = "(" + Hours24RegexString + ")(\\.|:)(" + MinutesRegexString + ")";
    private static final String Time12HourRegexString = "(" + Hours12RegexString + ")(\\.|:)?(" + MinutesRegexString + ")?(am|pm)";
    private static final String TimePresetsNightRegexString = "(this\\s+)?(night|tonight)";
    private static final String TimePresetsRegexString = "(this\\s+)?(morning|afternoon|noon|evening|midnight)";
    private static final String TimeHourNotationRegexString = "(?<hours>" + Hours24RegexString + ")(?<minutes>" + MinutesRegexString + ")h";

    private static final String[] supportedDateRegexStrings = new String[]{
        DateWithSlashesRegexString,
        DateWithMonthWordRegexString,
        DateWithMonthWordReversedRegexString,
        DateWithDayWordRegexString,
        DateWithLaterAgoRegexString,
        DateWithLastNextRegexString,
        DatePresetsRegexString
    };

    private static final String[] supportedTimeRegexStrings = new String[]{
        Time24HourRegexString,
        TimeHourNotationRegexString,
        Time12HourRegexString,
        TimePresetsRegexString,
        TimePresetsNightRegexString
    };

    private static final String InitializationDateString = "today";

    private Parser parser = new Parser();
    private LocalDate lastLocalDate; // Date of last parsed datetime

    public DateTimeParser() {
        parseDateTime(InitializationDateString, LocalTime.MIDNIGHT);
    }

    /**
     * Resets any contextual info used based on history of parsing.
     */
    public void resetContext() {
        lastLocalDate = null;
    }

    /**
     * Parses an input string as a datetime period.
     *
     * @param input input string to be parsed
     * @return optional of pair of (start datetime, end datetime), empty if invalid datetime
     * @see #parseDateTime(String, LocalTime)
     */
    public Optional<Pair<LocalDateTime, LocalDateTime>> parseDateTimePeriod(String input) {
        // Get the datetime the input string represents
        Optional<LocalDateTime> localDateTime = parseDateTime(input);

        // If input string doesn't represent a datetime, return empty
        if (!localDateTime.isPresent()) {
            return Optional.empty();
        }

        String trimmedInput = input.trim();

        // Check if the input is trying to represent a period of > 1 day
        if (ifInputMatchesWeekPeriod(trimmedInput)) {
            return Optional.of(getWeekPeriod(localDateTime.get()));
        } else if (ifInputMatchesMonthPeriod(trimmedInput)) {
            return Optional.of(getMonthPeriod(localDateTime.get()));
        } else if (ifInputMatchesYearPeriod(trimmedInput)) {
            return Optional.of(getYearPeriod(localDateTime.get()));
        }

        return Optional.of(getDayPeriod(localDateTime.get()));
    }

    /**
     * {@link #parseDateTime(String, LocalTime)}, but with a default time of midnight
     */
    public Optional<LocalDateTime> parseDateTime(String input) {
        return parseDateTime(input, LocalTime.MIDNIGHT);
    }

    /**
     * Parses an input string as a datetime.
     * <p>
     * Works like {@link com.joestelmach.natty.Parser#parse(String)}, but:
     * - First passed through a stricter filter that regulates what is a valid datetime format.
     * - Converted to LocalDateTime
     * - If time is deemed as "inferred" (in natty), time = {@param defaultTime}
     * - If date is "inferred" and there were previous parses, date is set as that of last parsed datetime
     * - Seconds and nano-seconds field is always set to 0 (ignored)
     *
     * @param input       input string to parse
     * @param defaultTime default time to set if time is not explicit in {@param input}
     * @return optional of datetime, empty if invalid datetime
     */
    public Optional<LocalDateTime> parseDateTime(String input, LocalTime defaultTime) {
        Optional<String> preprocessedInput = preprocessInput(input.trim());

        // If pre-processing fails, return empty
        if (!preprocessedInput.isPresent()) {
            return Optional.empty();
        }

        // Let natty parse pre-processed input
        List<DateGroup> dateGroups = parser.parse(preprocessedInput.get());

        // Returns empty if natty didn't manage to parse it
        if (dateGroups.isEmpty()) {
           return Optional.empty();
        }

        DateGroup dateGroup = dateGroups.get(0);
        List<Date> dates = dateGroup.getDates();
        if (dates.isEmpty()) {
            return Optional.empty();
        }

        // Returns the first datetime parsed by natty
        return Optional.of(toLocalDateTime(dateGroup, dates.get(0), defaultTime));
    }

    /**
     * Converts natty's output {@code DateGroup} and {@code Date} to a {@code LocalDateTime}.
     */
    private LocalDateTime toLocalDateTime(DateGroup dateGroup, Date date, LocalTime defaultTime) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        ZoneId zoneId = ZoneOffset.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        // Check if date is inferred
        if (dateGroup.isDateInferred() && lastLocalDate != null) {
            localDateTime = LocalDateTime.of(lastLocalDate, localDateTime.toLocalTime());
        }

        // Check if time is inferred
        if (dateGroup.isTimeInferred()) {
            localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), defaultTime);
        }

        // Reset seconds
        localDateTime = localDateTime.withSecond(0).withNano(0);

        // Remember last date parsed
        lastLocalDate = localDateTime.toLocalDate();

        return localDateTime;
    }

    /**
     * Determines with regex whether {@param input} is a supported datetime.
     * Pre-processes it to before being parsed in natty
     *
     * @param input input string to pre-process, must be trimmed
     * @return optional of the datetime string after validation and pre-processing, empty if
     * deemed as a datetime format not supported by this class
     */
    private Optional<String> preprocessInput(String input) {
        if (input.isEmpty()) {
            return Optional.empty();
        }

        // Try to match a date
        Optional<Matcher> dateMatcher = findDateRegexMatch(input);

        String dateString = "";
        if (dateMatcher.isPresent()) {
            dateString = getDateFromMatcher(dateMatcher.get());

            // Extract out date string from text
            input = input.substring(dateMatcher.get().end()).trim();
        }

        // Try to match a time
        Optional<Matcher> timeMatcher = findTimeRegexMatch(input);

        String timeString = "";
        if (timeMatcher.isPresent()) {
            timeString = getTimeFromMatcher(timeMatcher.get());

            // Extract out date string from text
            input = input.substring(timeMatcher.get().end()).trim();
        }

        // If there is any characters left in text, invalid datetime
        if (!input.trim().isEmpty()) {
            return Optional.empty();
        } else {
            String dateTimeString = dateString + " " + timeString;
            dateTimeString = handleDateTimeWithLaterAgo(dateString, timeString, dateTimeString);

            return Optional.of(dateTimeString);
        }
    }

    private String getTimeFromMatcher(Matcher timeMatcher) {
        String timeString = timeMatcher.group().trim();
        timeString = handleTimeFormatHour(timeString, timeMatcher);
        timeString = handleTimeNight(timeString, timeMatcher);
        return timeString;
    }

    private String getDateFromMatcher(Matcher dateMatcher) {
        String dateString = dateMatcher.group().trim();
        dateString = handleDateWithSlashes(dateString, dateMatcher);
        dateString = handleDateTwoDigitYear(dateString, dateMatcher);
        return dateString;
    }

    private String handleDateTimeWithLaterAgo(String dateString, String timeString, String dateTimeString) {
        // Special case: if DateWithLaterRegexString is used,
        // swap date and time (for parsing in natty)
        if (dateString.matches(DateWithLaterAgoRegexString)) {
            return timeString + " " + dateString;
        } else {
            return dateTimeString;
        }
    }

    private String handleTimeNight(String timeString, Matcher matcher) {
        // Special case: for TimePresetsNightRegexString format,
        // Set time to 9pm
        if (ifMatcherUsesRegex(matcher, TimePresetsNightRegexString)) {
            return NightLocalTime.toString();
        } else {
            return timeString;
        }
    }

    private String handleTimeFormatHour(String timeString, Matcher matcher) {
        // Special case: for TimeHourNotationRegexString format,
        // Change to colon format (natty parses it wrongly when there is no year in the date)
        if (ifMatcherUsesRegex(matcher, TimeHourNotationRegexString)) {
            return matcher.group("hours") + ":" + matcher.group("minutes");
        } else {
            return timeString;
        }
    }

    private String handleDateTwoDigitYear(String dateString, Matcher matcher) {
        // Tweak for year: if it is a 2 digit year, change it to 4
        // Check if year string exists in datetime
        try {
            if (matcher.group("year") != null && matcher.group("year").length() == 2) {
                // Get string version of today's year
                String thisFullYear = String.valueOf(LocalDateTime.now().getYear());
                // If today's year is more than 2 digits, append the front (size - 2) digits
                if (thisFullYear.length() > 2) {
                    String fullYear = thisFullYear.substring(0, thisFullYear.length() - 2)
                        + matcher.group("year");
                    return dateString.replaceFirst(TwoDigitYearRegexString, fullYear);
                }
            }
        } catch (IllegalArgumentException exception) {
        } // no group with "year"

        return dateString;
    }

    private String handleDateWithSlashes(String dateString, Matcher matcher) {
        // Special case: for DateWithSlashesRegexString format,
        // Swap month and day
        if (ifMatcherUsesRegex(matcher, DateWithSlashesRegexString)) {
            return matcher.group("month") + "/" + matcher.group("day")
                + (matcher.group("year") != null ? ("/" + matcher.group("year")) : "");
        } else {
            return dateString;
        }
    }


    private Pair<LocalDateTime, LocalDateTime> getDayPeriod(LocalDateTime localDateTime) {
        // Return a single day until 2359h
        return new Pair<>(localDateTime, localDateTime.withHour(23).withMinute(59));
    }

    private Pair<LocalDateTime, LocalDateTime> getYearPeriod(LocalDateTime localDateTime) {
        // Return from 1st to last day of year 2359h
        return new Pair<>(
            localDateTime.withDayOfYear(1),
            localDateTime.plusYears(1).withDayOfYear(1).minusDays(1)
                .withHour(23).withMinute(59)
        );
    }

    private Pair<LocalDateTime, LocalDateTime> getMonthPeriod(LocalDateTime localDateTime) {
        // Return from 1st to last day of month 2359h
        return new Pair<>(
            localDateTime.withDayOfMonth(1),
            localDateTime.plusMonths(1).withDayOfMonth(1).minusDays(1)
                .withHour(23).withMinute(59)
        );
    }

    private Pair<LocalDateTime, LocalDateTime> getWeekPeriod(LocalDateTime localDateTime) {
        // Return from monday to sunday of that week 2359h
        return new Pair<>(
            localDateTime.with(DayOfWeek.MONDAY),
            localDateTime.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59)
        );
    }

    private boolean ifInputMatchesWeekPeriod(String input) {
        return (input.matches(DateWithLaterAgoRegexString)
            || input.matches(DateWithLastNextRegexString))
            && input.toLowerCase().contains("week");
    }

    private boolean ifInputMatchesMonthPeriod(String input) {
        return (input.matches(DateWithLaterAgoRegexString)
            || input.matches(DateWithLastNextRegexString))
            && input.toLowerCase().contains("month");
    }

    private boolean ifInputMatchesYearPeriod(String input) {
        return (input.matches(DateWithLaterAgoRegexString)
            || input.matches(DateWithLastNextRegexString))
            && input.toLowerCase().contains("year");
    }

    private Optional<Matcher> findDateRegexMatch(String input) {
        for (String regexString : supportedDateRegexStrings) {
            Matcher matcher = Pattern.compile(prepareRegexString(regexString), Pattern.CASE_INSENSITIVE).matcher(input);

            // If matched from the start
            if (matcher.find() && matcher.start() == 0) {
                return Optional.of(matcher);
            }
        }

        return Optional.empty();
    }

    private Optional<Matcher> findTimeRegexMatch(String input) {
        for (String regexString : supportedTimeRegexStrings) {
            Matcher matcher = Pattern.compile(prepareRegexString(regexString), Pattern.CASE_INSENSITIVE).matcher(input);

            // If matched from the start
            if (matcher.find() && matcher.start() == 0) {
                return Optional.of(matcher);
            }
        }

        return Optional.empty();
    }

    private String prepareRegexString(String regexString) {
        // regex + (space or end of string)
        return regexString + "(\\s+|$)";
    }

    private boolean ifMatcherUsesRegex(Matcher matcher, String regexString) {
        return matcher.pattern().pattern().equals(prepareRegexString(regexString));
    }
}