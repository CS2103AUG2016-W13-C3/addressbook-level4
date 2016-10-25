package seedu.commando.logic.parser;

import com.google.common.collect.Sets;
import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.DueDate;
import seedu.commando.model.todo.Recurrence;
import seedu.commando.model.todo.Tag;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandParser commandParser = new CommandParser();

    @Before
    public void setup() {
    }

    @After
    public void teardown() {
    }

    @Test
    public void extractText_emptyString()  {
        commandParser.setInput("");
        assertFalse(commandParser.extractText().isPresent());
    }

    @Test
    public void extractText_noKeywords()  {
        commandParser.setInput("No keywords");
        assertEquals("No keywords", commandParser.extractText().orElse(""));
        assertEquals("", commandParser.getInput());
    }

    @Test
    public void extractText_trims()  {
        commandParser.setInput(" trims   ");
        assertEquals("trims", commandParser.extractText().orElse(""));
        assertEquals("", commandParser.getInput());
    }

    @Test
    public void extractText_withKeywords()  {
        commandParser.setInput("this is a sentence from here to there by the bay");
        assertEquals("this is a sentence from here to there by the bay", commandParser.extractText().orElse(""));
        assertEquals("", commandParser.getInput());
    }

    @Test
    public void extractWords_noWords()  {
        commandParser.setInput("");
        assertTrue(commandParser.extractWords().isEmpty());
    }

    @Test
    public void extractWords_1Word()  {
        commandParser.setInput("1Word");
        assertEquals(Arrays.asList("1Word"), commandParser.extractWords());
        assertEquals("", commandParser.getInput());
    }

    @Test
    public void extractWords_2Words()  {
        commandParser.setInput("2 words");
        assertEquals(Arrays.asList("2", "words"), commandParser.extractWords());
        assertEquals("", commandParser.getInput());
    }

    @Test
    public void extractWord_noWord()  {
        commandParser.setInput("");
        assertTrue(!commandParser.extractWord().isPresent());
    }

    @Test
    public void extractWord_firstWord()  {
        commandParser.setInput("command word");
        assertEquals("command", commandParser.extractWord().orElse(""));
        assertEquals("word", commandParser.getInput().trim());
    }

    @Test
    public void extractInteger_noInteger()  {
        commandParser.setInput("no index");
        assertTrue(!commandParser.extractInteger().isPresent());
        assertEquals("no index", commandParser.getInput());
    }

    @Test
    public void extractInteger_integers()  {
        commandParser.setInput("1 index");
        assertTrue(1 == commandParser.extractInteger().orElse(-1));
        assertEquals("index", commandParser.getInput());

        commandParser.setInput("0 index");
        assertTrue(0 == commandParser.extractInteger().orElse(-1));
        assertEquals("index", commandParser.getInput());

        commandParser.setInput("-2 index");
        assertTrue(-2 == commandParser.extractInteger().orElse(-1));
        assertEquals("index", commandParser.getInput());
    }

    @Test
    public void extractTrailingTags_noMatches()  {
        commandParser.setInput("no matches");
        assertTrue(commandParser.extractTrailingTags().isEmpty());
        assertEquals("no matches", commandParser.getInput());
    }

    @Test
    public void extractTrailingTags_allTags()  {
        commandParser.setInput("#tag1 #tag2 #tag3");
        assertEquals(
            Sets.newHashSet(new Tag("tag1"), new Tag("tag2"), new Tag("tag3")),
            commandParser.extractTrailingTags()
        );
        assertEquals("", commandParser.getInput().trim());
    }

    @Test
    public void extractTrailingTags_notTrailing()  {
        commandParser.setInput("other #tag1 #tag2 words");
        assertEquals(Collections.emptySet(), commandParser.extractTrailingTags());
        assertEquals("other #tag1 #tag2 words", commandParser.getInput());
    }


    @Test
    public void extractTrailingTags_nonTrailingTags()  {
        commandParser.setInput("other #tag1 words #tag2");
        assertEquals(Sets.newHashSet(new Tag("tag2")), commandParser.extractTrailingTags());
        assertEquals("other #tag1 words", commandParser.getInput());
    }

    @Test
    public void extractTrailingDueDate_valid() throws IllegalValueException {
        commandParser.setInput("extract text by 10 Nov 2015");

        Optional<DueDate> dueDate = commandParser.extractTrailingDueDate();

        assertTrue(dueDate.isPresent());
        assertEquals(LocalDateTime.of(
            2015, 11, 10, 12, 0
        ), dueDate.get().value);
        assertEquals("extract text", commandParser.getInput());
    }

    @Test
    public void extractTrailingDueDate_invalidDate() throws IllegalValueException {
        commandParser.setInput("extract text by invalid date");
        assertFalse(commandParser.extractTrailingDueDate().isPresent());
        assertEquals("extract text by invalid date", commandParser.getInput());
    }

    @Test
    public void extractTrailingDueDate_notTrailing() throws IllegalValueException {
        commandParser.setInput("extract text by 10 Nov 2015 #nottrailing");
        assertFalse(commandParser.extractTrailingDueDate().isPresent());
        assertEquals("extract text by 10 Nov 2015 #nottrailing", commandParser.getInput());
    }

    @Test
    public void extractTrailingDateRange_valid() throws IllegalValueException {
        commandParser.setInput("from 10 Apr 2016 9am to 11 Jan 2018 10:28");
        Optional<DateRange> dateRange = commandParser.extractTrailingDateRange();
        assertTrue(dateRange.isPresent());
        assertEquals(
            LocalDateTime.of(2016, 4, 10, 9, 0),
            dateRange.get().startDate
        );
        assertEquals(
            LocalDateTime.of(2018, 1, 11, 10, 28),
            dateRange.get().endDate
        );
        assertEquals("", commandParser.getInput());
    }

    @Test
    public void extractTrailingDateRange_invalidDates() throws IllegalValueException {
        commandParser.setInput("walk by the beach from 1 end to another");
        assertFalse(
            commandParser.extractTrailingDueDate().isPresent()
        );
        assertEquals(
            "walk by the beach from 1 end to another", commandParser.extractText().orElse("")
        );
    }

    @Test
    public void extractTrailingDateRange_onlyTrailing() throws IllegalValueException {
        commandParser.setInput("walk by the beach from today to tomorrow from 28 Oct 2018 1200h to 29 Nov 2019 1300h");
        Optional<DateRange> dateRange = commandParser.extractTrailingDateRange();
        assertTrue(dateRange.isPresent());
        assertEquals(
            LocalDateTime.of(2018, 10, 28, 12, 0),
            dateRange.get().startDate
        );
        assertEquals(
            LocalDateTime.of(2019, 11, 29, 13, 0),
            dateRange.get().endDate
        );
        assertEquals(
            "walk by the beach from today to tomorrow", commandParser.extractText().orElse("")
        );
    }

    @Test
    public void extractIndicesList_valid() throws IllegalValueException {
        commandParser.setInput("2to7");
        List<Integer> indices = commandParser.extractIndicesList();
        assertEquals("[2, 3, 4, 5, 6, 7]", indices.toString());
        commandParser.setInput("-2-7");
        indices = commandParser.extractIndicesList();
        assertEquals("[-2, -1, 0, 1, 2, 3, 4, 5, 6, 7]", indices.toString());
        commandParser.setInput("-2  -  7");
        indices = commandParser.extractIndicesList();
        assertEquals("[-2, -1, 0, 1, 2, 3, 4, 5, 6, 7]", indices.toString());
        commandParser.setInput("1 2 3 4 5");
        indices = commandParser.extractIndicesList();
        assertEquals("[1, 2, 3, 4, 5]", indices.toString());
        commandParser.setInput("2to2");
        indices = commandParser.extractIndicesList();
        assertEquals("[2]", indices.toString());
    }

    @Test
    public void extractTrailingDateRange_recurrence() throws IllegalValueException {
        commandParser.setInput("walk nowhere from 28 Oct 2018 1200h to 29 Nov 2018 1300h yearly");
        Optional<DateRange> dateRange = commandParser.extractTrailingDateRange();
        assertTrue(dateRange.isPresent());
        assertEquals(
            LocalDateTime.of(2018, 10, 28, 12, 0),
            dateRange.get().startDate
        );
        assertEquals(
            LocalDateTime.of(2018, 11, 29, 13, 0),
            dateRange.get().endDate
        );
        assertEquals(
            Recurrence.Yearly,
            dateRange.get().recurrence
        );
        assertEquals(
            "walk nowhere", commandParser.extractText().orElse("")
        );
    }

    @Test
    public void extractTrailingDateRange_recurrenceInvalid() throws IllegalValueException {
        commandParser.setInput("walk nowhere from 28 Oct 2018 1200h to 29 Nov 2018 1300h daily");
        thrown.expect(IllegalValueException.class);
        commandParser.extractTrailingDateRange();
    }
}
