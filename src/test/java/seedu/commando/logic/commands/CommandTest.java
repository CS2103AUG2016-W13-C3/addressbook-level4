package seedu.commando.logic.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.todo.DateRange;
import seedu.commando.model.todo.ReadOnlyToDoList;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.ui.UiToDo;

import java.util.Optional;
import java.util.Set;

//@@author A0139697H
public class CommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String feedback = "Feedback";
    private Command command;
    private Model modelStub = new Model() {
        @Override
        public ReadOnlyToDoList getToDoList() {
            return null;
        }

        @Override
        public void changeToDoList(ToDoListChange change) throws IllegalValueException {

        }

        @Override
        public boolean undoToDoList() {
            return false;
        }

        @Override
        public boolean redoToDoList() {
            return false;
        }

        @Override
        public UnmodifiableObservableList<UiToDo> getUiEvents() {
            return null;
        }

        @Override
        public UnmodifiableObservableList<UiToDo> getUiTasks() {
            return null;
        }

        @Override
        public Optional<UiToDo> getUiToDoAtIndex(int index) {
            return null;
        }

        @Override
        public void clearUiToDoListFilter(boolean ifHistoryMode) {

        }

        @Override
        public void setUiToDoListFilter(Set<String> keywords, Set<Tag> tags, boolean ifHistoryMode) {

        }

		@Override
		public void setUiToDoListFilter(DateRange dateRange) {
			
		}
    };

    @Before
    public void setup() {
        command = new Command() {
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
        };
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

}