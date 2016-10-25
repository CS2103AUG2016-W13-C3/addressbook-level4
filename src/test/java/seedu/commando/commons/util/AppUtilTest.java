package seedu.commando.commons.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.commando.commons.core.Config;
import seedu.commando.commons.util.AppUtil;
import seedu.commando.ui.MainWindow;

import static org.junit.Assert.assertNotNull;

public class AppUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getImage_exitingImage() {
        assertNotNull(AppUtil.getImage(Config.ApplicationIcon));
    }

    @Test
    public void getImage_nullGiven_assertionError() {
        thrown.expect(AssertionError.class);
        AppUtil.getImage(null);
    }
}
