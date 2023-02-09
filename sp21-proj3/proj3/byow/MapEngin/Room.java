package byow.MapEngin;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

/**
 * Author@ Saifullah Jaialni
 */
public class Room extends Shapes implements Comparable<Room>, Serializable {
    protected Position leftCorner;
    protected Position doorWay;
    protected int height;
    protected int width;


    public Room(Position leftCorner, int width, int height) {
        this.leftCorner = new Position(leftCorner);
        this.height = height;
        this.width = width;


    }

    public Room() {
        this(null, 0, 0);
    }

    public static LinkedList<Position> addRoom(TETile[][] tiles, Position p, int width, int height) {
        Position end = p.shift(width, height);
        return fill(tiles, p, end, Tileset.FLOOR);
    }


    public void drawWalls(TETile[][] tiles, Position p, int Widht, int height) {
        Position end = p.shift(width, height);
        drawRectangle(tiles, p, end, Tileset.WALL);
    }

    public LinkedList<Position> addRoom(TETile[][] tiles) {
        return addRoom(tiles, leftCorner, width, height);
    }

    public boolean collision(TETile[][] tiles) {
        Position pos = leftCorner;
        int xLimit = pos.getX() + width;
        int yLimit = pos.getY() + height;
        for (int i = pos.getX() - 1; i < xLimit + 2; i++) {
            for (int j = pos.getY() - 1; j < yLimit + 2; j++) {
                if (tiles[i][j] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }


    public int compareTo(Room other) {
        return leftCorner.compareTo(other.leftCorner);
    }

    public String toString() {
        return leftCorner.toString() + " W-> " + width + " H -> " + height;
    }

    public Position getRandomPoint(Random rand) {
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);
        return leftCorner.shift(x, y);
    }

}
