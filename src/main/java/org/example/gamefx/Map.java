package org.example.gamefx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.gamefx.blocks.Block;
import org.example.gamefx.blocks.BlockType;
import org.example.gamefx.utils.Utils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Represents the game world 2D map constructed from tile blocks.
 * Handles map loading, block storage, and rendering.
 */
public class Map {
    private final int heightTilesAmount;
    private final int widthTilesAmount;
    private final int TILE_SIZE = 64;
//    private final String worldMatName = "world1.txt";
//    private final String worldMatName = "world_create.txt";
//    private final String worldMatName = "tempWorld5May.txt";
    private HashMap<Integer, Block> blockMapping = new HashMap<>();
    private Block[][] mapBlocks;
    private final String worldMatName;

    /**
     * Creates a new game map from a matrix file
     *
     * @param widthTilesAmount Map width in tiles
     * @param heightTilesAmount Map height in tiles
     * @param mapFile Path to matrix definition file
     * @throws IOException If the map file cannot be read
     */
    public Map(int widthTilesAmount, int heightTilesAmount, String mapFile) throws IOException {
        this.widthTilesAmount = widthTilesAmount;
        this.heightTilesAmount = heightTilesAmount;
        this.worldMatName = mapFile;
        makeBlocksMapping();
        init();
    }

    /**
     * Creates the ID-to-Block mapping used to translate numerical matrix values
     * from the map file into actual block types.
     *
     * Mapping:
     * 1 -> GRASS (non-collidable)
     * 2 -> WALL (collidable)
     * 3 -> STONE (non-collidable)
     */
    private void makeBlocksMapping() {
        blockMapping.put(1, new Block(BlockType.GRASS));
        blockMapping.put(2, new Block(BlockType.WALL));
        blockMapping.put(3, new Block(BlockType.STONE));
    }

    /**
     * Initializes the game map by:
     * 1. Reading the numerical matrix from file using Utils
     * 2. Creating a 2D Block array where each entry is resolved using the matrix IDs
     *
     * @throws IOException If the world matrix file cannot be read or parsed
     */
    private void init() throws IOException {
        mapBlocks = new Block[heightTilesAmount][widthTilesAmount];
        int[][] worldMat = Utils.readWorldMat(worldMatName, heightTilesAmount, widthTilesAmount);
            for (int i = 0; i < heightTilesAmount; i++) {
                for (int j = 0; j < widthTilesAmount; j++) {
                    mapBlocks[i][j] = blockMapping.get(worldMat[i][j]);
            }
        }
    }

    /**
     * Gets the block at specified tile coordinates
     *
     * @param x Tile X coordinate (grid position)
     * @param y Tile Y coordinate (grid position)
     * @return Block at position, or default grass block if out of bounds
     */
    public Block getBlock(int x, int y) {
        if(x >= 0 && x < widthTilesAmount && y >= 0 && y < heightTilesAmount) {
           return mapBlocks[y][x];
        }
        return blockMapping.get(1);
    }

    /**
     * Renders the map relative to the game camera position
     *
     * @param gc GraphicsContext to draw on
     * @param cameraX Camera's X offset
     * @param cameraY Camera's Y offset
     */
    public void render(GraphicsContext gc, int cameraX, int cameraY) {
        for (int i = 0; i < heightTilesAmount; i++) {
            for (int j = 0; j < widthTilesAmount; j++) {
                Block block = mapBlocks[i][j];
                Image blockImg = Utils.loadImg("/blocks/" + block.getBlockType().getFileName());
                gc.drawImage(blockImg, j * TILE_SIZE - cameraX -24, i * TILE_SIZE - cameraY -24, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public int getTILE_SIZE() {
        return TILE_SIZE;
    }

    public int getWidthTiles() {
        return widthTilesAmount;
    }

    public int getHeightTiles() {
        return heightTilesAmount;
    }

    public int getWidthPixels() {
        return widthTilesAmount * TILE_SIZE;
    }


    public int getHeightPixels() {
        return heightTilesAmount * TILE_SIZE;
    }
}
