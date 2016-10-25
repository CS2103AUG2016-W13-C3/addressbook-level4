package guitests.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@@author A0122001M

public class LocalDateFormatter {
    static String toSimpleFormat(LocalDateTime ldt){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        return ldt.format(formatter) + "h";
    }
}
