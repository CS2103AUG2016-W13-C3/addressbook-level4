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
 * Panel that will contain the ListView of events
 */
public class EventListPanel extends UiPart {

    private static final String FXML = "EventListPanel.fxml";

    private VBox panel;
    private AnchorPane placeHolderPane;
    private ScrollBar scrollbar;

    @FXML
    private ListView<UiToDo> eventListView;

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
            ObservableList<UiToDo> events) {
        EventListPanel eventListPanel = UiPartLoader.loadUiPart(primaryStage, eventListPlaceholder,
                new EventListPanel());
        eventListPanel.configure(events);
        return eventListPanel;
    }

    private void configure(ObservableList<UiToDo> events) {
        setConnections(events);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<UiToDo> events) {
        ObservableList<UiToDo> eventsForUi = FXCollections.observableArrayList(events);

        events.addListener(new ListChangeListener<UiToDo>() {
            @Override
            public void onChanged(Change<? extends UiToDo> c) {
                Platform.runLater(() -> eventsForUi.setAll(events));
                scrollbar = (ScrollBar) eventListView.lookup(".scroll-bar:vertical");
            }
        });

        Platform.runLater(() -> scrollbar = (ScrollBar) eventListView.lookup(".scroll-bar:vertical"));
        eventListView.setItems(eventsForUi);
        eventListView.setCellFactory(listView -> new ToDoListViewCell(eventListView, Card.Event));
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    // @@author A0139080J
    protected ListView<UiToDo> getEventListView() {
        return eventListView;
    }

    private boolean isScrollBarPresent() {
        return scrollbar != null;
    }

    protected void scrollDown() {
        if (isScrollBarPresent()) {
            Platform.runLater(() -> scrollbar.setValue(Math.min(scrollbar.getValue() + 0.2, 1)));
        }
    }

    protected void scrollUp() {
        if (isScrollBarPresent()) {
            Platform.runLater(() -> scrollbar.setValue(Math.max(scrollbar.getValue() - 0.2, 0)));
        }
    }
    // @@author
}
