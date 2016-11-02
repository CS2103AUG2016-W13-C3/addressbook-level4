package seedu.commando.ui;

//@@author A0139080J
/**
 * Contains the common functions and variables that EventCard and TaskCard
 * shares and additional styling choices of elements that are doen
 * programmatically
 */
public class ToDoCardStyleManager {

    // Styles for EventCard and TaskCard for their respective states:
    // Recently modified
    // Finished
    // Hover and non hover
    public static String recentlyModifiedStateCSS = "-fx-border-color: red";

    public static String finishedStateContentCSS = "-fx-background-color: derive(#1d1d1d, 95%);";
    public static String finishedStateDateCSS = "-fx-border-color: derive(#1d1d1d, 100%);"
            + "-fx-background-color: derive(#1d1d1d, 100%);";
    public static String finishedStateIndexCSS = "-fx-background-color: derive(#1d1d1d, 30%);";

    public static String activateHoverStateContentCSS = "-fx-background-color: derive(#DCDCDC, 50%);";
    public static String activateHoverStateIndexCSS = "-fx-background-color: derive(#1d1d1d, 40%);";

    public static String deactivateHoverStateContentCSS = "-fx-background-color: #DCDCDC;";
    public static String deactivateHoverStateIndexCSS = "-fx-background-color: derive(#1d1d1d, 60%);";

    /**
     * @param dayDifference
     * @return colour code for a date label. The closer it is to today, the more
     *         red it will become, otherwise, tends towards green. If it is
     *         already over (neg), it is fully red
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
     *         already over (neg), it is fully red
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
