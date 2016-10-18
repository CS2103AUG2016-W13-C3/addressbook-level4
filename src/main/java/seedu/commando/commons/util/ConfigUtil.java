
package seedu.commando.commons.util;

import seedu.commando.commons.core.Config;
import seedu.commando.commons.exceptions.DataConversionException;

import java.io.IOException;
import java.util.Optional;

/**
 * A class for accessing the Config File.
 */
public class ConfigUtil {

    public static Optional<Config> readConfig(String configFilePath) throws DataConversionException {
        return JsonUtil.readJsonFile(configFilePath, Config.class);
    }

    public static void saveConfig(Config config, String configFilePath) throws IOException {
        JsonUtil.saveJsonFile(config, configFilePath);
    }

}