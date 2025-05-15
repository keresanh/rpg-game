package org.example.gamefx.items;

import javafx.scene.image.Image;
import org.example.gamefx.utils.Utils;

/**
 * Defines types of items available in the game
 */
public enum ItemType {
    SWORD("sword.png"),
    KEY("key.png"),
    HEALING_POTION("healingPotion_1.png");

    private String filename;
    private Image image;

    /**
     * Constructs an item type with the associated image file.
     *
     * @param fileName The filename of the image for this item type
     */
    ItemType(String fileName) {
        this.filename = fileName;
        this.image = Utils.loadImg("/items/" + filename);
    }

    /**
     * @return Filename of item's texture
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return Loaded image resource for this item
     */
    public Image getImage() {
        return image;
    }
}
