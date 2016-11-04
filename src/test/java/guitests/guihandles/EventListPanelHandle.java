package guitests.guihandles;


import guitests.GuiRobot;
import guitests.utils.TestUtil;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seedu.commando.model.todo.ToDo;
import seedu.commando.commons.core.Config;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

//@@author A0122001M

/**
 * Provides a handle for the panel containing the Todo list.
 */
public class EventListPanelHandle extends GuiHandle implements ToDoListPanelHandle {

    public static final int NOT_FOUND = -1;
    public static final String CARD_PANE_ID = "#eventPane";

    private static final String EVENT_LIST_VIEW_ID = "#eventListView";

    public EventListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, Config.ApplicationTitle);
    }

    public List<ReadOnlyToDo> getSelectedTodos() {
        ListView<ReadOnlyToDo> TodoList = getListView();
        return TodoList.getSelectionModel().getSelectedItems();
    }

    public ListView<ReadOnlyToDo> getListView() {
        return (ListView<ReadOnlyToDo>) getNode(EVENT_LIST_VIEW_ID);
    }
    
    /**
     * Returns true if the list is showing the Todo details correctly and in correct order.
     * @param Todos A list of Todo in the correct order.
     */
    public boolean isListMatching(ReadOnlyToDo... Todos) {
        return this.isListMatching(0, Todos);
    }

    /**
     * Clicks on the ListView.
     */
    public void clickOnListView() {
        Point2D point= TestUtil.getScreenMidPoint(getListView());
        guiRobot.clickOn(point.getX(), point.getY());
    }

    /**
     * Returns true if the {@code Todos} appear as the sub list (in that order) at position {@code startPosition}.
     */
    public boolean containsInOrder(int startPosition, ReadOnlyToDo... Todos) {
        List<ReadOnlyToDo> TodosInList = getListView().getItems();

        // Return false if the number of todos in the list is less than that in ui
        if (startPosition + Todos.length < TodosInList.size()){
            return false;
        }

        // Return false if any of the Todos doesn't match
        for (int i = 0; i < TodosInList.size(); i++) {
            if (!TodosInList.get(startPosition + i).getTitle().toString().equals(Todos[i].getTitle().toString())){
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if the list is showing the Todo details correctly and in correct order.
     * @param startPosition The starting position of the sub list.
     * @param Todos A list of Todo in the correct order.
     */
    public boolean isListMatching(int startPosition, ReadOnlyToDo... Todos) throws IllegalArgumentException {
        
        assertTrue(this.containsInOrder(startPosition, Todos));
        for (int i = 0; i < this.getNumberOfToDo(); i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> getListView().scrollTo(scrollTo));
            guiRobot.sleep(200);
            if (!TestUtil.compareCardAndToDo(getEventCardHandle(startPosition + i), Todos[i])) {
                return false;
            }
        }
        return true;
    }


    public EventCardHandle navigateToTodo(String title) {
        guiRobot.sleep(500); //Allow a bit of time for the list to be updated
        final Optional<ReadOnlyToDo> Todo = getListView().getItems().stream().filter(p -> p.getTitle().toString().equals(title)).findAny();
        if (!Todo.isPresent()) {
            return null;
        }

        return navigateToEvent(Todo.get());
    }

    /**
     * Navigates the listview to display and select the Todo.
     */
    public EventCardHandle navigateToEvent(ReadOnlyToDo Todo) {
        int index = getTodoIndex(Todo);

        guiRobot.interact(() -> {
            getListView().scrollTo(index);
            guiRobot.sleep(150);
            getListView().getSelectionModel().select(index);
        });
        guiRobot.sleep(100);
        return getEventCardHandle(Todo);
    }


    /**
     * Returns the position of the Todo given, {@code NOT_FOUND} if not found in the list.
     */
    public int getTodoIndex(ReadOnlyToDo targetTodo) {
        List<ReadOnlyToDo> TodosInList = getListView().getItems();
        for (int i = 0; i < TodosInList.size(); i++) {
            if(TodosInList.get(i).getTitle().equals(targetTodo.getTitle())){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Gets a Todo from the list by index
     */
    public ReadOnlyToDo getTodo(int index) {
        return getListView().getItems().get(index);
    }

    public EventCardHandle getEventCardHandle(int index) {
        return getEventCardHandle(new ToDo(getListView().getItems().get(index)));
    }

    public EventCardHandle getEventCardHandle(ReadOnlyToDo Todo) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> EventCardNode = nodes.stream()
                .filter(n -> new EventCardHandle(guiRobot, primaryStage, n).isSameToDo(Todo))
                .findFirst();
        if (EventCardNode.isPresent()) {
            return new EventCardHandle(guiRobot, primaryStage, EventCardNode.get());
        } else {
            return null;
        }
    }

    protected Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    public int getNumberOfToDo() {
        return getListView().getItems().size();
    }
}
