package seedu.address.ui;

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
import seedu.address.commons.events.ui.ToDoListPanelSelectionChangedEvent;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.todo.ReadOnlyToDo;

import java.util.logging.Logger;

/**
 * Panel containing the list of to-dos
 */
public class TaskListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private static final String FXML = "taskListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;
    private ObservableList<ReadOnlyToDo> taskList;

    @FXML
    private ListView<ReadOnlyToDo> taskListView;

    public TaskListPanel() {
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

    public static TaskListPanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
                                     ObservableList<ReadOnlyToDo> toDoList) {
        TaskListPanel taskListPanel =
                UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new TaskListPanel());
        taskListPanel.configure(toDoList);
        return taskListPanel;
    }

    private void configure(ObservableList<ReadOnlyToDo> toDos) {
        setConnections(toDos);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<ReadOnlyToDo> toDoList) {
        taskList = FXCollections.observableArrayList();
        extractEvents(toDoList);
        
        toDoList.addListener(new ListChangeListener<ReadOnlyToDo>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ReadOnlyToDo> change) {
                extractEvents(toDoList);
            }

        });
        
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new ToDoListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }
    
    /**
     * Extracts tasks from a todo list
     */
    private void extractEvents(ObservableList<ReadOnlyToDo> toDoList) {
        taskList.clear();
        for (ReadOnlyToDo toDo : toDoList) {
            if (!isEvent(toDo)) {
                taskList.add(toDo);
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
        taskListView.refresh();
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in to-do list panel changed to : '" + newValue + "'");
                raise(new ToDoListPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
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
