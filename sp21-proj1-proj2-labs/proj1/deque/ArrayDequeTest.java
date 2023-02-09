package deque;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void randomTest() {
        Deque<Integer> a = new ArrayDeque<>();
        Deque<Integer> b = new LinkedListDeque<>();
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            int op = rand.nextInt(6);
            int num = rand.nextInt(10000);

            switch (op) {
                case 0:
                    a.addLast(num);
                    b.addLast(num);
                    assertTrue(a.equals(b));
                    break;

                case 1:
                    a.addFirst(num);
                    b.addFirst(num);
                    assertTrue(a.equals(b));
                    break;

                case 2:
                    if (a.size() > 0) {
                        assertEquals(a.removeLast(), b.removeLast());
                    }
                    break;

                case 3:
                    if (a.size() > 0) {
                        int index = rand.nextInt(a.size() + 1);
                        assertEquals(a.get(index), b.get(index));
                        assertTrue(a.equals(b));
                    }
                    break;

                case 4:
                    int size = a.size();
                    if (size > 0) {
                        int aLast = a.removeLast();
                        int bLast = b.removeLast();
                        assertEquals(aLast, bLast);
                        assertTrue(a.equals(b));
                    } else {
                        assertNull(a.removeLast());
                        assertNull(b.removeLast());
                    }
                    break;

                default:
                    if (!a.isEmpty()) {
                        int aFirst = a.removeFirst();
                        int bFirst = b.removeFirst();
                        assertEquals(aFirst, bFirst);
                        assertTrue(a.equals(b));
                    } else {
                        assertNull(a.removeFirst());
                        assertNull(b.removeFirst());
                    }
                    break;
            }
        }
    }

    @Test
    public void fillAndEmptyArrayADL() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        int len = 100;
        for (int i = 0; i < len; i++) {
            a.addLast(2);
        }
        for (int i = 0; i < len - 1; i++) {
            a.removeLast();
        }
        long expected = 2;
        assertEquals(expected, (long) a.removeLast());
        assertTrue(a.isEmpty());
    }
}
