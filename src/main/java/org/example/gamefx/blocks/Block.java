package org.example.gamefx.blocks;

/**
 * Represents a single tile/block in the game world. Each block has a type
 * that determines its appearance and collision properties.
 */
public class Block {
    private final BlockType blockType;
    private boolean collidable;
    private int x;
    private int y;

    public static final double BLOCK_WIDTH = 64;
    public static final double BLOCK_HEIGHT = 64;

    /**
     * Constructs a Block of the specified type.
     * @param blockType The type of block to create, determines collision and texture
     */
    public Block(BlockType blockType) {
        this.blockType = blockType;
        this.collidable = blockType.isCollidable();
    }

    /**
     * @return The X coordinate of this block in pixels (world space)
     */
    public double getTileX() {
        return x * BLOCK_WIDTH;
    }

    /**
     * @return The Y coordinate of this block in pixels (world space)
     */
    public double getTileY() {
        return y * BLOCK_HEIGHT;
    }

    /**
     * @return True if this block type is collidable (prevents entity movement)
     */
    public boolean isCollidable() {
        return collidable;
    }

    public BlockType getBlockType() {
        return blockType;
    }
}