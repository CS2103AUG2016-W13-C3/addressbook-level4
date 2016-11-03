package seedu.commando.storage;

import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
//@@author A0142230B

/**
 * JAXB-friendly version of the to-do
 */
class XmlAdaptedToDo {
    private static final Logger logger = LogsCenter.getLogger(XmlAdaptedToDo.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String MaxDateString = "MAX";
    private static final String MinDateString = "MIN";

    @XmlElement(required = true)
    private String title;
    @XmlElement(required = true)
    private String dueDate;
    @XmlElement(required = true)
    private String dateRangeStart;
    @XmlElement(required = true)
    private String dateRangeEnd;
    @XmlElement(required = true)
    private String dateCreated;
    @XmlElement(required = true)
    private String dateFinished;
    @XmlElement(required = true)
    private String dateRangeRecurrence;
    @XmlElement(required = true)
    private String dueDateRecurrence;

    @XmlElement
    private Set<String> tagged;

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedToDo() {
    }

    /**
     * Converts a given to-do into this class for JAXB use.
     */
    public XmlAdaptedToDo(ReadOnlyToDo toDo) {
        title = toDo.getTitle().value;

        if (toDo.getDateRange().isPresent()) {
            dateRangeStart = stringifyDateTime(toDo.getDateRange().get().startDate);
            dateRangeEnd = stringifyDateTime(toDo.getDateRange().get().endDate);
            dateRangeRecurrence = toDo.getDateRange().get().recurrence.toString();
        }

        if (toDo.getDueDate().isPresent()) {
            dueDate = stringifyDateTime(toDo.getDueDate().get().value);
            dueDateRecurrence = toDo.getDueDate().get().recurrence.toString();
        }

        if (toDo.getDateFinished().isPresent()) {
            dateFinished = stringifyDateTime(toDo.getDateFinished().get());
        }

        dateCreated = stringifyDateTime(toDo.getDateCreated());
        tagged = toDo.getTags().stream().map(tag -> tag.value).collect(Collectors.toSet());
    }

    /**
     * Converts this jaxb-friendly adapted ToDo object into the model's to-do
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted ToDo
     */
    public ToDo toModelType() throws IllegalValueException {
        final Title title = new Title(this.title);
        ToDo todo = new ToDo(title);

        if (this.tagged != null) {
            final Set<Tag> toDoTags = this.tagged.stream().map(Tag::new).collect(Collectors.toSet());

            todo.setTags(toDoTags);
        }

        // Check if the value is empty
        if (dueDate != null) {
            try {
                todo.setDueDate(new DueDate(
                    parseDateTime(dueDate),
                    parseRecurrence(dueDateRecurrence)
                ));
            } catch (DateTimeParseException exception) {
                // invalid due date, log it for debugging
                logger.warning("Unable to parse due date: " + dueDate);
            }
        }

        // Check if the dateRange is empty
        if (dateRangeStart != null && dateRangeEnd != null) {
            try {
                todo.setDateRange(new DateRange(
                    parseDateTime(dateRangeStart),
                    parseDateTime(dateRangeEnd),
                    parseRecurrence(dateRangeRecurrence)
                ));
            } catch (DateTimeParseException exception) {
                // invalid date range, log it for debugging
                logger.warning("Unable to parse date range: " + dateRangeStart + " - " + dateRangeEnd);
            }
        }

        // Check if the date finished is empty
        if (dateFinished != null) {
            try {
                todo.setDateFinished(parseDateTime(dateFinished));
            } catch (DateTimeParseException exception) {
                // invalid date finished, log it for debugging
                logger.warning("Unable to parse date finished: " + dateFinished);
            }
        }

        // Check if the date created is empty
        if (dateCreated != null) {
            try {
                todo.setDateCreated(parseDateTime(dateCreated));
            } catch (DateTimeParseException exception) {
                // invalid date created, log it for debugging
                logger.warning("Unable to parse date created: " + dateCreated);
            }
        }

        return todo;
    }

    private static Recurrence parseRecurrence(String recurrenceString) {
        Recurrence validRecurrence = Recurrence.None;
        if (recurrenceString != null) {
            try {
                validRecurrence = Recurrence.valueOf(recurrenceString);
            } catch (IllegalArgumentException exception) {
                // invalid recurrence, log it for debugging
                logger.warning("Unable to parse recurrence: " + recurrenceString);
            }
        }

        return validRecurrence;
    }

    private static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString.equals(MaxDateString)) {
            return LocalDateTime.MAX;
        } else if (dateTimeString.equals(MinDateString)) {
            return LocalDateTime.MIN;
        } else {
            return LocalDateTime.parse(dateTimeString, dateFormatter);
        }
    }

    private static String stringifyDateTime(LocalDateTime localDateTime) {
       if (localDateTime.equals(LocalDateTime.MAX)) {
           return MaxDateString;
       } else if (localDateTime.equals(LocalDateTime.MIN)) {
           return MinDateString;
       } else {
           return dateFormatter.format(localDateTime);
       }
    }
}