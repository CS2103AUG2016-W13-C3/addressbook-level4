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

/**
 * Parses a given input string, part by part
 * Methods modify the input string by extracting appropriate parts of it
 * Input is always kept trimmed
 */
public class SequentialParser {

    public static final String KEYWORD_DATERANGE_START = "from";
    public static final String KEYWORD_DATERANGE_END = "to";
    public static final String KEYWORD_DUEDATE = "by";
    public static final String KEYWORD_RECURRENCE = "daily|weekly|monthly|yearly";
    public static final String TAG_PREFIX = "#";

    private static final Pattern FIRST_WORD_PATTERN = Pattern.compile("^(?<word>\\S+)(?<left>.*?)$");
    private static final Pattern FIRST_INTEGER_PATTERN = Pattern.compile("^(?<integer>-?\\d+)(?<left>.*?)$");
    private static final Pattern DATERANGE_PATTERN = Pattern.compile(
        "(?<left>.*)" + KEYWORD_DATERANGE_START + "\\s+" + "(?<start>.+?)" + "\\s+"
            + KEYWORD_DATERANGE_END + "\\s+" + "(?<end>.+?)"
            + "(?<recurrence>(\\s+" + KEYWORD_RECURRENCE + ")?)$",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern DUEDATE_PATTERN = Pattern.compile(
        "(?<left>.*)" + KEYWORD_DUEDATE + "\\s+" + "(?<date>.+?)$",
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
    private DateTimeParser dateTimeParser;
    {
        dateTimeParser = new DateTimeParser();
    }

    /**
     * Set the current input the parser is working on
     */
    public void setInput(String input) {
        this.input = input;
        dateTimeParser.resetContext(); // reset any contextual info from last command
    }

    /**
     * Gets current input the parser is working on
     */
    public String getInput() {
        return input.trim();
    }
    
    /**
     * Checks whether current input is empty
     */
    public boolean isInputEmpty(){
        return input.trim().isEmpty();
    }

    /**
     * Extract a trailing due date from the input.
     * Date range is defined by: "by (valid_datetime)", and must be at the end of the string to be considered.
     * If a trailing date range pattern is found but datetime is not valid, also returns empty
     * @throws IllegalValueException if parsed DueDate is not valid
     */
    public Optional<DueDate> extractTrailingDueDate() throws IllegalValueException {
        final Matcher matcher = DUEDATE_PATTERN.matcher(input);

        if (matcher.find()) {
            String dateString = matcher.group("date");

            // Check if datetimes can be parsed
            Optional<LocalDateTime> date = dateTimeParser.parseDateTime(dateString);

            if (date.isPresent()) {
                // Legit date
                input = matcher.group("left").trim();
                return Optional.of(new DueDate(date.get()));
            }
        }

        return Optional.empty(); // Didn't find any matches
    }

    /**
     * Extract a trailing date range from the input.
     * Date range is defined by: "from (valid_datetime) to (valid_datetime)" + optional "(valid recurrence),
     * and must be at the end of the string to be considered.
     * If a trailing date range pattern is found but both datetimes are not valid, also returns empty
     * @throws IllegalValueException if a trailing date range pattern is found but either one of the datetime is valid, other invalid,
     * or parsed DateRange is not invalid
     */
    public Optional<DateRange> extractTrailingDateRange() throws IllegalValueException {
        final Matcher matcher = DATERANGE_PATTERN.matcher(input);

        if (matcher.find()) {
            String startString = matcher.group("start");
            String endString = matcher.group("end");
            String recurrenceString = matcher.group("recurrence");

            // Check if datetimes can be parsed
            Optional<LocalDateTime> startDateTime = dateTimeParser.parseDateTime(startString);
            Optional<LocalDateTime> endDateTime = dateTimeParser.parseDateTime(endString);

            if (startDateTime.isPresent() && endDateTime.isPresent()) {

                // Legit date range
                input = matcher.group("left").trim();

                // Parse recurrence
                Recurrence recurrence = parseRecurrence(recurrenceString);

                assert recurrence != null : "Regex should ensure that recurrence string is valid";

                return Optional.of(new DateRange(startDateTime.get(), endDateTime.get(), recurrence));
            } else if (startDateTime.isPresent()) {
                throw new IllegalValueException(Messages.INVALID_TODO_DATERANGE_END);
            } else if (endDateTime.isPresent()) {
                throw new IllegalValueException(Messages.INVALID_TODO_DATERANGE_START);
            }
        }

        return Optional.empty(); // Didn't find any matches
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
     * From start, extracts all trailing tags from the current input.
     * Extraction ends when the next word is not a tag.
     * Tags which are empty strings are removed.
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

    // Returns null if invalid recurrence
    // Returns Recurrence.None if empty string
    private Recurrence parseRecurrence(String recurrence) {
        recurrence = recurrence.trim().toLowerCase();

        switch (recurrence) {
            case "daily": return Recurrence.Daily;
            case "weekly": return Recurrence.Weekly;
            case "monthly": return Recurrence.Monthly;
            case "yearly": return Recurrence.Yearly;
            case "": return Recurrence.None;
            default: return null;
        }
    }
}