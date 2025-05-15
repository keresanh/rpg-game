package org.example.gamefx.entities;

import javafx.scene.image.Image;
import org.example.gamefx.Collision;
import org.example.gamefx.Direction;
import org.example.gamefx.World;
import javafx.scene.canvas.GraphicsContext;
import org.example.gamefx.utils.Logger;
import org.example.gamefx.utils.Utils;

/**
 *  Base class for all enemy entities.
 *  Handles shared logic such as movement, attack, animation, rendering, and health
 */
public abstract class Enemy extends Entity {
    protected Direction currentDirection = Direction.DOWN;
    protected boolean isMoving = false;

    protected final Image[][] walkAnimations; // [direction][frame]
    protected int currentFrame = 0;
    protected long lastFrameTime = 0;
    protected static final long FRAME_DURATION = 200;

    protected Image[][] attackAnimations;
    protected boolean isAttacking = false;
    protected long attackStartTime;
    protected static final long ATTACK_DURATION = 500;
    protected static final long ATTACK_COOLDOWN = 1000;
    protected long lastAttackTime = 0;
    protected int attackDamage = 10;
    protected static final double ATTACK_RANGE = 50;

    private long hpDisplayTime = 0;
    private static final long HP_DISPLAY_DURATION = 2000; // 2 sekundy

    protected int curHp;
    private static Image[] heartImages = new Image[5];

    static {
        // Load heart images once for all enemies
        heartImages[0] = Utils.loadImg("/hearts/heart_0.png");
        heartImages[1] = Utils.loadImg("/hearts/heart_1.png");
        heartImages[2] = Utils.loadImg("/hearts/heart_2.png");
        heartImages[3] = Utils.loadImg("/hearts/heart_3.png");
        heartImages[4] = Utils.loadImg("/hearts/heart_4.png");
    }

    protected boolean isDead = false;
    protected long deathStartTime;
    protected static final long DEATH_DURATION = 1000; // 1 sekunda
    protected Image deathImage;
    protected boolean shouldRemove = false;

    /**
     * Creates a new enemy entity
     *
     * @param x Initial X position in pixels
     * @param y Initial Y position in pixels
     * @param hp Initial health points
     * @param imgName Base sprite filename
     * @param basePath Folder path for animation sprites
     * @param collision Collision system reference
     * @param world Game world reference
     */
    public Enemy(int x, int y, int hp, String imgName, String basePath, Collision collision, World world) {
        super(x, y, hp, basePath + imgName + "_down_1.png", collision, world);
        this.walkAnimations = loadWalkAnimations(imgName, basePath);
        this.attackAnimations = loadAttackAnimations(imgName, basePath);
        this.img = walkAnimations[Direction.DOWN.ordinal()][0];

        this.curHp = hp;
        this.deathImage = loadImage(basePath + imgName + "_die.png");
    }

    /**
     * Main update method to be implemented by concrete enemies
     *
     * @param player Reference to the player entity
     */
    public abstract void update (Player player);


    /**
     * Loads attack animation frames for all directions from resource files.
     *
     * <p>Attack animations use a simplified single-frame format where all directions
     * share the same base attack sprite ({@code [basePath][imgName]_attack_1.png}).
     *
     * @param imgName    Base name of the enemy's sprites
     * @param basePath   Directory path containing the attack animation sprites
     * @return 2D array of images structured as [direction][frame] where:
     *         - Direction indexes: LEFT=0, RIGHT=1, UP=2, DOWN=3
     *         - Single frame (index 0) for all directions
     */
    protected Image[][] loadAttackAnimations(String imgName, String basePath) {
//        Image[][] animations = new Image[Direction.values().length][3];
        Image[][] animations = new Image[Direction.values().length][1];
        for (int i = 0; i < 1; i++) {

            animations[Direction.LEFT.ordinal()][i] = loadImage(basePath + imgName + "_attack_" + (i+1) + ".png");
            animations[Direction.RIGHT.ordinal()][i] = loadImage(basePath + imgName + "_attack_" + (i+1) + ".png");
            animations[Direction.UP.ordinal()][i] = loadImage(basePath + imgName + "_attack_" + (i+1) + ".png");
            animations[Direction.DOWN.ordinal()][i] = loadImage(basePath + imgName + "_attack_" + (i+1) + ".png");
        }
        return animations;
    }

    /**
     * Checks attack conditions and damages player if in range
     *
     * @param player Target player entity
     * @param now Current system time in milliseconds
     */
    protected void checkAndAttack(Player player, long now) {
        double distance = Math.hypot(player.getX() - x, player.getY() - y);

        if (distance < ATTACK_RANGE) {
            if (now - lastAttackTime > ATTACK_COOLDOWN) {
                player.takeDamage(attackDamage);
                lastAttackTime = now;
                isAttacking = true;
                attackStartTime = now;
            }
        }
    }

    /**
     * Updates animation frames based on movement and attack state
     *
     * @param now Current system time in milliseconds
     */
    protected void updateAnimation(long now) {
        if (isAttacking) {
            long timeSinceAttack = now - attackStartTime;
            if (timeSinceAttack >= ATTACK_DURATION) {
                isAttacking = false;
            } else {
//                int frame = (int) (timeSinceAttack / (ATTACK_DURATION / 3));
//                currentFrame = frame % 3;
                //pro 1 snimek
                int frame = 0; // Only the first image
                currentFrame = frame;
                img = attackAnimations[currentDirection.ordinal()][currentFrame];
                return;
            }
        }

        if (!isMoving) {
            currentFrame = 0;
            img = walkAnimations[currentDirection.ordinal()][0];
            return;
        }

        if (now - lastFrameTime > FRAME_DURATION) {
            currentFrame = (currentFrame + 1) % 3;
            lastFrameTime = now;
            img = walkAnimations[currentDirection.ordinal()][currentFrame];
        }
    }

    /**
     * Applies damage to the enemy and handles death state
     *
     * @param damage Amount of damage to apply
     */
    public void takeDamage(int damage) {
        if (isDead) return;

        curHp = Math.max(0, curHp - damage);
        hpDisplayTime = System.currentTimeMillis();

        if (curHp <= 0) {
            isDead = true;
            deathStartTime = System.currentTimeMillis();
            Logger.getInstance().info(getEnemyName() + " died");
        }
    }

    /**
     * Renders health bar above enemy when recently damaged
     *
     * @param gc Graphics context for drawing
     * @param cameraX Camera X offset
     * @param cameraY Camera Y offset
     */
    public void renderHP(GraphicsContext gc, int cameraX, int cameraY) {
        if (System.currentTimeMillis() - hpDisplayTime < HP_DISPLAY_DURATION) {
            Image heartImage = getCurrentHeartImage();
            if (heartImage == null) return;

            double heartWidth = heartImage.getWidth() * 0.5; //Size reduction
            double heartHeight = heartImage.getHeight() * 0.5;
            double xPos = x - cameraX - heartWidth/2;
            double yPos = y - cameraY - 50; // Position over the enemy

            gc.drawImage(heartImage, xPos, yPos, heartWidth, heartHeight);
        }
    }

    /**
     * Determines which heart image to display based on current HP percentage
     *
     * @return Appropriate heart image for health display (0-4)
     */
    private Image getCurrentHeartImage() {
        Logger.getInstance().info("MAX HP: " + hp + ", Current HP: " + curHp);
        double percent = (double) curHp * 100 / hp;
//        System.out.println("ENEMY HP " + curHp + " percent " + percent);

        if (percent > 80) return heartImages[4];
        if (percent > 60) return heartImages[3];
        if (percent > 30) return heartImages[2];
        if (percent > 25) return heartImages[1];
        return heartImages[0];
    }

    /**
     * @return Current animation frame image for the enemy's direction
     */
    public Image getImage() {
        return walkAnimations[currentDirection.ordinal()][currentFrame];
    }

    /**
     * Loads walking animation sprites from files
     *
     * @param imgName Base image name
     * @param basePath Folder path containing sprites
     * @return array [direction][frame] of animation images
     */
    private Image[][] loadWalkAnimations(String imgName, String basePath) {
        Image[][] animations = new Image[Direction.values().length][3];

        for (int i = 0; i < 3; i++) {
            animations[Direction.LEFT.ordinal()][i] = loadImage(basePath + imgName + "_left_" + (i + 1) + ".png");
            animations[Direction.RIGHT.ordinal()][i] = loadImage(basePath + imgName + "_right_" + (i + 1) + ".png");
            animations[Direction.UP.ordinal()][i] = loadImage(basePath + imgName + "_up_" + (i + 1) + ".png");
            animations[Direction.DOWN.ordinal()][i] = loadImage(basePath + imgName + "_down_" + (i + 1) + ".png");
            animations[Direction.NONE.ordinal()][i] = loadImage(basePath + imgName + "_down_1.png");
        }
        return animations;
    }

    /**
     * Loads an image from the resource path
     *
     * @param path Full resource path to image
     * @return Loaded Image object
     */
    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
//        return new Image(String.valueOf(new File(path)));
    }

    /**
            * Handles death sequence - marks enemy for removal after death animation
     */
    protected void handleDeath() {
        if (System.currentTimeMillis() - deathStartTime > DEATH_DURATION) {
            shouldRemove = true;
        }
}


    /**
     * Renders the enemy at its current position with camera offset
     *
     * @param gc Graphics context for drawing
     * @param tileSize Unused parameter (legacy code)
     * @param cameraX Horizontal camera offset
     * @param cameraY Vertical camera offset
     */
    public void render(GraphicsContext gc, int tileSize, int cameraX, int cameraY) {
//        gc.drawImage(img, x - cameraX -24, y - cameraY - 24, img.getWidth(), img.getHeight());
        if (isDead) {
            gc.drawImage(deathImage, x - cameraX -24, y - cameraY - 24,
                    deathImage.getWidth(), deathImage.getHeight());
        } else {
            gc.drawImage(img, x - cameraX -24, y - cameraY - 24,
                    img.getWidth(), img.getHeight());
        }
    }

    /**
     * @return True if enemy should be removed from game world (death animation completed)
     */
    public boolean shouldRemove() {
        return shouldRemove;
    }

    public abstract String getEnemyName();

    /**
     * Empty base implementation for patrolling behavior (override in subclasses)
     */

    public void patrol() {}

    public int getCurHp() {
        return curHp;
    }

    public void setCurHp(int curHp) {
        this.curHp = curHp;
    }
}
