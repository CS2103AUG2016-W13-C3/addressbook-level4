package seedu.commando.logic.parser;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import javafx.util.Pair;

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
    private static final String DateWithDayWordRegexString = "((coming|next)\\s+)?(" + DayWordRegexString + ")";
    private static final String DateWithLaterAgoRegexString = "((\\d+\\d)|([2-9]))\\s+(days|weeks|months|years)\\s+(later|ago)";
    private static final String DateWithLastNextRegexString = "(last|this|next)\\s+(week|month|year)";
    private static final String TimeNightRegexString = "(this\\s+)?(night|tonight)";
    private static final String TimeHourRegexString = "(?<hours>" + Hours24RegexString + ")(?<minutes>" + MinutesRegexString + ")h";

    private static final String[] supportedDateRegexStrings = new String[] {
        DateWithSlashesRegexString,
        DateWithMonthWordRegexString,
        DateWithMonthWordReversedRegexString,
        DateWithDayWordRegexString,
        DateWithLaterAgoRegexString,
        DateWithLastNextRegexString,
        "today", "tomorrow|tmr", "yesterday"
    };

    private static final String[] supportedTimeRegexStrings = new String[] {
        "(" + Hours24RegexString + ")(\\.|:)(" + MinutesRegexString + ")",
        TimeHourRegexString,
        "(" + Hours12RegexString + ")(\\.|:)?(" + MinutesRegexString + ")?(am|pm)",
        "(this\\s+)?(morning|afternoon|noon|evening|midnight)",
        TimeNightRegexString
    };
    private static final String InitializationDateString = "today";

    private Parser parser = new Parser();
    private LocalDate lastLocalDate; // Date of last parsed datetime

    public DateTimeParser() {
        parseDateTime(InitializationDateString, LocalTime.MIDNIGHT);
    }

    /**
     * Resets any contextual info used based on history of parsing
     */
    public void resetContext() {
        lastLocalDate = null;
    }

    /**
     * @see #parseDateTime(String, LocalTime), but additional processing to
     *   interpret the datetime as a period.
     * @return pair of (start datetime, end datetime)
     */
    public Optional<Pair<LocalDateTime, LocalDateTime>> parseDateTimePeriod(String input) {
        Optional<LocalDateTime> localDateTime = parseDateTime(input);

        if (!localDateTime.isPresent()) {
            return Optional.empty();
        }

        String trimmedInput = input.trim();

        // Check if the input is trying to represent a period of > 1 day
        if (trimmedInput.matches(DateWithLaterAgoRegexString)
            || trimmedInput.matches(DateWithLastNextRegexString)) {
            if (trimmedInput.contains("week")) {
                // Return from monday to sunday of that week 2359h
                return Optional.of(
                    new Pair<>(
                        localDateTime.get().with(DayOfWeek.MONDAY),
                        localDateTime.get().with(DayOfWeek.SUNDAY).withHour(23).withMinute(59)
                    )
                );
            } else if (trimmedInput.contains("month")) {
                // Return from 1st to last day of month 2359h
                return Optional.of(
                    new Pair<>(
                        localDateTime.get().withDayOfMonth(1),
                        localDateTime.get().plusMonths(1).withDayOfMonth(1).minusDays(1)
                            .withHour(23).withMinute(59)
                    )
                );
            } else if (trimmedInput.contains("year")) {
                // Return from 1st to last day of year 2359h
                return Optional.of(
                    new Pair<>(
                        localDateTime.get().withDayOfYear(1),
                        localDateTime.get().plusYears(1).withDayOfYear(1).minusDays(1)
                            .withHour(23).withMinute(59)
                    )
                );
            }
        }

        // If not, treat it as on a single day until 2359h
        return Optional.of(new Pair<>(localDateTime.get(), localDateTime.get().withHour(23).withMinute(59)));
    }

    /**
     * @see #parseDateTime(String, LocalTime), but with a default time of
     *   midnight
     */
    public Optional<LocalDateTime> parseDateTime(String input) {
        return parseDateTime(input, LocalTime.MIDNIGHT);
    }

    /**
     * Gets the first datetime encountered in text
     * Works based on {@link com.joestelmach.natty.Parser}, but:
     * - Converted to `LocalDateTime`
     * - If time is deemed as "inferred" (in natty), time = {@param defaultTime}
     * - If date is "inferred" and there were previous parses, date = {@link #lastLocalDate}
     * - Seconds and nano-seconds field is always set to 0 (ignored)
     */
    public Optional<LocalDateTime> parseDateTime(String input, LocalTime defaultTime) {
        Optional<String> preprocessedInput = preprocessInput(input.trim());

        // If preprocessing fails, return empty
        if (!preprocessedInput.isPresent()) {
            return Optional.empty();
        }

        List<DateGroup> dateGroups = parser.parse(preprocessedInput.get());

        // Return first date parsed
        if (!dateGroups.isEmpty()) {
            DateGroup dateGroup = dateGroups.get(0);
            List<Date> dates = dateGroup.getDates();
            if (!dates.isEmpty()) {
                return Optional.of(toLocalDateTime(dateGroup, dates.get(0), defaultTime));
            }
        }

        return Optional.empty();
    }

    private LocalDateTime toLocalDateTime(DateGroup dateGroup, Date date, LocalTime defaultTime) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        ZoneId zoneId = ZoneOffset.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        // Check if date is inferred
        if (dateGroup.isDateInferred() && lastLocalDate != null) {
            localDateTime = LocalDateTime.of(
                lastLocalDate,
                localDateTime.toLocalTime()
            );
        }

        // Check if time is inferred
        if (dateGroup.isTimeInferred()) {
            localDateTime = LocalDateTime.of(
                localDateTime.toLocalDate(),
                defaultTime
            );
        }

        // Reset seconds
        localDateTime = localDateTime.withSecond(0);

        // Remember last date parsed
        lastLocalDate = localDateTime.toLocalDate();

        return localDateTime;
    }

    /**
     * Determines with regex whether this is a supported datetime
     * Preprocesses it to before being parsed in natty
     * Assumes input is trimmed.
     * Returns empty if not supported
     */
    private Optional<String> preprocessInput(String input) {
        if (input.isEmpty()) {
            return Optional.empty();
        }

        // Try to match a date
        String dateString = "";
        for (String regexString : supportedDateRegexStrings) {
            // Try to match regex + (space or end of string)
            Matcher matcher = Pattern.compile(regexString + "(\\s|$)", Pattern.CASE_INSENSITIVE).matcher(input);

            // If matched from the start
            if (matcher.find() && matcher.start() == 0) {
                dateString = matcher.group().trim();
                dateString = handleDateWithSlashes(dateString, regexString, matcher);
                dateString = handleDateTwoDigitYear(dateString, matcher);

                // Extract out date string from text
                input = input.substring(matcher.end()).trim();

                break;
            }
        }

        // Try to match a time
        String timeString = "";
        for (String regexString : supportedTimeRegexStrings) {
            // Try to match regex + (space or end of string)
            Matcher matcher = Pattern.compile(regexString + "(\\s|$)").matcher(input);

            // If matched from the start
            if (matcher.find() && matcher.start() == 0) {
                timeString = matcher.group().trim();
                timeString = handleTimeFormatHour(timeString, regexString, matcher);
                timeString = handleTimeNight(timeString, regexString);

                input = input.substring(matcher.end()).trim();
                break;
            }
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

    private String handleDateTimeWithLaterAgo(String dateString, String timeString, String dateTimeString) {
        // Special case: if DateWithLaterRegexString is used,
        // swap date and time (for parsing in natty)
        if (dateString.matches(DateWithLaterAgoRegexString)) {
            return timeString + " " + dateString;
        } else {
            return dateTimeString;
        }
    }

    private String handleTimeNight(String timeString, String regexString) {
        // Special case: for TimeNightRegexString format,
        // Set time to 9pm
        if (regexString.equals(TimeNightRegexString)) {
            return NightLocalTime.toString();
        } else {
            return timeString;
        }
    }

    private String handleTimeFormatHour(String timeString, String regexString, Matcher matcher) {
        // Special case: for TimeHourRegexString format,
        // Change to colon format (natty parses it wrongly when there is no year in the date)
        if (regexString.equals(TimeHourRegexString)) {
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
        } catch (IllegalArgumentException exception) { } // no group with "year"

        return dateString;
    }

    private String handleDateWithSlashes(String dateString, String regexString, Matcher matcher) {
        // Special case: for DateWithSlashesRegexString format,
        // Swap month and day
        if (regexString.equals(DateWithSlashesRegexString)) {
            return matcher.group("month") + "/" + matcher.group("day")
            +  (matcher.group("year") != null ? ("/" + matcher.group("year")) : "");
        } else {
            return dateString;
        }
    }
}