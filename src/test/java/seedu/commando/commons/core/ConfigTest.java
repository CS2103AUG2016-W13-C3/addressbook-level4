package seedu.commando.commons.core;

import static org.junit.Assert.*;

import org.junit.Test;

//@@author A0122001M

public class ConfigTest {
    @Test
    public void getUserGuideAnchorForCommandWord_validCommandWord() {
        assertTrue(Config.getUserGuideAnchorForCommandWord("add").isPresent());
    }
    
    @Test
    public void getUserGuideAnchorForCommandWord_invalidCommandWord() {
        assertFalse(Config.getUserGuideAnchorForCommandWord("adds").isPresent());
        assertFalse(Config.getUserGuideAnchorForCommandWord("ad").isPresent());
    }
}
