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

public class RecallCommandTest extends CommanDoGuiTest {

    @Test
    public void recallCommand_nonEmptyList() {
        //recall when no finished items
        commandBox.runCommand("recall");
        assertListSize(0);
        
        //go back to current list
        commandBox.runCommand("find"); 

        //recall when some items are finished
        commandBox.runCommand("finish 4 5");
        
        //no results
        assertRecallResult("recall titles", Sets.newHashSet("titles"), Collections.emptySet());
        
        //multiple results
        assertRecallResult("recall title", Sets.newHashSet("title"), Collections.emptySet(), td.toDoItem5.setIsFinished(true), td.toDoItem1.setIsFinished(true));
        assertRecallResult("recall #tag2", Collections.emptySet(), Sets.newHashSet("tag2"), td.toDoItem5.setIsFinished(true));
    }

    @Test
    public void recallCommand_emptyList() {
        commandBox.runCommand("clear");
        assertRecallResult("recall title", Sets.newHashSet("title"), Collections.emptySet()); //no results
    }

    @Test
    public void recallCommand_invalidCommand_reportErrorMessage() {
        commandBox.runCommand("recalltest");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "recalltest"));
    }
    
    /**
     * Runs the recall command to filter ToDoList according to given keywords
     * 
     * @param command        The recall command to be executed.
     * @param keywords       Keywords that are used
     * @param tags           Tags that are used
     * @param expectedHits   The expected result list after filtering.
     */
    private void assertRecallResult(String command, Set<String> keywords, Set<String> tags, ToDo... expectedHits ) {
        Set<Tag> tagsSet = tags.stream().map(Tag::new).collect(Collectors.toSet());

        commandBox.runCommand(command);
        assertListSize(expectedHits.length);  //number of expected todos = number of listed todos
        
        assertResultMessage(String.format(Messages.RECALL_COMMAND, new TreeSet<>(keywords), new TreeSet<>(tagsSet)));
        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedHits));
    }
}
