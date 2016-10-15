package seedu.commando.commons.core;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

import javafx.stage.Screen;

/**
 * A Serializable class that contains the GUI settings.
 */
public class GuiSettings implements Serializable {

    public static final double MAX_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
    public static final double MAX_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    private static final double DEFAULT_HEIGHT = MAX_HEIGHT * (3 / 4);
    private static final double DEFAULT_WIDTH = MAX_WIDTH * (3 / 4);

    private Point windowCoordinates;

    public GuiSettings() {
        this.windowCoordinates = null; // null represent no coordinates
    }

    public GuiSettings(int xPosition, int yPosition) {
        this.windowCoordinates = new Point(xPosition, yPosition);
    }

    public Double getWindowWidth() {
        return DEFAULT_WIDTH;
    }

    public Double getWindowHeight() {
        return DEFAULT_HEIGHT;
    }
    
    public Point getWindowCoordinates() {
        return windowCoordinates;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof GuiSettings)){ //this handles null as well.
            return false;
        }

        GuiSettings o = (GuiSettings)other;

        return Objects.equals(windowCoordinates.x, o.windowCoordinates.x)
                && Objects.equals(windowCoordinates.y, o.windowCoordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowCoordinates);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Width : " + MAX_WIDTH + "\n");
        sb.append("Height : " + MAX_HEIGHT + "\n");
        sb.append("Position : " + windowCoordinates);
        return sb.toString();
    }
}
