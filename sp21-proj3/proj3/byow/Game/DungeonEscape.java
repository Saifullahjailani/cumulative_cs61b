package byow.Game;

import byow.MapEngin.MapGenerator;
import byow.MapEngin.Position;
import byow.MapEngin.Room;
import byow.MapEngin.Shapes;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;
import java.util.LinkedList;
import java.util.Random;

import static byow.Game.Utils.*;
import static java.lang.System.exit;

/**
 * Author@ Saifullah Jaialni
 *
 */
public class DungeonEscape {
    public static final int MIN_ROOM_SIDE = 5;
    public static final int MAX_ROOM_SIDE = 7;
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File DUNGEN = join(CWD, "GameObject.txt");
    private static final int xScale = 16;
    private static final int yScale = 16;
    private final long seed;
    private final MapGenerator mg;
    private final Avatar player;
    private final int width;
    private final int height;
    private final Enemy enemy;
    public boolean gameOver = false;
    public boolean render = true;
    public int health = 1;
    private int score = 0;
    private int level = 1;
    private LinkedList<java.lang.Character> keys;
    private Random random;

    public DungeonEscape(GameObject g) {
        this(g.getWidth(), g.getHeight(), g.getSeed(), g.getPlayerPos(), g.getEnemyPos());

        this.score = g.getScore();
        this.level = g.getLevel();
        this.health = g.getHealth();
        this.gameOver = g.isGameOver();

    }

    public DungeonEscape(int width, int height, long seed, Position playerPos, Position enemyPos) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        mg = new MapGenerator(width, height, MAX_ROOM_SIDE, MIN_ROOM_SIDE, MAX_ROOM_SIDE, MIN_ROOM_SIDE, seed);
        mg.renderMap();

        player = new Avatar(playerPos, mg.tiles);
        player.addNonWalkables(Tileset.WALL);
        enemy = new Enemy(enemyPos, mg.tiles);
        enemy.addNonWalkables(Tileset.WALL);
    }


    public DungeonEscape(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        mg = new MapGenerator(width, height, MAX_ROOM_SIDE, MIN_ROOM_SIDE, MAX_ROOM_SIDE, MIN_ROOM_SIDE, seed);
        mg.renderMap();
        random = new Random();
        player = new Avatar(mg.randomFloor(), mg.tiles);
        player.addNonWalkables(Tileset.WALL);
        enemy = new Enemy(mg.randomFloor(), mg.tiles);
        enemy.addNonWalkables(Tileset.WALL);

    }

    public static DungeonEscape loadGame() {
        GameObject g = readObject(DUNGEN, GameObject.class);
        return new DungeonEscape(g);
    }

    public static void playWithKeyboard(int width, int height) {
        DungeonEscape d;
        Gui.init(width, height, xScale, yScale);
        Gui.start(width, height);
        StdDraw.show();
        char start = Gui.getStartKey();
        if (start == 'n' || start == 'N') {
            long seed = Gui.seedMenue(width, height);
            d = new DungeonEscape(width, height, seed);
            d.play();
        } else if (start == 'L' || start == 'l') {
            d = loadGame();
            d.play();
        } else if (start == 'Q' || start == 'q') {
            exit(2);
        }

    }

    public static void main(String[] args) {
        DungeonEscape d = new DungeonEscape(80, 50, 234);
        TERenderer TER = new TERenderer();
        TER.initialize(80, 50, 0, -1);
        d.newRoom(null, TER);

    }

    public static void replay() {
        if (!DUNGEN.exists()) {
            return;
        }
        GameObject g = readObject(DUNGEN, GameObject.class);
        DungeonEscape d = new DungeonEscape(g.getWidth(), g.getHeight(), g.getSeed());
        TERenderer TER = new TERenderer();
        TER.initialize(g.getWidth(), g.getHeight(), 0, -1);
        StdDraw.enableDoubleBuffering();
        for (char c : g.getKeysPlayer()) {
            char enemy = g.getKeysEnemy().removeFirst();
            TER.renderFrame(d.world());
            StdDraw.pause(500);
            d.movePlayer(c);
            d.enemy.control.move(enemy);
        }
    }

    public void saveGame() {
        GameObject g = new GameObject(width, height, health, score, level
                , gameOver, seed, player.getPosition(), player.getHistory(), enemy.getHistory(), enemy.p);
        writeObject(DUNGEN, g);

    }

    public void drawPath(LinkedList<Position> pos, TETile[][] tiles, TETile sym) {
        for (Position p : pos) {
            tiles[p.getX()][p.getY()] = sym;
        }
        tiles[player.p.getX()][player.p.getY()] = Tileset.AVATAR;
        tiles[enemy.p.getX()][enemy.p.getY()] = Tileset.ENEMY;
    }

    public int play() {
        TERenderer TER = new TERenderer();
        TER.initialize(width, height, 0, -1);
        StdDraw.enableDoubleBuffering();
        LinkedList<java.lang.Character> keys = new LinkedList<>();
        LinkedList<Position> p = new LinkedList<>();
        boolean trace = true;
        while (health > 0) {

            TER.renderFrame(mg.tiles);
            Gui.displayHud(width, height, score, level, health, mg.tiles);
            StdDraw.pause(50);
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if ((c == 'q' || c == 'Q') && !keys.isEmpty() && keys.getLast() == ':') {
                    this.keys = keys;
                    saveGame();
                    exit(1);
                }
                if (player.getPosition().euclidian(enemy.getPosition()) <= 1) {
                    health--;
                    trace = false;
                    Gui.gameOVer(width, height, score);
                    StdDraw.show();
                    char s = Gui.listen();
                    if (s == 'S' || s == 's') {
                        return 1;
                    } else {
                        exit(2);
                    }

                } else {


                    Dijkstra path = new Dijkstra(mg.tiles, player.getPosition(), enemy.getPosition(), width, height);
                    p = path.optimalPath();
                    p.removeLast();
                    p.removeLast();
                    drawPath(p, mg.tiles, Tileset.MARKER);
                    TER.renderFrame(mg.tiles);
                    StdDraw.pause(50);
                    drawPath(p, mg.tiles, Tileset.FLOOR);
                    enemy.move(p.getLast());
                    keys.addLast(c);
                    movePlayer(c);
                }


            }
        }
        return 0;
    }

    public void newRoom(LinkedList<java.lang.Character> keys, TERenderer ter) {
        int maxWidth = random.nextInt(10) + 10;
        int maxHeight = random.nextInt(10) + 10;
        int x = width / 2 - maxWidth / 2;
        int y = height / 2 - maxHeight / 2;
        ter.initialize(width, height, 0, -1);
        TETile[][] tiles = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }

        Position conrner = new Position(x, y);
        Room r = new Room(conrner, maxWidth, maxHeight);
        LinkedList<Position> p = r.addRoom(tiles);
        Shapes.fill(tiles, conrner, conrner.shift(maxWidth, maxHeight), Tileset.TREE);
        Avatar avatar = new Avatar(r.getRandomPoint(random), tiles);
        while (true) {
            Gui.displayHud(width, height, score, level, health, tiles);
            ter.renderFrame(tiles);

        }


    }

    public void movePlayer(char c) {
        player.getControl().move(c);
    }

    public TETile[][] world() {
        return mg.tiles;
    }

}
