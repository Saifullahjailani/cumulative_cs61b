package byow.MapEngin;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Author@ Saifullah Jaialni
 */
public class Hallway extends Shapes implements Serializable {
    private final TETile[][] tiles;
    private final LinkedList<Position> path;
    private final LinkedList<Position> brush;
    private final int q;

    public Hallway(TETile[][] tiels, LinkedList<Position> path) {
        this.tiles = tiels;
        this.path = path;
        brush = new LinkedList<>();
        q = 0;
    }


    public void drawHallway() {
        for (Position p : path) {
            tiles[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }


}
