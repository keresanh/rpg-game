package org.example.gamefx.items;

import org.example.gamefx.entities.Player;
/**
 * Melee weapon that enables player attacks
 */
public class Sword extends Item {
    /**
     * Creates sword item with default properties
     */
    public Sword() {
        super(ItemType.SWORD);
    }

    /**
     * Initiates player attack sequence if not already attacking
     *
     *  @param player Player wielding the sword
     */
    @Override
    public void use(Player player) {
        if (!player.isAttacking()) {
            player.setAttacking(true);
            player.setAttackStartTime(System.currentTimeMillis());
            player.setCurrentFrame(0);
            player.performAttack();
        }
    }
}