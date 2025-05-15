package org.example.gamefx.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.example.gamefx.entities.Player;
import org.example.gamefx.items.Item;
import org.example.gamefx.items.ItemType;
import org.example.gamefx.Input;

import org.example.gamefx.utils.Logger;
import org.example.gamefx.utils.Utils;

import java.io.IOException;

/**
 * Locked passage that requires a key to open and progress to next level
 */
public class Door extends GameObject {
    private boolean isOpened;
    private Image doorOpenImg;
    private boolean playerInRange;
    private static final int INTERACTION_DISTANCE = 64;
    private long lastInteractionTime = 0;
    private static final long INTERACTION_COOLDOWN = 1000;

    /**
     * Creates closed door at specified position
     *
     * @param x X position in world coordinates
     * @param y Y position in world coordinates
     */
    public Door(int x, int y) {
        super(x, y, "doorClose.png", true);
        this.doorOpenImg = Utils.loadImg("/objects/doorOpen.png");
//        System.out.println("doorOpenImg loaded: " + doorOpenImg);
    }

    /**
     * Handles player interaction attempts with cooldown
     *
     * @param player Player trying to interact
     * @throws IOException If level transition fails
     */
    @Override
    public void update(Player player) throws IOException {
        if (isOpened) return;
        // Distance control
        double distance = Math.sqrt(
                Math.pow(player.getX() - x, 2) +
                        Math.pow(player.getY() - y, 2)
        );
        playerInRange = distance < INTERACTION_DISTANCE;

        if (playerInRange && Input.getInstance().isKeyPressed(KeyCode.V)) {
            handleDoorInteraction(player);
        }
    }

    /**
     * Handles door interaction logic, including checking for a key, consuming it,
     * and triggering level transition.
     *
     * @param player The player attempting to open the door
     * @throws IOException If loading the next level fails
     */
    public void handleDoorInteraction(Player player) throws IOException {
        if (System.currentTimeMillis() - lastInteractionTime < INTERACTION_COOLDOWN) return;

        lastInteractionTime = System.currentTimeMillis();

        int slot = player.getInventory().getSelectedSlot();
        Item item = player.getInventory().getItem(slot);

        if (item != null && item.getType() == ItemType.KEY) {
            open();
            item.use(player);
            Input.getInstance().consumeKeyPress(KeyCode.V);
            player.getWorld().changeLevel(player.getWorld().getCurrentLevel().getNextLevel());
        }
    }

    /**
     * Opens the door and disables collision.
     */
    public void open() {
        isOpened = true;
        img = doorOpenImg;
        isSolid = false;
        Logger.getInstance().info("Door opened at: x=" + x + ", y=" + y);
    }

    public boolean nearPlayer(Player player) {
        // (64 = velikost tile)
        return Math.abs(player.getX() - x) <= 64 && Math.abs(player.getY() - y) <= 64;
    }

    /**
     * Renders door state and interaction prompt
     * @param gc Graphics context for drawing
     * @param tileSize Unused parameter (inherited from parent)
     * @param cameraX Camera horizontal offset
     * @param cameraY Camera vertical offset
     */
    @Override
    public void render(GraphicsContext gc, int tileSize, int cameraX, int cameraY) {
        int drawX = x - cameraX;
        int drawY = y - cameraY;

        // Center the image
        Image currentImage = isOpened ? doorOpenImg : img;
        gc.drawImage(currentImage, drawX - img.getWidth()/2, drawY - img.getHeight()/2);

        // View instructions when interacting
        if (playerInRange && !isOpened) {
            gc.setFill(Color.WHITE);
            gc.fillText("Press V with key to open", drawX - 60, drawY - 20);
        }
    }

    /**
     * @return True if door has been successfully opened
     */
    public boolean isOpened() {
        return isOpened;
    }
}
