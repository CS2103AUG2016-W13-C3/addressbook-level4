package seedu.commando.storage;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

/**
 * JAXB-friendly version of the to-do
 */
public class XmlAdaptedToDo {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    private String recurrence;

    @XmlElement
    private Set<String> tagged;

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedToDo() {}

    /**
     * Converts a given to-do into this class for JAXB use.
     */
    public XmlAdaptedToDo(ReadOnlyToDo toDo) {
        title = toDo.getTitle().value;

        if (toDo.getDueDate().isPresent()) {
            dueDate = dateFormatter.format(toDo.getDueDate().get().value);
        }

        if (toDo.getDateRange().isPresent()) {
            dateRangeStart = dateFormatter.format(toDo.getDateRange().get().startDate);
            dateRangeEnd = dateFormatter.format(toDo.getDateRange().get().endDate);
            recurrence = toDo.getDateRange().get().recurrence.toString();
        }

        if (toDo.getDateFinished().isPresent()) {
            dateFinished = dateFormatter.format(toDo.getDateFinished().get());
        }

        dateCreated = dateFormatter.format(toDo.getDateCreated());
        tagged = toDo.getTags().stream().map(tag -> tag.value).collect(Collectors.toSet());
    }

    /**
     * Converts this jaxb-friendly adapted ToDo object into the model's to-do
     * @throws IllegalValueException if there were any data constraints violated in the adapted ToDo
     */
    public ToDo toModelType() throws IllegalValueException {
        final Title title = new Title(this.title);
        ToDo todo = new ToDo(title);

        if (this.tagged != null) {
            final Set<Tag> toDoTags = new HashSet<>();

            for (String tagValue : this.tagged) {
                toDoTags.add(new Tag(tagValue));
            }

            todo.setTags(toDoTags);
        }

        // Check if the value is empty
        if (dueDate != null){
        	try {
                todo.setDueDate(new DueDate(LocalDateTime.parse(dueDate, dateFormatter)));
            } catch (DateTimeParseException exception) {
                // invalid due date, ignore
            }
        }

        // Check if the dateRange is empty
        if (dateRangeStart != null && dateRangeEnd != null){

            Recurrence validRecurrence = Recurrence.None;
            if (recurrence != null) {
                try {
                    validRecurrence = Recurrence.valueOf(recurrence);
                } catch(IllegalArgumentException exception) {
                    // Invalid recurrence, ignore
                }
            }

            try {
                todo.setDateRange(new DateRange(
                    LocalDateTime.parse(dateRangeStart, dateFormatter),
                    LocalDateTime.parse(dateRangeEnd, dateFormatter),
                    validRecurrence
                ));
            } catch (DateTimeParseException exception) {
                // invalid date range, ignore
            }
        }

        // Check if the date finished is empty
        if (dateFinished != null){
            todo.setDateFinished(LocalDateTime.parse(dateFinished, dateFormatter));
        }

        // Check if the date created is empty
        if (dateCreated != null){
            todo.setDateCreated(LocalDateTime.parse(dateCreated, dateFormatter));
        }

        return todo;
    }
}