package byow.MapEngin;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author@ Saifullah Jaialni
 */
public class BinaryRoomTree implements Serializable {
    private BinaryRoom root;

    public BinaryRoomTree(Position p, int width, int height, long seed) {
        root = new BinaryRoom(p, width, height);
        root.setSeed(seed);
        root = root.splitTree(root);
    }

    public static void main(String[] args) {

        BinaryRoomTree btree = new BinaryRoomTree(new Position(2, 2), 60 - 4, 30 - 4, 24229l);
        BinaryRoom broom = btree.getTree();
        broom.print();
        System.out.println();


    }

    public void initializeRooms(int minWidth, int maxWidth, int minHeight, int maxHeight, long seed) {
        root.init(minWidth, maxWidth, minHeight, maxHeight, seed);
    }

    public BinaryRoom getTree() {
        return root.splitTree(root);
    }

    public ArrayList<BinaryRoom> getRooms() {
        return BinaryRoom.getLeafs(root);
    }
}
