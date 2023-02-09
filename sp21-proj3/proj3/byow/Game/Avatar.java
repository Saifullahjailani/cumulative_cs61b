package byow.Game;

import byow.MapEngin.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar extends Character {
    private Control control;

    public Avatar(Position pos, TETile[][] tiles) {
        super(pos, Tileset.AVATAR, tiles);
        control = new Control(this);
    }

    public Control getControl() {

        return control;
    }
}
