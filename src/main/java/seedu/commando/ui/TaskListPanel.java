package seedu.commando.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.commando.model.ui.UiToDo;

/**
 * Panel containing the list of to-dos
 */
public class TaskListPanel extends UiPart {

    private static final String FXML = "TaskListPanel.fxml";

    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<UiToDo> taskListView;

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

    protected static TaskListPanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
            ObservableList<UiToDo> list) {
        TaskListPanel taskListPanel =
                UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new TaskListPanel());
        taskListPanel.configure(list);
        return taskListPanel;
    }

    private void configure(ObservableList<UiToDo> toDos) {
        setConnections(toDos);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<UiToDo> list) {
        taskListView.setItems(list);
        taskListView.setCellFactory(listView -> new ToDoListViewCell());
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    protected void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class ToDoListViewCell extends ListCell<UiToDo> {

        public ToDoListViewCell() { }

        @Override
        protected void updateItem(UiToDo toDo, boolean empty) {
            super.updateItem(toDo, empty);

            if (empty || toDo == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(TaskCard.load(toDo, toDo.getIndex()).getLayoutState(toDo.isNew(), toDo.isFinished()));
            }
        }
    }
}
