package seedu.commando.storage;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ToDoList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//@@author A0142230B
/**
 * An Immutable ToDoList that is serializable to XML format
 */
@XmlRootElement(name = "todolist")
public class XmlSerializableToDoList {

    @XmlElement
    private List<XmlAdaptedToDo> toDos = new ArrayList<>();

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableToDoList() {
    }

    /**
     * Conversion
     */
    public XmlSerializableToDoList(ReadOnlyToDoList toDos) {
        this.toDos.addAll(toDos.getToDos().stream().map(XmlAdaptedToDo::new).collect(Collectors.toList()));
    }

    public ReadOnlyToDoList getToDos() throws IllegalValueException {
        ToDoList toDoList = new ToDoList();

        for (XmlAdaptedToDo xmlAdaptedToDo : toDos) {
            toDoList.add(xmlAdaptedToDo.toModelType());
        }

        return toDoList;
    }
}
