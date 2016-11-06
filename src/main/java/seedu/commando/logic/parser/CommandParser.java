package seedu.commando.logic.parser;

import javafx.util.Pair;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.DueDate;
import seedu.commando.model.todo.Recurrence;
import seedu.commando.model.todo.Tag;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//@@author A0139697H

/**
 * In charge of parsing an input command string, piece-wise, with the help of
 * {@link DateTimeParser}.
 * <p>
 * Methods modify the input string by extracting appropriate parts of it,
 * and input is always kept trimmed.
 */
public class CommandParser {
    public static final String KEYWORD_DATERANGE_START = "from";
    public static final String KEYWORD_DATERANGE_END = "to";
    public static final String KEYWORD_DATERANGE_DATE = "on";
    public static final String KEYWORD_DUEDATE = "by";
    public static final String RECURRENCE_REGEX = "daily|weekly|monthly|yearly";
    public static final String TAG_PREFIX = "#";
    public static final String QUOTE_CHARACTER = "`";
    public static final String KEYWORD_OVERRIDE = "override";

    // Pattern for "from ... to ... (recurrence)?"
    private static final Pattern DATERANGE_TWO_SIDED_PATTERN = Pattern.compile(
        "(?<left>.*)"
            + "(" + KEYWORD_DATERANGE_START + "\\s+" + "(?<start>(.+\\s+)?)" + KEYWORD_DATERANGE_END + "(?<end>(\\s+.+?)?))"
            + "(?<recurrence>(\\s+" + RECURRENCE_REGEX + ")?)$",
        Pattern.CASE_INSENSITIVE
    );

    // Pattern for "on ... (recurrence)?"
    private static final Pattern DATERANGE_SINGLE_DATE_PATTERN = Pattern.compile(
        "(?<left>.*)"
            + "(" + KEYWORD_DATERANGE_DATE + "\\s+(?<date>(.+?)?))"
            + "(?<recurrence>(\\s+" + RECURRENCE_REGEX + ")?)$",
        Pattern.CASE_INSENSITIVE
    );

    // Pattern for "from ... (recurrence)?"
    private static final Pattern DATERANGE_START_DATE_PATTERN = Pattern.compile(
        "(?<left>.*)(" + KEYWORD_DATERANGE_START + "\\s+(?<start>(.+?)))"
            + "(?<recurrence>(\\s+" + RECURRENCE_REGEX + ")?)$",
        Pattern.CASE_INSENSITIVE
    );

    // Pattern for "to ... (recurrence)?"
    private static final Pattern DATERANGE_END_DATE_PATTERN = Pattern.compile(
        "(?<left>.*)(" + KEYWORD_DATERANGE_END + "\\s+(?<end>(.+?)))"
            + "(?<recurrence>(\\s+" + RECURRENCE_REGEX + ")?)$",
        Pattern.CASE_INSENSITIVE
    );
    
    // Pattern for "path (override)?"
    private static final Pattern OVERRIDE_PATTERN = Pattern.compile(
        "^(?<path>.*)("+ KEYWORD_OVERRIDE + ")$",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern DUEDATE_PATTERN = Pattern.compile(
        "(?<left>.*)" + KEYWORD_DUEDATE + "\\s+" + "(?<date>.+?)"
            + "(?<recurrence>(\\s+" + RECURRENCE_REGEX + ")?)$",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern FIRST_WORD_PATTERN = Pattern.compile("^(?<word>\\S+)(?<left>.*?)$");
    private static final Pattern FIRST_QUOTED_TEXT_PATTERN = Pattern.compile("^" + QUOTE_CHARACTER + "(?<text>.*)" + QUOTE_CHARACTER + "(?<left>.*?)$");
    private static final Pattern FIRST_INTEGER_PATTERN = Pattern.compile("^(?<integer>-?\\d+)(?<left>.*?)$");

    private static final Pattern TAGS_PATTERN = Pattern.compile(
        "(?<left>.*?)(?<tags>((\\s+|^)" + TAG_PREFIX + "\\S*)+)$",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern INDEXRANGE_PATTERN = Pattern.compile(
        "^(?<firstInt>-?\\d+)" + "\\s*" + "((to)|-)" + "\\s*" + "(?<secondInt>-?\\d+)(?<left>.*?)$",
        Pattern.CASE_INSENSITIVE
    );

    private String input;
    private DateTimeParser dateTimeParser = new DateTimeParser();

    /**
     * Set the current input the parser is working on, and resets
     * any contextual info from the last input.
     *
     * @param input input to set
     */
    public void setInput(String input) {
        this.input = input;
        dateTimeParser.resetContext();
    }

    /**
     * Gets current input the parser is working on.
     *
     * @return current input
     */
    public String getInput() {
        return input.trim();
    }

    /**
     * Checks whether current input trimmed is an empty string.
     *
     * @return return whether the input is empty
     */
    public boolean isInputEmpty() {
        return input.trim().isEmpty();
    }

    /**
     * Extract a trailing due date from the input.
     * <p>
     * Date range is defined by: "by (valid_datetime)" + optional " (valid_recurrence)",
     * and must be at the end of the string to be considered.
     *
     * @return optional of a due date if found, empty otherwise
     */
    public Optional<DueDate> extractTrailingDueDate() {
        final Matcher matcher = DUEDATE_PATTERN.matcher(input);

        if (matcher.find()) {
            String dateString = matcher.group("date");
            String recurrenceString = matcher.group("recurrence");

            // Parse datetime and recurrence
            Optional<DueDate> dueDate = parseDueDate(dateString, recurrenceString);

            // If legit date, extract from input and return due date
            if (dueDate.isPresent()) {
                input = matcher.group("left").trim();
                return dueDate;
            }
        }

        // Didn't find any matches
        return Optional.empty();
    }

    /**
     * Extract a trailing date range from the input of the form:
     * - "from (valid_datetime) to (valid_datetime)" + an optional "(valid_recurrence)"
     * and must be at the end of the string to be considered.
     *
     * @return optional of date range, empty if not found
     * @throws IllegalValueException if a trailing date range pattern is found but either one of the datetime is valid,
     *                               other invalid, or parsed DateRange is not invalid
     */
    public Optional<DateRange> extractTrailingTwoSidedDateRange() throws IllegalValueException {
        final Matcher matcher = DATERANGE_TWO_SIDED_PATTERN.matcher(input);

        // Find "from ... to ..."
        if (matcher.find()) {
            String recurrenceString = matcher.group("recurrence");
            String startString = matcher.group("start");
            String endString = matcher.group("end");

            Optional<DateRange> dateRange = parseDateRangeWithStartAndEnd(startString, endString, recurrenceString);

            // If legit date range, extract from input and return it
            if (dateRange.isPresent()) {
                input = matcher.group("left").trim();
                return dateRange;
            }
        }

        return Optional.empty();
    }

    /**
     * Extract a trailing date range from the input of the form:
     * - "on (valid_datetime)" + an optional "(valid_recurrence)"
     * and must be at the end of the string to be considered.
     *
     * @return optional of date range, empty if not found
     */
    public Optional<DateRange> extractTrailingSingleDateDateRange() {
        final Matcher matcher = DATERANGE_SINGLE_DATE_PATTERN.matcher(input);

        // Find "on ..."
        if (matcher.find()) {
            String recurrenceString = matcher.group("recurrence");
            String dateString = matcher.group("date");

            Optional<DateRange> dateRange = parseDateRangeWithSingleDate(dateString, recurrenceString);

            // If legit date range, extract from input and return it
            if (dateRange.isPresent()) {
                input = matcher.group("left").trim();
                return dateRange;
            }
        }

        return Optional.empty();
    }

    /**
     * Extract a trailing date range from the input of the form:
     * - "to (valid_datetime)" + an optional "(valid_recurrence)"
     * and must be at the end of the string to be considered.
     *
     * @return optional of date range, empty if not found
     */
    public Optional<DateRange> extractTrailingEndDateDateRange() {
        final Matcher matcher = DATERANGE_END_DATE_PATTERN.matcher(input);

        // Find "to ..."
        if (matcher.find()) {
            String recurrenceString = matcher.group("recurrence");
            String dateString = matcher.group("end");

            Optional<DateRange> dateRange = parseDateRangeWithEnd(dateString, recurrenceString);

            // If legit date range, extract from input and return it
            if (dateRange.isPresent()) {
                input = matcher.group("left").trim();
                return dateRange;
            }
        }

        return Optional.empty();
    }

    /**
     * Extract a trailing date range from the input of the form:
     * - "from (valid_datetime)" + an optional "(valid_recurrence)"
     * and must be at the end of the string to be considered.
     *
     * @return optional of date range, empty if not found
     */
    public Optional<DateRange> extractTrailingStartDateDateRange() {
        final Matcher matcher = DATERANGE_START_DATE_PATTERN.matcher(input);

        // Find "from ..."
        if (matcher.find()) {
            String recurrenceString = matcher.group("recurrence");
            String dateString = matcher.group("start");

            Optional<DateRange> dateRange = parseDateRangeWithStart(dateString, recurrenceString);

            // If legit date range, extract from input and return it
            if (dateRange.isPresent()) {
                input = matcher.group("left").trim();
                return dateRange;
            }
        }

        return Optional.empty();
    }

    /**
     * Extracts all text in input. Input of parser will be empty after this call.
     *
     * @return text extracted
     */
    public Optional<String> extractText() {
        String text = input.trim();
        input = "";

        return text.isEmpty() ? Optional.empty() : Optional.of(text);
    }


    /**
     * From start, extracts a quoted title in input, if found.
     * e.g. "`quoted` text" returns "quoted" and retains input "text".
     *
     * @return quoted text with quotes removed and trimmed if found,
     * empty if no quotes found
     * @throws IllegalValueException if quoted title found is empty
     */
    public Optional<String> extractQuotedTitle() throws IllegalValueException {
        final Matcher matcher = FIRST_QUOTED_TEXT_PATTERN.matcher(input);

        if (matcher.find()) {
            String text = matcher.group("text").trim();

            if (text.isEmpty()) {
                throw new IllegalValueException(Messages.MISSING_TODO_TITLE);
            }

            input = matcher.group("left").trim();
            return Optional.of(text);
        }

        return Optional.empty();
    }

    /**
     * From start, extracts all trailing tags from the current input.
     * Extraction ends when the next word is not a tag.
     * Tags which are empty strings are removed.
     *
     * @return a set of tags, with the tag prefix removed
     */
    public Set<Tag> extractTrailingTags() {
        final Matcher matcher = TAGS_PATTERN.matcher(input);

        if (matcher.find()) {
            input = matcher.group("left").trim();

            // Split to words to get tags
            // trim any leading spaces and filter out empty tags
            return Arrays.stream(matcher.group("tags").trim()
                .split("\\s+"))
                .map(word -> {
                    word = word.trim();

                    // pattern should have ensured this
                    assert word.indexOf(TAG_PREFIX) == 0;

                    return new Tag(word.substring(TAG_PREFIX.length()).trim());

                }).filter(x -> !x.value.isEmpty())
                .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

    /**
     * From start, extracts a single word in input, if found.
     *
     * @return optional of word extracted from input, empty if not found
     */
    public Optional<String> extractWord() {
        final Matcher matcher = FIRST_WORD_PATTERN.matcher(input.trim());
        if (matcher.find()) {
            // Remove extracted first word
            input = matcher.group("left").trim();
            return Optional.of(matcher.group("word"));
        }

        return Optional.empty();
    }

    /**
     * Extracts all words in input (space-separated).
     *
     * @return list of words found, an empty list if input is empty
     */
    public List<String> extractWords() {
        return Arrays.stream(extractText().orElse("").split("\\s+"))
            .filter(x -> !x.trim().isEmpty())
            .collect(Collectors.toList());
    }

    /**
     * From start, extracts an integer in input, if found.
     *
     * @return optional of found integer extracted from input, empty if not found
     */
    public Optional<Integer> extractInteger() {
        final Matcher matcher = FIRST_INTEGER_PATTERN.matcher(input.trim());

        if (matcher.find()) {
            // Remove extracted first integer
            String integerString = matcher.group("integer");
            input = matcher.group("left").trim();

            try {
                return Optional.of(Integer.parseInt(integerString));
            } catch (NumberFormatException exception) {
                assert false : "Shouldn't be able to fail parsing of integer based on pattern";
            }

        }

        return Optional.empty();
    }
    //@@author A0142230B

    /**
     * From start, extracts multiple integers in input.
     * It can be a range of integers or different integers separate by space.
     *
     * @return A List of integers found
     * @throws IllegalValueException
     */
    public List<Integer> extractIndicesList() throws IllegalValueException {
        final Matcher matcher = INDEXRANGE_PATTERN.matcher(input.trim());
        List<Integer> indices = new ArrayList<Integer>();
        int firstInt = -1, secondInt = -1;
        Optional<Integer> aNumber;

        // Add the index range to a list of indices
        // Case one: command type : [index] [to|-] [index]
        if (matcher.find()) {
            try {
                firstInt = Integer.parseInt(matcher.group("firstInt"));
                secondInt = Integer.parseInt(matcher.group("secondInt"));
            } catch (NumberFormatException exception) {
                assert false : "Shouldn't be able to fail parsing of integer based on pattern";
            }

            if (firstInt > secondInt) {
                throw new IllegalValueException(Messages.INDEXRANGE_CONSTRAINTS);
            } else {
                for (int i = firstInt; i <= secondInt; i++) {
                    indices.add(i);
                }
            }
            input = matcher.group("left").trim();
        }
        // Case two: command type : {[index]..}
        else {
            aNumber = extractInteger();
            while (aNumber.isPresent()) {
                indices.add(aNumber.get());
                aNumber = extractInteger();
            }
        }
        return indices;

    }
    
    /**
     * Extract the keyword 'override' from input
     * @return true if 'override' found
     */
    public boolean isOverrideThenExtract() {
        final Matcher matcher = OVERRIDE_PATTERN.matcher(input.trim());
        
        if (matcher.find()) {
            input = matcher.group("path").trim();
            return true;
        }
        else {
        	return false;
        }
    }
    
    

    //@@author A0139697H

    /**
     * Parses a recurrence string to a {@link Recurrence}
     * Asserts {@param recurrence} matches {@link #RECURRENCE_REGEX} or an empty string
     *
     * @return recurrence if valid, null if invalid, Recurrence.None if empty string
     */
    private Recurrence parseRecurrence(String recurrence) {
        switch (recurrence.trim()) {
            case "daily":
                return Recurrence.Daily;
            case "weekly":
                return Recurrence.Weekly;
            case "monthly":
                return Recurrence.Monthly;
            case "yearly":
                return Recurrence.Yearly;
            case "":
                return Recurrence.None;
            default:
                assert false;
        }

        return Recurrence.None;
    }

    private void throwIfEmptyString(String value, String message)
        throws IllegalValueException {
        if (value.trim().isEmpty()) {
            throw new IllegalValueException(message);
        }
    }

    private Optional<DateRange> parseDateRangeWithStartAndEnd(String startDateString, String endDateString, String recurrenceString)
        throws IllegalValueException {

        // Check both "from" and "to" fields are not empty
        throwIfEmptyString(startDateString, Messages.MISSING_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT);
        throwIfEmptyString(endDateString, Messages.MISSING_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT);

        // Parse start datetime with default time of midnight, end datetime with default time of 2359h
        Optional<LocalDateTime> startDateTime = dateTimeParser.parseDateTime(startDateString, LocalTime.MIDNIGHT);
        Optional<LocalDateTime> endDateTime = dateTimeParser.parseDateTime(endDateString, LocalTime.of(23, 59));

        if (!startDateTime.isPresent() && !endDateTime.isPresent()) {
            // Both start and end dates invalid, maybe its not a date range
            return Optional.empty();
        } else if (startDateTime.isPresent() && !endDateTime.isPresent()) {
            throw new IllegalValueException(Messages.INVALID_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT);
        } else if (endDateTime.isPresent() && !startDateTime.isPresent()) {
            throw new IllegalValueException(Messages.INVALID_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT);
        } else {
            assert startDateTime.isPresent() && endDateTime.isPresent();

            Recurrence recurrence = parseRecurrence(recurrenceString);
            return Optional.of(new DateRange(startDateTime.get(), endDateTime.get(), recurrence));
        }
    }

    private Optional<DateRange> parseDateRangeWithEnd(String endDateString, String recurrenceString) {
        if (endDateString.isEmpty()) {
            return Optional.empty();
        }

        Optional<LocalDateTime> endDateTime
            = dateTimeParser.parseEndDateTime(endDateString);

        // Invalid datetime
        if (!endDateTime.isPresent()) {
            return Optional.empty();
        }

        Recurrence recurrence = parseRecurrence(recurrenceString);

        try {
            // Return from now to the date end
            DateRange dateRange = new DateRange(LocalDateTime.now(), endDateTime.get(), recurrence);
            return Optional.of(dateRange);
        } catch (IllegalValueException e) {
            // Date range was invalid - should not treat it as date range then
            return Optional.empty();
        }
    }

    private Optional<DateRange> parseDateRangeWithStart(String startDateString, String recurrenceString) {
        if (startDateString.isEmpty()) {
            return Optional.empty();
        }

        Optional<LocalDateTime> date = dateTimeParser.parseDateTime(startDateString);

        // Invalid datetime
        if (!date.isPresent()) {
            return Optional.empty();
        }

        Recurrence recurrence = parseRecurrence(recurrenceString);

        try {
            // Return from date start onwards
            DateRange dateRange = new DateRange(date.get(), LocalDateTime.MAX, recurrence);
            return Optional.of(dateRange);
        } catch (IllegalValueException e) {
            // Date range was invalid - should not treat it as date range then
            return Optional.empty();
        }
    }

    private Optional<DateRange> parseDateRangeWithSingleDate(String dateString, String recurrenceString) {
        if (dateString.isEmpty()) {
            return Optional.empty();
        }

        Optional<Pair<LocalDateTime, LocalDateTime>> period
            = dateTimeParser.parseDateTimePeriod(dateString);

        // Invalid datetime
        if (!period.isPresent()) {
            return Optional.empty();
        }

        Recurrence recurrence = parseRecurrence(recurrenceString);

        try {
            DateRange dateRange = new DateRange(period.get().getKey(), period.get().getValue(), recurrence);
            return Optional.of(dateRange);
        } catch (IllegalValueException e) {
            // Date range was invalid - should not treat it as date range then
            return Optional.empty();
        }
    }

    private Optional<DueDate> parseDueDate(String dateString, String recurrenceString) {
        // Parse datetime and recurrence
        Optional<LocalDateTime> datetime
            = dateTimeParser.parseEndDateTime(dateString);
        Recurrence recurrence = parseRecurrence(recurrenceString);

        if (datetime.isPresent()) {
            return Optional.of(new DueDate(datetime.get(), recurrence));
        } else {
            return Optional.empty();
        }
    }
}