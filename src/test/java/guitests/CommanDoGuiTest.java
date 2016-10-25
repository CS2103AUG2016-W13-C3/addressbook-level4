package guitests;

import guitests.guihandles.*;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxToolkit;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.model.todo.ReadOnlyToDo;
import guitests.utils.TestApp;
import guitests.utils.TestUtil;
import guitests.utils.TypicalTestToDos;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//@@author A0122001M

/**
 * A GUI Test class for CommanDo.
 */
public abstract class CommanDoGuiTest {

    public static final String SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.xml");

    /* The TestName Rule makes the current test name available inside test methods */
    @Rule
    public TestName name = new TestName();

    TestApp testApp;

    protected static TypicalTestToDos td = new TypicalTestToDos();

    /*
     *   Handles to GUI elements present at the start up are created in advance
     *   for easy access from child classes.
     */
    protected MainGuiHandle mainGui;
    protected MainMenuHandle mainMenu;
    protected EventListPanelHandle eventListPanel;
    protected TaskListPanelHandle taskListPanel;
    protected ResultDisplayHandle resultDisplay;
    protected CommandBoxHandle commandBox;
    private Stage stage;

    @BeforeClass
    public static void setupSpec() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage((stage) -> {
            mainGui = new MainGuiHandle(new GuiRobot(), stage);
            mainMenu = mainGui.getMainMenu();
            eventListPanel = mainGui.getEventListPanel();
            taskListPanel = mainGui.getTaskListPanel();
            resultDisplay = mainGui.getResultDisplay();
            commandBox = mainGui.getCommandBox();
            this.stage = stage;
        });

        EventsCenter.clearSubscribers();
        testApp = (TestApp) FxToolkit.setupApplication(() -> new TestApp(this::getInitialData, getToDoListFileLocation()));
        FxToolkit.showStage();
        while (!stage.isShowing());
        mainGui.focusOnMainApp();
    }

    /**
     * Override this in child classes to set the initial local data.
     * Return null to use the data in the data file at toDoListFilePath specified in file of {@link #getToDoListFileLocation()}
     */
    protected ToDoList getInitialData() {
        ToDoList todoList = TestUtil.generateEmptyToDoList();
        td.loadToDoListWithSampleData(todoList);
        return todoList;
    }

    /**
     * Override this in child classes to set the save file location.
     */
    protected String getToDoListFileLocation() {
        return SAVE_LOCATION_FOR_TESTING;
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    /**
     * Asserts the person shown in the card is same as the given person
     */
    public void assertMatching(ReadOnlyToDo todo, ToDoCardHandle card) {
        assertTrue(TestUtil.compareCardAndToDo(card, todo));
    }
    
    /**
     * Asserts the size of the person list is equal to the given number.
     */
    protected void assertListSize(int size) {
        int numberOfToDo = eventListPanel.getNumberOfToDo() + taskListPanel.getNumberOfToDo();
        assertEquals(size, numberOfToDo);
    }

    /**
     * Asserts the message shown in the Result Display area is same as the given string.
     * @param expected
     */
    protected void assertResultMessage(String expected) {
        assertEquals(expected, resultDisplay.getText());
    }
}
