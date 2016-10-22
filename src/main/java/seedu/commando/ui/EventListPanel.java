package seedu.commando.ui;

import javafx.application.Platform;
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

    protected static EventListPanel load(Stage primaryStage, AnchorPane eventListPlaceholder,
            ObservableList<UiToDo> eventsToday, ObservableList<UiToDo> eventsUpcoming) {
        EventListPanel eventListPanel =
                UiPartLoader.loadUiPart(primaryStage, eventListPlaceholder, new EventListPanel());
        eventListPanel.configure(eventsToday, eventsUpcoming);
        return eventListPanel;
    }

    private void configure(ObservableList<UiToDo> eventsToday, ObservableList<UiToDo> eventsUpcoming) {
        setConnections(eventsToday, eventsUpcoming);
        addToPlaceholder();
        scrollbar = (ScrollBar) eventListView.lookup(".scroll-bar:vertical");
    }

    private void setConnections(ObservableList<UiToDo> eventsToday, ObservableList<UiToDo> eventsUpcoming) {
        eventListView.setItems(eventsToday);
        eventListView.setCellFactory(listView -> new ToDoListViewCell(Card.Event));
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    protected void scrollTo(int index) {
        Platform.runLater(() -> {
            eventListView.scrollTo(index);
            eventListView.getSelectionModel().clearAndSelect(index);
        });
    }
    
    protected ListView<UiToDo> getEventListView() {
        return eventListView;
    }
    
    private boolean isScrollBarPresent() {
        return scrollbar != null;
    }
    
    protected void scrollDown() {
        if (isScrollBarPresent()) {
            scrollbar.increment();
        }
    }
    
    protected void scrollUp() {
        if (isScrollBarPresent()) {
            scrollbar.decrement();
        }
    }
}
