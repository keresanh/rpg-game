package org.example.gamefx;

import org.example.gamefx.blocks.Block;
import org.example.gamefx.entities.Entity;
import org.example.gamefx.entities.Player;
import org.example.gamefx.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision detection between entities, game objects, and map tiles
 */
public class Collision {
    private World world;
//    private Map map;

    public Collision(World world) {
        this.world = world;
//        this.map = world.getMap();
    }

    /**
     * Checks if an entity can move to a target position without collisions
     *
     * @param entity The entity attempting to move
     * @param targetX Proposed X coordinate in pixels
     * @param targetY Proposed Y coordinate in pixels
     * @return True if movement is allowed, false if any collision occurs
     */
    public boolean canMove(Entity entity, double targetX, double targetY) {
        boolean mapCollision = checkMapCollision(entity, targetX, targetY);
        boolean objCollision = checkObjectCollision(entity, targetX, targetY);
        boolean entityCollision = checkEntityCollision(entity, targetX, targetY);

        if (mapCollision || objCollision || entityCollision) {
//            if (mapCollision) System.out.println("map col");
//            if (objCollision) System.out.println("obj col");
//            if (entityCollision) System.out.println("ent col");
            return false;
        }
        return true;
    }

    /**
     * Checks for collisions with map tiles
     *
     * @param entity Entity being checked
     * @param targetX Proposed X position
     * @param targetY Proposed Y position
     * @return True if entity would collide with solid tiles at target position
     */
    private boolean checkMapCollision(Entity entity, double targetX, double targetY) {
        Map map = world.getMap();

        double entityWidth = entity.getImg().getWidth();
        double entityHeight = entity.getImg().getHeight();

        int leftTile = (int) (targetX / map.getTILE_SIZE());
        int rightTile = (int) ((targetX + entityWidth - 1) / map.getTILE_SIZE());
        int topTile = (int) (targetY / map.getTILE_SIZE());
        int bottomTile = (int) ((targetY + entityHeight - 1) / map.getTILE_SIZE());
        for (int x = leftTile; x <= rightTile; x++) {
            for (int y = topTile; y <= bottomTile; y++) {
                Block curBlock = map.getBlock(x, y);
                if (curBlock.isCollidable()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks for collisions with solid game objects
     *
     * @param entity Entity being checked
     * @param targetX Proposed X position
     * @param targetY Proposed Y position
     * @return True if entity would collide with any solid objects
     */
    private boolean checkObjectCollision(Entity entity, double targetX, double targetY) {
//        List<GameObject> objects = map.getObjectsAt(targetX, targetY);
        List<GameObject> objects = world.getGameObjects();
        double entityWidth = entity.getImg().getWidth();
        double entityHeight = entity.getImg().getHeight();

        // entity hitbox
        double entityLeft = targetX;
        double entityRight = targetX + entityWidth;
        double entityTop = targetY;
        double entityBottom = targetY + entityHeight;
        for (GameObject obj : objects) {
            if (obj.isSolid) {
                // obj hitbox
                double objLeft = obj.getX();
                double objRight = obj.getX() + obj.getImg().getWidth();
                double objTop = obj.getY();
                double objBottom = obj.getY() + obj.getImg().getHeight();
                // check collision
                boolean collisionX = (entityRight > objLeft) && (entityLeft < objRight);
                boolean collisionY = (entityBottom > objTop) && (entityTop < objBottom);
                if (collisionX && collisionY) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks for collisions with other entities
     *
     * @param entity Entity being checked
     * @param targetX Proposed X position
     * @param targetY Proposed Y position
     * @return True if entity would collide with any relevant entities
     */
    private boolean checkEntityCollision(Entity entity, double targetX, double targetY) {
        List<Entity> entitiesToCheck = new ArrayList<>(world.getEnemies());
        if (!(entity instanceof Player)) {
            entitiesToCheck.add(world.getPlayer());
        }
        // Hitbox moving entities
        double[] movingHitbox = getHitbox(entity, targetX, targetY);

        for (Entity other : entitiesToCheck) {
            if (other == entity) continue;
            // Hitbox other entities
            double[] otherHitbox = getHitbox(other, other.getX(), other.getY());
            // Collision detection
            boolean collisionX = (movingHitbox[1] > otherHitbox[0]) && (movingHitbox[0] < otherHitbox[1]);
            boolean collisionY = (movingHitbox[3] > otherHitbox[2]) && (movingHitbox[2] < otherHitbox[3]);
            // Special case: getting out of a collision
            if (isAlreadyColliding(entity, other) && isMovingAway(entity, targetX, targetY, other)) {
                continue;
            }
            if (collisionX && collisionY) return true;
        }
        return false;
    }

    /**
     * Calculates entity's hitbox coordinates [left, right, top, bottom] at given position
     * Player uses centered 48x48 hitbox, enemies use full image bounds
     *
     * @param entity Entity to calculate for
     * @param x Proposed X position
     * @param y Proposed Y position
     * @return Array containing [leftX, rightX, topY, bottomY] coordinates
     */
    private double[] getHitbox(Entity entity, double x, double y) {
        if (entity instanceof Player) {
            // Player: 48x64 centred hitbox
            return new double[]{x, x + 48, y, y + 48};
        } else {
            // Enemies: hitbox according to the picture (x and y are the upper left corner)
            double width = entity.getImg().getWidth();
            double height = entity.getImg().getHeight();
            return new double[]{x, x + width, y, y + height};
        }
    }

    /**
     * Checks if two entities are currently colliding using Axis-Aligned Bounding Box (AABB) detection
     *
     * @param a First entity
     * @param b Second entity
     * @return True if their hitboxes currently overlap
     */
    private boolean isAlreadyColliding(Entity a, Entity b) {
        double[] aHitbox = getHitbox(a, a.getX(), a.getY());
        double[] bHitbox = getHitbox(b, b.getX(), b.getY());

        return (aHitbox[1] > bHitbox[0]) && (aHitbox[0] < bHitbox[1]) &&
                (aHitbox[3] > bHitbox[2]) && (aHitbox[2] < bHitbox[3]);
    }

    /**
     * Determines whether an entity is moving away from another entity.
     * <p>
     * Used to avoid detecting collisions in cases where two entities are overlapping
     * but are separating due to movement.
     *
     * @param entity Moving entity to check
     * @param targetX Entity's destination X
     * @param targetY Entity's destination Y
     * @param other Entity to check relative movement against
     * @return True if movement vector points away from other entity
     */
    private boolean isMovingAway(Entity entity, double targetX, double targetY, Entity other) {
        //Direction of movement of the entity
        double dx = targetX - entity.getX();
        double dy = targetY - entity.getY();

        // Relative position of another entity
        double relativeX = other.getX() - entity.getX();
        double relativeY = other.getY() - entity.getY();

        // Movement away from the entity (negative scalar product)
        return (dx * relativeX + dy * relativeY) < 0;
    }
}
