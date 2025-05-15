package org.example.gamefx.items;

import org.example.gamefx.entities.Player;
/**
 * Restores player's health when used. Can be stacked in inventory.
 */
public class HealingPotion extends StackableItem{
    private int healAmount = 25;

    /**
     * Creates a healing potion stack
     *
     * @param count Initial number of potions in stack
     */
    public HealingPotion(int count) {
        super(ItemType.HEALING_POTION, count);
    }

    /**
     * Heals player and consumes one potion from stack
     *
     * @param player Target player to heal
     */
    @Override
    public void use(Player player) {
        decrementCount();
        player.heal(healAmount);
        player.setHp(Math.min(100, player.getHp()));

        if (getCount() <= 0) {
            player.getInventory().getItems().remove(this);
        }
    }
}
