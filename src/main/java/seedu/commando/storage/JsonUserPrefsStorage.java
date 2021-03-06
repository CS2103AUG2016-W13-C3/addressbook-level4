package seedu.commando.storage;

import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.exceptions.DataConversionException;
import seedu.commando.commons.util.FileUtil;
import seedu.commando.model.UserPrefs;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
//@@author A0142230B
/**
 * A class to access UserPrefs stored in the hard disk as a json file
 */
public class JsonUserPrefsStorage implements UserPrefsStorage{

    private static final Logger logger = LogsCenter.getLogger(JsonUserPrefsStorage.class);

    private String filePath;

    public JsonUserPrefsStorage(String filePath){
        this.filePath = filePath;
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return readUserPrefs(filePath);
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        saveUserPrefs(userPrefs, filePath);
    }

    @Override
    public void setUserPrefsFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Similar to {@link #readUserPrefs()}
     * @param prefsFilePath location of the data. Cannot be null.
     * @throws DataConversionException if the file format is not as expected.
     */
    public Optional<UserPrefs> readUserPrefs(String prefsFilePath) throws DataConversionException {
        assert prefsFilePath != null;

        File prefsFile = new File(prefsFilePath);

        if (!prefsFile.exists()) {
            logger.info("Prefs file "  + prefsFile + " not found");
            return Optional.empty();
        }

        UserPrefs prefs;

        try {

            UserPrefs.JsonObject jsonObject = FileUtil.deserializeObjectFromJsonFile(prefsFile, UserPrefs.JsonObject.class);
            prefs = new UserPrefs();
            prefs.setJsonObject(jsonObject);

        } catch (IOException e) {
            logger.warning("Error reading from prefs file " + prefsFile + ": " + e);
            throw new DataConversionException(e);
        }

        return Optional.of(prefs);
    }

    /**
     * Similar to {@link #saveUserPrefs(UserPrefs)}
     * @param prefsFilePath location of the data. Cannot be null.
     */
    public void saveUserPrefs(UserPrefs userPrefs, String prefsFilePath) throws IOException {
        assert userPrefs != null;
        assert prefsFilePath != null;

        FileUtil.serializeObjectToJsonFile(new File(prefsFilePath), userPrefs.getJsonObject());
    }
}
