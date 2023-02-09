package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class TestMaxArrayDeque {
    @Test
    public void testMax() {
        IntComp comp = new IntComp();
        IntMin min = new IntMin();
        MaxArrayDeque<Integer> maxArr = new MaxArrayDeque<>(comp);
        assertEquals(null, maxArr.max());
        assertEquals(null, maxArr.max(min));
        maxArr.addLast(1);
        maxArr.addLast(2);
        maxArr.addLast(3);
        assertEquals(3, (long) maxArr.max());
        assertEquals(1, (long) maxArr.max(min));
        maxArr.addLast(22);
        assertEquals(22, (long) maxArr.max());
        assertEquals(1, (long) maxArr.max(min));
        maxArr.addLast(0);
        maxArr.addLast(12);
        assertEquals(22, (long) maxArr.max());
        assertEquals(0, (long) maxArr.max(min));
    }

    private static class IntComp implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a.compareTo(b);

        }
    }

    private static class IntMin implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return b.compareTo(a);
        }
    }
}
