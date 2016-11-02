package guitests;

import com.google.common.collect.Sets;
import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.ToDo;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

//@@author A0122001M

public class FindCommandTest extends CommanDoGuiTest {

    @Test
    public void findCommand_nonEmptyList() {
        //no results
        assertFindResult("find titles", Sets.newHashSet("titles"), Collections.emptySet());
        
        //multiple results
        assertFindResult("find title 3", Sets.newHashSet("title", "3"), Collections.emptySet(), td.toDoItem3);
        assertFindResult("find title tag2", Sets.newHashSet("title", "tag2"), Collections.emptySet(), td.toDoItem2, td.toDoItem5);
        
        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find title 2", Sets.newHashSet("title", "2"), Collections.emptySet(), td.toDoItem5);
    }

    @Test
    public void findCommand_emptyList(){
        commandBox.runCommand("clear");
        
        //no results
        assertFindResult("find Jean", Sets.newHashSet("Jean"), Collections.emptySet());
    }

    @Test
    public void findCommand_invalidCommand_reportUnknownCommand() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "findgeorge"));
    }
    
    /**
     * Runs the find command to filter ToDoList according to given keywords
     * 
     * @param command        The find command to be executed.
     * @param keywords       Keywords that are used
     * @param tags           Tags that are used
     * @param expectedHits   The expected result list after filtering.
     */
    private void assertFindResult(String command, Set<String> keywords, Set<String> tags, ToDo... expectedHits) {
        Set<Tag> tagsSet = tags.stream().map(Tag::new).collect(Collectors.toSet());

        commandBox.runCommand(command);
        assertListSize(expectedHits.length); //number of expected todos = number of listed todos in result
        assertResultMessage(String.format(Messages.FIND_COMMAND, new TreeSet<>(keywords), new TreeSet<>(tagsSet)));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedHits));
    }
}
