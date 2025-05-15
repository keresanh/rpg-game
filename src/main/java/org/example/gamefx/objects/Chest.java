package org.example.gamefx.objects;

import javafx.scene.image.Image;
import org.example.gamefx.entities.Player;
import org.example.gamefx.items.HealingPotion;
import org.example.gamefx.items.Item;
import org.example.gamefx.items.Key;
import org.example.gamefx.utils.Logger;
import org.example.gamefx.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interactive container that holds items and can be opened by player proximity
 */
public class Chest extends GameObject{
    private boolean isOpened;
    private Key key;
    private Image chestOpenImg;
    private List<Item> contents = new ArrayList<>();

    /**
     * Creates chest with default contents (1 key and 2 healing potions)
     *
     * @param x X position in world coordinates
     * @param y Y position in world coordinates
     * @param imgName Texture filename
     */
    public Chest(int x, int y, String imgName) {
        this(x, y, "chest.png", Arrays.asList(new Key(1), new HealingPotion(2)));
    }

    /**
     * Creates chest with custom contents
     *
     * @param x X position in world coordinates
     * @param y Y position in world coordinates
     * @param imgName Texture filename
     * @param contents List of items to contain
     */
    public Chest(int x, int y, String imgName, List<Item> contents) {
        super(x, y, "chest.png", true);
        this.contents = contents;
        this.chestOpenImg = Utils.loadImg("/objects/chestOpen.png");
    }

    /**
     * Checks player proximity and transfers contents to inventory when opened
     * @param player Player entity to check interaction with
     */
    @Override
    public void update(Player player) {
        if (!isOpened && nearPlayer(player)) {
            isOpened = true;
            super.img = chestOpenImg;
            Logger.getInstance().info("Chest opened at: x=" + x + ", y=" + y);

            for (Item item : contents) {
                player.addItemToInventory(item);
            }
        }
    }

    /**
     * Checks whether the player is within interaction range of the chest.
     *
     * @param player The player to check
     * @return True if player is near the chest, false otherwise
     */
    private boolean nearPlayer(Player player) {
        // (64 = velikost tile)
        return Math.abs(player.getX() - x) <= 64 && Math.abs(player.getY() - y) <= 64; //puvodni
    }
}
