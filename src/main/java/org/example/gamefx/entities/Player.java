package org.example.gamefx.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import org.example.gamefx.Collision;
import org.example.gamefx.Direction;
import org.example.gamefx.Input;
import org.example.gamefx.World;
import org.example.gamefx.items.*;
import org.example.gamefx.utils.Logger;
import org.example.gamefx.utils.Utils;
import java.io.File;

/**
 * Represents the player character with movement, combat, inventory, and health .
 */
public class Player extends Entity {
private boolean interacting = false;
    private long lastInteractionTime = 0;
    private static final long INTERACTION_COOLDOWN = 500;
    private int hp;
    private Inventory inventory;

    // Character properties
    private Image characterImage;
    private final double CHARACTER_SPEED = 3.0;
    private double characterWidth;
    private double characterHeight;
    private Direction currentDirection = Direction.NONE;
    private boolean isMoving = false;

    // Animation frames
    private final Image[][] walkAnimations; // [direction][frame]
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DURATION = 200; // 200ms

    // Input handling
    private Input input;
    // Attack animations
    private final Image[][] attackAnimations;
    private boolean isAttacking = false;
    private long attackStartTime;
    private static final long ATTACK_DURATION = 500; // 0.5 sekundy
    private static final long ATTACK_FRAME_DURATION = ATTACK_DURATION / 2;

    private World world;
    private Image[] heartImages = new Image[5];

    private boolean cKeyWasPressed = false;
    private boolean vKeyWasPressed = false;

    /**
     * Constructs a new player character.
     *
     * @param x Initial X position in pixels
     * @param y Initial Y position in pixels
     * @param hp Initial health points
     * @param imgName Base name for character sprites
     * @param collision Collision detection system reference
     * @param world Game world reference
     * @param input Input handling system reference
     */
    public Player(int x, int y, int hp, String imgName, Collision collision, World world, Input input) {
        super(x, y, hp, imgName, collision, world);
        this.inventory = new Inventory(4, world);
        this.input = input;
        this.walkAnimations = loadWalkAnimations();
        this.characterImage = walkAnimations[Direction.DOWN.ordinal()][0];
        this.characterWidth = characterImage.getWidth();
        this.characterHeight = characterImage.getHeight();

        this.attackAnimations = loadAttackAnimations();
        this.hp = hp;
        this.world = world;
        // Loading images of hearts
        for(int i = 0; i < 5; i++) {
            heartImages[i] = Utils.loadImg("/hearts/heart_"+i+".png");
        }
        addItemToInventory(new Sword());
    }

    private void handleAttackInput() {
        if (input.isKeyPressed(KeyCode.SPACE)) {
            input.consumeKeyPress(KeyCode.SPACE);
        }
    }

    /**
     * Handles input for inventory interactions.
     * <p>
     * Pressing the C key cycles through inventory slots, and pressing the V key uses the selected item
     * (if it is usable and not a key). This method prevents repeated actions while holding down keys.
     */
    private void handleInventoryInput() {
        if (input.isKeyPressed(KeyCode.C)) {
            if (!cKeyWasPressed) { // Prevents repeated cycling while holding the key
                int currentSlot = inventory.getSelectedSlot();
                int newSlot = (currentSlot + 1) % inventory.getCapacity();
                inventory.setSelectedSlot(newSlot);
                cKeyWasPressed = true;
            }
        } else {
            cKeyWasPressed = false;
        }

        if (input.isKeyPressed(KeyCode.V)) {
            if (!vKeyWasPressed && !input.isKeyConsumed(KeyCode.V)) {
                int slot = inventory.getSelectedSlot();
                if (slot != -1) {
                    Item item = inventory.getItem(slot);
                    if (item != null && item.getType() != ItemType.KEY) {
                        Logger.getInstance().info("Item " + item.getType() + " is used.");
                        item.use(this);
                    }
                }
                vKeyWasPressed = true;
            }
        } else {
            vKeyWasPressed= false;
        }
    }

    private Image getCurrentHeartImage() {
        if (hp > 80) return heartImages[4];     // Full of heart
        else if (hp > 75) return heartImages[3]; // 3/4 heart
        else if (hp > 50) return heartImages[2]; // 1/2 heart
        else if (hp > 25) return heartImages[1];
        else return heartImages[0]; // 1/4 heart
    }

    /**
     * Renders player's health indicator using heart images
     *
     * @param gc Graphics context for drawing
     */
    public void renderHearts(GraphicsContext gc) {
        Image heartImage = getCurrentHeartImage();
        double heartWidth = heartImage.getWidth();
        double heartHeight = heartImage.getHeight();
        double startX = (world.getWidth() / 2 -  2 * heartWidth) / 2;
        double startY = 5;

        gc.drawImage(heartImage, startX, startY, heartWidth, heartHeight);
    }

    /**
     * @return True if player's health has reached zero
     */
    public boolean isDead() {
        return this.hp <= 0;
    }

    /**
     * Reduces the player's health by the specified damage amount.
     * Health will not drop below 0.
     *
     * @param damage Amount of damage to subtract from health
     */
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
    }

    /**
     * Returns the current animation frame for rendering the player.
     * Shows either attack or movement animation depending on the state.
     *
     * @return Current animation frame as an Image.
     */
    public Image getImage() {
        return isAttacking ?
                attackAnimations[currentDirection.ordinal()][currentFrame] :
                walkAnimations[currentDirection.ordinal()][currentFrame];
    }

    public double getSpriteX() {
        return 0;
    }

    public double getSpriteY() {
        return 0;
    }

    public double getImageWidth() {
        return getImage().getWidth();
    }

    public double getImageHeight() {
        return getImage().getHeight();
    }

    /**
     * Loads walking animation frames for each direction (UP, DOWN, LEFT, RIGHT, NONE).
     *
     * @return a 2D array of walking animation images indexed by direction and frame.
     */
    private Image[][] loadWalkAnimations() {
        Image[][] animations = new Image[Direction.values().length][3];

        for (int i = 0; i < 3; i++) {
            animations[Direction.LEFT.ordinal()][i] = loadImage("character_left_" + (i+1) + ".png");
            animations[Direction.RIGHT.ordinal()][i] = loadImage("character_right_" + (i+1) + ".png");
            animations[Direction.UP.ordinal()][i] = loadImage("character_up_" + (i+1) + ".png");
            animations[Direction.DOWN.ordinal()][i] = loadImage("character_down_" + (i+1) + ".png");
            animations[Direction.NONE.ordinal()][i] = loadImage("character_down_1.png");
        }
        return animations;
    }

    /**
     * Loads attack animation frames for each direction (UP, DOWN, LEFT, RIGHT, NONE).
     *
     * @return a 2D array of attack animation images indexed by direction and frame.
     */
    private Image[][] loadAttackAnimations() {
        Image[][] animations = new Image[Direction.values().length][2];
        for (int i = 0; i < 2; i++) {
            animations[Direction.LEFT.ordinal()][i] = loadImage("character_attack_left_" + (i+1) + ".png");
            animations[Direction.RIGHT.ordinal()][i] = loadImage("character_attack_right_" + (i+1) + ".png");
            animations[Direction.UP.ordinal()][i] = loadImage("character_attack_up_" + (i+1) + ".png");
            animations[Direction.DOWN.ordinal()][i] = loadImage("character_attack_down_" + (i+1) + ".png");
            animations[Direction.NONE.ordinal()][i] = loadImage("character_attack_down_1.png");
        }
        return animations;
    }


    private Image loadImage(String path) {
//        return new Image(getClass().getResourceAsStream(path));
        return new Image(String.valueOf(new File(path)));
    }

    /**
     * Resets player position to default location
     */
    public void resetPosition() {
        x = (int) (400 - characterWidth/2);
        y = (int) (300 - characterHeight/2);
    }

    public double getWidth() {
        return characterWidth;
    }

    public double getHeight() {
        return characterHeight;
    }

    public World getWorld() {
        return world;
    }

    public boolean isInteracting() {
        return interacting;
    }

    public void resetInteraction() {
        interacting = false;
    }

    /**
     * Updates the player's state during the game loop.
     * Handles movement, animation, attack and inventory input.
     *
     * @param now Current timestamp in milliseconds
     * @param sceneWidth Width of game scene for boundary checks
     * @param sceneHeight Height of game scene for boundary checks
     */
    public void update(long now, double sceneWidth, double sceneHeight) {
        if (!isAttacking) {
            updatePosition(sceneWidth, sceneHeight);
        }
        updateAnimation(now);
        handleAttackInput();
        handleInventoryInput();
    }

    /**
     * Updates the player's position based on input keys.
     * <p>
     * Supports movement in four directions and normalizes speed when moving diagonally.
     * Also checks for collisions before applying new position.
     *
     * @param sceneWidth the width of the game scene, used for boundary conditions.
     * @param sceneHeight the height of the game scene, used for boundary conditions.
     */
    private void updatePosition(double sceneWidth, double sceneHeight) {
        double dx = 0, dy = 0;

        // Calculate movement direction using direct controls
        if (input.isKeyPressed(KeyCode.W) || input.isKeyPressed(KeyCode.UP)) {
            dy -= 1;
            currentDirection = Direction.UP;
        }
        if (input.isKeyPressed(KeyCode.S) || input.isKeyPressed(KeyCode.DOWN)) {
            dy += 1;
            currentDirection = Direction.DOWN;
        }
        if (input.isKeyPressed(KeyCode.A) || input.isKeyPressed(KeyCode.LEFT)) {
            dx -= 1;
            currentDirection = Direction.LEFT;
        }
        if (input.isKeyPressed(KeyCode.D) || input.isKeyPressed(KeyCode.RIGHT)) {
            dx += 1;
            currentDirection = Direction.RIGHT;
        }

        // Normalize diagonal movement
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx*dx + dy*dy);
            dx /= length;
            dy /= length;
        }

        double newX = x + dx * CHARACTER_SPEED;
        double newY = y + dy * CHARACTER_SPEED;
        if (collision.canMove(this, newX , newY )) {
            x = newX;
            y = newY;
        }

        // Update moving state
        isMoving = dx != 0 || dy != 0;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void setAttackStartTime(long attackStartTime) {
        this.attackStartTime = attackStartTime;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    /**
     * Executes an attack in the direction the player is facing.
     * Applies damage to any enemies within the attack area.
     */
    public void performAttack() {
        double attackRange = 50; // Total attack range
        double playerCenterX = x + getImageWidth()/2;
        double playerCenterY = y + getImageHeight()/2;

        // Determine the attack zone by direction
        double attackX = playerCenterX;
        double attackY = playerCenterY;
        double attackWidth = getImageWidth();
        double attackHeight = getImageHeight();

        switch(currentDirection) {
            case UP:
                attackY = playerCenterY - attackRange;
                attackHeight = attackRange;
                break;
            case DOWN:
                attackY = playerCenterY;
                attackHeight = attackRange;
                break;
            case LEFT:
                attackX = playerCenterX - attackRange;
                attackWidth = attackRange;
                break;
            case RIGHT:
                attackX = playerCenterX;
                attackWidth = attackRange;
                break;
        }

        // Attacking hitbox
        Rectangle2D attackArea = new Rectangle2D(
                attackX - attackWidth/2,
                attackY - attackHeight/2,
                attackWidth,
                attackHeight
        );

        for (Enemy enemy : world.getEnemies()) {
            // Hitbox enemy
            Rectangle2D enemyHitbox = new Rectangle2D(
                    enemy.getX(),
                    enemy.getY(),
                    enemy.getWidth(),
                    enemy.getHeight()
            );

            // Attack area collision control with the enemy
            if (attackArea.intersects(enemyHitbox)) {
                enemy.takeDamage(10);
            }
        }
    }

    /**
     * Updates the player's animation frame based on their movement or attack state.
     * <p>
     * Handles both idle/movement animation and timed attack animations with frame switching.
     *
     * @param now the current time in nanoseconds, used to determine frame timing.
     */
    private void updateAnimation(long now) {
        if (isAttacking) {
            // Attack animation
            long timeSinceAttack = now - attackStartTime;
            if (timeSinceAttack >= ATTACK_DURATION) {
                isAttacking = false;
                return;
            }

            int frame = (int) (timeSinceAttack / ATTACK_FRAME_DURATION);
            currentFrame = frame % 2;
        } else {
            // Motion animation
            if (!isMoving) {
                currentFrame = 0;
                return;
            }

            if (now - lastFrameTime > FRAME_DURATION) {
                currentFrame = (currentFrame + 1) % 3;
                lastFrameTime = now;
            }
        }
    }

    /**
     * @return Current health points
     */
    public int getHp() {
        return hp;
    }

    /**
     * Sets player's health points
     *
     * @param hp New health value (clamped to 0-100)
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Heals the player by specified amount
     *
     * @param healAmount Number of health points to restore
     */
    public void heal(int healAmount) {
        this.setHp(Math.min(this.getHp() + healAmount, 100));
        Logger.getInstance().info("Player used Healing Potion. HP: " + healAmount + " → HP: " + this.hp);
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
    * Adds an item to player's inventory
    *
    * @param item Item to add to inventory
     */
    public void addItemToInventory(Item item) {
        if (inventory != null) {
            inventory.addItem(item);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
//        System.out.println("Koordinaty; X:" + x + "Y "+ y);
    }
}