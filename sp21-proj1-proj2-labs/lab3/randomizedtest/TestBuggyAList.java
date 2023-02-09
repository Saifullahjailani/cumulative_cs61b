package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import net.sf.saxon.trans.SymbolicName;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> fine = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();

        for(int i = 0; i < 3; i++){
            fine.addLast(i);
            buggy.addLast(i);
        }
        assertEquals(fine.removeLast(), buggy.removeLast());
        assertEquals(fine.removeLast(), buggy.removeLast());
        assertEquals(fine.removeLast(), buggy.removeLast());
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();
        int N = 10000;
        for (int i = 0 ; i < N; i++){
            int operationNumber = StdRandom.uniform(4);
            switch (operationNumber){
                case 0:
                    int randVal = StdRandom.uniform(100);
                    correct.addLast(randVal);
                    buggy.addLast(randVal);
                    break;

                case 1:
                    int size = correct.size();
                    assertEquals(size, buggy.size());
                    break;

                case 2:
                    if (correct.size() > 0) {
                        assertEquals(correct.getLast(), buggy.getLast());
                    }
                    break;

                case 3:
                    if (correct.size() > 0){
                        assertEquals(correct.removeLast(), buggy.removeLast());
                    }
                    break;
            }
        }
    }
}
