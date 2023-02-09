package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private final int initialArraySize = 8;
    private T[] container;
    private int size;

    public ArrayDeque() {
        container = (T[]) new Object[initialArraySize];
        size = 0;
    }

    private ArrayDeque(T item) {
        container = (T[]) new Object[initialArraySize];
        container[size] = item;
        size += 1;
    }

    private void checkSizeOfArray() {
        if (size == container.length) {
            resize((int) Math.round(1.25 * size + 1));
        } else if (size < 16) {
            return;
        } else if (size < container.length / 4) {
            resize(container.length / 4);
        }
    }


    @Override
    public void addLast(T item) {
        checkSizeOfArray();
        container[size] = item;
        size += 1;
    }

    @Override
    public void addFirst(T item) {
        checkSizeOfArray();
        shifRight();
        container[0] = item;
    }

    private void resize(int newSize) {
        T[] newContiner = (T[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            newContiner[i] = container[i];
        }
        container = newContiner;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return container[index];
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T ret = container[size - 1];
        container[size - 1] = null;
        size -= 1;
        checkSizeOfArray();
        resize(size);
        return ret;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        T first = container[0];
        container[0] = null;
        shiftLeft();
        checkSizeOfArray();
        resize(size);
        return first;
    }

    private void shifRight() {

        for (int i = size; i != 0; i--) {
            container[i] = container[i - 1];
        }
        size += 1;
    }

    private void shiftLeft() {
        for (int i = 0; i < size; i++) {
            container[i] = container[i + 1];
        }
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public void printDeque() {
        StringBuilder toStr = new StringBuilder();
        for (int i = 0; i < size; i++) {
            T item = container[i];
            toStr.append(item.toString());
            toStr.append(" ");
        }
        System.out.println(toStr);
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (this == other) {
            return true;
        } else if (!(other instanceof Deque)) {
            return false;
        }
        Deque<T> o = (Deque<T>) other;
        if (o.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(get(i).equals(o.get(i)))) {
                return false;
            }
        }
        return true;
    }

    private class ArrayDequeIterator implements Iterator<T> {
        int currentPos;

        ArrayDequeIterator() {
            currentPos = 0;
        }

        public boolean hasNext() {
            return currentPos < size;
        }

        public T next() {
            currentPos++;
            return get(currentPos - 1);
        }
    }

}
