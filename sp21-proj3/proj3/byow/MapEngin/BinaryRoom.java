package byow.MapEngin;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author@ Saifullah Jaialni
 */
public class BinaryRoom extends Room {
    public int minWidth = 7;
    public int minHeight = 7;
    public int maxWidth = 9;
    public int maxHeight = 9;
    private long seed;
    private Random rand = new Random(seed);
    private final int halwayWidht = 3;
    private final int maxRoomWidth = 8;
    private final int minRoomWidth = 6;
    private final int shift = halwayWidht;
    private BinaryRoom leftRoom;
    private BinaryRoom rightRoom;

    public BinaryRoom(Position p, int width, int height) {
        super(p, width, height);
        leftRoom = null;
        rightRoom = null;

        if (this.height < minHeight || this.width < minWidth) {
            System.out.println("Error created Small broom");
        }


    }

    public static int magicNumber(int min, int max, Random rand) {
        return rand.nextInt(max - min) + min;
    }

    public static void print(BinaryRoom b) {
        if (b.leftRoom == null && b.rightRoom == null) {
            System.out.println(b);
            return;
        }
        if (b.leftRoom != null) {
            print(b.leftRoom);
        }
        if (b.rightRoom != null) {
            print(b.rightRoom);
        }
    }

    public static ArrayList<BinaryRoom> getLeafs(BinaryRoom room) {
        ArrayList<BinaryRoom> rooms = new ArrayList<>();
        if (room.leftRoom == null && room.rightRoom == null) {
            rooms.add(room);
        }
        if (room.leftRoom != null) {
            rooms.addAll(getLeafs(room.leftRoom));
        }
        if (room.rightRoom != null) {
            rooms.addAll(getLeafs(room.rightRoom));
        }
        return rooms;
    }

    public void init(int minWidth, int maxWidht, int minHeight, int maxHeight, long seed) {
        this.minHeight = minHeight;
        this.minWidth = minWidth;
        this.maxWidth = maxWidht;
        this.maxHeight = maxHeight;
        this.seed = seed;
        rand = new Random(seed);
    }

    public void setSeed(long seed) {
        this.seed = seed;
        rand = new Random(seed);
    }

    public Room generateRoom() {
        int w = width - rand.nextInt(width - 4);
        int h = height - rand.nextInt(height - 4);
        return new Room(getPosition(w, h), w, h);
    }

    public Position getPosition(int w, int h) {

        Position p = leftCorner.shift(1, 1);
        return p;
    }

    public boolean isLeaf() {
        return leftRoom == null && rightRoom == null;
    }

    public void split(BinaryRoom b) {
        if (b == null) {
            return;
        }
        boolean doHorizontal = rand.nextBoolean();

        /*
        horizontal   =======
                     -------
                     =======

                ==============
         vertical     |
                ======|=======
         */
        if (b.width > b.height && b.width / b.height >= 2) {
            if (!splitVertiacally(b)) {
                splitHorizontally(b);
            }
        } else if (b.height > b.width && b.height / b.width >= 2) {
            if (!splitHorizontally(b)) {
                splitVertiacally(b);
            }

        }

        if (doHorizontal) {
            if (!splitHorizontally(b)) {
                splitVertiacally(b);
            }

        } else {
            if (splitVertiacally(b)) {
                splitHorizontally(b);
            }
        }
    }

    private boolean splitVertiacally(BinaryRoom b) {
        int wLeft = b.rand.nextInt(b.width / 2);
        int wRight = b.width - wLeft;
        if (wLeft >= minWidth && wRight >= minWidth) {
            b.leftRoom = new BinaryRoom(b.leftCorner, wLeft, b.height);
            b.rightRoom = new BinaryRoom(b.leftCorner.shift(wLeft, 0), wRight, b.height);
            return true;

        }
        return false;
    }

    private boolean splitHorizontally(BinaryRoom b) {
        int hDown = b.rand.nextInt(b.height / 2);
        int hUp = b.height - hDown;
        if (hDown >= minHeight && hUp >= minHeight) {
            b.leftRoom = new BinaryRoom(b.leftCorner, b.width, hDown);
            b.rightRoom = new BinaryRoom(b.leftCorner.shift(0, hDown), b.width, hUp);
            return true;

        }
        return false;
    }

    public BinaryRoom splitTree(BinaryRoom b) {
        split(b);
        if (b.leftRoom != null) {
            splitTree(b.leftRoom);
        }
        if (b.rightRoom != null) {
            splitTree(b.rightRoom);
        }

        return b;
    }

    public void print() {
        print(this);
    }


}
