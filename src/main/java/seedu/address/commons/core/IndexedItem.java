package seedu.address.commons.core;

public class IndexedItem<T> {
    private T item;
    private int index;

    public IndexedItem(T item, int index) {
        this.item = item;
        this.index = index;
    }

    public T get() { return item; }

    public int getIndex() {
        return index;
    }
}
