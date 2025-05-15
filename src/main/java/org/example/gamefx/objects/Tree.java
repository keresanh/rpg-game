package org.example.gamefx.objects;

import org.example.gamefx.entities.Player;

/**
 * Non-interactive environment object that blocks movement
 */
public class Tree extends GameObject{
    /**
     * Creates new tree obstacle
     *
     * @param x World X position
     * @param y World Y position
     * @param imgName Texture filename
     */
    public Tree(int x, int y, String imgName) {
        super(x, y, imgName, true);

    }

    @Override
    public void update(Player player) {}
}
