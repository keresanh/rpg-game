package org.example.gamefx;

/**
 * Controls level progression and current level state
 */
public class LevelManager {
    private static Level curLevel;

    /**
     * Initializes level sequence:
     * - Level1 -> Level2
     * - Level2 is terminal
     */
    public static void initializeLevels() {
        Level1 level1 = new Level1();
        Level2 level2 = new Level2();

        level1.setNextLevel(level2);
        curLevel = level1;
    }

    /**
     * @return Currently active level instance
     */
    public static Level getCurLevel() {
        return curLevel;
    }

    /**
     * Changes currently active level (for save/load and progression)
     * @param level Level instance to activate
     */
    public static void setCurLevel(Level level) {
    curLevel = level;
}
}
