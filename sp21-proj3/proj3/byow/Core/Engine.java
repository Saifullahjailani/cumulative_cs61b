package byow.Core;

import byow.Game.DungeonEscape;
import byow.Game.Gui;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import static java.lang.System.exit;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    public static final int XSCALE = 16;
    public static final int YSCALE = 16;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        int stat = 1;
        while (true) {
            DungeonEscape d;
            Gui.init(WIDTH, HEIGHT, XSCALE, YSCALE);
            Gui.start(WIDTH, HEIGHT);
            StdDraw.show();
            char start = Gui.getStartKey();
            if (start == 'n' || start == 'N') {
                long seed = Gui.seedMenue(WIDTH, HEIGHT);
                d = new DungeonEscape(WIDTH, HEIGHT, seed);
                stat = d.play();
            } else if (start == 'L' || start == 'l') {
                d = DungeonEscape.loadGame();
                stat = d.play();
            } else if (start == 'Q' || start == 'q') {
                exit(2);
            } else if (start == 'R' || start == 'r') {
                DungeonEscape.replay();
            }
        }


    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        String moves;
        input = input.toLowerCase();
        DungeonEscape d;
        if (input.charAt(0) == 'n') {
            Parser parser = new Parser(input);
            moves = parser.moves;
            d = new DungeonEscape(WIDTH, HEIGHT, parser.seed);
        } else {
            d = DungeonEscape.loadGame();
            moves = input.substring(1);
        }
        for (int i = 0; i < moves.length(); i++) {
            if (moves.charAt(i) == ':' && moves.charAt(i + 1) == 'q') {
                d.saveGame();
                return d.world();
            }
            d.movePlayer(moves.charAt(i));
        }

        return d.world();
    }

    private static class Parser {
        private final long seed;
        private final String moves;
        private final boolean newGame;

        Parser(String str) {
            str = str.toLowerCase();
            newGame = str.charAt(0) == 'n';
            int index = str.indexOf('s');
            seed = Long.parseLong(str.substring(1, index));
            moves = str.substring(index + 1);
        }
    }

}
