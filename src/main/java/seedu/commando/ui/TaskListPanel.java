package seedu.commando.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.ui.ToDoListViewCell.Card;

/**
 * Panel containing the list of to-dos
 */
public class TaskListPanel extends UiPart {

    private static final String FXML = "TaskListPanel.fxml";

    private VBox panel;
    private AnchorPane placeHolderPane;
    private ScrollBar scrollbar;

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
        TaskListPanel taskListPanel = UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new TaskListPanel());
        taskListPanel.configure(list);
        return taskListPanel;
    }

    private void configure(ObservableList<UiToDo> toDos) {
        setConnections(toDos);
        addToPlaceholder();
        // @@author A0139080J
        Platform.runLater(() -> {
            scrollbar = (ScrollBar) taskListView.lookup(".scroll-bar:vertical");
        });
        // @@author
    }

    private void setConnections(ObservableList<UiToDo> tasks) {
        ObservableList<UiToDo> tasksForUi = FXCollections.observableArrayList(tasks);
        tasks.addListener(new ListChangeListener<UiToDo>() {
            @Override
            public void onChanged(Change<? extends UiToDo> c) {
                Platform.runLater(() -> tasksForUi.setAll(tasks));
            }
        });

        taskListView.setItems(tasksForUi);
        taskListView.setCellFactory(listView -> new ToDoListViewCell(taskListView, Card.Task));
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    // @@author A0139080J
    protected ListView<UiToDo> getTaskListView() {
        return taskListView;
    }

    private boolean isScrollBarPresent() {
        return scrollbar != null;
    }

    protected void scrollDown() {
        if (isScrollBarPresent()) {
            scrollbar.setValue(Math.min(scrollbar.getValue() + 0.1, 1));
        }
    }

    protected void scrollUp() {
        if (isScrollBarPresent()) {
            scrollbar.setValue(Math.max(scrollbar.getValue() - 0.1, 0));
        }
    }
    // @@author
}