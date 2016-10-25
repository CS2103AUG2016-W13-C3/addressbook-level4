package guitests.utils;

import seedu.commando.model.todo.ToDo;

//@@author A0122001M

public class CommandBuilder {
    public static String buildAddCommand(ToDo todo) {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + todo.getTitle().toString());
        
        if (todo.getDateRange().isPresent()) {
            sb.append(" from " + LocalDateFormatter.toSimpleFormat(todo.getDateRange().get().startDate) + " to " + LocalDateFormatter.toSimpleFormat(todo.getDateRange().get().endDate));
        }
        
        if (todo.getDueDate().isPresent()) {
            sb.append(" by" + " " + LocalDateFormatter.toSimpleFormat(todo.getDueDate().get().value));
        }
        
        if (todo.getTags() != null && todo.getTags().size()!=0) {
            todo.getTags().stream().forEach(s -> sb.append(" #" + s.value));
        }
        return sb.toString();
    }
}
