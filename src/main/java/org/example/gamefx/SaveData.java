package org.example.gamefx;

import java.util.ArrayList;
import java.util.List;

/**
 * Serializable container for game progress persistence
 */
public class SaveData {
    public String currentLevel;
    public int playerX;
    public int playerY;
    public int playerHp;
    public List<EnemyState> enemies = new ArrayList<>();

    public SaveData() {}

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPlayerHp() {
        return playerHp;
    }

    public void setPlayerHp(int playerHp) {
        this.playerHp = playerHp;
    }

    public List<EnemyState> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<EnemyState> enemies) {
        this.enemies = enemies;
    }

    /**
     * Represents saved state of individual enemies
     */
    public static class EnemyState {
        public String type;
        public int x;
        public int y;
        public int hp;

        public EnemyState() {}

        public EnemyState(String type, int x, int y, int hp) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.hp = hp;
        }

        public void setType(String type) {
            this.type = type;
        }


        public String getType() {
            return type;
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

        public int getHp() {
            return hp;
        }

        public void setHp(int hp) {
            this.hp = hp;
        }
    }
}
