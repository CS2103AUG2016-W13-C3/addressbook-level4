package seedu.commando.logic.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static seedu.commando.testutil.TestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SequentialParserTest {
    private SequentialParser sequentialParser = new SequentialParser();

    @Before
    public void setup() {
    }

    @After
    public void teardown() {
    }

    @Test
    public void extractText_noKeywords()  {
        sequentialParser.setInput("No keywords");
        assertEquals("No keywords", sequentialParser.extractText().orElse(""));
        assertEquals("", sequentialParser.getInput());
    }

    @Test
    public void extractText_trims()  {
        sequentialParser.setInput(" trims   ");
        assertEquals("trims", sequentialParser.extractText().orElse(""));
        assertEquals("", sequentialParser.getInput());
    }

    @Test
    public void extractText_1Keyword()  {
        sequentialParser.setInput("1 keyword only");

        assertEquals("1", sequentialParser.extractText(
            "keyword"
        ).orElse(""));
        assertEquals("keyword only", sequentialParser.getInput().trim());
    }

    @Test
    public void extractText_2Keywords()  {
        sequentialParser.setInput("2 keywords, not just 1");

        assertEquals("2 keywords,", sequentialParser.extractText(
            "not", "just"
        ).orElse(""));
        assertEquals("not just 1", sequentialParser.getInput().trim());
    }

    @Test
    public void extractText_caseInsensitiveKeywords()  {
        sequentialParser.setInput("Case iN Sens itive");

        assertEquals("Case", sequentialParser.extractText(
            "In", "seNs"
        ).orElse(""));
        assertEquals("iN Sens itive", sequentialParser.getInput().trim());
    }

    @Test
    public void extractText_noMatchingKeywords()  {
        sequentialParser.setInput("No matching keywords");

        assertEquals("No matching keywords", sequentialParser.extractText(
            "random", "words"
        ).orElse(""));
        assertEquals("", sequentialParser.getInput());
    }

    @Test
    public void extractText_notMatchSubstrings()  {
        sequentialParser.setInput("Don't match substrings");

        assertEquals("Don't match substrings", sequentialParser.extractText(
            "mat", "Don"
        ).orElse(""));
        assertEquals("", sequentialParser.getInput());
    }

    @Test
    public void extractWords_noWords()  {
        sequentialParser.setInput("");
        assertTrue(sequentialParser.extractWords().isEmpty());
    }

    @Test
    public void extractWords_1Word()  {
        sequentialParser.setInput("1Word");
        assertEquals(Arrays.asList("1Word"), sequentialParser.extractWords());
        assertEquals("", sequentialParser.getInput());
    }

    @Test
    public void extractWords_2Words()  {
        sequentialParser.setInput("2 words");
        assertEquals(Arrays.asList("2", "words"), sequentialParser.extractWords());
        assertEquals("", sequentialParser.getInput());
    }

    @Test
    public void extractFirstWord_noWord()  {
        sequentialParser.setInput("");
        assertTrue(!sequentialParser.extractFirstWord().isPresent());
    }

    @Test
    public void extractFirstWord_firstWord()  {
        sequentialParser.setInput("command word");
        assertEquals("command", sequentialParser.extractFirstWord().orElse(""));
        assertEquals("word", sequentialParser.getInput().trim());
    }

    @Test
    public void extractFirstInteger_noInteger()  {
        sequentialParser.setInput("no index");
        assertTrue(!sequentialParser.extractFirstInteger().isPresent());
    }

    @Test
    public void extractFirstInteger_integer()  {
        sequentialParser.setInput("1 index");
        assertTrue(1 == sequentialParser.extractFirstInteger().orElse(-1));
    }

    @Test
    public void extractPrefixedWords_noMatches()  {
        sequentialParser.setInput("no matches");
        assertTrue(sequentialParser.extractPrefixedWords("#", false).isEmpty());
        assertEquals("no matches", sequentialParser.getInput());
    }

    @Test
    public void extractPrefixedWords_allTags()  {
        sequentialParser.setInput("#tag1 #tag2 #tag3");
        assertEquals(Arrays.asList("#tag1", "#tag2", "#tag3"), sequentialParser.extractPrefixedWords("#", false));
        assertEquals("", sequentialParser.getInput().trim());
    }

    @Test
    public void extractPrefixedWords_tagsWithOtherWords()  {
        sequentialParser.setInput("other #tag1 #tag2 words");
        assertEquals(Arrays.asList("#tag1", "#tag2"), sequentialParser.extractPrefixedWords("#", false));
        assertEquals(Arrays.asList("other", "words"), sequentialParser.extractWords());
    }

    @Test
    public void extractPrefixedWords_removePrefix_tagsWithOtherWords()  {
        sequentialParser.setInput("other #tag1 #tag2 words");
        assertEquals(Arrays.asList("tag1", "tag2"), sequentialParser.extractPrefixedWords("#", true));
        assertEquals(Arrays.asList("other", "words"), sequentialParser.extractWords());
    }

    @Test
    public void extractTextAfterKeyword_keyword()  {
        sequentialParser.setInput("extract text after keyword");
        assertEquals("keyword", sequentialParser.extractTextAfterKeyword("after").orElse(""));
        assertEquals(Arrays.asList("extract","text"), sequentialParser.extractWords());
    }

    @Test
    public void extractTextAfterKeyword_keywordNotFound()  {
        sequentialParser.setInput("keyword not found");
        assertTrue(!sequentialParser.extractTextAfterKeyword("key").isPresent());
        assertEquals(Arrays.asList("keyword", "not", "found"), sequentialParser.extractWords());
    }

    @Test
    public void extractTextAfterKeyword_caseInsensitive()  {
        sequentialParser.setInput("case InSensitive keyword");
        assertEquals("keyword", sequentialParser.extractTextAfterKeyword("iNsensitive").orElse(""));
        assertEquals(Arrays.asList("case"), sequentialParser.extractWords());
    }

    @Test
    public void extractDateTimeAfterKeyword_dateRange()  {
        sequentialParser.setInput("from 10 Apr 2016 9am to 11 Jan 2018 10:28");
        assertEquals(
            LocalDateTime.of(2016, 4, 10, 9, 0),
            sequentialParser.extractDateTimeAfterKeyword("from", "to").orElse(null)
        );
        assertEquals(
            LocalDateTime.of(2018, 1, 11, 10, 28),
            sequentialParser.extractDateTimeAfterKeyword("to").orElse(null)
        );
    }

    @Test
    public void extractDateTimeAfterKeyword_invalidDates()  {
        sequentialParser.setInput("walk by the beach from 1 end to another");
        assertFalse(
            sequentialParser.extractDateTimeAfterKeyword("by", "to", "from").isPresent()
        );
        assertFalse(
            sequentialParser.extractDateTimeAfterKeyword("from", "by", "to").isPresent()
        );
        assertFalse(
            sequentialParser.extractDateTimeAfterKeyword("to", "by", "from").isPresent()
        );
        assertEquals(
            "walk by the beach from 1 end to another", sequentialParser.extractText().orElse("")
        );
    }
}
