package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

public class Blob implements Serializable {
    static final long UID = 3849323L;

    private final String content;
    private final String iD;

    public Blob(String fileName) {
        File f = join(Repository.CWD, fileName);
        if (!f.exists()) {
            throw new GitletException("File does not exist.");
        }
        content = readContentsAsString(f);
        iD = sha1(content);
    }

    public static Blob getBlob(String id) {

        return Blob.getBlob(Repository.BLOBS_DIR, id);
    }

    public static Blob getBlob(File dir, String id) {
        File blob = join(dir, id);
        if (!blob.exists()) {
            return null;
        }
        return readObject(blob, Blob.class);
    }

    public String getContent() {
        return content;
    }

    public String getiD() {
        return iD;
    }

    public void serialize() {
        this.serialize(Repository.BLOBS_DIR);
    }

    public void serialize(File dir) {
        File blob = join(dir, iD);
        writeObject(blob, this);
    }

    public void checkout(String fileName) {
        File target = join(Repository.CWD, fileName);
        writeContents(target, content);
    }

}
