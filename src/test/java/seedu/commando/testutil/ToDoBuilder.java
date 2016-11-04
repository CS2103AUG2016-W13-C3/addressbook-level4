package seedu.commando.testutil;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//@@author A0139697H

/**
 * Helps build a to-do in 1 line
 * Also contains sample to-dos that are relative to the current day
 */
public class ToDoBuilder {
    private ToDo toDo;

    public static LocalDateTime now = LocalDateTime.now();

    public static LocalDateTime OldestDate = now.minusDays(4);
    public static LocalDateTime OlderDate = now.minusDays(3);
    public static LocalDateTime OldDate = now.minusDays(2);
    public static LocalDateTime RecentDate = now.minusDays(1);
    public static LocalDateTime FutureDate = now.plusDays(1);
    public static LocalDateTime FutureDate2 = now.plusDays(2);
    public static LocalDateTime FutureDate3 = now.plusDays(2);

    public static ReadOnlyToDo TaskOldDueUnfinishedNewCreated =
        new ToDoBuilder("<DD|UF|>C").withDueDate(RecentDate).created(OldDate).build();

    public static ReadOnlyToDo TaskOldDueUnfinishedOldCreated =
        new ToDoBuilder("<DD|UF|<C").withDueDate(RecentDate).created(OlderDate).build();

    public static ReadOnlyToDo TaskNewDueUnfinishedNewCreated =
        new ToDoBuilder(">DD|UF|>C").withDueDate(FutureDate).created(OldDate).build();

    public static ReadOnlyToDo TaskNewDueUnfinishedOldCreated =
        new ToDoBuilder(">DD|UF|<C").withDueDate(FutureDate).created(OlderDate).build();

    public static ReadOnlyToDo TaskUnfinishedNewCreated =
        new ToDoBuilder("UF|>C").created(OldDate).build();

    public static ReadOnlyToDo TaskUnfinishedOldCreated =
        new ToDoBuilder("UF|<C").created(OlderDate).build();

    public static ReadOnlyToDo TaskOldFinished =
        new ToDoBuilder(">F").created(OldDate).finish(OldDate).build();

    public static ReadOnlyToDo TaskOlderFinished =
        new ToDoBuilder("<F").created(OldDate).finish(OlderDate).build();

    public static ReadOnlyToDo EventOldRangeUnfinishedNewCreated =
        new ToDoBuilder("<DR|UF|>C").withDateRange(
            FutureDate, FutureDate2
        ).created(OldDate).build();

    public static ReadOnlyToDo EventOldRangeUnfinishedOldCreated =
        new ToDoBuilder("<DR|UF|<C").withDateRange(
            FutureDate, FutureDate2
        ).created(OlderDate).build();

    public static ReadOnlyToDo EventNewRangeUnfinishedNewCreated =
        new ToDoBuilder(">DR|UF|>C").withDateRange(
            FutureDate2, FutureDate3
        ).created(OldDate).build();

    public static ReadOnlyToDo EventNewRangeUnfinishedOldCreated =
        new ToDoBuilder(">DR|UF|<C").withDateRange(
            FutureDate2, FutureDate3
        ).created(OlderDate).build();

    public static ReadOnlyToDo EventNewFinished =
        new ToDoBuilder(">F").withDateRange(
            OlderDate, OldDate
        ).build();

    public static ReadOnlyToDo EventOldFinished =
        new ToDoBuilder("<F").withDateRange(
            OldestDate, OlderDate
        ).build();

    public ToDoBuilder(String title) {
        toDo = new ToDo(new Title(title));
    }

    public ToDoBuilder withDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                     Recurrence recurrence) {
        try {
            toDo.setDateRange(new DateRange(
                startDate, endDate, recurrence
            ));
        } catch (IllegalValueException e) {
            assert false : "Test data should not be invalid";
        }

        return this;
    }

    public ToDoBuilder withDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            toDo.setDateRange(new DateRange(
                startDate, endDate
            ));
        } catch (IllegalValueException e) {
            assert false : "Test data should not be invalid";
        }

        return this;
    }

    public ToDoBuilder withDueDate(LocalDateTime dueDate) {
        toDo.setDueDate(
            new DueDate(dueDate)
        );

        return this;
    }

    public ToDoBuilder withDueDate(LocalDateTime dueDate, Recurrence recurrence) {
        toDo.setDueDate(
            new DueDate(dueDate, recurrence)
        );

        return this;
    }

    public ToDoBuilder withTags(String... tags) {
        Set<Tag> tagsSet = new HashSet<>();
        for (String tag : tags) {
            tagsSet.add(new Tag(tag));
        }

        toDo.setTags(tagsSet);

        return this;
    }

    public ToDoBuilder finish(LocalDateTime date) {
        toDo.setDateFinished(date);

        return this;
    }

    public ToDoBuilder created(LocalDateTime date) {
        toDo.setDateCreated(date);

        return this;
    }

    public ToDo build() {
        return toDo;
    }
}
