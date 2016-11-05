package seedu.commando.commons.util;

import com.fasterxml.jackson.core.JsonParseException;

import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.util.JsonUtil;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests JSON Read and Write
 */
public class JsonUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @After
    public void remove() throws IOException {
        Files.deleteIfExists(Paths.get("testJsonUtil.json"));
    }
    
    @Test
    public void fromJsonString_invalidJson() throws IOException {
        thrown.expect(JsonParseException.class);
        JsonUtil.fromJsonString("Invalid JSON", JsonSerializable.class);
    }

    @Test
    public void fromJsonString_valid() throws IOException {
        JsonSerializable actual = JsonUtil.fromJsonString(JsonSerializable.JSON_STRING_REPRESENTATION, JsonSerializable.class);
        JsonSerializable expected = new JsonSerializable();
        expected.setTestValues();
        assertEquals(expected, actual);
    }

    @Test
    public void toJsonString_valid() throws IOException {
        JsonSerializable testInstance = new JsonSerializable();
        testInstance.setTestValues();
        String actual = JsonUtil.toJsonString(testInstance);

        assertEquals(JsonSerializable.JSON_STRING_REPRESENTATION, actual);
    }
    
    @Test
    public void saveReadJsonFile_valid() throws IOException, DataConversionException {
        JsonUtil.saveJsonFile(JsonSerializable.JSON_STRING_REPRESENTATION, "testJsonUtil.json");
        Optional<String> test = JsonUtil.readJsonFile("testJsonUtil.json", String.class);
        assertEquals(test.get(), (JsonSerializable.JSON_STRING_REPRESENTATION));
        
        test = JsonUtil.readJsonFile("wronttestJsonUtil.json", String.class);
        assertFalse(test.isPresent());
        

        thrown.expect(DataConversionException.class);
        JsonUtil.readJsonFile("testJsonUtil.json", Integer.class);
    }
}
