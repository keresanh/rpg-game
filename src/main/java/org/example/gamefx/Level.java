package org.example.gamefx;

/**
 * Defines the interface for game levels with progression and content loading
 */
public interface Level {
    /**
     * @return File name of the level's map resource
     */
    String getMapFileName();
    /**
     * Populates the game world with level-specific content
     * @param world World instance to initialize with entities and objects
     */
    void load(World world);
    /**
     * @return Next level in progression sequence, null if final level
     */
    Level getNextLevel();
    /**
     * Sets the subsequent level in progression
     * @param nextLevel Level instance to follow this one
     */
    void setNextLevel(Level nextLevel);
}
