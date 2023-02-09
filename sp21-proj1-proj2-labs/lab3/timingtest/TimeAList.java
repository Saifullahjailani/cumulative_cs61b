package timingtest;
import edu.princeton.cs.algs4.In;
<<<<<<< HEAD
import edu.princeton.cs.algs4.Stopwatch;
import org.checkerframework.checker.units.qual.*;
=======
//import edu.princeton.cs.algs4.Stopwatch;
>>>>>>> ade5cc423926ccbba6d6e131d6b699c2b811077d

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        int maxNum = 128000;
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int i = 1000; i <= maxNum; i=2*i){
            Ns.addLast(i);
            times.addLast(timeCreation(i));
            opCounts.addLast(i);
        }
        printTimingTable(Ns, times, opCounts);

    }

    private static double timeCreation(int size){
        AList<Integer> Ns = new AList<>();
        Stopwatch sw = new Stopwatch();
        for (int i =0; i < size; i++){
            Ns.addLast(786);
        }
        return sw.elapsedTime();
    }
}
