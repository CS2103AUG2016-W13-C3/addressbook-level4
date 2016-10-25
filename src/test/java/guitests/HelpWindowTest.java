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

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        assertTrue(helpWindowHandle.isWindowShowing(""));
        helpWindowHandle.closeWindow();
    }
    
    private void assertHelpWindowOpenAtCommand(HelpWindowHandle helpWindowHandle, String commandName) {
        assertTrue(helpWindowHandle.isWindowOpen());
        assertTrue(helpWindowHandle.isWindowShowing("add"));
        helpWindowHandle.closeWindow();
    }
}
