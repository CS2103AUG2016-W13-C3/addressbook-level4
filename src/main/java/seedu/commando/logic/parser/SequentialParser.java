package seedu.commando.logic.parser;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a given input string, part by part
 * Methods modify the input string by extracting appropriate parts of it
 */
public class SequentialParser {

    private static final Pattern FIRST_WORD_PATTERN = Pattern.compile("^(?<word>\\S+)(?<tail>.*)$");
    private static final Pattern FIRST_INTEGER_PATTERN = Pattern.compile("^(?<index>-?\\d+)(?<tail>.*)$");
    private static final Pattern WORD_PATTERN = Pattern.compile("(?<word>\\S+)");
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
        return input;
    }

    /**
     * From start, extracts the text after the first occurrence of {@param keyword} from input
     * until any keyword in the set of {@param otherKeywords}
     * {@param keyword} and the subsequent text extracted is removed from input
     * Keywords are matched as whole words, not substrings, and as case-insensitive ("word" does not match in "keyword")
     * It can match up to the end of input, if an empty set of keywords is provided or none in
     * set is encountered
     * Asserts {@param keyword} is non-null
     *
     * @return optional of text extracted from input, empty if not found
     */

    public Optional<String> extractTextAfterKeyword(String keyword, String... otherKeywords) {
        assert keyword != null;

        int keywordStartIndex = getFirstOccurrenceOf(0, keyword);
        int startIndex = keywordStartIndex + keyword.length();
        int endIndex = getFirstOccurrenceOf(startIndex, otherKeywords);

        String text = input.substring(startIndex, endIndex).trim();

        if (text.isEmpty()) {
            return Optional.empty();
        } else {
            input = input.substring(0, keywordStartIndex) + input.substring(endIndex);
            return Optional.of(text);
        }
    }

    /**
     * From start, extracts a datetime after the first occurrence of {@param keyword} from input
     * until any keyword in the set of {@param otherKeywords}
     * If datetime is invalid, extraction is not done, even if there is a match in keyword
     * {@param keyword} and the subsequent text extracted is removed from input
     * Similar to {@link this#extractTextAfterKeyword(String, String...)}}
     * Asserts {@param keyword} is non-null
     *
     * @return optional of datetime extracted from input, empty if not found
     */
    public Optional<LocalDateTime> extractDateTimeAfterKeyword(String keyword, String... otherKeywords) {
        assert keyword != null;

        int keywordStartIndex = getFirstOccurrenceOf(0, keyword);
        int startIndex = keywordStartIndex + keyword.length();
        int endIndex = getFirstOccurrenceOf(startIndex, otherKeywords);

        String datetimeString = input.substring(startIndex, endIndex);

        // Check if datetime can be parsed
        Optional<LocalDateTime> dateTime = dateTimeParser.parseDateTime(datetimeString);
        if (dateTime.isPresent()) {
            // Legit datetime, extract keyword + datetime from input
            input = input.substring(0, keywordStartIndex) + input.substring(endIndex);
            return dateTime;
        } else {
            // Invalid datetime, ignore
            return Optional.empty();
        }
    }

    /**
     * Search for earliest occurrence of any keyword, case insensitive
      */
    private int getFirstOccurrenceOf(int startIndex, String... keywords) {
        String lowerCaseInput = input.substring(startIndex).toLowerCase();

        Matcher matcher = WORD_PATTERN.matcher(lowerCaseInput);
        loop:
        while (matcher.find()) {
            for (String keyword : keywords) {
                // If any keyword matched current word
                if (matcher.group("word").equals(keyword.trim().toLowerCase())) {
                    return matcher.start() + startIndex;
                }
            }
        }

        return input.length(); // no keywords found, return up to the last index of input
    }

    /**
     * Similar to {@link this#extractTextAfterKeyword(String, String...)}, but from start of input
     */
    public Optional<String> extractText(String... keywords) {
        int endIndex = getFirstOccurrenceOf(0, keywords);
        String text = input.substring(0, endIndex).trim();

        // Remove extracted text
        input = input.substring(endIndex);

        if (text.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(text);
        }
    }

    /**
     * Extracts all words prefixed with {@param prefix} as a list,
     * in the order that appears in the input
     * Eg, extractPrefixedWords("#", true) on input = "some #tag1 #tag2 thing" returns ["tag1", "tag2"]
     * and resulting input = "some  thing"
     * Asserts {@param prefix} is non-null
     */
    public List<String> extractPrefixedWords(String prefix, boolean ifRemovePrefix) {
        assert prefix != null;

        List<String> words = new LinkedList<>();

        // Keep trying to find the next word, until cannot be found
        for (Matcher matcher = WORD_PATTERN.matcher(input);
             matcher.find(); ) {

            String rawWord = matcher.group("word");

            // Check prefix of word
            if (rawWord.indexOf(prefix) == 0) {
                String word = ifRemovePrefix ? rawWord.substring(prefix.length()) : rawWord;

                // Add word only if it doesn't reduce to empty string
                if (!word.trim().isEmpty()) {
                    words.add(word);
                }

                input = input.substring(0, matcher.start())
                    + input.substring(matcher.end()); // Remove matched word from input

                matcher = WORD_PATTERN.matcher(input); // Reset matcher to new input string
            }
        }

        return words;
    }

    /**
     * From start, extracts a list of words in input until its end
     * @return list of words found, in the order that appears in input
     */
    public List<String> extractWords() {
        final List<String> words = new LinkedList<>();

        final Matcher matcher = WORD_PATTERN.matcher(input.trim());

        while (matcher.find()) {
            words.add(matcher.group("word"));
        }

        input = ""; // empty input

        return words;
    }

    /**
     * From start, extracts the first word in input, if found
     *
     * @return optional of word extracted from input, empty if not found
     */
    public Optional<String> extractFirstWord() {
        final Matcher matcher = FIRST_WORD_PATTERN.matcher(input.trim());

        if (matcher.matches()) {
            input = matcher.group("tail"); // Remove extracted command word
            return Optional.of(matcher.group("word"));
        }

        return Optional.empty();
    }

    /**
     * From start, extracts first integer in input, if found
     *
     * @return optional of found integer extracted from input, empty if not found
     */
    public Optional<Integer> extractFirstInteger() {
        final Matcher matcher = FIRST_INTEGER_PATTERN.matcher(input.trim());

        if (matcher.matches()) {
            input = matcher.group("tail"); // Remove extracted item index
            String indexString = matcher.group("index");

            try {
                return Optional.of(Integer.parseInt(indexString));
            } catch (NumberFormatException exception) {
                assert false : "Shouldn't be able to fail parsing of item index based on pattern";
            }
        }

        return Optional.empty();
    }
}