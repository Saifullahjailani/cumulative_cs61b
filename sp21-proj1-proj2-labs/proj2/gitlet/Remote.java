package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeSet;

import static gitlet.Utils.*;

public class Remote implements Serializable {
    private String name;
    private String dir;

    public Remote(String name, String dir) {
        File f = join(Repository.REMOTE, name);
        if (f.exists()) {
            throw new GitletException("A remote with that name "
                    + "already exists.");
        }
        this.name = name;
        this.dir = dir;
    }

    public static void rmRemote(String name) {
        File f = join(Repository.REMOTE, name);
        if (!f.exists()) {
            throw new GitletException("A remote with that "
                    + "name does not exist.");
        }
    }

    public static Remote getRemote(String name) {
        File f = join(Repository.REMOTE, name);
        return readObject(f, Remote.class);
    }

    public static void push(String remoteName, String remoteBranch) {
        TreeSet<String> history = new TreeSet<>();
        history.add(Repository.currentBranch().getHead());
        history.addAll(Repository.currentBranch().history(history, Repository.getHead()));
        Remote remote = Remote.getRemote(remoteName);
        File f = new File(remote.getDir());
        if (!f.exists()) {
            throw new GitletException("Remote directory not found.");
        }
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void serialize() {
        File f = join(Repository.REMOTE, name);
        writeObject(f, this);
    }


}
