package seedu.commando.ui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import seedu.commando.model.ui.UiToDo;

//@@author A0139080J
class ToDoListViewCell extends ListCell<UiToDo> {

    protected enum Card {
        Event, Task
    }

    private Card chosenCard;

    public ToDoListViewCell(ListView<UiToDo> list, Card card) {
        assert card != null;
        chosenCard = card;

        // Sets the maximum width of EventCard or TaskCard to be always equals
        // to the ListView containing it.
        prefWidthProperty().bind(list.widthProperty().subtract(10));
    }

    @Override
    protected void updateItem(UiToDo toDo, boolean empty) {
        super.updateItem(toDo, empty);
        if (empty || toDo == null) {
            setGraphic(null);
            setText(null);
        } else {
            HBox layout;
            if (chosenCard == Card.Event) {
                layout = EventCard.load(toDo, toDo.getIndex()).getLayoutState(toDo.isNew(), toDo.isFinished());
            } else {
                layout = TaskCard.load(toDo, toDo.getIndex()).getLayoutState(toDo.isNew(), toDo.isFinished());
            }
            setGraphic(layout);
        }
    }
}