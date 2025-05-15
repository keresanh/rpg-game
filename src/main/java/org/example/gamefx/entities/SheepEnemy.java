package org.example.gamefx.entities;

import org.example.gamefx.Collision;
import org.example.gamefx.Direction;
import org.example.gamefx.World;

public class SheepEnemy extends Enemy{
    private final double SHEEP_SPEED = 1.0;

    private long lastDirectionChange = 0;
    private static final long DIRECTION_CHANGE_INTERVAL = 2000;
    private final Direction[] patrolDirections = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};
    private int currentPatrolIndex = 0;
    // Distance for player detection (pixels)
    private static final double DETECTION_RANGE = 200;


    public SheepEnemy(int x, int y, int hp, String imgName, Collision collision, World world) {
//        super(x, y, hp, imgName, collision, world);
        super(x, y, hp, imgName, "/sprites/sheepEnemy/", collision, world);
    }

    @Override
    public void update(Player player) {
        if (isDead) {
            handleDeath();
            return;
        }

        double distanceToPlayer = Math.hypot(player.getX() - x, player.getY() - y);

        if (distanceToPlayer < DETECTION_RANGE) {
            chasePlayer(player);
        } else {
            patrol();
        }
        checkAndAttack(player, System.currentTimeMillis());
        updateAnimation(System.currentTimeMillis());
    }

    private void chasePlayer(Player player) {
        double dx = Double.compare(player.getX(), x);
        double dy = Double.compare(player.getY(), y);

        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
        }
        move(dx, dy);
    }

    public void patrol() {
        long now = System.currentTimeMillis();
        // Change direction after interval
        if (now - lastDirectionChange > DIRECTION_CHANGE_INTERVAL) {
            currentPatrolIndex = (currentPatrolIndex + 1) % patrolDirections.length;
            currentDirection = patrolDirections[currentPatrolIndex];
            lastDirectionChange = now;
        }

        double dx = 0, dy = 0;
        switch(currentDirection) {
            case LEFT: dx = -1; break;
            case RIGHT: dx = 1; break;
            case UP: dy = -1; break;
            case DOWN: dy = 1; break;
        }
        move(dx, dy);
    }

    private void move(double dx, double dy) {
        double newX = x + dx * SHEEP_SPEED;
        double newY = y + dy * SHEEP_SPEED;

        isMoving = (dx != 0 || dy != 0);

        if (collision.canMove(this, newX, newY)) {
            x = newX;
            y = newY;
        }

        // Update direction for animations
        if (Math.abs(dx) > Math.abs(dy)) {
            currentDirection = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else if (dy != 0) {
            currentDirection = dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }

    @Override
    public String getEnemyName() {
    return "Sheep";
}
}
