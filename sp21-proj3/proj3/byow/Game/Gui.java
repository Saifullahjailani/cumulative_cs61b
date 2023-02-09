package byow.Game;

import byow.MapEngin.Position;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Author@ Saifullah Jaialni
 */
public abstract class Gui {
    public static final Map<String, String> descriptions = new HashMap<>();
    public static final String HEART = "\\u+2764";
    public static final Font bigFont = new Font("Comic Sans", Font.BOLD, 45);
    public static final Font smallFont = new Font("Comic Sans", Font.BOLD, 30);
    private static final String GAME_TITLE = "CS61b:- Dungeon Escape";
    private static final int xShift = 1;
    private static final int yShift = 1;

    static {
        descriptions.put("wall", ": no way you can move there.");
        descriptions.put("sand", ": beach vibes!.");
        descriptions.put("mountain", ": very hard to climb.");
        descriptions.put("locked door", ": You can ulock this, it is easier than cs61b.");
        descriptions.put("unlocked door", ": Congratulations!!!");
        descriptions.put("flower", ": come closer and smell it.");
        descriptions.put("tree", ": Please! Don't hurt me.");
        descriptions.put("nothing", ": No oxygen there.");
        descriptions.put("floor", ": Walkable, but be aware of enemy.");
        descriptions.put("marker", ": this is the path of death.");
        descriptions.put("enemy", ": fire is comming.");
        descriptions.put("you", ": hello its me!");
    }

    /**
     * 1080p initializer (1920X1080)
     **/
    public static void init(int width, int height, int xScale, int yScale) {
        StdDraw.setCanvasSize(width * xScale, height * yScale);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.enableDoubleBuffering();
    }


    public static void start(int width, int height) {
        StdDraw.setFont(bigFont);
        Path path = Paths.get("resources","door.png");

        StdDraw.picture(width / 2, height / 2, path.toString(), width, height);
        Position p = new Position(width / 2, height - 5);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(p.getX(), p.getY(), GAME_TITLE);
        StdDraw.setFont(smallFont);
        p = p.shift(0, -13);
        StdDraw.text(p.getX(), p.getY(), "New Dungeon (N)");
        p = p.shift(0, -10);
        StdDraw.text(p.getX(), p.getY(), "Load Dungeon (L)");
        StdDraw.text(p.getX(), p.getY()-10, "Replay (R)");
        p = p.shift(0, -18);
        StdDraw.text(p.getX(), p.getY(), "Quit (Q)");
    }

    public static void gameOVer(int width, int height, int score) {
        StdDraw.setFont(bigFont);
        Path path = Paths.get("resources","door.png");

        StdDraw.picture(width / 2, height / 2, path.toString(), width, height);
        Position p = new Position(width / 2, height - 5);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(p.getX(), p.getY(), "GAME OVER!");
        StdDraw.setFont(smallFont);
        p = p.shift(0, -13);
        StdDraw.text(p.getX(), p.getY(), "Score: " + score);
        p = p.shift(0, -10);
        StdDraw.text(p.getX(), p.getY(), "Start Menue (S)");
        StdDraw.text(p.getX(), p.getY()-10, "Press any key to exit.");

    }


    public static long seedMenue(int width, int height) {
        String seed = "";
        char c = 'a';
        while (true) {
            if (c == 's' || c == 'S') {
                return Long.parseLong(seed);
            }
            if (c >= '0' && c <= '9') {
                seed += c;
            } else if (c == 'b' || c == 'B') {
                seed = seed.substring(0, seed.length() - 1);
            }
            if (seed.isBlank()) {
                drawSeedMenu(width, height, "#########");
            } else {
                drawSeedMenu(width, height, seed);
            }
            c = listen();
        }

    }

    private static void drawSeedMenu(int width, int height, String seed) {
        System.out.println(seed);
        Path path = Paths.get("resources", "door.png");
        StdDraw.picture(width / 2, height / 2, path.toString(), width, height);
        StdDraw.setPenColor(Color.white);
        Position p = new Position(width / 2, height - 10);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(p.getX(), p.getY(), "Enter a seed number and press (S).");
        StdDraw.text(p.getX(), p.getY() - 3, "Press (b) for backspace.");
        p = p.shift(0, -8);
        StdDraw.text(p.getX(), p.getY(), seed);
        StdDraw.show();
    }

    public static char getStartKey() {
        while (true) {
            char key = listen();
            if (key == 'l' || key == 'L'
                    || key == 'n' || key == 'N'
                    || key == 'q' || key == 'Q'
                    || key == 'r' || key == 'R') {
                return key;
            }
        }
    }

    public static char listen() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return StdDraw.nextKeyTyped();

            }
        }
    }

    public static void displayHud(int width, int height, int score, int level, int health, TETile[][] tiles) {
        StdDraw.setPenColor(Color.GRAY);
        StdDraw.filledRectangle(0, height, 2 * width, 2);
        drawHealthBar(width, height, health);
        drawLevelBar(width, height, level);
        drawScoreBar(width, height, score);
        StdDraw.show();
        if (mouseInsideScreen(width, height)) {
            displayTileInfo(width, height, tiles);
            StdDraw.show();
        }

    }

    private static void drawScoreBar(int width, int height, int score) {
        String s = "Score: " + score;
        Font f = new Font("Comic Sans", Font.BOLD, 20);
        StdDraw.setFont(f);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(f);
        StdDraw.textRight(width - 1, height - 1, s);
    }

    private static void drawLevelBar(int width, int height, int level) {
        StdDraw.setPenColor(Color.white);
        Font f = new Font("Comic Sans", Font.BOLD | Font.ITALIC, 20);
        StdDraw.setFont(f);
        String str = "Level: " + level;
        StdDraw.textLeft(xShift, height - yShift, str);
    }


    /**
     * Draw hearts for the ammount of hearts.
     */
    private static void drawHealthBar(int width, int height, int health) {
        Font f = new Font("Comic Sans", Font.BOLD, 40);
        StdDraw.setFont(f);
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(width / 2, height - yShift * 1.3, getHearts(health));
    }


    /**
     * Find the description of Tile at the location of the mouse.
     */
    public static void displayTileInfo(int width, int height, TETile[][] tiles) {
        int x = (int) Math.floor(StdDraw.mouseX());
        int y = (int) Math.floor(StdDraw.mouseY()) + 1;

        if (mouseInsideScreen(width, height)) {
            TETile tile = tiles[x][y];
            String str = tile.description();
            str += (" " + descriptions.get(str));
            StdDraw.textLeft(xShift, yShift, str);
        }

    }


    /**
     * @param n number of hearts to return
     * @return returns n hearts decoded to Ascii
     */
    private static String getHearts(int n) {
        String container = "";
        for (int i = 0; i < n; i++) {
            container += uniCodeDecoder(HEART);
            container += "";
        }
        return container;
    }


    /**
     * @param myString string in for of \\u+num
     * @return string of Ascii
     * Source:- https://stackoverflow.com/questions/5585919/
     * creating-unicode-character-from-its-number
     */
    private static String uniCodeDecoder(String myString) {
        String str = myString.split(" ")[0];
        str = str.replace("\\", "");
        String[] arr = str.split("u");
        String text = "";
        for (int i = 1; i < arr.length; i++) {
            int hexVal = Integer.parseInt(arr[i], 16);
            text += (char) hexVal;
        }
        return text;
    }


    public static boolean mouseInsideScreen(int width, int height) {
        int x = (int) Math.floor(StdDraw.mouseX());
        int y = (int) Math.floor(StdDraw.mouseY());
        return (x >= 0 && x < width)
                && (y >= 0 && y < height - 1);
    }


}
