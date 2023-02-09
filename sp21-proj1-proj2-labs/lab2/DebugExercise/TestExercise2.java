package DebugExercise;
import org.junit.Assert;
import org.junit.Test;
public class TestExercise2 {
    @Test
    public void testArraySum(){
        int[] aa = {1,2,3,4,5};
        int expected = 15;
        int actual = DebugExercise2.arraySum(aa);
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void testSumOfElementMaxes(){
        int expected = 57;
        int[] a = {2, 0, 10, 14};
        int[] b = {-5, 5, 20, 30};
        int actual = DebugExercise2.sumOfElementwiseMaxes(a,b);
        Assert.assertEquals(expected, actual);
    }
    @Test
    public void testMax() {
        int a = 10;
        int b = 3;
        int max = DebugExercise2.max(a,b);
        Assert.assertEquals(10, max);

        int aa = 10;
        int bb = -3;
        int max2 = DebugExercise2.max(a,b);
        Assert.assertEquals(10, max2);
    }

}
