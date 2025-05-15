package org.example.gamefx.entities;

import javafx.scene.image.Image;
import org.example.gamefx.Collision;
import org.example.gamefx.World;
import org.example.gamefx.utils.Utils;

/**
 * Base class for all movable game entities with position and collision
 */
public abstract class Entity {
    public Image img;
    private World world;
    protected double x, y;
    protected int hp;
    protected double width;
    protected double height;
    public Collision collision;

    /**
     * Creates a new game entity
     *
     * @param x Initial X position in pixels
     * @param y Initial Y position in pixels
     * @param hp Initial health points
     * @param imgName Path to default sprite image
     * @param collision Collision system reference
     * @param world Game world reference
     */
    public Entity(int x, int y, int hp, String imgName, Collision collision, World world) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.img = Utils.loadImg(imgName);
        this.collision = collision;
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getImg() {
        return img;
    }

    public double getWidth() {
        return img.getWidth();
    }

    public double getHeight() {
        return img.getHeight();
    }
}
