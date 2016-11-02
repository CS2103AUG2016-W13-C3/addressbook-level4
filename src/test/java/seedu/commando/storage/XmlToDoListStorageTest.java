package seedu.commando.storage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.FileUtil;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.ToDoList;
import seedu.commando.testutil.ToDoBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XmlToDoListStorageTest {
    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlToDoListStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File tempFile;

    @Before
    public void setup() throws IOException {
        tempFile = folder.newFile();
    }

    @Test
    public void readToDoList_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readToDoList(null);
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readToDoList("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {
        thrown.expect(DataConversionException.class);
        readToDoList("NotXmlFormat.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void read_invalidDateFormats_dataNotRead() throws Exception {
        Optional<ReadOnlyToDoList> toDoList = readToDoList("InvalidDataFormats.xml");
        assertTrue(toDoList.isPresent());
        assertEquals(2, toDoList.get().getToDos().size());
        assertFalse(toDoList.get().getToDos().get(0).getDateFinished().isPresent());
        assertFalse(toDoList.get().getToDos().get(1).getDateFinished().isPresent());
        assertFalse(toDoList.get().getToDos().get(0).getDueDate().isPresent());
        assertFalse(toDoList.get().getToDos().get(1).getDueDate().isPresent());
        assertFalse(toDoList.get().getToDos().get(0).getDateRange().isPresent());
        assertFalse(toDoList.get().getToDos().get(1).getDateRange().isPresent());
    }


    @Test
    public void readAndSaveToDoList_noModifications() throws Exception {
        String filePath = tempFile.getAbsolutePath();
        ToDoList original = getSample();
        XmlToDoListStorage xmlStorage = new XmlToDoListStorage(filePath);

        // Save in new file and read back
        xmlStorage.saveToDoList(original, filePath);
        ReadOnlyToDoList readBack = xmlStorage.readToDoList(filePath).get();
        assertTrue(original.isSimilar(new ToDoList(readBack)));
    }

    @Test
    public void readAndSaveToDoList_modifyAndOverwrite() throws Exception {
        String filePath = tempFile.getAbsolutePath();
        ToDoList original = getSample();
        XmlToDoListStorage xmlStorage = new XmlToDoListStorage(filePath);

        // Modify data, overwrite exiting file, and read back
        xmlStorage.saveToDoList(original, filePath);
        original.add(new ToDoBuilder("value")
            .withTags("tag1", "tag2").build()
        );
        original.remove(original.getToDos().get(0));
        xmlStorage.saveToDoList(original, filePath);
        ReadOnlyToDoList readBack = xmlStorage.readToDoList(filePath).get();
        assertTrue(original.isSimilar(new ToDoList(readBack)));
    }

    @Test
    public void readAndSaveToDoList_filePathNotSpecified() throws Exception {
        String filePath = tempFile.getAbsolutePath();
        ToDoList original = getSample();
        XmlToDoListStorage xmlStorage = new XmlToDoListStorage(filePath);

        // Save and read without specifying file path
        xmlStorage.saveToDoList(original); //file path not specified
        ReadOnlyToDoList readBack = xmlStorage.readToDoList().get(); //file path not specified
        assertTrue(original.isSimilar(new ToDoList(readBack)));
    }

    @Test
    public void saveAddressBook_nullAddressBook_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveToDoList(null, "SomeFile.xml");
    }

    @Test
    public void saveAddressBook_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveToDoList(new ToDoList(), null);
    }

    private java.util.Optional<ReadOnlyToDoList> readToDoList(String filePath) throws Exception {
        return new XmlToDoListStorage(filePath).readToDoList(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
            : null;
    }

    private void saveToDoList(ReadOnlyToDoList toDoList, String filePath) throws IOException {
        new XmlToDoListStorage(filePath).saveToDoList(toDoList, addToTestDataPathIfNotNull(filePath));
    }
    
    private static ToDoList getSample() throws IllegalValueException {
        return new ToDoList()
            .add(new ToDoBuilder("valid title")
                    .withTags("tag1", "tag2" )
                    .withDueDate(LocalDateTime.of(2016, 5, 1, 20, 1))
                    .withDateRange(LocalDateTime.of(2016, 3, 1, 20, 1),
                                   LocalDateTime.of(2016, 4, 1, 20, 1))
                    .build())
            .add(new ToDoBuilder("valid title 2")
                    .finish(LocalDateTime.now())
                    .build());
    }
    
}
