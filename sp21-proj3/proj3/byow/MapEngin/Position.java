package byow.MapEngin;

import java.io.Serializable;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Author@ Saifullah Jaialni
 */
public class Position implements Comparable<Position>, Serializable {
    private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Coppyn constructor
    public Position(Position p) {
        this(p.x, p.y);
    }

    public static Position upLeftPos(Position... positions) {
        Position pos = positions[0];
        int x = pos.x;
        int y = pos.y;
        for (Position p : positions) {
            if (p.x < x) {
                x = p.x;
            }
            if (p.y > y) {
                y = p.y;
            }
        }
        return new Position(x, y);
    }

    public static Position downLeft(Position... positions) {
        Position pos = positions[0];
        int x = pos.x;
        int y = pos.y;
        for (Position p : positions) {
            if (p.x < x) {
                x = p.x;
            }
            if (p.y < y) {
                y = p.y;
            }
        }
        return new Position(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position shift(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }

    public double euclidian() {
        return euclidian(new Position(0, 0));
    }

    public double euclidian(Position p) {
        double a = pow((p.x - x), 2);
        double b = pow((p.y - y), 2);
        return sqrt(a + b);

    }

    public int compareTo(Position p) {
        return (int) Math.round(euclidian() - p.euclidian());
    }

    public boolean equals(Object p) {
        if (p.getClass() != this.getClass()) {
            return false;
        }
        Position a = (Position) p;
        return a.x == x && a.y == y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int compareX(Position p) {
        return x - p.x;
    }

    public int CompareY(Position p) {
        return y - p.y;
    }
}
