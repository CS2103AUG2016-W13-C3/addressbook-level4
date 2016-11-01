package seedu.commando.commons.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

public class GuiSettingsTest {
    
    @Test
    public void equals_sameObject_returnTrue() {
        GuiSettings sample = Mockito.mock(GuiSettings.class);
        GuiSettings mockedGuiSettings = new GuiSettings(sample);
        GuiSettings testGuiSettings = new GuiSettings(sample);
        assertEquals(mockedGuiSettings, mockedGuiSettings);
        assertEquals(mockedGuiSettings, testGuiSettings);
    }
    
    @Test
    public void equals_differentObject_returnFalse() {
        GuiSettings sample = Mockito.mock(GuiSettings.class);
        GuiSettings mockedGuiSettings = new GuiSettings(sample);
        assertFalse(mockedGuiSettings.equals(new Object()));
    }
    
    @Test
    public void hashCodeAndToString_sameObject_returnTrue() {
        GuiSettings sample = Mockito.mock(GuiSettings.class);
        GuiSettings mockedGuiSettings = new GuiSettings(sample);
        assertEquals(mockedGuiSettings.hashCode(), mockedGuiSettings.hashCode());
        assertEquals(mockedGuiSettings.toString(), "Width: null,Height: null,Position: null");
    }
    
}
