package seedu.commando.testutil;

import javafx.collections.FXCollections;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ReadOnlyToDoList;

public class ToDoListStub implements ReadOnlyToDoList {

    @Override
    public UnmodifiableObservableList<ReadOnlyToDo> getToDos() {
        return new UnmodifiableObservableList<>(FXCollections.observableArrayList());
    }

    @Override
    public boolean contains(ReadOnlyToDo toDo) {
        return false;
    }

    @Override
    public boolean isSimilar(ReadOnlyToDoList toDoList) {
        return false;
    }

}