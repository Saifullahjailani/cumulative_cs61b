package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comperatorClass;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.comperatorClass = c;
    }

    public T max() {
        return max(comperatorClass);
    }


    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T m = get(0);
        Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            T a = iter.next();
            int diff = c.compare(a, m);
            if (diff > 0) {
                m = a;
            }
        }
        return m;
    }

}
