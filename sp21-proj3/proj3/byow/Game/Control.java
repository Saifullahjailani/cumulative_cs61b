package byow.Game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author@ Saifullah Jaialni
 */
public class Control implements Serializable {
    public static final char UP = 'w';
    public static final char DOWN = 's';
    public static final char LEFT = 'a';
    public static final char RIGHT = 'd';

    public static final char UP_CAP = 'W';
    public static final char DOWN_CAP = 'S';
    public static final char LEFT_CAP = 'A';
    public static final char RIGHT_CAP = 'D';
    public static final Map<java.lang.Character, java.lang.Character> OPOSIT = new TreeMap<>();

    static {
        OPOSIT.put(UP, DOWN);
        OPOSIT.put(UP_CAP, DOWN);
        OPOSIT.put(DOWN, UP);
        OPOSIT.put(DOWN_CAP, UP);
        OPOSIT.put(LEFT, RIGHT);
        OPOSIT.put(LEFT_CAP, RIGHT);
        OPOSIT.put(RIGHT, LEFT);
        OPOSIT.put(RIGHT_CAP, LEFT);
    }

    private final Character character;

    public Control(Character character) {
        this.character = character;
    }

    public boolean move(char s) {
        if (s == UP || s == UP_CAP) {
            return character.moveUp();
        } else if (s == DOWN || s == DOWN_CAP) {
            return character.moveDown();
        } else if (s == RIGHT || s == RIGHT_CAP) {
            return character.moveRight();
        } else if (s == LEFT || s == LEFT_CAP) {
            return character.moveLeft();
        }
        return false;
    }

    public void walkBack(int x) {
        LinkedList<java.lang.Character> walkThis = new LinkedList<>();
        int len = character.getHistory().size();
        for (int i = 0; i < x; i++) {
            if ((len - i) < 0) {
                break;
            }
            char last = character.getHistory().get(len - i);
            if (OPOSIT.containsKey(last)) {
                walkThis.add(OPOSIT.get(last));
            }
        }
        for (char a : walkThis) {
            move(a);
        }
    }
}
