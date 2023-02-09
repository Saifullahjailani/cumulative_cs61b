package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeSet;

import static gitlet.Utils.*;

public class Branch implements Serializable {

    private static final long UID = 28938902L;
    private final String name;
    private String splitCommitId;
    private String lastCommit;
    private TreeSet<String> commits;

    public Branch(String name, String splitCommitID) {
        this.name = name;
        this.splitCommitId = splitCommitID;
        this.lastCommit = splitCommitID;
    }

    public static Branch getBranch(File dir, String name) {
        File branch = join(dir, name);
        return Branch.getBranch(branch);
    }

    public static Branch getBranch(File branch) {
        if (!branch.exists()) {
            throw new GitletException("A branch with that "
                    + "name does not exist ");
        }
        return readObject(branch, Branch.class);
    }

    public static Branch getBranch(String name) {
        return Branch.getBranch(Repository.BRANCHES_DIR, name);
    }

    public String getHead() {
        return lastCommit;
    }

    public void setHead(String head) {
        this.lastCommit = head;
    }

    public String getName() {
        return name;
    }

    public String getSplitCommitId() {
        return splitCommitId;
    }

    public void serialize(File dir) {
        File branch = join(dir, name);
        writeObject(branch, this);
    }

    public void serialize() {
        this.serialize(Repository.BRANCHES_DIR);
    }

    public TreeSet<String> history(TreeSet<String> hist, Commit c) {
        String firstParent = c.getParentId();
        String secondParent = c.getSecondParent();
        if (firstParent == null) {
            return hist;
        }
        hist.add(firstParent);
        hist.addAll(history(hist, Commit.getCommit(firstParent)));
        if (secondParent != null) {
            hist.add(secondParent);
            hist.addAll(history(hist, Commit.getCommit(secondParent)));
        }
        return hist;
    }

    public Commit getSplitPoint(Branch other) {
        SplitPoints.clearTemp();
        generateSpPoints(getHead());
        SplitPoints history = SplitPoints.getTemp();
        Commit otherHead = Commit.getCommit(other.getHead());
        Commit split = findSpPoint(otherHead);
        if (split != null) {
            splitCommitId = split.getId();
        }
        return split;
    }

    private void generateSpPoints(String commit) {
        SplitPoints allSplitPoints = SplitPoints.getSplitPoints();
        Commit pointer = Commit.getCommit(commit);
        if (pointer == null) {
            return;
        }
        if (allSplitPoints.contains(pointer.getId())) {
            SplitPoints.addToTemp(pointer.getId());
        }
        if (pointer.getSecondParent() != null) {
            generateSpPoints(pointer.getSecondParent());
        }
        if (pointer.getParentId() != null) {
            generateSpPoints(pointer.getParentId());
        }


    }

    private Commit findSpPoint(Commit c) {
        SplitPoints sp = SplitPoints.getTemp();
        if (c == null) {
            return c;
        }
        LinkedList<Commit> queue = new LinkedList<>();
        queue.add(c);

        while (!queue.isEmpty()) {
            Commit curr = queue.poll();
            if (sp.contains(curr.getId())) {
                return curr;
            }
            if (curr.getParentId() != null) {
                queue.add(Commit.getCommit(curr.getParentId()));
            }
            if (curr.getSecondParent() != null) {
                queue.add(Commit.getCommit(curr.getSecondParent()));

            }
        }
        return null;

    }

}


