package seedu.commando.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import seedu.commando.commons.core.Config;
import seedu.commando.commons.core.GuiSettings;

import java.util.Objects;

//@@author A0139697H

/**
 * Represents the user's preferences.
 */
public class UserPrefs {
    private GuiSettings guiSettings = new GuiSettings();
    private StringProperty toDoListFilePath = new SimpleStringProperty(Config.DefaultToDoListFilePath);

    public UserPrefs() {
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    /**
     * Gets its to-do list file path,
     *
     * @return an observable string value of its to-do list file path.
     */
    public ObservableValue<String> getToDoListFilePath() {
        return toDoListFilePath;
    }

    /**
     * Sets its to-do list file path and triggers change listeners of {@link #getToDoListFilePath()}
     */
    public void setToDoListFilePath(String filePath) {
        toDoListFilePath.setValue(filePath);
    }


    /**
     * Sets its GUI settings to that given.
     * Does a deep copy of {@param guiSettings}.
     */
    public void setGuiSettings(GuiSettings guiSettings) {
        this.guiSettings = new GuiSettings(guiSettings);
    }

    /**
     * @see #setGuiSettings(GuiSettings)
     */
    public void setGuiSettings(double width, double height, int x, int y, boolean isMaximized) {
        guiSettings = new GuiSettings(width, height, x, y, isMaximized);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        // instanceof handles nulls
        return other == this
            || (other instanceof UserPrefs
            && (guiSettings.equals(((UserPrefs) other).guiSettings)
            && toDoListFilePath.getValue().equals(((UserPrefs) other).toDoListFilePath.getValue())));
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, toDoListFilePath);
    }

    @Override
    public String toString() {
        return String.join(",",
            "Gui Settings: " + guiSettings,
            "To-do List File Path: " + toDoListFilePath.getValue()
        );
    }

    /**
     * Gets a {@link JsonObject} that fully defines its current state.
     *
     * @return a json object that fully defines its state.
     */
    public JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.guiSettings = guiSettings;
        jsonObject.toDoListFilePath = toDoListFilePath.getValue();
        return jsonObject;
    }

    /**
     * Sets fields of user prefs to that of {@param jsonObject}.
     * If any of the fields are null, the default of user prefs are used.
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
