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
    private static final String FullYearRegexString = "\\d{4}";
    private static final String TwoDigitYearRegexString = "\\d{2}";
    private static final String MonthRegexString = "(0?[1-9])|10|11|12";
    private static final String DayOfMonthRegexString = "([12]\\d)|([3][01])|(0?[1-9])";
    private static final String Hours24RegexString = "(1\\d)|20|21|22|23|(0?\\d)";
    private static final String Hours12RegexString = "10|11|12|(0?[1-9])";
    private static final String MinutesRegexString = "([1234]\\d)|(5[0-9])|(0\\d)";

    private static final String YearRegexString = "(?<year>(" + FullYearRegexString + ")|(" + TwoDigitYearRegexString + "))";

    private static final String DateWithSlashesRegexString = "(?<day>" + DayOfMonthRegexString + ")\\/(?<month>" + MonthRegexString + ")(\\/" + YearRegexString + ")?";
    private static final String DateWithMonthWordAndDayRegexString = "((" + DayOfMonthRegexString + ")(th|rd|st|nd)?)\\s+" +
        "(" + MonthWordRegexString + ")(\\s+" + YearRegexString + ")?";
    private static final String DateWithMonthWordReversedRegexString = "(" + MonthWordRegexString + ")\\s+" +
        "(" + DayOfMonthRegexString + ")(th|rd|st|nd)?(\\s+" + YearRegexString + ")?";
    private static final String DateWithDayWordRegexString = "((this|last|coming|next)\\s+)?(" + DayWordRegexString + ")";
    private static final String DateWithMonthWordAndYearRegexString = "(" + MonthWordRegexString + ")\\s+(" + FullYearRegexString + ")";
    private static final String DateWithMonthWordRegexString = "((this|last|coming||next)\\s+)?(" + MonthWordRegexString + ")";
    private static final String DateWithLaterAgoRegexString = "((\\d+\\d)|([2-9]))\\s+(days|weeks|months|years)\\s+(later|ago)";
    private static final String DateWithLastNextRegexString = "(this|last|this|next)\\s+(week|month|year)";
    private static final String DatePresetsRegexString = "today|tomorrow|tmr|yesterday";

    private static final String Time24HourRegexString = "(" + Hours24RegexString + ")(\\.|:)(" + MinutesRegexString + ")";
    private static final String Time12HourRegexString = "(" + Hours12RegexString + ")(\\.|:)?(" + MinutesRegexString + ")?(am|pm)";
    private static final String TimePresetsNightRegexString = "(this\\s+)?(night|tonight)";
    private static final String TimePresetsRegexString = "(this\\s+)?(morning|afternoon|noon|evening|midnight)";
    private static final String TimeHourNotationRegexString = "(?<hours>" + Hours24RegexString + ")(?<minutes>" + MinutesRegexString + ")h";

    private static final String[] supportedDateRegexStrings = new String[]{
        DateWithSlashesRegexString,
        DateWithMonthWordAndDayRegexString,
        DateWithMonthWordReversedRegexString,
        DateWithMonthWordAndYearRegexString,
        DateWithDayWordRegexString,
        DateWithMonthWordRegexString,
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
            // Return from monday to sunday of that week 2359h
            return Optional.of(new Pair<>(
                getWeekStartDateTime(localDateTime.get()),
                getWeekEndDateTime(localDateTime.get())
            ));
        } else if (ifInputMatchesMonthPeriod(trimmedInput)) {
            // Return from 1st to last day of month 2359h
            return Optional.of(new Pair<>(
                getMonthStartDateTime(localDateTime.get()),
                getMonthEndDateTime(localDateTime.get())
            ));
        } else if (ifInputMatchesYearPeriod(trimmedInput)) {
            // Return from 1st to last day of year 2359h
            return Optional.of(new Pair<>(
                getYearStartDateTime(localDateTime.get()),
                getYearEndDateTime(localDateTime.get())
            ));
        }

        // Return a single day until 2359h
        return Optional.of(new Pair<>(
            localDateTime.get(),
            getDayEndDateTime(localDateTime.get())
        ));
    }

    /**
     * Parses an input string as an end datetime.
     *
     * @param input input string to be parsed
     * @return optional of datetime, empty if invalid datetime
     * @see #parseDateTime(String, LocalTime)
     */
    public Optional<LocalDateTime> parseEndDateTime(String input) {
        // Get the datetime the input string represents
        Optional<LocalDateTime> localDateTime = parseDateTime(input, LocalTime.of(23, 59));

        // If input string doesn't represent a datetime, return empty
        if (!localDateTime.isPresent()) {
            return Optional.empty();
        }

        String trimmedInput = input.trim();

        // Check if the input is trying to represent a period of > 1 day
        if (ifInputMatchesWeekPeriod(trimmedInput)) {
            // Return sunday of that week 2359h
            return Optional.of(getWeekEndDateTime(localDateTime.get()));
        } else if (ifInputMatchesMonthPeriod(trimmedInput)) {
            // Return from last day of month 2359h
            return Optional.of(getMonthEndDateTime(localDateTime.get()));
        } else if (ifInputMatchesYearPeriod(trimmedInput)) {
            // Return from 1st to last day of year 2359h
            return Optional.of(getYearEndDateTime(localDateTime.get()));
        }

        // Return that datetime
        return Optional.of(localDateTime.get());
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

        localDateTime = handleInferredDate(dateGroup, localDateTime);
        localDateTime = handleInferredTime(dateGroup, defaultTime, localDateTime);
        localDateTime = resetSeconds(localDateTime);

        // Remember last date parsed
        lastLocalDate = localDateTime.toLocalDate();

        return localDateTime;
    }

    private LocalDateTime resetSeconds(LocalDateTime localDateTime) {
        // Reset seconds
        localDateTime = localDateTime.withSecond(0).withNano(0);
        return localDateTime;
    }

    private LocalDateTime handleInferredTime(DateGroup dateGroup, LocalTime defaultTime, LocalDateTime localDateTime) {
        // Check if time is inferred
        if (dateGroup.isTimeInferred()) {
            localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), defaultTime);
        }
        return localDateTime;
    }

    private LocalDateTime handleInferredDate(DateGroup dateGroup, LocalDateTime localDateTime) {
        // Check if date is inferred
        if (dateGroup.isDateInferred() && lastLocalDate != null) {
            localDateTime = LocalDateTime.of(lastLocalDate, localDateTime.toLocalTime());
        }

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
            String dateTimeString = getCombinedDateTimeString(dateString, timeString);
            return Optional.of(dateTimeString);
        }
    }

    private String getCombinedDateTimeString(String dateString, String timeString) {
        String dateTimeString = dateString + " " + timeString;
        dateTimeString = handleDateTimeWithLaterAgo(dateString, timeString, dateTimeString);
        return dateTimeString;
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
        dateString = handleDateWithMonthWord(dateString, dateMatcher);
        return dateString;
    }

    private String handleDateWithMonthWord(String dateString, Matcher dateMatcher) {
        // Special case: for DateWithMonthWordRegexString format,
        // If its just plain month, add an "coming"
        if (ifMatcherUsesRegex(dateMatcher, DateWithMonthWordRegexString)) {
            if (ifMatches(dateString.trim(), MonthWordRegexString)) {
                return "coming " + dateString;
            }
        }

        return dateString;
    }

    private String handleDateTimeWithLaterAgo(String dateString, String timeString, String dateTimeString) {
        // Special case: if DateWithLaterRegexString is used,
        // swap date and time (for parsing in natty)
        if (ifMatches(dateString, DateWithLaterAgoRegexString)) {
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
                    return dateString.replaceFirst(TwoDigitYearRegexString + "$", fullYear);
                }
            }
        } catch (IllegalArgumentException exception) {
            // no group with "year", it's okay
        }

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


    private LocalDateTime getDayEndDateTime(LocalDateTime localDateTime) {
        return localDateTime.withHour(23).withMinute(59);
    }

    private LocalDateTime getYearStartDateTime(LocalDateTime localDateTime) {
        return localDateTime.withDayOfYear(1);
    }

    private LocalDateTime getYearEndDateTime(LocalDateTime localDateTime) {
        return localDateTime.plusYears(1).withDayOfYear(1).minusDays(1)
            .withHour(23).withMinute(59);
    }

    private LocalDateTime getMonthStartDateTime(LocalDateTime localDateTime) {
        return localDateTime.withDayOfMonth(1);
    }

    private LocalDateTime getMonthEndDateTime(LocalDateTime localDateTime) {
        return localDateTime.plusMonths(1).withDayOfMonth(1).minusDays(1)
            .withHour(23).withMinute(59);
    }

    private LocalDateTime getWeekStartDateTime(LocalDateTime localDateTime) {
        return localDateTime.with(DayOfWeek.MONDAY);
    }

    private LocalDateTime getWeekEndDateTime(LocalDateTime localDateTime) {
        return localDateTime.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59);
    }

    private boolean ifInputMatchesWeekPeriod(String input) {
        return (ifMatches(input, DateWithLaterAgoRegexString)
            || ifMatches(input, DateWithLastNextRegexString))
            && input.toLowerCase().contains("week");
    }

    private boolean ifInputMatchesMonthPeriod(String input) {
        // last/next month or x months ago/later
        if ((ifMatches(input, DateWithLaterAgoRegexString)
            || ifMatches(input, DateWithLastNextRegexString))
            && input.toLowerCase().contains("month")) {
            return true;
        }

        // month word only or month word + year
        if (ifMatches(input, DateWithMonthWordRegexString)
            || ifMatches(input, DateWithMonthWordAndYearRegexString)) {
            return true;
        }

        return false;
    }

    private boolean ifInputMatchesYearPeriod(String input) {
        return (ifMatches(input, DateWithLaterAgoRegexString)
            || ifMatches(input, DateWithLastNextRegexString))
            && input.toLowerCase().contains("year");
    }



    private Optional<Matcher> findDateRegexMatch(String input) {
        for (String regexString : supportedDateRegexStrings) {
            Matcher matcher = preparePattern(prepareRegexString(regexString)).matcher(input);

            // If matched from the start
            if (matcher.find() && matcher.start() == 0) {
                return Optional.of(matcher);
            }
        }

        return Optional.empty();
    }

    private Optional<Matcher> findTimeRegexMatch(String input) {
        for (String regexString : supportedTimeRegexStrings) {
            Matcher matcher = preparePattern(prepareRegexString(regexString)).matcher(input);

            // If matched from the start
            if (matcher.find() && matcher.start() == 0) {
                return Optional.of(matcher);
            }
        }

        return Optional.empty();
    }

    private boolean ifMatches(String input, String regexString) {
        return preparePattern("^" + regexString + "$").matcher(input).find();
    }

    private Pattern preparePattern(String regexString) {
        return Pattern.compile(regexString, Pattern.CASE_INSENSITIVE);
    }

    private String prepareRegexString(String regexString) {
        // regex + (space or end of string)
        return regexString + "(\\s+|$)";
    }

    private boolean ifMatcherUsesRegex(Matcher matcher, String regexString) {
        return matcher.pattern().pattern().equals(prepareRegexString(regexString));
    }
}