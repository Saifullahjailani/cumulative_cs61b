package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.*;

/**
 * Represents a gitlet commit object.
 *
 * @author Saifullah Jailani
 */
public class Commit implements Serializable {

    private final TreeMap<String, String> files;
    private final String time;
    private final String parentId;
    private String message;
    private String secondParent;
    private String sha1;

    public Commit(String message, String parentId) {
        this.message = message;
        this.parentId = parentId;
        this.time = Repository.dateFormater().format(new Date());
        this.files = new TreeMap<>();
        Commit parent = Commit.getCommit(parentId);
        files.putAll(parent.files);
    }

    public Commit() {
        this.message = "initial commit";
        this.parentId = null;
        this.time = Repository.dateFormater().format(new Date(0));
        this.files = new TreeMap<>();
    }

    public static Commit getCommit(String id) {
        File commitFile = join(Repository.COMMITS_DIR, id);
        if (!commitFile.exists()) {
            throw new GitletException("No commit with that id exists.");
        }
        return readObject(commitFile, Commit.class);
    }

    public static void commit(String message, String secondParent) {
        Staging staged = Staging.getStaging();
        Branch currentBranch = Branch.getBranch(staged.getCurrentBranch());
        if (message == null || message.isEmpty()) {
            throw new GitletException("Please enter a commit message.");
        }
        if (staged.isEmpty()) {
            throw new GitletException("No changes added to the commit");
        }
        Commit currentCommit = new Commit(message, currentBranch.getHead());
        currentCommit.secondParent = secondParent;
        Set<String> fileNameSet = staged.getAdd().keySet();
        for (String f : fileNameSet) {
            String blobID = staged.getAdd().get(f);
            Blob tempBlob = Blob.getBlob(Repository.STAGING_DIR, blobID);
            tempBlob.serialize();
            currentCommit.files.put(f, blobID);
        }
        for (String f : staged.getDelete()) {
            currentCommit.files.remove(f);
        }
        Staging.clear();
        currentBranch.setHead(currentCommit.serialize());
        currentBranch.serialize();
    }

    public static void commit(String message) {
        Commit.commit(message, null);
    }

    public String getId() {
        return sha1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public TreeMap<String, String> getFiles() {
        return files;
    }

    public String getParentId() {
        return parentId;
    }

    public String serialize() {
        sha1 = sha1(Utils.serialize(this));
        File commitFile = join(Repository.COMMITS_DIR, sha1);
        writeObject(commitFile, this);
        return sha1;
    }

    public boolean contains(String file) {

        return files.containsKey(file);
    }

    public String getBlobID(String fileName) {

        return files.get(fileName);
    }

    public boolean moddified(String file, Commit split) {

        return !split.getBlobID(file).equals(files.get(file));
    }

    public boolean moddifiedInSameWay(String file, Commit split, Commit other) {
        if (split.contains(file) && files.containsKey(file)
                && other.contains(file)
                && moddified(file, split) && other.moddified(file, split)
                && !moddified(file, other)) {
            return true;
        }
        return split.contains(file) && !other.contains(file) && !contains(file);
    }

    public String getSecondParent() {
        return secondParent;
    }

}
