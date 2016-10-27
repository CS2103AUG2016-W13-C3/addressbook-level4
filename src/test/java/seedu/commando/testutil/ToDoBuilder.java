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

    public static LocalDateTime OldestDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 4, 12, 0);
    public static LocalDateTime OlderDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 3, 12, 0);
    public static LocalDateTime OldDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 2, 12, 0);
    public static LocalDateTime RecentDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 1, 12, 0);
    public static LocalDateTime TodayPastDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() - 2, 0);
    public static LocalDateTime TodayPastDate2 = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() - 1, 0);
    public static LocalDateTime TodayFutureDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 1, 0);
    public static LocalDateTime TodayFutureDate2 = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 2, 0);
    public static LocalDateTime FutureDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, 12, 0);

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

    public static ReadOnlyToDo TaskTodayNewFinished =
        new ToDoBuilder(">Ft").created(OldDate).finish(TodayPastDate2).build();

    public static ReadOnlyToDo TaskTodayOldFinished =
        new ToDoBuilder("<Ft").created(OldDate).finish(TodayPastDate).build();

    public static ReadOnlyToDo TaskOldFinished =
        new ToDoBuilder(">F").created(OldDate).finish(OldDate).build();

    public static ReadOnlyToDo TaskOlderFinished =
        new ToDoBuilder("<F").created(OldDate).finish(OlderDate).build();

    public static ReadOnlyToDo EventOldRangeUnfinishedNewCreated =
        new ToDoBuilder("<DR|UF|>C").withDateRange(
            TodayFutureDate, TodayFutureDate2
        ).created(OldDate).build();

    public static ReadOnlyToDo EventOldRangeUnfinishedOldCreated =
        new ToDoBuilder("<DR|UF|<C").withDateRange(
            TodayFutureDate, TodayFutureDate2
        ).created(OlderDate).build();

    public static ReadOnlyToDo EventNewRangeUnfinishedNewCreated =
        new ToDoBuilder(">DR|UF|>C").withDateRange(
            TodayFutureDate2, FutureDate
        ).created(OldDate).build();

    public static ReadOnlyToDo EventNewRangeUnfinishedOldCreated =
        new ToDoBuilder(">DR|UF|<C").withDateRange(
            TodayFutureDate2, FutureDate
        ).created(OlderDate).build();

    public static ReadOnlyToDo EventTodayNewFinished =
        new ToDoBuilder(">Ft").withDateRange(
            TodayPastDate, TodayPastDate2
        ).build();

    public static ReadOnlyToDo EventTodayOldFinished =
        new ToDoBuilder("<Ft").withDateRange(
            OldDate, TodayPastDate
        ).build();

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
                                     Recurrence recurrence)
        throws IllegalValueException {
        toDo.setDateRange(new DateRange(
            startDate, endDate, recurrence
        ));

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
        try {
            toDo.setDueDate(
                new DueDate(dueDate)
            );
        } catch (IllegalValueException e) {
            assert false : "Test data should not be invalid";
        }

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
