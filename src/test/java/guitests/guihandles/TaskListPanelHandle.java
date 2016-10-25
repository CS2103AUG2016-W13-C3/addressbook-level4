package guitests.guihandles;


import guitests.GuiRobot;
import guitests.utils.TestApp;
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
public class TaskListPanelHandle extends GuiHandle implements ToDoListPanelHandle {

    public static final int NOT_FOUND = -1;
    public static final String CARD_PANE_ID = "#taskPane";

    private static final String TASK_LIST_VIEW_ID = "#taskListView";

    public TaskListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, Config.ApplicationTitle);
    }

    public List<ReadOnlyToDo> getSelectedTodos() {
        ListView<ReadOnlyToDo> TodoList = getListView();
        return TodoList.getSelectionModel().getSelectedItems();
    }

    public ListView<ReadOnlyToDo> getListView() {
        return (ListView<ReadOnlyToDo>) getNode(TASK_LIST_VIEW_ID);
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

        // Return false if the list in panel is too short to contain the given list
        if (Todos.length - startPosition < TodosInList.size()){
            return false;
        }

        // Return false if any of the Todos doesn't match
        for (int i = 0; i < TodosInList.size(); i++) {
            if (!TodosInList.get(i).getTitle().toString().equals(Todos[startPosition + i].getTitle().toString())){
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
        for (int i = 0; i < getNumberOfToDo(); i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> getListView().scrollTo(scrollTo));
            guiRobot.sleep(200);
            if (!TestUtil.compareCardAndToDo(getTaskCardHandle(i), Todos[startPosition + i])) {
                return false;
            }
        }
        return true;
    }

    public TaskCardHandle navigateToTodo(String title) {
        guiRobot.sleep(500); //Allow a bit of time for the list to be updated
        final Optional<ReadOnlyToDo> Todo = getListView().getItems().stream().filter(p -> p.getTitle().toString().equals(title)).findAny();
        if (!Todo.isPresent()) {
            return null;
        }

        return navigateToTask(Todo.get());
    }

    /**
     * Navigates the listview to display and select the Todo.
     */
    public TaskCardHandle navigateToTask(ReadOnlyToDo Todo) {
        int index = getTodoIndex(Todo);

        guiRobot.interact(() -> {
            getListView().scrollTo(index);
            guiRobot.sleep(150);
            getListView().getSelectionModel().select(index);
        });
        guiRobot.sleep(100);
        return getTaskCardHandle(Todo);
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

    public TaskCardHandle getTaskCardHandle(int index) {
        return getTaskCardHandle(new ToDo(getListView().getItems().get(index)));
    }

    public TaskCardHandle getTaskCardHandle(ReadOnlyToDo Todo) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> taskCardNode = nodes.stream()
                .filter(n -> new TaskCardHandle(guiRobot, primaryStage, n).isSameToDo(Todo))
                .findFirst();
        if (taskCardNode.isPresent()) {
            return new TaskCardHandle(guiRobot, primaryStage, taskCardNode.get());
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
