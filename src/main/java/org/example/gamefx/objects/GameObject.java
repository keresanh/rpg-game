package org.example.gamefx.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.gamefx.entities.Player;
import org.example.gamefx.utils.Utils;

import java.io.IOException;

/**
 * Base class for all interactive world objects
 */
public abstract class GameObject {
    public int x;
    public int y;
    public Image img;
    public boolean isSolid;

    /**
     * Creates new game object
     *
     * @param x World X position
     * @param y World Y position
     * @param imgName Texture file path
     * @param isSolid Whether object blocks movement
     */
    public GameObject(int x, int y, String imgName, boolean isSolid) {
        this.x = x;
        this.y = y;
        this.img = Utils.loadImg("/objects/" + imgName);
        this.isSolid = isSolid;
    }

    /**
     * Updates object state based on player interaction
     *
     * @param player Player entity to check against
     * @throws IOException If interaction causes I/O operations
     */
    public abstract void update(Player player) throws IOException;

    /**
     * Renders the object to the screen.
     *
     * @param gc Graphics context
     * @param tileSize Size of a tile in pixels
     * @param cameraX X-offset of the camera
     * @param cameraY Y-offset of the camera
     */
    public void render(GraphicsContext gc, int tileSize, int cameraX, int cameraY) {
        gc.drawImage(img, x - cameraX - 24, y - cameraY - 24, img.getWidth(), img.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @return True if object blocks entity movement
     */
    public boolean isSolid() {
        return isSolid;
    }

    public Image getImg() {
        return img;
    }
}
