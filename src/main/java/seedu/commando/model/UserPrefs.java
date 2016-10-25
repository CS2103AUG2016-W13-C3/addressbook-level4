package seedu.commando.model;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.GuiSettings;

//@@author A0139697H
/**
 * Represents the user's preferences.
 */
public class UserPrefs {
    private GuiSettings guiSettings = new GuiSettings();
    private StringProperty toDoListFilePath = new SimpleStringProperty(Config.DefaultToDoListFilePath);

    public UserPrefs() {}

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public ObservableValue<String> getToDoListFilePath() {
        return toDoListFilePath;
    }

    public void setToDoListFilePath(String filePath) {
        toDoListFilePath.setValue(filePath);
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y, boolean isMaximized) {
        guiSettings = new GuiSettings(width, height, x, y, isMaximized);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { // this handles null as well.
            return false;
        }

        return (guiSettings.equals(((UserPrefs) other).guiSettings)
            && toDoListFilePath.getValue().equals(((UserPrefs) other).toDoListFilePath.getValue()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, toDoListFilePath);
    }

    @Override
    public String toString() {
        return String.join(",",
            "GuiSettings: " + guiSettings,
            "To-do List File Path: " + toDoListFilePath.getValue()
        );
    }

    public JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.guiSettings = guiSettings;
        jsonObject.toDoListFilePath = toDoListFilePath.getValue();
        return jsonObject;
    }

    /**
     * Sets fields of user prefs to that of the json object's
     * If any of the json object's fields are null, the default of user prefs are used
     */
    public void setJsonObject(JsonObject jsonObject) {
        if (jsonObject.guiSettings != null) {
            guiSettings = jsonObject.guiSettings;
        }

        if (jsonObject.toDoListFilePath != null) {
            toDoListFilePath.setValue(jsonObject.toDoListFilePath);
        }
    }

    public static class JsonObject {
        public GuiSettings guiSettings;
        public String toDoListFilePath;
    }
}
