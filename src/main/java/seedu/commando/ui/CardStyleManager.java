package seedu.commando.ui;

import javafx.scene.Node;

//@@author A0139080J
/**
 * Contains the common functions and variables that EventCard and TaskCard
 * shares
 */
public class CardStyleManager {
    /**
     * Adds given style to all nodes providede
     */
    public static void addStyleAll(String style, Node... nodes) {
        for (Node node : nodes) {
            node.getStyleClass().add(style);
        }
    }

    /**
     * Removes given style to all nodes providede
     */
    public static void removeStyleAll(String style, Node... nodes) {
        for (Node node : nodes) {
            node.getStyleClass().remove(style);
        }
    }

    /**
     * @param dayDifference
     * @return colour code for a date label. The closer it is to today, the more
     *         red it will become, otherwise, tends towards green. If it is
     *         already over (neg), it is fully red.
     */
    protected static String getDateProximityGreen(int dayDifference) {
        if (dayDifference < 0) {
            return "#FF0000";
        } else if (dayDifference <= 1) {
            return "#882200";
        } else if (dayDifference <= 3) {
            return "#775500";
        } else if (dayDifference <= 7) {
            return "#778800";
        } else if (dayDifference <= 14) {
            return "#686033";
        } else {
            return "#386D33";
        }
    }

    /**
     * @param dayDifference
     * @return colour code for a date label. The closer it is to today, the more
     *         red it will become, otherwise, tends towards blue. If it is
     *         already over (neg), it is fully red.
     */
    protected static String getDateProximityBlue(int dayDifference) {
        if (dayDifference < 0) {
            return "#FF0000";
        } else if (dayDifference <= 1) {
            return "#DD0A00";
        } else if (dayDifference <= 3) {
            return "#AA2100";
        } else if (dayDifference <= 7) {
            return "#69326B";
        } else if (dayDifference <= 14) {
            return "#3C26A8";
        } else {
            return "#0011FF";
        }
    }

}
