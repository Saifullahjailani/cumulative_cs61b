package byow.Game;

import byow.MapEngin.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Author@ Saifullah Jaialni
 */
public class GameObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 6577136929180248996L;
    private final int height;
    private final int width;
    private final boolean gameOver;
    private final int health;
    private final int score;
    private final int level;
    private final Position playerPos;
    private final long seed;
    private final LinkedList<java.lang.Character> keysPlayer;
    private final LinkedList<java.lang.Character> keysEnemy;
    private final Position enemyPos;

    public GameObject(int width, int height, int health, int score, int level, boolean gameOver, long seed, Position player,
                      LinkedList<java.lang.Character> keysPlayer, LinkedList<java.lang.Character> keysEnemy, Position enemyPosition) {
        this.width = width;
        this.height = height;
        this.health = health;
        this.score = score;
        this.level = level;
        this.gameOver = gameOver;
        this.seed = seed;
        this.playerPos = player;
        this.keysPlayer = keysPlayer;
        this.keysEnemy = keysEnemy;
        this.enemyPos = enemyPosition;

    }

    public Position getEnemyPos() {
        return enemyPos;
    }

    public LinkedList<java.lang.Character> getKeysPlayer() {
        return keysPlayer;
    }

    public LinkedList<java.lang.Character> getKeysEnemy() {
        return keysEnemy;
    }

    public long getSeed() {
        return seed;
    }

    public int getHealth() {
        return health;
    }

    public int getHeight() {
        return height;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public Position getPlayerPos() {
        return playerPos;
    }

    public int getWidth() {
        return width;
    }

}
