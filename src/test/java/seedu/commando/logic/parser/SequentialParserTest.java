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

public class SequentialParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SequentialParser sequentialParser = new SequentialParser();

    @Before
    public void setup() {
    }

    @After
    public void teardown() {
    }

    @Test
    public void extractText_emptyString()  {
        sequentialParser.setInput("");
        assertFalse(sequentialParser.extractText().isPresent());
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
    public void extractText_withKeywords()  {
        sequentialParser.setInput("this is a sentence from here to there by the bay");
        assertEquals("this is a sentence from here to there by the bay", sequentialParser.extractText().orElse(""));
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
    public void extractWord_noWord()  {
        sequentialParser.setInput("");
        assertTrue(!sequentialParser.extractWord().isPresent());
    }

    @Test
    public void extractWord_firstWord()  {
        sequentialParser.setInput("command word");
        assertEquals("command", sequentialParser.extractWord().orElse(""));
        assertEquals("word", sequentialParser.getInput().trim());
    }

    @Test
    public void extractInteger_noInteger()  {
        sequentialParser.setInput("no index");
        assertTrue(!sequentialParser.extractInteger().isPresent());
        assertEquals("no index", sequentialParser.getInput());
    }

    @Test
    public void extractInteger_integers()  {
        sequentialParser.setInput("1 index");
        assertTrue(1 == sequentialParser.extractInteger().orElse(-1));
        assertEquals("index", sequentialParser.getInput());

        sequentialParser.setInput("0 index");
        assertTrue(0 == sequentialParser.extractInteger().orElse(-1));
        assertEquals("index", sequentialParser.getInput());

        sequentialParser.setInput("-2 index");
        assertTrue(-2 == sequentialParser.extractInteger().orElse(-1));
        assertEquals("index", sequentialParser.getInput());
    }

    @Test
    public void extractTrailingTags_noMatches()  {
        sequentialParser.setInput("no matches");
        assertTrue(sequentialParser.extractTrailingTags().isEmpty());
        assertEquals("no matches", sequentialParser.getInput());
    }

    @Test
    public void extractTrailingTags_allTags()  {
        sequentialParser.setInput("#tag1 #tag2 #tag3");
        assertEquals(
            Sets.newHashSet(new Tag("tag1"), new Tag("tag2"), new Tag("tag3")),
            sequentialParser.extractTrailingTags()
        );
        assertEquals("", sequentialParser.getInput().trim());
    }

    @Test
    public void extractTrailingTags_notTrailing()  {
        sequentialParser.setInput("other #tag1 #tag2 words");
        assertEquals(Collections.emptySet(), sequentialParser.extractTrailingTags());
        assertEquals("other #tag1 #tag2 words", sequentialParser.getInput());
    }


    @Test
    public void extractTrailingTags_nonTrailingTags()  {
        sequentialParser.setInput("other #tag1 words #tag2");
        assertEquals(Sets.newHashSet(new Tag("tag2")), sequentialParser.extractTrailingTags());
        assertEquals("other #tag1 words", sequentialParser.getInput());
    }

    @Test
    public void extractTrailingDueDate_valid() throws IllegalValueException {
        sequentialParser.setInput("extract text by 10 Nov 2015");

        Optional<DueDate> dueDate = sequentialParser.extractTrailingDueDate();

        assertTrue(dueDate.isPresent());
        assertEquals(LocalDateTime.of(
            2015, 11, 10, 12, 0
        ), dueDate.get().value);
        assertEquals("extract text", sequentialParser.getInput());
    }

    @Test
    public void extractTrailingDueDate_invalidDate() throws IllegalValueException {
        sequentialParser.setInput("extract text by invalid date");
        assertFalse(sequentialParser.extractTrailingDueDate().isPresent());
        assertEquals("extract text by invalid date", sequentialParser.getInput());
    }

    @Test
    public void extractTrailingDueDate_notTrailing() throws IllegalValueException {
        sequentialParser.setInput("extract text by 10 Nov 2015 #nottrailing");
        assertFalse(sequentialParser.extractTrailingDueDate().isPresent());
        assertEquals("extract text by 10 Nov 2015 #nottrailing", sequentialParser.getInput());
    }

    @Test
    public void extractTrailingDateRange_valid() throws IllegalValueException {
        sequentialParser.setInput("from 10 Apr 2016 9am to 11 Jan 2018 10:28");
        Optional<DateRange> dateRange = sequentialParser.extractTrailingDateRange();
        assertTrue(dateRange.isPresent());
        assertEquals(
            LocalDateTime.of(2016, 4, 10, 9, 0),
            dateRange.get().startDate
        );
        assertEquals(
            LocalDateTime.of(2018, 1, 11, 10, 28),
            dateRange.get().endDate
        );
        assertEquals("", sequentialParser.getInput());
    }

    @Test
    public void extractTrailingDateRange_invalidDates() throws IllegalValueException {
        sequentialParser.setInput("walk by the beach from 1 end to another");
        assertFalse(
            sequentialParser.extractTrailingDueDate().isPresent()
        );
        assertEquals(
            "walk by the beach from 1 end to another", sequentialParser.extractText().orElse("")
        );
    }

    @Test
    public void extractTrailingDateRange_onlyTrailing() throws IllegalValueException {
        sequentialParser.setInput("walk by the beach from today to tomorrow from 28 Oct 2018 1200h to 29 Nov 2019 1300h");
        Optional<DateRange> dateRange = sequentialParser.extractTrailingDateRange();
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
            "walk by the beach from today to tomorrow", sequentialParser.extractText().orElse("")
        );
    }
  //@@author A0142230B
    @Test
    public void extractIndicesList_valid() throws IllegalValueException {
        sequentialParser.setInput("2to7");
        List<Integer> indices = sequentialParser.extractIndicesList();
        assertEquals("[2, 3, 4, 5, 6, 7]", indices.toString());
        sequentialParser.setInput("-2-7");
        indices = sequentialParser.extractIndicesList();
        assertEquals("[-2, -1, 0, 1, 2, 3, 4, 5, 6, 7]", indices.toString());
        sequentialParser.setInput("-2  -  7");
        indices = sequentialParser.extractIndicesList();
        assertEquals("[-2, -1, 0, 1, 2, 3, 4, 5, 6, 7]", indices.toString());
        sequentialParser.setInput("1 2 3 4 5");
        indices = sequentialParser.extractIndicesList();
        assertEquals("[1, 2, 3, 4, 5]", indices.toString());
        sequentialParser.setInput("2to2");
        indices = sequentialParser.extractIndicesList();
        assertEquals("[2]", indices.toString());
    }
  //@@author 
    @Test
    public void extractTrailingDateRange_recurrence() throws IllegalValueException {
        sequentialParser.setInput("walk nowhere from 28 Oct 2018 1200h to 29 Nov 2018 1300h yearly");
        Optional<DateRange> dateRange = sequentialParser.extractTrailingDateRange();
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
            "walk nowhere", sequentialParser.extractText().orElse("")
        );
    }

    @Test
    public void extractTrailingDateRange_recurrenceInvalid() throws IllegalValueException {
        sequentialParser.setInput("walk nowhere from 28 Oct 2018 1200h to 29 Nov 2018 1300h daily");
        thrown.expect(IllegalValueException.class);
        sequentialParser.extractTrailingDateRange();
    }
}
