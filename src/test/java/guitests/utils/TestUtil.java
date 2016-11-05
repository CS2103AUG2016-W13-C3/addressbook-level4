package guitests.utils;

import com.google.common.io.Files;

import guitests.CommanDoGuiTest;
import guitests.guihandles.ToDoCardHandle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import junit.framework.AssertionFailedError;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.FileUtil;
import seedu.commando.commons.util.XmlUtil;
import seedu.commando.model.todo.*;
import seedu.commando.storage.XmlSerializableToDoList;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

//@@author A0122001M

/**
 * A utility class for test cases.
 */
public class TestUtil {

    public static final String LS = System.lineSeparator();

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    public static final ToDo[] SAMPLE_TODO_DATA = getSampleToDoData();

    public static final Tag[] SAMPLE_TAG_DATA = getSampleTagData();

    public static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
        try {
            executable.run();
        } catch (Throwable actualException) {
            if (actualException.getClass().isAssignableFrom(expected)) {
                return;
            }
            String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
                    actualException.getClass().getName());
            throw new AssertionFailedError(message);
        }
        throw new AssertionFailedError(
                String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
    }

    /**
     * Generate sample ToDo data 
     * 
     * @return array of sample ToDo data
     */
    private static ToDo[] getSampleToDoData() {
        try {
            //CHECKSTYLE.OFF: LineLength
            return new ToDo[]{
                new ToDo(new Title("test1")).setDateRange(new DateRange(LocalDateTime.now(), LocalDateTime.now().plusDays(2))).setIsFinished(false),
                new ToDo(new Title("test2")).setDateRange(new DateRange(LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusWeeks(2))).setIsFinished(false),
                new ToDo(new Title("test3")).setDueDate(new DueDate(LocalDateTime.now())).setIsFinished(false),
                new ToDo(new Title("test4")).setDueDate(new DueDate(LocalDateTime.now().plusDays(3))).setIsFinished(false),
                new ToDo(new Title("test5")),
                new ToDo(new Title("test6")),
            };
            //CHECKSTYLE.ON: LineLength
        } catch (IllegalValueException e) {
            assert false;
            // not possible
            return null;
        }
    }


    /**
     * Generate sample tags data
     * 
     * @return array of sample tags
     */
    private static Tag[] getSampleTagData() {
        return new Tag[]{
            new Tag("tag1"),
            new Tag("tag2")
        };
    }

    public static List<ToDo> generateSampleToDoData() {
        return Arrays.asList(SAMPLE_TODO_DATA);
    }

    /**
     * Appends the file name to the sandbox folder path.
     * Creates the sandbox folder if it doesn't exist.
     * 
     * @param fileName
     * @return the relative path of the file path
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    public static void createDataFileWithSampleData(String filePath) {
        createDataFileWithData(generateSampleStorageToDoList(), filePath);
    }

    public static <T> void createDataFileWithData(T data, String filePath) {
        try {
            File saveFileForTesting = new File(filePath);
            FileUtil.createIfMissing(saveFileForTesting);
            XmlUtil.saveDataToFile(saveFileForTesting, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... s) {
        createDataFileWithSampleData(CommanDoGuiTest.SAVE_LOCATION_FOR_TESTING);
    }

    public static ToDoList generateEmptyToDoList() {
        return new ToDoList();
    }

    public static XmlSerializableToDoList generateSampleStorageToDoList() {
        return new XmlSerializableToDoList(generateEmptyToDoList());
    }

    /**
     * Tweaks the {@code keyCodeCombination} to resolve the {@code KeyCode.SHORTCUT} to their
     * respective platform-specific keycodes
     */
    public static KeyCode[] scrub(KeyCodeCombination keyCodeCombination) {
        List<KeyCode> keys = new ArrayList<>();
        if (keyCodeCombination.getAlt() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.ALT);
        }
        if (keyCodeCombination.getShift() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.SHIFT);
        }
        if (keyCodeCombination.getMeta() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.META);
        }
        if (keyCodeCombination.getControl() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.CONTROL);
        }
        keys.add(keyCodeCombination.getCode());
        return keys.toArray(new KeyCode[]{});
    }

    public static boolean isHeadlessEnvironment() {
        String headlessProperty = System.getProperty("testfx.headless");
        return headlessProperty != null && headlessProperty.equals("true");
    }

    public static void captureScreenShot(String fileName) {
        File file = GuiTest.captureScreenshot();
        try {
            Files.copy(file, new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String descOnFail(Object... comparedObjects) {
        return "Comparison failed \n"
                + Arrays.asList(comparedObjects).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    public static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException,
                                                                           IllegalAccessException {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        // ~Modifier.FINAL is used to remove the final modifier from field so that its value is no longer
        // final and can be changed
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    public static void initRuntime() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.hideStage();
    }

    public static void tearDownRuntime() throws Exception {
        FxToolkit.cleanupStages();
    }

    /**
     * Gets private method of a class
     * Invoke the method using method.invoke(objectInstance, params...)
     *
     * Caveat: only find method declared in the current Class, not inherited from supertypes
     */
    public static Method getPrivateMethod(Class objectClass, String methodName) throws NoSuchMethodException {
        Method method = objectClass.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method;
    }

    public static void renameFile(File file, String newFileName) {
        try {
            Files.copy(file, new File(newFileName));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Gets mid point of a node relative to the screen.
     * @param node
     * @return
     */
    public static Point2D getScreenMidPoint(Node node) {
        double x = getScreenPos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScreenPos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    /**
     * Gets mid point of a node relative to its scene.
     * @param node
     * @return
     */
    public static Point2D getSceneMidPoint(Node node) {
        double x = getScenePos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScenePos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    /**
     * Gets the bound of the node relative to the parent scene.
     * @param node
     * @return
     */
    public static Bounds getScenePos(Node node) {
        return node.localToScene(node.getBoundsInLocal());
    }

    public static Bounds getScreenPos(Node node) {
        return node.localToScreen(node.getBoundsInLocal());
    }

    public static double getSceneMaxX(Scene scene) {
        return scene.getX() + scene.getWidth();
    }

    public static double getSceneMaxY(Scene scene) {
        return scene.getX() + scene.getHeight();
    }

    public static Object getLastElement(List<?> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Removes a subset from the list of todos.
     * @param todos The list of todos
     * @param todosToRemove The subset of todos.
     * @return The modified todos after removal of the subset from todos.
     */
    public static ToDo[] removeToDosFromList(final ToDo[] todos, ToDo... todosToRemove) {
        List<ToDo> listOfToDos = asList(todos);
        listOfToDos.removeAll(asList(todosToRemove));
        return listOfToDos.toArray(new ToDo[listOfToDos.size()]);
    }


    /**
     * Returns a copy of the list with the todo at specified index removed.
     * @param list original list to copy from
     * @param targetIndexInOneIndexedFormat e.g. index 1 if the first element is to be removed
     */
    public static ToDo[] removeToDoFromList(final ToDo[] list, int targetIndexInOneIndexedFormat) {
        return removeToDosFromList(list, list[targetIndexInOneIndexedFormat - 1]);
    }

    /**
     * Replaces todos[i] with a todo.
     * 
     * @param todos The array of todos.
     * @param todo The replacement todo
     * @param index The index of the todo to be replaced.
     * @return
     */
    public static ToDo[] replaceToDoFromList(ToDo[] todos, ToDo todo, int index) {
        todos[index] = todo;
        return todos;
    }

    /**
     * Appends todos to the array of todos.
     * @param todos A array of todos.
     * @param todosToAdd The todos that are to be appended behind the original array.
     * @return The modified array of todos.
     */
    public static ToDo[] addToDosToList(final ToDo[] todos, int idx, ToDo... todosToAdd) {
        List<ToDo> listOfToDos = asList(todos);
        List<ToDo> todoitem = asList(todosToAdd);
        listOfToDos.addAll(idx, todoitem);
        return listOfToDos.toArray(new ToDo[listOfToDos.size()]);
    }

    public static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareCardAndToDo(ToDoCardHandle card, ReadOnlyToDo todo) {
        return card.isSameToDo(todo);
    }
    
    
    /**
     * Retrieve the set of tags in the current todo
     * 
     * @param tags
     * @return array of tags
     */
    public static Tag[] getTagList(String tags) {
        if ("".equals(tags)) {
            return new Tag[]{};
        }

        final String[] split = tags.split(", ");

        final List<Tag> collect = Arrays.asList(split).stream().map(e -> {
            return new Tag(e.replaceFirst("Tag: ", ""));
        }).collect(Collectors.toList());

        return collect.toArray(new Tag[split.length]);
    }

}
