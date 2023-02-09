package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static gitlet.Utils.*;

/**
 * Represents a gitlet repository.
 *
 * @author Saifullah Jailani
 */
public class Repository implements Serializable {
    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The .gitlet/commits directory.
     */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /**
     * The .gitlet/blobs directory.
     */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /**
     * The .gitlet/braches directory.
     */
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    /**
     * The .gitlet/staging directory .
     */
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /**
     * The .gitlet/stagingArea file that has the staging object.
     */
    public static final File STAGING_OBJECT = join(GITLET_DIR, "stagingArea");

    public static final File REMOTE = join(GITLET_DIR, "REMOTE");

    public static final File REPOSITORY = join(GITLET_DIR, "REPO");

    public static final File SPLITPOINTS = join(GITLET_DIR, "SPLITPOINTS");

    public static Branch currentBranch() {
        Staging staging = Staging.getStaging();
        return Branch.getBranch(staging.getCurrentBranch());
    }

    public static Commit getHead() {
        return Commit.getCommit(currentBranch().getHead());
    }

    public static void init() {
        if (GITLET_DIR.exists()) {
            throw new GitletException("A Gitlet version-control system already "
                    + "exists in the current directory.");
        } else {
            GITLET_DIR.mkdir();
            COMMITS_DIR.mkdir();
            BLOBS_DIR.mkdir();
            BRANCHES_DIR.mkdir();
            STAGING_DIR.mkdir();
            REMOTE.mkdir();


            Staging staging = new Staging();

            Commit initialCommit = new Commit();
            String commitId = initialCommit.serialize();

            Branch master = new Branch(staging.getCurrentBranch(), commitId);
            master.serialize();
            writeObject(REPOSITORY, Repository.class);
            SplitPoints.initializeTemp();
        }
    }

    public static Repository getRepository(File dir) {
        File repoFile = join(dir, "REPO");
        return readObject(repoFile, Repository.class);
    }


    public static SimpleDateFormat dateFormater() {
        return new SimpleDateFormat("EEE MMM dd HH:mm:ss YYYY Z", Locale.getDefault());
    }


    public static void rm(String fileName) {
        File file = join(CWD, fileName);
        Staging staging = Staging.getStaging();
        Commit parent = Repository.getHead();
        if (staging.getAdd().containsKey(fileName)) {
            staging.getAdd().remove(fileName);
            staging.serialize();
        } else if (parent.getFiles().containsKey(fileName)) {
            staging.getDelete().add(fileName);
            if (file.exists()) {
                restrictedDelete(fileName);
            }
            staging.serialize();
        } else {
            throw new GitletException("No reason to remove the file.");
        }

    }

    public static void log(String headPtr) {
        if (headPtr == null) {
            return;
        }
        Commit c = Commit.getCommit(headPtr);
        generateLog(c, headPtr);
        log(c.getParentId());
    }

    private static void generateLog(Commit c, String id) {
        System.out.println("===");
        System.out.println("commit " + id);
        System.out.println("Date: " + c.getTime());
        System.out.println(c.getMessage());
        System.out.println();


    }

    private static void generateLog(String id) {
        Commit c = Commit.getCommit(id);
        generateLog(c, id);
    }

    public static void globalLog() {
        List<String> commits = plainFilenamesIn(Repository.COMMITS_DIR);
        for (String com : commits) {
            generateLog(com);
        }
    }

    public static void find(String message) {
        String[] commits = Repository.COMMITS_DIR.list();
        boolean noSuchCommit = true;
        for (int i = 0; i < commits.length; i++) {
            Commit commit = Commit.getCommit(commits[i]);
            if (commit.getMessage().equals(message)) {
                System.out.println(commits[i]);
                noSuchCommit = false;
            }
        }
        if (noSuchCommit) {
            throw new GitletException("Found no commit with that message.");
        }
    }

    public static void status() {
        String[] b = Repository.BRANCHES_DIR.list();
        Set<String> branches = new TreeSet<>();
        Staging staging = Staging.getStaging();
        List<Set<String>> ls = staging.unTrackedModified();

        branches.add("*" + staging.getCurrentBranch());
        for (int i = 0; i < b.length; i++) {
            if (!b[i].equals(staging.getCurrentBranch())) {
                branches.add(b[i]);
            }
        }
        print("Branches", branches);
        print("Staged Files", staging.getAdd().keySet());
        print("Removed Files", staging.getDelete());
        print("Modifications Not Staged For Commit", ls.get(1),
                "(modified)", ls.get(2), "(deleted)");
        print("Untracked Files", ls.get(0));
    }

    private static void print(String header, Set<String> list, String set1,
                              Set<String> list2, String set2) {
        Set<String> files = new TreeSet<>(list);
        files.addAll(list2);
        System.out.println("=== " + header + " ===");
        String suffix;
        for (String s : files) {
            if (list.contains(s)) {
                suffix = set1;
            } else {
                suffix = set2;
            }
            System.out.println(s + " " + suffix);
        }
        System.out.println();
    }

    private static void print(String header, Set<String> list) {

        print(header, list, "", new TreeSet<>(), "");
    }

    public static void branch(String name) {
        File branch = join(Repository.BRANCHES_DIR, name);
        if (branch.exists()) {
            throw new GitletException("A branch with that name already exists.");
        }
        Branch curr = currentBranch();
        Branch newBranch = new Branch(name, curr.getHead());
        SplitPoints.addSplitPoint(curr.getHead());
        newBranch.serialize();
    }

    public static void rmBranch(String name) {
        if (name.equals(currentBranch().getName())) {
            throw new GitletException("Cannot remove the current branch.");
        }
        File branch = join(Repository.BRANCHES_DIR, name);
        if (!branch.exists()) {
            throw new GitletException("A branch with that name does not exist.");
        }
        branch.delete();
    }

    public static void checkout(String fileName, Commit head) {
        if (!head.getFiles().containsKey(fileName)) {
            throw new GitletException("File does not exist in that commit");
        }
        Blob blob = Blob.getBlob(head.getFiles().get(fileName));
        blob.checkout(fileName);
    }

    public static void checkout(String fileName, String commitId) {
        commitId = getFullCommitID(commitId);
        Commit commit = Commit.getCommit(commitId);
        checkout(fileName, commit);
    }

    public static void checkout(String branchName, boolean checkTree) {
        Staging staging = Staging.getStaging();
        File branchFile = join(Repository.BRANCHES_DIR, branchName);

        if (!branchFile.exists()) {
            throw new GitletException("No such branch exists.");
        }

        if (staging.getCurrentBranch().equals(branchName)) {
            throw new GitletException("No need to checkout the current branch.");
        }

        if (checkTree && !staging.workingTreeIsClean()) {
            throw new GitletException("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
        }

        Branch currentBranch = Repository.currentBranch();
        Branch targetBranch = Branch.getBranch(branchName);

        Commit currentBranchLastCommit = Commit.getCommit(currentBranch.getHead());
        Commit targetBranchLastCommit = Commit.getCommit(targetBranch.getHead());

        for (String fileName : currentBranchLastCommit.getFiles().keySet()) {
            if (!targetBranchLastCommit.getFiles().containsKey(fileName)) {
                restrictedDelete(fileName);
            }
        }


        for (String fileName : targetBranchLastCommit.getFiles().keySet()) {
            checkout(fileName, targetBranchLastCommit);
        }

        Staging.clear();
        staging.setCurrentBranch(branchName);
        staging.serialize();

    }

    public static void checkout(String branchName) {
        checkout(branchName, true);
    }

    public static String getFullCommitID(String id) {
        if (id.length() > 8) {
            File commit = join(COMMITS_DIR, id);
            if (!commit.exists()) {
                throw new GitletException("No commit with that id exists.");
            }
            return id;
        }
        List<String> commits = plainFilenamesIn(Repository.COMMITS_DIR);
        for (String com : commits) {
            String shortCom = com.substring(0, 8);
            if (id.equals(shortCom)) {
                return com;
            }
        }
        throw new GitletException("No commit with that id exists.");
    }

    public static List<String> isTreeClean(Commit head, Commit given) {
        List<String> files = plainFilenamesIn(CWD);
        for (String fileName : files) {
            if (!head.getFiles().containsKey(fileName) && given.getFiles().containsKey(fileName)) {
                Blob b = new Blob(fileName);
                if (!b.getiD().equals(given.getFiles().get(fileName))) {
                    throw new GitletException("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                }
            }
        }
        return files;
    }

    public static void reset(String commitId) {
        commitId = getFullCommitID(commitId);

        Commit head = getHead();
        Commit given = Commit.getCommit(commitId);

        List<String> files = isTreeClean(head, given);
        Set<String> allFiles = new TreeSet<>();
        allFiles.addAll(files);
        allFiles.addAll(given.getFiles().keySet());
        for (String file : allFiles) {
            if (given.getFiles().containsKey(file)) {
                checkout(file, given);
            } else {
                restrictedDelete(file);
            }
        }

        Branch current = currentBranch();
        current.setHead(commitId);
        current.serialize();

        Staging.clear();

    }


    public static void merge(String branch) {
        mergeErrorsHandler(branch);
        Branch givenBranch = Branch.getBranch(branch);
        Branch current = Repository.currentBranch();
        givenBranch.getSplitPoint(current);
        if (givenBranch.getSplitCommitId().equals(givenBranch.getHead())) {
            throw new GitletException("Given branch is an ancestor of the current branch.");
        }
        if (givenBranch.getSplitCommitId().equals(current.getHead())) {
            checkout(branch);
            throw new GitletException("Current branch fast-forwarded.");
        }


        Commit head = Commit.getCommit(current.getHead());
        Commit other = Commit.getCommit(givenBranch.getHead());
        Commit split = Commit.getCommit(givenBranch.getSplitCommitId());
        boolean mergeIt = mergeHelper(head, other, split);
        String logMessage = "Merged " + branch + " into " + current.getName() + ".";
        Commit.commit(logMessage, givenBranch.getHead());


        if (!mergeIt) {
            throw new GitletException("Encountered a merge conflict.");
        }

    }

    private static void mergeErrorsHandler(String branch) {
        Staging staging = Staging.getStaging();
        if (!staging.isEmpty()) {
            throw new GitletException("You have uncommitted changes.");
        }
        Branch given = Branch.getBranch(branch);
        Branch current = Repository.currentBranch();

        if (given.getName().equals(current.getName())) {
            throw new GitletException("Cannot merge a branch with itself.");
        }
        if (!staging.unTrackedModified().get(0).isEmpty()) {
            throw new GitletException("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
        }

    }

    public static String generateContentForModified(String head, String given) {
        return "<<<<<<< HEAD\n" + head + "=======\n" + given + ">>>>>>>\n";
    }

    public static boolean mergeHelper(Commit head, Commit other, Commit split) {
        boolean flag = true;
        Set<String> allFiles = new TreeSet<>();
        allFiles.addAll(head.getFiles().keySet());
        allFiles.addAll(other.getFiles().keySet());
        allFiles.addAll(split.getFiles().keySet());

        for (String file : allFiles) {
            if (split.contains(file) && head.contains(file) && other.contains(file)
                    && !head.moddified(file, split) && other.moddified(file, split)) {
                checkout(file, other);
                Staging.add(file);
            } else if (split.contains(file) && head.contains(file)
                    && other.contains(file) && head.moddified(file, split)
                    && !other.moddified(file, split)) {
                continue;
            } else if (head.moddifiedInSameWay(file, split, other)) {
                continue;
            } else if (!split.contains(file) && head.contains(file)
                    && !other.contains(file)) {
                continue;
            } else if (!split.contains(file) && other.contains(file)
                    && !head.contains(file)) {
                checkout(file, other);
                Staging.add(file);
            } else if (split.contains(file) && head.contains(file)
                    && !head.moddified(file, split) && !other.contains(file)) {
                rm(file);
            } else if (split.contains(file) && other.contains(file)
                    && !other.moddified(file, split) && !head.contains(file)) {
                continue;

            } else if (split.contains(file) && head.contains(file) && other.contains(file)
                    && head.moddified(file, split) && other.moddified(file, split)
                    && head.moddified(file, other)
                    || (split.contains(file) && head.contains(file) && !other.contains(file)
                    && head.moddified(file, split))
                    || (split.contains(file) && other.contains(file) && !head.contains(file)
                    && other.moddified(file, split))
                    || (!split.contains(file) && other.moddified(file, head))) {
                File f = join(CWD, file);
                flag = false;
                String headContent = "";
                String branchContent = "";
                if (head.contains(file)) {
                    headContent = Blob.getBlob(head.getBlobID(file)).getContent();
                }
                if (other.contains(file)) {
                    branchContent = Blob.getBlob(other.getBlobID(file)).getContent();
                }
                writeContents(f, generateContentForModified(headContent, branchContent));
                Staging.add(file);


            }

        }
        return flag;
    }


}
