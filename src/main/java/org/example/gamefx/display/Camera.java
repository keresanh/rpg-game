package org.example.gamefx.display;

import org.example.gamefx.Map;
import org.example.gamefx.entities.Player;

/**
 * Handles viewport tracking of player and map boundary constraints
 */
public class Camera {
    private int x;
    private int y;
    private int width;
    private int height;
    private Player player;

    /**
     * Creates a new camera viewport with specified dimensions
     *
     * @param width Viewport width in pixels
     * @param height Viewport height in pixels
     */
    public Camera(int width, int height) {
        this.x = 0;
        this.y = 0;
        this.width = width;
        this.height = height;
    }

    /**
     * Updates camera position to center on player while respecting map boundaries
     *
     * @param map Current game map for boundary checking
     */
    public void update(Map map) {
        if (player != null) {
            double playerX = player.getX();
            double playerY = player.getY();

            this.x = (int)playerX - width / 2;
            this.y = (int)playerY - height / 2;

            stayInBounds(map);
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Adjusts the camera position to ensure it doesn't move outside the map boundaries.
     *
     * @param map The map providing boundary dimensions.
     */
    private void stayInBounds(Map map) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + width > map.getWidthPixels()) x = map.getWidthPixels() - width;
        if (y + height > map.getHeightPixels()) y = map.getHeightPixels() - height;
    }
}
