package seedu.commando.storage;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ReadOnlyToDo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable ToDoList that is serializable to XML format
 */
@XmlRootElement(name = "todolist")
public class XmlSerializableToDoList {

    @XmlElement
    private List<XmlAdaptedToDo> toDos;

    {
        toDos = new ArrayList<>();
    }

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

    public List<ReadOnlyToDo> getToDos() throws IllegalValueException {
        List<ReadOnlyToDo> list = new ArrayList<>();

        for (XmlAdaptedToDo xmlAdaptedToDo : toDos) {
            list.add(xmlAdaptedToDo.toModelType());
        }

        return list;
    }
}
