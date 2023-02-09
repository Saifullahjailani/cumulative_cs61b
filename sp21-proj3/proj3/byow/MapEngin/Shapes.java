package byow.MapEngin;

import byow.TileEngine.TETile;

import java.util.LinkedList;

/**
 * Author@ Saifullah Jaialni
 */
public abstract class Shapes {


    public static void drawRectangle(TETile[][] tiles, Position start, Position end, TETile tile) {
        Position leftDown = Position.downLeft(start, end);
        int xLen = java.lang.Math.abs(start.getX() - end.getX());
        int yLen = java.lang.Math.abs(start.getY() - end.getY());
        //Draw first Wall
        Position upLeft = drawVerticalLine(tiles, leftDown, yLen, tile);
        Position downRight = drawHorizontalLine(tiles, leftDown, xLen, tile);
        Position upRight = drawHorizontalLine(tiles, upLeft, xLen, tile);
        drawVerticalLine(tiles, downRight, yLen, tile);

    }

    public static LinkedList<Position> fill(TETile[][] tiles, Position start, Position end, TETile tile) {
        LinkedList<Position> p = new LinkedList<>();
        int xLen = java.lang.Math.abs(start.getX() - end.getX());
        int yLen = java.lang.Math.abs(start.getY() - end.getY());
        Position leftDown = Position.downLeft(start, end);
        for (int i = 0; i < yLen; i++) {
            p.addAll(drawHorizontalLineReturnList(tiles, leftDown, xLen + 1, tile));
            leftDown = leftDown.shift(0, 1);
        }
        return p;
    }


    public static Position drawHorizontalLine(TETile[][] tiles, Position start, int len, TETile tile) {
        Position p = drawHorizontalLineReturnList(tiles, start, len, tile).getLast();
        return p;
    }

    public static LinkedList<Position> drawHorizontalLineReturnList(TETile[][] tiles, Position start, int len, TETile tile) {
        LinkedList<Position> p = new LinkedList<>();
        Position pos = new Position(start);
        p.add(pos);
        int orientation = (len < 0) ? -1 : 1;
        len = Math.abs(len);
        for (int i = 0; i < len - 1; i++) {
            setTiles(tiles, pos, tile);
            pos = pos.shift(orientation, 0);
            p.add(pos);
        }
        return p;
    }

    public static LinkedList<Position> drawVerticalLineReturnList(TETile[][] tiles, Position start, int len, TETile tile) {
        LinkedList<Position> p = new LinkedList<>();
        Position pos = new Position(start);
        p.add(pos);
        int orientation = (len < 0) ? -1 : 1;
        len = Math.abs(len);
        for (int j = 0; j < len - 1; j++) {
            setTiles(tiles, pos, tile);
            pos = pos.shift(0, orientation);
            p.add(pos);
        }
        return p;
    }

    public static Position drawVerticalLine(TETile[][] tiles, Position start, int len, TETile tile) {
        Position p = drawHorizontalLineReturnList(tiles, start, len, tile).getLast();
        return p;
    }

    private static void setTiles(TETile[][] tiles, Position pos, TETile tile) {
        tiles[pos.getX()][pos.getY()] = tile;
    }


}
