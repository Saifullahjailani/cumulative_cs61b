package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private final Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<>();
        size = 0;
    }

    private LinkedListDeque(T item) {
        sentinel = new Node<>();
        sentinel.next = new Node<>(sentinel, item, sentinel);
        size = 1;
    }

    @Override
    public void addFirst(T item) {
        sentinel.next = new Node<>(sentinel, item, sentinel.next);
        size += 1;
    }

    @Override
    public void addLast(T item) {
        sentinel.prev.next = new Node<>(sentinel.prev, item, sentinel);
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> ptr = sentinel.next;
        while (ptr != sentinel) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        if (!isEmpty()) {
            System.out.println();
        }
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        Node<T> first = sentinel.next;
        sentinel.next = first.next;
        first.next.prev = sentinel;
        return first.item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        Node<T> last = sentinel.prev;
        sentinel.prev = last.prev;
        last.prev.next = sentinel;
        return last.item;
    }

    @Override
    public T get(int index) {
        Node<T> p = sentinel.next;
        while (p != sentinel) {
            if (index == 0) {
                return p.item;
            }
            index--;
            p = p.next;
        }
        return null;
    }

    public T getRecursive(int index) {
        return getRecursive(sentinel.next, index);
    }

    private T getRecursive(Node<T> n, int index) {
        if (n == sentinel) {
            return null;
        } else if (index == 0) {
            return n.item;
        }
        return getRecursive(n.next, index - 1);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
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

    private static class Node<T> {
        T item;
        Node<T> next;
        Node<T> prev;

        Node(Node<T> prev, T item, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
            prev.next = this;
            next.prev = this;
        }

        Node() {
            this.prev = this;
            this.next = this;
            this.item = null;

        }
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> p;

        LinkedListDequeIterator() {
            p = sentinel.next;
        }

        public boolean hasNext() {
            return p != sentinel;
        }

        public T next() {
            T item = p.item;
            p = p.next;
            return item;
        }
    }
}

