package seedu.commando.commons.core;

import javafx.collections.FXCollections;
import junit.framework.AssertionFailedError;
import seedu.commando.commons.core.UnmodifiableObservableList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import java.util.*;

public class UnmodifiableObservableListTest {

    List<Integer> backing;
    UnmodifiableObservableList<Integer> list;
    
    @Before
    public void setup() {
        backing = new ArrayList<>();
        backing.add(10);
        list = new UnmodifiableObservableList<>(FXCollections.observableList(backing));
    }

    @Test
    public void transformationListGenerators_correctBackingList() {
        assertSame(list.sorted().getSource(), list);
        assertSame(list.filtered(i -> true).getSource(), list);
    }
    
    @Test
    public void backinglist_operation() {
        assertFalse(list.isEmpty());
        assertTrue(list.contains(new Integer(10)));
        Object[] arr = list.toArray();
        assertEquals(arr[0], 10);
        Integer[] intarr = list.toArray(new Integer[0]);
        assertEquals(intarr[0], new Integer(10));
        ArrayList<Integer> arrlist = new ArrayList<Integer>();
        arrlist.add(10);
        assertTrue(list.containsAll(arrlist));
        assertTrue(list.equals(arrlist));
        assertEquals(list.indexOf(new Integer(10)), 0);
        assertEquals(list.lastIndexOf(new Integer(10)), 0);
        assertEquals(list.subList(0, 1).get(0), new Integer(10));
        assertTrue(list.stream().findFirst().isPresent());
        
        ListIterator<Integer> itr = list.listIterator();
        assertTrue(itr.hasNext());
        assertFalse(itr.hasPrevious());
        assertEquals(itr.nextIndex(), 0);
    }

    @Test
    public void mutatingMethods_disabled() {
        final Class<UnsupportedOperationException> ex = UnsupportedOperationException.class;

        assertThrows(ex, () -> list.add(0, 2));
        assertThrows(ex, () -> list.add(3));

        assertThrows(ex, () -> list.addAll(2, 1));
        assertThrows(ex, () -> list.addAll(backing));
        assertThrows(ex, () -> list.addAll(0, backing));

        assertThrows(ex, () -> list.set(0, 2));

        assertThrows(ex, () -> list.setAll(new ArrayList<Number>()));
        assertThrows(ex, () -> list.setAll(1, 2));

        assertThrows(ex, () -> list.remove(0, 1));
        assertThrows(ex, () -> list.remove(null));
        assertThrows(ex, () -> list.remove(0));

        assertThrows(ex, () -> list.removeAll(backing));
        assertThrows(ex, () -> list.removeAll(1, 2));

        assertThrows(ex, () -> list.retainAll(backing));
        assertThrows(ex, () -> list.retainAll(1, 2));

        assertThrows(ex, () -> list.replaceAll(i -> 1));

        assertThrows(ex, () -> list.sort(Comparator.naturalOrder()));

        assertThrows(ex, () -> list.clear());

        final Iterator<Integer> iter = list.iterator();
        iter.next();
        assertThrows(ex, iter::remove);

        final ListIterator<Integer> liter = list.listIterator();
        liter.next();
        assertThrows(ex, liter::remove);
        assertThrows(ex, () -> liter.add(5));
        assertThrows(ex, () -> liter.set(3));
        assertThrows(ex, () -> list.removeIf(i -> true));
    }

    private static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
        try {
            executable.run();
        } catch (Throwable actualException) {
            if (!actualException.getClass().isAssignableFrom(expected)) {
                String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
                        actualException.getClass().getName());
                throw new AssertionFailedError(message);
            } else return;
        }
        throw new AssertionFailedError(
                String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
    }
}
