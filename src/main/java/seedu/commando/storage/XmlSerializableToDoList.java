package seedu.commando.storage;

import javafx.collections.FXCollections;
import seedu.commando.commons.core.UnmodifiableObservableList;
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
public class XmlSerializableToDoList implements ReadOnlyToDoList {

    @XmlElement
    private List<XmlAdaptedToDo> toDos;
    {
        toDos = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableToDoList() {}

    /**
     * Conversion
     */
    public XmlSerializableToDoList(ReadOnlyToDoList src) {
           toDos.addAll(src.getToDos().stream().map(XmlAdaptedToDo::new).collect(Collectors.toList()));
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyToDo> getToDos() {
        List<ReadOnlyToDo> list = toDos.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));

        return new UnmodifiableObservableList<>(
            FXCollections.observableList(list)
        );
    }
}
