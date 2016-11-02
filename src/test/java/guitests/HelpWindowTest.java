package guitests;

import guitests.guihandles.HelpWindowHandle;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

//@@author A0122001M

public class HelpWindowTest extends CommanDoGuiTest {

    @Test
    public void openHelpWindow() {

        assertHelpWindowOpen(commandBox.runHelpCommand());
        
        assertHelpWindowOpenAtCommand(commandBox.runHelpCommand("add"), "add");
    }
    
    /**
     * Runs help command and asserts that correct page and url is displayed
     * 
     * @param helpWindowHandle The current handle of help window 
     */
    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        assertTrue(helpWindowHandle.isWindowShowing(""));
        helpWindowHandle.closeWindow();
    }
    
    /**
     * Runs help command and asserts that correct page and url is displayed
     * and the page is positioned at the section of given command
     * 
     * @param helpWindowHandle      The current handle of help window 
     * @param command               The command name of section to be displayed
     */    
    private void assertHelpWindowOpenAtCommand(HelpWindowHandle helpWindowHandle, String commandName) {
        assertTrue(helpWindowHandle.isWindowOpen());
        assertTrue(helpWindowHandle.isWindowShowing("add"));
        helpWindowHandle.closeWindow();
    }
}
