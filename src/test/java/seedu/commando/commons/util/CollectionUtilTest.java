package seedu.commando.commons.util;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import seedu.commando.model.todo.Title;
import seedu.commando.model.todo.ToDo;

public class CollectionUtilTest {
    @Test
    public void isAnyNull_NoNull() {
        assertFalse(CollectionUtil.isAnyNull(new Object(), new String(), new ToDo(new Title(new String()))));
    }

    @Test
    public void isAnyNull_someNull() {
        assertTrue(CollectionUtil.isAnyNull(null, new Object(), new Object()));
    }

    @Test
    public void elementsAreUnique_NotUnique() {
        ArrayList<Object> test = new ArrayList<Object>();
        test.add(new Integer(0));
        test.add(new Integer(1));
        test.add(new Integer(2));
        test.add(new Integer(2));
        test.add("test0");
        test.add("test0");
        assertFalse(CollectionUtil.elementsAreUnique(test));
    }

    @Test
    public void elementsAreUnique_AllUnique() {
        ArrayList<Object> test = new ArrayList<Object>();
        test.add(new Integer(0));
        test.add(new Integer(1));
        test.add(new Integer(2));
        test.add("test0");
        assertTrue(CollectionUtil.elementsAreUnique(test));
    }
}
