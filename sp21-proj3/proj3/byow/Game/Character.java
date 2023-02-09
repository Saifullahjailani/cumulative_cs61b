package byow.Game;

import byow.MapEngin.Position;
import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Author@ Saifullah Jaialni
 */
public class Character implements Serializable {
    private final TETile[][] tiles;
    private final TETile symbol;
    private final ArrayList<TETile> cantWalk;
    private final int width;
    private final int height;
    private final Position origin;
    protected Position p;
    private LinkedList<java.lang.Character> history;
    private TETile before;

    public Character(Position pos, TETile symbol, TETile[][] tiles) {
        this.p = new Position(pos);
        this.symbol = symbol;
        this.tiles = tiles;
        cantWalk = new ArrayList<>();
        before = tiles[p.getX()][p.getY()];
        renderCharacter();
        width = tiles.length;
        height = tiles[0].length;
        history = new LinkedList<>();
        origin = pos;
    }

    public LinkedList<java.lang.Character> getHistory() {
        return history;
    }

    public void renderCharacter() {
        tiles[p.getX()][p.getY()] = symbol;
    }

    public void addNonWalkables(TETile... tiles) {
        for (TETile t : tiles) {
            cantWalk.add(t);
        }
    }

    public Position getPosition() {
        return new Position(p);
    }

    private boolean canMoveLeft() {
        return canMove(-1, 0);
    }

    private boolean canMoveRight() {
        return canMove(1, 0);
    }

    private boolean canMoveUp() {
        return canMove(0, 1);
    }

    private boolean canMoveDown() {
        return canMove(0, -1);
    }


    private boolean canMove(int xShift, int yShift) {
        Position pos = new Position(p);
        pos = pos.shift(xShift, yShift);
        if (pos.getX() < 0 || pos.getX() >= width) {
            return false;
        }
        if (pos.getY() < 0 || pos.getY() >= height) {
            return false;
        }
        for (TETile tile : cantWalk) {
            if (tiles[pos.getX()][pos.getY()] == tile) {
                return false;
            }
        }
        return true;
    }

    private void renderAftermove(Position current) {
        tiles[current.getX()][current.getY()] = before;
        before = tiles[p.getX()][p.getY()];
        tiles[p.getX()][p.getY()] = symbol;
    }

    public boolean moveRight() {
        if (canMoveRight()) {
            Position current = new Position(p);
            p = p.shift(1, 0);
            renderAftermove(current);
            history.add('d');
            return true;
        }
        return false;
    }

    public boolean moveDown() {
        if (canMoveDown()) {
            Position current = new Position(p);
            p = p.shift(0, -1);
            renderAftermove(current);
            history.add('s');
            return true;
        }
        return false;
    }

    public boolean moveUp() {
        if (canMoveUp()) {
            Position current = new Position(p);
            p = p.shift(0, 1);
            renderAftermove(current);
            history.add('w');
            return true;
        }
        return false;
    }

    public boolean moveLeft() {
        if (canMoveLeft()) {
            Position current = new Position(p);
            p = p.shift(-1, 0);
            renderAftermove(current);
            history.add('a');
            return true;
        }
        return false;
    }


}
