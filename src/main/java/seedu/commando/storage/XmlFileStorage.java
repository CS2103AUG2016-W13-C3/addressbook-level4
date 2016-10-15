package seedu.commando.storage;

import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.util.XmlUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Stores ToDoList data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given toDoList data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableToDoList toDoList)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, toDoList);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns toDoList in the file or an empty toDoList
     */
    public static XmlSerializableToDoList loadDataFromSaveFile(File file)
        throws DataConversionException, FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableToDoList.class);
        } catch (JAXBException exception) {
            throw new DataConversionException(exception);
        }
    }
}
