package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

public class Staging implements Serializable {
    private final long serialVersionUID = 39489723L;

    /**
     * Key of the below maps are file name in CWD and values are Blob ID.
     */
    private final TreeMap<String, String> markedForAddition;
    private final Set<String> markedForDeletion;
    private String currentBranch;

    public Staging() {
        markedForAddition = new TreeMap<>();
        markedForDeletion = new TreeSet<>();
        currentBranch = "master";
        this.serialize();
    }

    public static Staging getStaging() {
        return readObject(Repository.STAGING_OBJECT, Staging.class);
    }

    public static void add(String fileName) {
        Blob b = new Blob(fileName);
        String parentID = Repository.currentBranch().getHead();
        Commit parentCmmit = Commit.getCommit(parentID);
        Staging staging = Staging.getStaging();
        staging.markedForDeletion.remove(fileName);
        if (parentCmmit.getFiles().containsKey(fileName)
                && parentCmmit.getFiles().get(fileName).equals(b.getiD())) {
            staging.markedForAddition.remove(fileName);
            staging.serialize();
            return;
        }
        if (staging.markedForAddition.containsKey(fileName)
                && staging.markedForAddition.get(fileName).equals(b.getiD())) {
            return;
        }
        staging.markedForAddition.put(fileName, b.getiD());
        b.serialize(Repository.STAGING_DIR);
        staging.serialize();
    }

    public static void clear() {
        Staging staging = Staging.getStaging();
        staging.markedForDeletion.clear();
        staging.markedForAddition.clear();
        File[] files = Repository.STAGING_DIR.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        staging.serialize();
    }

    private static boolean trackedInCommit(Commit c, String fileName) {

        return c.getFiles().containsKey(fileName);
    }

    public TreeMap<String, String> getAdd() {
        return markedForAddition;
    }

    public Set<String> getDelete() {
        return markedForDeletion;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(String branch) {
        currentBranch = branch;
    }

    public void serialize() {

        writeObject(Repository.STAGING_OBJECT, this);
    }

    public boolean isEmpty() {

        return markedForAddition.isEmpty() && markedForDeletion.isEmpty();
    }

    public List<Set<String>> unTrackedModified() {
        List<Set<String>> notTrackedModdified = new LinkedList<>();
        Set<String> notTracked = new TreeSet<>();
        Set<String> moddified = new TreeSet<>();
        Set<String> deleted = new TreeSet<>();
        Set<String> allFiles = new TreeSet<>();
        Set<String> files = new TreeSet<>(plainFilenamesIn(Repository.CWD));

        Commit head = Repository.getHead();

        allFiles.addAll(files);
        allFiles.addAll(markedForAddition.keySet());
        allFiles.addAll(markedForDeletion);
        allFiles.addAll(head.getFiles().keySet());

        for (String fileName : allFiles) {
            if (files.contains(fileName)) {
                Blob b = new Blob(fileName);
                if ((trackedInCommit(head, fileName)
                        && !b.getiD().equals(head.getFiles().get(fileName))
                        && !statged(fileName))) {
                    moddified.add(fileName);
                    continue;
                }
                if (markedForAddition.containsKey(fileName)) {
                    Blob b1 = Blob.getBlob(Repository.STAGING_DIR, markedForAddition.get(fileName));
                    if (!b1.getiD().equals(b.getiD())) {
                        moddified.add(fileName);
                        continue;
                    }
                }
            }
            if (markedForAddition.containsKey(fileName)
                    && !files.contains(fileName)) {
                deleted.add(fileName);
                continue;
            }

            if (!files.contains(fileName) && trackedInCommit(head, fileName)
                    && !markedForDeletion.contains(fileName)) {
                deleted.add(fileName);
                continue;
            }

            if (fileName.charAt(0) != '.' && !statged(fileName)
                    && !trackedInCommit(head, fileName)) {
                notTracked.add(fileName);
                continue;
            }

        }


        notTrackedModdified.add(notTracked);
        notTrackedModdified.add(moddified);
        notTrackedModdified.add(deleted);
        return notTrackedModdified;
    }

    private boolean statged(String s) {
        return markedForAddition.containsKey(s) || markedForDeletion.contains(s);
    }

    public boolean workingTreeIsClean() {
        List<Set<String>> st = unTrackedModified();
        return isEmpty() && st.get(0).isEmpty() && st.get(1).isEmpty();
    }

}
