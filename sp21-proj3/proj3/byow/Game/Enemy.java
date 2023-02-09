package byow.Game;

import byow.MapEngin.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * Author@ Saifullah Jaialni
 */
public class Enemy extends Character {
    public Control control;

    public Enemy(Position p, TETile[][] tiles) {
        super(p, Tileset.ENEMY, tiles);
        control = new Control(this);
    }

    public void move(Position newPos) {
        if (newPos.equals(p.shift(-1, 0))) {
            control.move('a');
        }
        if (newPos.equals(p.shift(1, 0))) {
            control.move('d');
        }
        if (newPos.equals(p.shift(0, 1))) {
            control.move('w');
        }
        if (newPos.equals(p.shift(0, -1))) {
            control.move('s');
        }
    }
}
