package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeSet;

import static gitlet.Utils.*;

public class SplitPoints extends TreeSet<String> implements Serializable {
    private static final File TEMP = join(Repository.GITLET_DIR, "TempSP");

    public static SplitPoints getSplitPoints() {
        return readObject(Repository.SPLITPOINTS, SplitPoints.class);
    }

    public static void addSplitPoint(String commitId) {
        SplitPoints sp = SplitPoints.getSplitPoints();
        sp.add(commitId);
        sp.serialize();
    }

    public static SplitPoints getTemp() {
        return readObject(TEMP, SplitPoints.class);
    }

    public static void initializeTemp() {
        SplitPoints sp = new SplitPoints();
        writeObject(TEMP, sp);
        sp.serialize();
    }

    public static void addToTemp(String commit) {
        SplitPoints sp = getTemp();
        sp.add(commit);
        writeObject(TEMP, sp);
    }

    public static void clearTemp() {
        SplitPoints sp = new SplitPoints();
        sp.clear();
        writeObject(TEMP, sp);
    }

    public void serialize() {
        writeObject(Repository.SPLITPOINTS, this);
    }

}
