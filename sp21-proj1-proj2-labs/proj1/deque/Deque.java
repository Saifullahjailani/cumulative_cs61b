package deque;

public interface Deque<T> {
    /**
     * Adds a non null item to the beginning of the Deque.
     */
    void addFirst(T item);

    /**
     * Adds a non null item to the last of the list.
     */
    void addLast(T item);

    /**
     * Returns true if the Deque is empty else return false.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the size of the Deque.
     */
    int size();

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    void printDeque();

    /**
     * Removes and returns the item at the front of the deque.
     */
    T removeFirst();

    /**
     * Removes and returns the item at the back of the deque.
     */
    T removeLast();

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists, returns null.
     */
    T get(int index);
}
