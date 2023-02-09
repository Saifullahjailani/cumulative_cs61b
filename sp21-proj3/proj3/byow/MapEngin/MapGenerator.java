package byow.MapEngin;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Author@ Saifullah Jaialni
 */
public class MapGenerator implements Serializable {
    public TETile[][] tiles;
    private final int width;
    private final int height;
    private final long SEED;
    private final Random RANDOM;
    private int MAX_ROOM;
    private int MIN_ROOM;
    private final int MAX_ROOM_HEIGHT;
    private final int MIN_ROOM_HEIGHT;
    private final int MAX_ROOM_WIDTH;
    private final int MIN_ROOM_WIDTH;
    private final ArrayList<Room> rooms;
    private final ArrayList<Position> doors;
    private final LinkedList<Position> floor;
    private final LinkedList<Hallway> hallways;
    private final LinkedList<Position> walls;

    public MapGenerator(int width, int height, int maxWidth, int minWidht, int maxHeight, int minHeight, long seed) {
        this.width = width;
        this.height = height;
        this.SEED = seed;
        RANDOM = new Random(seed);


        MAX_ROOM_HEIGHT = maxHeight;
        MIN_ROOM_HEIGHT = minHeight;
        MAX_ROOM_WIDTH = maxWidth;
        MIN_ROOM_WIDTH = minWidht;


        rooms = new ArrayList<>();
        doors = new ArrayList<>();
        hallways = new LinkedList<>();
        tiles = new TETile[width][height];
        floor = new LinkedList<>();
        walls = new LinkedList<>();

        fill();

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void fill() {
        Position start = new Position(0, 0);
        Position end = new Position(width, height);
        Shapes.fill(tiles, start, end, Tileset.NOTHING);
    }

    private void generateRooms() {
        BinaryRoomTree b = new BinaryRoomTree(new Position(2, 2), width - 4, height - 4, SEED);

        ArrayList<BinaryRoom> regions = b.getRooms();
        for (BinaryRoom region : regions) {
            Room r = roomFromRegion(region);
            rooms.add(r);
            Position a = r.getRandomPoint(RANDOM);
            doors.add(a);
            floor.addAll(r.addRoom(tiles));
        }

    }

    private void generateWalls() {
        for (Position p : floor) {
            renderCircle(p);
        }
    }

    private void renderCircle(Position p) {
        swap(p.shift(0, 1));
        swap(p.shift(1, 0));
        swap(p.shift(0, -1));
        swap(p.shift(-1, 0));
        swap(p.shift(1, 1));
        swap(p.shift(-1, -1));
        swap(p.shift(1, -1));
        swap(p.shift(-1, 1));
    }


    private void swap(Position p) {
        if (isNothing(p)) {
            setTiles(p);
        }
    }

    private boolean isFloor(Position p) {
        return getTile(p) == Tileset.FLOOR;
    }

    private boolean isNothing(Position p) {
        return getTile(p) == Tileset.NOTHING;
    }

    public TETile getTile(Position p) {
        return tiles[p.getX()][p.getY()];
    }

    private void setTiles(Position p) {
        tiles[p.getX()][p.getY()] = Tileset.WALL;
    }

    public void connectRooms() {
        for (int i = 1; i < rooms.size(); i++) {
            Position p = doors.get(i - 1);
            Position p2 = doors.get(i);
            Astar d = new Astar(tiles, p, p2, width, height);
            LinkedList<Position> path = d.optimalPath();
            Hallway h = new Hallway(tiles, path);
            floor.addAll(path);
            hallways.add(h);
            h.drawHallway();
        }

    }

    public Position randomFloor() {
        return floor.get(RANDOM.nextInt(floor.size()));
    }

    private Room roomFromRegion(BinaryRoom r) {
        int w = RANDOM.nextInt(r.width - 2);
        int fromMax = Math.abs(w - r.maxWidth);
        int fromMin = Math.abs(w - r.minWidth);
        w = (fromMax > fromMin) ? MIN_ROOM_WIDTH : MAX_ROOM_WIDTH;

        int h = RANDOM.nextInt(r.height - 1);
        fromMax = Math.abs(r.height - r.maxHeight);
        fromMin = Math.abs(r.height - r.minHeight);
        h = (fromMax > fromMin) ? MIN_ROOM_HEIGHT : MAX_ROOM_HEIGHT;

        int x = RANDOM.nextInt(r.width - w) + 1;
        int y = RANDOM.nextInt(r.height - h) + 1;

        return new Room(new Position(x, y).shift(r.leftCorner.getX(), r.leftCorner.getY()), w, h);

    }

    public void renderMap() {
        generateRooms();
        connectRooms();
        generateWalls();
    }


}
