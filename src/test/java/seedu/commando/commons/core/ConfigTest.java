package seedu.commando.commons.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigTest {
    @Test
    public void getUserGuideAnchorForCommandWord_validCommandWord() {
        assertTrue(Config.getUserGuideAnchorForCommandWord("add").isPresent());
    }
    
    @Test
    public void getUserGuideAnchorForCommandWord_invalidCommandWord() {
        assertFalse(Config.getUserGuideAnchorForCommandWord("adds").isPresent());
    }
}
