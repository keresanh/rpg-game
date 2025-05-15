package org.example.gamefx.blocks;
/**
 * Enumeration of all possible block types in the game.
 * Each type defines its texture file and collision properties.
 */
public enum BlockType {
    GRASS("grass_4.png", false),
    WALL("wall_3.png", true),
    STONE("stone_5.png", false);


    private final String FileName;
    private final boolean collidable;

    /**
     * @param FileName The file name of the block's image.
     * @param collidable Whether the block can be collided with.
     */
    BlockType(String FileName, boolean collidable) {
        this.FileName = FileName;
        this.collidable = collidable;
    }

    public String getFileName() {
        return FileName;
    }

    /**
     * @return True if this block type is collidable, false otherwise.
     */
    public boolean isCollidable() {
        return collidable;
    }
}