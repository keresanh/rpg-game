package org.example.gamefx.items;

import org.example.gamefx.entities.Player;

/**
 * Represents a key item that can be stacked and used to unlock doors/chests
 */
public class Key extends StackableItem {
    /**
     * Creates a stack of keys
     *
     * @param count Initial number of keys in stack
     */
    public Key(int count) {
        super(ItemType.KEY, count);
    }

    /**
     * Consumes one key from the stack when used
     *
     * @param player Player using the key (not directly used, but required by interface)
     */
    @Override
    public void use(Player player) {
        decrementCount();
        if (getCount() <= 0) {
            player.getInventory().getItems().remove(this);
        }
    }
}
