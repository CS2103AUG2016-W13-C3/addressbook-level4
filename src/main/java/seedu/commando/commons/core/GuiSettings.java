package seedu.commando.commons.core;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

import javafx.stage.Screen;

/**
 * A Serializable class that contains the GUI settings.
 */
public class GuiSettings implements Serializable {

    private static final double DEFAULT_HEIGHT = 750;
    private static final double DEFAULT_WIDTH = 1000;
    private Double windowWidth;
    private Double windowHeight;
    private boolean isMaximized;

    private Point windowCoordinates;

    public GuiSettings() {
        this.windowHeight = DEFAULT_HEIGHT; 
        this.windowWidth = DEFAULT_WIDTH;
        this.windowCoordinates = null; // null represent no coordinates
        this.isMaximized = false;
    }

    public GuiSettings(double windowWidth, double windowHeight, int xPosition, int yPosition, boolean isMaximized) {
        this.windowHeight = windowHeight; 
        this.windowWidth = windowWidth;
        this.windowCoordinates = new Point(xPosition, yPosition);
        this.isMaximized = isMaximized;
    }

    public Double getWindowWidth() {
        return windowWidth;
    }

    public Double getWindowHeight() {
        return windowHeight;
    }
    
    public Point getWindowCoordinates() {
        return windowCoordinates;
    }
    
    public boolean getIsMaximized() {
        return isMaximized;
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
        return Objects.equals(windowWidth, o.windowWidth)
                && Objects.equals(windowHeight, o.windowHeight)
                && Objects.equals(windowCoordinates.x, o.windowCoordinates.x)
                && Objects.equals(windowCoordinates.y, o.windowCoordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowCoordinates);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Width : " + windowWidth + "\n");
        sb.append("Height : " + windowHeight + "\n");
        sb.append("Position : " + windowCoordinates);
        return sb.toString();
    }
}
