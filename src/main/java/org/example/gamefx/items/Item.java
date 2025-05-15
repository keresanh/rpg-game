package org.example.gamefx.items;

import javafx.scene.image.Image;
import org.example.gamefx.entities.Player;
import org.example.gamefx.utils.Utils;

/**
 * Base class for all inventory items
 */
public abstract class Item {
    private ItemType type;
    private Image img;

    /**
     * Creates new item of specified type
     *
     * @param type Category and behavior identifier
     */
    public Item(ItemType type) {
        this.type = type;
        this.img = Utils.loadImg("/items/" + type.getFilename());
    }

    /**
     * Performs item-specific action when used
     *
     * @param player Player using the item
     */
    public abstract void use(Player player);

    /**
     * @return Item's classification type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * @return Visual representation of item
     */
    public Image getImg() {
        return img;
    }
}
