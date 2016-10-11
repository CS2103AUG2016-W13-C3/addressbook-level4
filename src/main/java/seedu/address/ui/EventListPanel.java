package seedu.address.ui;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.events.model.ToDoListChangedEvent;
import seedu.address.commons.events.ui.ToDoListPanelSelectionChangedEvent;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.todo.ReadOnlyToDo;

import java.util.logging.Logger;

/**
 * Panel containing the list of to-dos
 */
public class EventListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);
    private static final String FXML = "eventListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;
    private ObservableList<ReadOnlyToDo> eventList;

    @FXML
    private ListView<ReadOnlyToDo> eventListView;

    public EventListPanel() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static EventListPanel load(Stage primaryStage, AnchorPane eventListPlaceholder,
                                     ObservableList<ReadOnlyToDo> toDoList) {
        EventListPanel eventListPanel =
                UiPartLoader.loadUiPart(primaryStage, eventListPlaceholder, new EventListPanel());
        eventListPanel.configure(toDoList);
        return eventListPanel;
    }

    private void configure(ObservableList<ReadOnlyToDo> toDos) {
        setConnections(toDos);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<ReadOnlyToDo> toDoList) {
        eventList = FXCollections.observableArrayList();
        extractEvents(toDoList);
        
        toDoList.addListener(new ListChangeListener<ReadOnlyToDo>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ReadOnlyToDo> change) {
                extractEvents(toDoList);
            }

        });
        
        eventListView.setItems(eventList);
        eventListView.setCellFactory(listView -> new ToDoListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }
    
    /**
     * Extracts events from a todo list
     */
    private void extractEvents(ObservableList<ReadOnlyToDo> toDoList) {
        eventList.clear();
        for (ReadOnlyToDo toDo : toDoList) {
            if (isEvent(toDo)) {
                eventList.add(toDo);
            }
        }
    }
    
    /**
     * Returns whether a todo item contains a date range
     */
    private boolean isEvent(ReadOnlyToDo todo) {
        if (todo.getDateRange().isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * Update the list and each list item
     */
    public void update() {
        eventListView.refresh();
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent() {
        eventListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in to-do list panel changed to : '" + newValue + "'");
                raise(new ToDoListPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            eventListView.scrollTo(index);
            eventListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class ToDoListViewCell extends ListCell<ReadOnlyToDo> {

        public ToDoListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyToDo toDo, boolean empty) {
            super.updateItem(toDo, empty);

            if (empty || toDo == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(EventCard.load(toDo, getIndex() + 1).getLayout());
            }
        }
    }
}
