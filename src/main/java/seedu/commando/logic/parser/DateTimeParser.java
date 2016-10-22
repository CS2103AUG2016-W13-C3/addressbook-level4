package seedu.commando.logic.parser;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Parses datetimes with help of {@link com.joestelmach.natty.Parser}
 */
public class DateTimeParser {
    public static final LocalTime DefaultLocalTime = LocalTime.NOON;
    public static final LocalTime MorningLocalTime = LocalTime.of(8, 0);
    public static final LocalTime AfternoonLocalTime = LocalTime.of(12, 0);
    public static final LocalTime EveningLocalTime = LocalTime.of(19, 0);
    public static final LocalTime NightLocalTime = LocalTime.of(21, 0);

    private static final String MonthWordRegexString = "January|Feburary|March|April|June|July|August|September|October|November|December|" +
        "Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec";
    private static final String DayWordRegexString = "Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|Mon|Tue|Tues|Wed|Thu|Thur|Thurs|Fri|Sat|Sun";
    private static final String YearRegexString = "(?<year>\\d{4}|\\d{2})";
    private static final String TwoDigitYearRegexString = "\\d{2}$";

    private static final String DateWithSlashesRegexString = "(?<month>\\d{1,2})\\/(?<day>\\d{1,2})(\\/" + YearRegexString + ")?";
    private static final String DateWithMonthWordRegexString = "(\\d{1,2})((th|rd|st|nd)?)\\s+" +
        "(" + MonthWordRegexString + ")(\\s+" + YearRegexString + ")?";
    private static final String DateWithMonthWordReversedRegexString = "(" + MonthWordRegexString + ")\\s+" +
        "(\\d{1,2})(th|rd|st|nd)?(\\s+" + YearRegexString + ")?";
    private static final String DateWithDayWordRegexString = "((coming|next)\\s+)?(" + DayWordRegexString + ")";
    private static final String DateWithLaterAgoRegexString = "((\\d+\\d)|([2-9]))\\s+(days|weeks|months|years)\\s+(later|ago)";
    private static final String DateWithNextRegexString = "next (week|month|year)";
    private static final String TimeNightRegexString = "(this\\s+)?(night|tonight)";

    private static final String[] supportedDateRegexStrings = new String[] {
        DateWithSlashesRegexString,
        DateWithMonthWordRegexString,
        DateWithMonthWordReversedRegexString,
        DateWithDayWordRegexString,
        DateWithLaterAgoRegexString,
        DateWithNextRegexString,
        "today", "tomorrow|tmr", "yesterday"
    };

    private static final String[] supportedTimeRegexStrings = new String[] {
        "(\\d{2})(\\.|:)(\\d{2})",
        "(\\d{2}):?(\\d{2})h",
        "(\\d{1,2})(\\.|:)?(\\d{2})?(am|pm)",
        "(this\\s+)?(morning|afternoon|noon|evening|midnight)",
        TimeNightRegexString
    };

    private Parser parser = new Parser();
    private LocalDate lastLocalDate; // Date of last parsed datetime

    /**
     * Resets any contextual info used based on history of parsing
     */
    public void resetContext() {
        lastLocalDate = null;
    }

    /**
     * Gets the first datetime encountered in text
     * Works based on {@link com.joestelmach.natty.Parser}, but:
     * - Converted to `LocalDateTime`
     * - If time is deemed as "inferred" (in natty), time = {@link #DefaultLocalTime}
     * - If date is "inferred" and there were previous parses, date = {@link #lastLocalDate}
     * - Seconds field is always set to 0 (ignored)
     */
    public Optional<LocalDateTime> parseDateTime(String input) {
        Optional<String> preprocessedInput = preprocessInput(input);

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
                return Optional.of(toLocalDateTime(dateGroup, dates.get(0)));
            }
        }

        return Optional.empty();
    }

    private LocalDateTime toLocalDateTime(DateGroup dateGroup, Date date) {
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
                DefaultLocalTime
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
      * Returns Optional.empty() if not supported
     */
    private Optional<String> preprocessInput(String input) {
        input = input.trim();

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

                // Special case: for DateWithSlashesRegexString format,
                // Swap month and day
                if (regexString.equals(DateWithSlashesRegexString)) {
                    dateString = matcher.group("day") + "/" + matcher.group("month") + "/" + matcher.group("year");
                } else {
                    dateString = matcher.group().trim();
                }

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
                            dateString = dateString.replaceFirst(TwoDigitYearRegexString, fullYear);
                        }
                    }
                } catch (IllegalArgumentException exception) { } // no group with "year"

                // Extract out date string from text
                input = input.substring(matcher.end()).trim();

                break; // exit loop
            }
        }

        // Try to match a time
        String timeString = "";
        for (String regexString : supportedTimeRegexStrings) {
            // Try to match regex + (space or end of string)
            Matcher matcher = Pattern.compile(regexString + "(\\s|$)").matcher(input);

            // If matched from the start
            if (matcher.find() && matcher.start() == 0) {

                // Special case: for TimeNightRegexString format,
                // Set time to 9pm
                if (regexString.equals(TimeNightRegexString)) {
                    timeString = NightLocalTime.toString();
                    System.out.println(NightLocalTime);
                } else {
                    timeString = matcher.group().trim();
                }

                input = input.substring(matcher.end()).trim();

                break; // exit loop
            }
        }

        // If there is any characters left in text, invalid datetime
        if (!input.isEmpty()) {
            return Optional.empty();
        } else {

            // Special case: if DateWithLaterRegexString is used,
            // swap date and time (for parsing in natty)
            if (dateString.matches(DateWithLaterAgoRegexString)) {
                return Optional.of(timeString + " " + dateString);
            } else {
                return Optional.of(dateString + " " + timeString);
            }
        }
    }
}