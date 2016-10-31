package seedu.commando.logic.parser;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.DueDate;
import seedu.commando.model.todo.Recurrence;
import seedu.commando.model.todo.Tag;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//@@author A0139697H

/**
 * In charge of parsing an input command string, piece-wise, with the help of
 * {@link DateTimeParser}.
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

    private static final Pattern FIRST_WORD_PATTERN = Pattern.compile("^(?<word>\\S+)(?<left>.*?)$");
    private static final Pattern FIRST_QUOTED_TEXT_PATTERN = Pattern.compile("^" + QUOTE_CHARACTER + "(?<text>.*)" + QUOTE_CHARACTER + "(?<left>.*?)$");
    private static final Pattern FIRST_INTEGER_PATTERN = Pattern.compile("^(?<integer>-?\\d+)(?<left>.*?)$");
    private static final Pattern DATERANGE_PATTERN = Pattern.compile(
        "(?<left>.*)((" + KEYWORD_DATERANGE_START + "\\s+" + "(?<start>(.+\\s+)?)"
            + KEYWORD_DATERANGE_END + "(?<end>(\\s+.+?)?))|"
            + "(" + KEYWORD_DATERANGE_DATE + "\\s+(?<date>(.+?)?)))"
            + "(?<recurrence>(\\s+" + RECURRENCE_REGEX + ")?)$",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern DUEDATE_PATTERN = Pattern.compile(
        "(?<left>.*)" + KEYWORD_DUEDATE + "\\s+" + "(?<date>.+?)"
            + "(?<recurrence>(\\s+" + RECURRENCE_REGEX + ")?)$",
        Pattern.CASE_INSENSITIVE
    );
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
     */
    public void setInput(String input) {
        this.input = input;
        dateTimeParser.resetContext();
    }

    /**
     * Gets current input the parser is working on.
     */
    public String getInput() {
        return input.trim();
    }

    /**
     * Checks whether current input is empty.
     */
    public boolean isInputEmpty() {
        return input.trim().isEmpty();
    }

    /**
     * Extract a trailing due date from the input.
     * Date range is defined by: "by (valid_datetime)" + optional " (valid_recurrence)",
     * and must be at the end of the string to be considered.
     *
     * @return a due date if found, empty otherwise
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
     * Extract a trailing date range from the input.
     * Date range is defined by:
     *   - "from (valid_datetime) to (valid_datetime)" + an optional "(valid_recurrence)"
     *   - "on (valid_datetime)" + an optional "(valid_recurrence)",
     * and must be at the end of the string to be considered.
     *
     * @throws IllegalValueException if a trailing date range pattern is found but either one of the datetime is valid,
     *  other invalid, or parsed DateRange is not invalid
     */
    public Optional<DateRange> extractTrailingDateRange() throws IllegalValueException {
        final Matcher matcher = DATERANGE_PATTERN.matcher(input);

        if (matcher.find()) {
            String recurrenceString = matcher.group("recurrence");

            String startString = matcher.group("start");
            String endString = matcher.group("end");

            Optional<DateRange> dateRange;

            // if both "from" and "to" fields not specified, check "on"
            if (startString == null && endString == null) {
                String dateString = matcher.group("date");

                // Regex should ensure that "on" date is non-empty
                assert !dateString.isEmpty();

                dateRange = parseDateRange(dateString, recurrenceString);
            } else {
                dateRange = parseDateRange(startString, endString, recurrenceString);
            }

            // If legit date range, extract from input and return it
            if (dateRange.isPresent()) {
                input = matcher.group("left").trim();
                return dateRange;
            }
        }

        // Didn't find any matches
        return Optional.empty();
    }

    /**
     * Extracts all text in input
     */
    public Optional<String> extractText() {
        String text = input.trim();
        input = "";

        return text.isEmpty() ? Optional.empty() : Optional.of(text);
    }


    /**
     * From start, extracts a quoted title in input, if found
     * e.g. "`quoted` text" returns "quoted" and retains input "text"
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
     * @return a set of tags, with the tag prefix removed.
     */
    public Set<Tag> extractTrailingTags() {
        final Matcher matcher = TAGS_PATTERN.matcher(input);

        if (matcher.find()) {
            input = matcher.group("left").trim();

            // Split to words to get tags
            return Arrays.stream(matcher.group("tags").trim()
                .split("\\s+"))
                .map(word -> {
                    word = word.trim(); // trim any leading spaces
                    assert word.indexOf(TAG_PREFIX) == 0; // pattern should have ensured this
                    return new Tag(word.substring(TAG_PREFIX.length()).trim());
                }).filter(x -> !x.value.isEmpty())
                .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

    /**
     * From start, extracts a single word in input, if found
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
     * Extracts all words in input
     * If input is empty, returns empty list
     */
    public List<String> extractWords() {
        return Arrays.stream(extractText().orElse("").split("\\s+"))
            .filter(x -> !x.trim().isEmpty())
            .collect(Collectors.toList());
    }

    /**
     * From start, extracts an integer in input, if found
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
     * From start, extracts multiple integers in input, it can be a range of integers or different integers separate by space.
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

    private DateRange getDateRange(Optional<LocalDateTime> startDateTime,
                                   Optional<LocalDateTime> endDateTime)
        throws IllegalValueException {

        if (startDateTime.isPresent() && !endDateTime.isPresent()) {
            throw new IllegalValueException(Messages.INVALID_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT);
        } else if (endDateTime.isPresent() && !startDateTime.isPresent()) {
            throw new IllegalValueException(Messages.INVALID_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT);
        }

        return new DateRange(startDateTime.get(), endDateTime.get());
    }

    private Optional<DateRange> parseDateRange(String startDateString, String endDateString, String recurrenceString)
        throws IllegalValueException {

        // check both "from" and "to" fields are not empty
        throwIfEmptyString(startDateString, Messages.MISSING_TODO_DATERANGE_START + "\n" + Messages.DATE_FORMAT);
        throwIfEmptyString(endDateString, Messages.MISSING_TODO_DATERANGE_END + "\n" + Messages.DATE_FORMAT);

        Optional<LocalDateTime> startDateTime = dateTimeParser.parseDateTime(startDateString);
        Optional<LocalDateTime> endDateTime = dateTimeParser.parseDateTime(endDateString);

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

    private Optional<DateRange> parseDateRange(String dateString, String recurrenceString)
        throws IllegalValueException {

        if (dateString.isEmpty()) {
            return Optional.empty();
        }

        Optional<LocalDateTime> dateTime = dateTimeParser.parseDateTime(dateString);

        // Invalid datetime
        if (!dateTime.isPresent()) {
            return Optional.empty();
        }

        Recurrence recurrence = parseRecurrence(recurrenceString);
        return Optional.of(
            new DateRange(dateTime.get(), dateTime.get().plusDays(1).withHour(0).withMinute(0), recurrence)
        );
    }

    private Optional<DueDate> parseDueDate(String dateString, String recurrenceString) {
        // Parse datetime and recurrence
        Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dateString);
        Recurrence recurrence = parseRecurrence(recurrenceString);

        if (date.isPresent()) {
            return Optional.of(new DueDate(date.get(), recurrence));
        } else {
            return Optional.empty();
        }
    }
}