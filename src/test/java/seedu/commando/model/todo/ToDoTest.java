package seedu.commando.model.todo;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.testutil.ToDoBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

//@@author A0139697H
public class ToDoTest {
    private ToDo toDo;

    @Before
    public void setup() throws IllegalValueException {
        toDo = new ToDo(new Title("title"));
    }

    @Test
    public void ToDo() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("title"));
        assertEquals(toDo.getTitle(), new Title("title"));
        assertTrue(toDo.getDateCreated().toLocalDate().equals(LocalDate.now()));
    }

    @Test
    public void ToDo_copyConstructor_equals() throws IllegalValueException {
        ToDo toDo = new ToDo(new Title("title"))
            .setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")))
            .setIsFinished(true);

        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2001, 10, 8, 15, 59),
            Recurrence.Weekly
        ));
        toDo.setDueDate(new DueDate(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            Recurrence.Yearly
        ));

        ToDo newToDo = new ToDo(toDo);
        assertEquals(newToDo, toDo);
    }

    @Test
    public void setTitle_validTitle_getTitleEquals() throws IllegalValueException {
        toDo.setTitle(new Title("set title"));
        assertEquals(toDo.getTitle(), new Title("set title"));
    }

    @Test
    public void setTags_validTags_getTagsEquals() throws IllegalValueException {
        assertTrue(toDo.getTags().size() == 0);
        toDo.setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
        assertEquals(toDo.getTags(), Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
    }

    @Test
    public void setDueDate_validDueDate_getDueDateEquals() throws IllegalValueException {
        assertFalse(toDo.getDueDate().isPresent());
        toDo.setDueDate(new DueDate(LocalDateTime.of(2001, 10, 8, 12, 59)));
        assertTrue(toDo.getDueDate().isPresent());
        assertEquals(toDo.getDueDate().get(),
            new DueDate(LocalDateTime.of(2001, 10, 8, 12, 59))
        );
    }

    @Test
    public void setDateRange_validDateRange_getDateRangeEquals() throws IllegalValueException {
        assertFalse(toDo.getDateRange().isPresent());
        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2001, 10, 8, 13, 59)
        ));
        assertTrue(toDo.getDateRange().isPresent());
        assertEquals(toDo.getDateRange().get(), new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2001, 10, 8, 13, 59)
        ));
    }

    @Test
    public void setDateRange_recurringDateRange_getDateRangeConsidersRecurrence() throws IllegalValueException {
        assertFalse(toDo.getDateRange().isPresent());
        toDo.setDateRange(new DateRange(
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().minusDays(1),
            Recurrence.Yearly
        ));
        assertTrue(toDo.getDateRange().isPresent());
        assertEquals(toDo.getDateRange().get(), new DateRange(
            LocalDateTime.now().minusDays(2).plusYears(1),
            LocalDateTime.now().minusDays(1).plusYears(1),
            Recurrence.Yearly
        ));
    }

    @Test
    public void setIsFinished_trueThenFalse_getDateFinishedEqualsNowThenEmpty() throws IllegalValueException {
        assertFalse(toDo.isFinished());
        assertFalse(toDo.getDateFinished().isPresent());
        toDo.setIsFinished(true);
        assertTrue(toDo.getDateFinished().isPresent());
        assertTrue(toDo.getDateFinished().get().toLocalDate().equals(LocalDate.now()));
        toDo.setIsFinished(false);
        assertFalse(toDo.isFinished());
        assertFalse(toDo.getDateFinished().isPresent());
    }

    @Test
    public void setIsFinished_withRecurringDueDate_advancesDueDate() throws IllegalValueException {
        toDo.setDueDate(new DueDate(
            LocalDateTime.now().minusDays(1),
            Recurrence.Yearly
        ));

        assertEquals(new DueDate(
            LocalDateTime.now().minusDays(1),
            Recurrence.Yearly
        ), toDo.getDueDate().get());

        toDo.setIsFinished(true);
        assertFalse(toDo.isFinished());

        assertEquals(new DueDate(
            LocalDateTime.now().minusDays(1).plusYears(1),
            Recurrence.Yearly
        ), toDo.getDueDate().get());

        toDo.setIsFinished(true);
        assertFalse(toDo.isFinished());

        assertEquals(new DueDate(
            LocalDateTime.now().minusDays(1).plusYears(2),
            Recurrence.Yearly
        ), toDo.getDueDate().get());
    }

    @Test
    public void setGetDateFinished_withDateRange_dateFinishedAlwaysEqualsEndDate() throws IllegalValueException {
        LocalDateTime endDate = LocalDateTime.of(2012, 11, 2, 1, 23);
        toDo.setDateRange(
            new DateRange(
                LocalDateTime.of(2011, 11, 2, 1, 23),
                endDate
            )
        );
        assertEquals(endDate, toDo.getDateFinished().get());
        toDo.setDateFinished(LocalDateTime.now());
        assertEquals(endDate, toDo.getDateFinished().get());
    }

    @Test
    public void setGetDateFinished_withRecurringDateRange_dateFinishedAlwaysEmpty() throws IllegalValueException {
        LocalDateTime endDate = LocalDateTime.of(2011, 11, 2, 2, 23);
        toDo.setDateRange(
            new DateRange(
                LocalDateTime.of(2011, 11, 2, 1, 23),
                endDate,
                Recurrence.Weekly
            )
        );
        assertFalse(toDo.getDateFinished().isPresent());
        toDo.setDateFinished(LocalDateTime.now());
        assertFalse(toDo.getDateFinished().isPresent());
    }

    @Test
    public void setGetDateFinished_withRecurringDueDate_dateFinishedAlwaysEmpty() throws IllegalValueException {
        LocalDateTime dueDate = LocalDateTime.of(2011, 11, 2, 2, 23);
        toDo.setDueDate(
            new DueDate(
                dueDate,
                Recurrence.Weekly
            )
        );
        assertFalse(toDo.getDateFinished().isPresent());
        toDo.setDateFinished(LocalDateTime.now());
        assertFalse(toDo.getDateFinished().isPresent());
    }


    @Test
    public void setDateFinished_validDateTime_getDateFinishedEquals() throws IllegalValueException {
        LocalDateTime datetime = LocalDateTime.of(2011, 11, 2, 1, 23);
        toDo.setDateFinished(datetime);
        assertEquals(datetime, toDo.getDateFinished().orElse(null));
    }

    @Test
    public void setDateCreated_validDateTime_getDateCreatedEquals() throws IllegalValueException {
        LocalDateTime datetime = LocalDateTime.of(2011, 11, 2, 1, 23);
        toDo.setDateCreated(datetime);
        assertEquals(datetime, toDo.getDateCreated());
    }

    @Test
    public void equalsAndHashCode_sameFields_equals() throws IllegalValueException {
        ReadOnlyToDo toDo1 = new ToDoBuilder("title")
            .withDateRange(LocalDateTime.MIN, LocalDateTime.MAX)
            .withDueDate(LocalDateTime.MAX)
            .withTags("tag1", "tag2")
            .finish(LocalDateTime.MAX)
            .build();

        ReadOnlyToDo toDo2 = new ToDoBuilder("title")
            .withDateRange(LocalDateTime.MIN, LocalDateTime.MAX)
            .withDueDate(LocalDateTime.MAX)
            .withTags("tag1", "tag2")
            .finish(LocalDateTime.MAX)
            .build();

        assertEquals(toDo1, toDo2);
        assertEquals(toDo1.hashCode(), toDo2.hashCode());
    }

    @Test
    public void getObservableValue_allFields_changeListenerCalledEachFieldChange() throws IllegalValueException {
        List<String> changes = new LinkedList<>();
        toDo.getObservableValue().addListener((observable, oldValue, newValue) -> {
            changes.add(newValue);
        });

        toDo.setTitle(new Title("new title"));
        assertTrue(changes.size() == 1);

        toDo.setIsFinished(true);
        assertTrue(changes.size() == 2);

        toDo.setDateRange(new DateRange(
            LocalDateTime.of(2001, 10, 8, 12, 59),
            LocalDateTime.of(2002, 10, 8, 11, 59)
        ));
        assertTrue(changes.size() == 3);

        toDo.setDueDate(new DueDate(
            LocalDateTime.of(2001, 10, 8, 12, 59)
        ));
        assertTrue(changes.size() == 4);

        toDo.setTags(Sets.newHashSet(new Tag("tag1"), new Tag("tag2")));
        assertTrue(changes.size() == 5);

        toDo.setDateCreated(LocalDateTime.of(2011, 11, 11, 12, 12));
        assertTrue(changes.size() == 6);

        // with date range, date finished won't change
        toDo.clearTimeConstraint();
        assertTrue(changes.size() == 7);

        toDo.setDateFinished(LocalDateTime.now().plusYears(1));
        assertTrue(changes.size() == 8);
    }
}