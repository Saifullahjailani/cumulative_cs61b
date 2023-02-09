package tester;
import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;

import java.util.Deque;
import java.util.Random;

public class TestArrayDequeEC {

    @Test
    public void randomTest() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> tester = new ArrayDequeSolution<>();
        int loop = (int) Math.round(Math.random() * 1000 + 1000);
        Random rand = new Random();
        StringBuilder message = new StringBuilder();
        for(int i = 0; i < loop; i++){
            int num = rand.nextInt(loop);
            int op = rand.nextInt(4);
        switch (op){
            case 0:
                student.addLast(num);
                tester.addLast(num);
                message.append("\naddLast(" + num+")");
                break;
            case 1:
                student.addFirst(num);
                tester.addFirst(num);
                message.append("\naddFirst(" + num+")");
                break;
            case 2:
                    if(!student.isEmpty()) {
                        Integer st = student.removeLast();
                        Integer t = tester.removeLast();
                        message.append("\nremoveLast()");
                        assertEquals(message.toString(), t, st);
                    }
                break;
            case 3:
                if(!student.isEmpty()) {
                    Integer st = student.removeFirst();
                    Integer t = tester.removeFirst();
                    message.append("\nremoveFirst()");
                    assertEquals(message.toString(), t, st);
                }
                break;
        }

        }

    }
}
