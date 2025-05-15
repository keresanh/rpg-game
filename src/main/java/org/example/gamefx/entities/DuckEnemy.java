package org.example.gamefx.entities;

import org.example.gamefx.Collision;
import org.example.gamefx.Direction;
import org.example.gamefx.World;

/**
 * Represents a duck enemy that follows the player.
 * Moves directly toward the player and attacks when in range.
 */
public class DuckEnemy extends Enemy {
    private final double DUCK_SPEED = 1.0;

    /**
     * Creates a duck enemy with specified parameters
     *
     * @param x Initial X position in pixels
     * @param y Initial Y position in pixels
     * @param hp Initial health points
     * @param imgName Base name for animation sprites
     * @param collision Collision detection system reference
     * @param world Game world reference
     */
    public DuckEnemy(int x, int y, int hp, String imgName, Collision collision, World world) {
        super(x, y, hp, imgName, "/sprites/duckEnemy/", collision, world);
    }

    /**
     * Updates enemy state - moves directly toward player using normalized vectors,
     * handles collisions, and performs attacks
     *
     * @param player Reference to the player entity
     */
    @Override
    public void update(Player player) {
        if (isDead) {
            handleDeath();
            return;
        }

        double dx = Double.compare(player.getX(), x);
        double dy = Double.compare(player.getY(), y);

        isMoving = dx != 0 || dy != 0;
        // Set the direction
        if (Math.abs(dx) > Math.abs(dy)) {
            currentDirection = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else if (dy != 0) {
            currentDirection = dy > 0 ? Direction.DOWN : Direction.UP;
        }

        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx*dx + dy*dy);
            dx /= length;
            dy /= length;
        }

        double newX = x + dx * DUCK_SPEED;
        double newY = y + dy * DUCK_SPEED;

        if (collision.canMove(this, newX , newY)) {
            x = newX;
            y = newY;
        }
        checkAndAttack(player, System.currentTimeMillis());
        updateAnimation(System.currentTimeMillis());
    }

    /**
     * @return Display name for this enemy type
     */
    @Override
    public String getEnemyName() {
    return "Duck";
}
}
