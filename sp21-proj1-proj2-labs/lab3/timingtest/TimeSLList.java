package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import org.checkerframework.checker.units.qual.A;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }


    public static void timeGetLast() {
        AList<Integer> n = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCount = new AList<>();
        for (int i = 1000; i <= 128000; i = i * 2){
            n.addLast(i);
            times.addLast(timeReachingToLast(i));
            opCount.addLast(10000);
        }
        printTimingTable(n, times, opCount);

    }

    private static double timeReachingToLast(int size){
        SLList<Integer> Sn = new SLList<>();
        for (int i = 0; i < size; i++){
            Sn.addLast(i);
        }
        Stopwatch sw = new Stopwatch();
        for(int i = 0 ; i <= 10000; i++) {
            int last = Sn.getLast();
        }
        Double time = sw.elapsedTime();
        return time;
    }
}
