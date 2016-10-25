package seedu.commando.ui;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import seedu.commando.model.ui.UiToDo;

//@@author A0139080J
class ToDoListViewCell extends ListCell<UiToDo> {

    protected enum Card {
        Event,
        Task
    }
    
    private Card chosenCard;
    
    public ToDoListViewCell(Card card) {
        chosenCard = card;
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