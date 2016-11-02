package seedu.commando.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.assertToDoExistsFiltered;
import static seedu.commando.testutil.TestHelper.assertToDoNotExistsFiltered;
import static seedu.commando.testutil.TestHelper.wasToDoListChangedEventPosted;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.Logic;
import seedu.commando.testutil.EventsCollector;
import seedu.commando.testutil.ToDoBuilder;
//@@author A0142230B
public class ListCommandTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private Logic logic;
	private EventsCollector eventsCollector;

	@Before
	public void setup() throws IOException {
		logic = initLogic();
		eventsCollector = new EventsCollector();
	}

	@After
	public void teardown() {
		EventsCenter.clearSubscribers();
	}

	@Test
	public void execute_listOnly_ListCommandClearMessage() {
		CommandResult result = logic.execute("list");
		assertFalse(result.hasError());
		assertEquals(Messages.LIST_COMMAND_CLEAR, result.getFeedback());
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
	}

	@Test
	public void execute_listWithInValidDate_invalidCommandFormatMessage() {
		// with an invalid date
		CommandResult result = logic.execute("list a");
		assertTrue(result.hasError());
		assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, ListCommand.COMMAND_WORD) + "\n"
				+ Messages.getCommandFormatMessage("list").get(), result.getFeedback());
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));

		// with an valid date but invalid keywords(from..to OR on)
		result = logic.execute("list tmr");
		assertTrue(result.hasError());
		assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, ListCommand.COMMAND_WORD) + "\n"
				+ Messages.getCommandFormatMessage("list").get(), result.getFeedback());
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));

		// with an valid date and keywords but with recurrence keywords
		result = logic.execute("list on tmr daily");
		assertTrue(result.hasError());
		assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, ListCommand.COMMAND_WORD) + "\n"
				+ Messages.getCommandFormatMessage("list").get(), result.getFeedback());
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
	}

	@Test
	public void execute_listOnDate_listCorrectly() {
		logic.execute("add title from 12/11/2016 to 13/11/2016");
		logic.execute("add title2 from 14/11/2016 to 16/11/2016");
		logic.execute("add title3 by 12/11/2016");
		logic.execute("add title4 by 14/11/2016");

		eventsCollector.reset();
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		CommandResult result = logic.execute("list on 12/11/2016");

		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		assertFalse(result.hasError());
		
		//Events
		assertToDoExistsFiltered(logic, new ToDoBuilder("title")
				.withDateRange(LocalDateTime.of(2016, 11, 12, 0, 0), LocalDateTime.of(2016, 11, 13, 23, 59)).build());
		assertToDoNotExistsFiltered(logic, new ToDoBuilder("title2")
				.withDateRange(LocalDateTime.of(2016, 11, 14, 0, 0), LocalDateTime.of(2016, 11, 16, 23, 59)).build());
		
		//Tasks
		assertToDoExistsFiltered(logic, new ToDoBuilder("title3")
				.withDueDate(LocalDateTime.of(2016, 11, 12, 0, 0)).build());
		assertToDoNotExistsFiltered(logic, new ToDoBuilder("title4")
				.withDueDate(LocalDateTime.of(2016, 11, 14, 0, 0)).build());
	}
	
	@Test   
	public void execute_listStartDateInTheRange_listCorrectly() {
		logic.execute("add title from 12/11/2016 to 15/11/2016");
		logic.execute("add title2 from 14/11/2016 to 16/11/2016");
		logic.execute("add title3 by 12/11/2016");
		logic.execute("add title4 by 15/11/2016");
		logic.execute("add title5 from 16/11/2016 to 17/11/2016");
		logic.execute("add title6 by 17/11/2016");

		eventsCollector.reset();
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		CommandResult result = logic.execute("list from 12/11/2016 to 15/11/2016");

		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		assertFalse(result.hasError());
		
		//Events
		assertToDoExistsFiltered(logic, new ToDoBuilder("title")
				.withDateRange(LocalDateTime.of(2016, 11, 12, 0, 0), LocalDateTime.of(2016, 11, 15, 23, 59)).build());
		assertToDoExistsFiltered(logic, new ToDoBuilder("title2")
				.withDateRange(LocalDateTime.of(2016, 11, 14, 0, 0), LocalDateTime.of(2016, 11, 16, 23, 59)).build());
		assertToDoNotExistsFiltered(logic, new ToDoBuilder("title5")
				.withDateRange(LocalDateTime.of(2016, 11, 16, 0, 0), LocalDateTime.of(2016, 11, 17, 23, 59)).build());
		
		//Tasks
		assertToDoExistsFiltered(logic, new ToDoBuilder("title3")
				.withDueDate(LocalDateTime.of(2016, 11, 12, 0, 0)).build());
		assertToDoExistsFiltered(logic, new ToDoBuilder("title4")
				.withDueDate(LocalDateTime.of(2016, 11, 15, 0, 0)).build());
		assertToDoNotExistsFiltered(logic, new ToDoBuilder("title6")
				.withDueDate(LocalDateTime.of(2016, 11, 17, 0, 0)).build());
	}
	
	@Test   
	public void execute_listEndDateInTheRange_listCorrectly() {
		logic.execute("add title from 12/11/2016 to 13/11/2016");
		logic.execute("add title2 from 19/11/2016 to 20/11/2016");

		eventsCollector.reset();
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		CommandResult result = logic.execute("list from 13/11/2016 to 18/11/2016");

		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		assertFalse(result.hasError());
		
		assertToDoExistsFiltered(logic, new ToDoBuilder("title")
				.withDateRange(LocalDateTime.of(2016, 11, 12, 0, 0), LocalDateTime.of(2016, 11, 13, 23, 59)).build());
		assertToDoNotExistsFiltered(logic, new ToDoBuilder("title2")
				.withDateRange(LocalDateTime.of(2016, 11, 19, 0, 0), LocalDateTime.of(2016, 11, 20, 23, 59)).build());

	}
	
	@Test   
	public void execute_listLargerThanTheRange_listCorrectly() {
		logic.execute("add title from 10/11/2016 to 14/11/2016");

		eventsCollector.reset();
		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		CommandResult result = logic.execute("list from 12/11/2016 to 13/11/2016");

		assertFalse(wasToDoListChangedEventPosted(eventsCollector));
		assertFalse(result.hasError());

		assertToDoExistsFiltered(logic, new ToDoBuilder("title")
				.withDateRange(LocalDateTime.of(2016, 11, 10, 0, 0), LocalDateTime.of(2016, 11, 14, 23, 59)).build());

	}
}
