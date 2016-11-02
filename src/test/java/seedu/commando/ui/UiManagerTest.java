package seedu.commando.ui;

import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.eventbus.EventBus;

import seedu.commando.commons.events.storage.DataSavingExceptionEvent;

public class UiManagerTest {
    @Test
    public void handleDataSavingExceptionEvent() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        UiManager um = Mockito.mock(UiManager.class);
        EventBus uibus = new EventBus();
        uibus.register(um);
        DataSavingExceptionEvent event = new DataSavingExceptionEvent(new Exception("fail"));
        uibus.post(event);
    }
}
