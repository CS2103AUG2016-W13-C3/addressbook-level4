package seedu.commando.logic.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.commando.commons.core.EventsCenter;
import seedu.commando.logic.ModelStub;
import seedu.commando.model.Model;

//@@author A0139697H
public class CommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String feedback = "Feedback";
    private Command command;
    private Model modelStub = new ModelStub();

    @Before
    public void setUp() {
        command = new CommandStub();
    }

    @Test
    public void setModel_onlyModel_exceptionThrown() throws Command.NoModelException, Command.NoEventsCenterException {
        thrown.expect(Command.NoEventsCenterException.class);
        command.setModel(modelStub);
        command.execute();
    }

    @Test
    public void setEventsCenter_onlyEventsCenter_exceptionThrown() throws Command.NoModelException, Command.NoEventsCenterException {
        thrown.expect(Command.NoModelException.class);
        command.setEventsCenter(EventsCenter.getInstance());
        command.execute();
    }

    private class CommandStub extends Command {
        @Override
        public CommandResult execute() throws NoModelException, NoEventsCenterException {
            if (getModel() == null) {
                throw new NoModelException();
            }

            if (getEventsCenter() == null) {
                throw new NoEventsCenterException();
            }

            return new CommandResult(feedback);
        }
    }
}