package org.example.gamefx;

import org.example.gamefx.entities.DuckEnemy;
import org.example.gamefx.entities.SheepEnemy;
import org.example.gamefx.items.HealingPotion;
import org.example.gamefx.items.Key;
import org.example.gamefx.objects.Chest;
import org.example.gamefx.objects.Door;
import org.example.gamefx.objects.Tree;

import java.util.Arrays;
/**
 * Initial game level with basic enemy placement and treasure chests
 */
public class Level1 implements Level {
    private Level nextLevel;

    /**
     * Creates first level with default progression to Level2
     */
    public Level1() {}

    @Override
    public void setNextLevel(Level nextLevel) {
        this.nextLevel = nextLevel;
    }

    /**
     * @return Configured map file name for this level
     */
    @Override
    public String getMapFileName() {
        return "tempWorld5May.txt";
    }


    /**
     * Loads level content:
     * - Spawns 2 DuckEnemy and 1 SheepEnemy
     * - Places 2 chests with healing potions and keys
     * - Adds exit door to next level
     *
     * @param world World instance to populate with entities
     */
    @Override
    public void load(World world) {
        // Add enemies
        world.addEnemy(new DuckEnemy(544, 268, 50, "duck", world.getCollision(), world));
        world.addEnemy(new DuckEnemy( 100, 700, 50, "duck", world.getCollision(), world));
        world.addEnemy(new SheepEnemy(950, 200, 100, "sheep", world.getCollision(), world));

        // Add objects
//        world.addObject(new Tree(70, 200, "tree_1.png"));
//        world.addObject(new Tree(150, 350, "tree_0.png"));
//        world.addObject(new Chest(750, 200, "/objects/chest.png"));
//        world.addObject(new Chest(850, 300, "/objects/chest.png"));

        world.addObject(new Chest(500, 100, "/objects/chest.png",
                Arrays.asList(new HealingPotion(2))));
//
        world.addObject(new Chest(750, 100, "/objects/chest.png",
                Arrays.asList(new HealingPotion(2), new Key(1))));

        world.addObject(new Door(1420, 1400));
    }

    /**
     * @return Next level in progression sequence (Level2)
     */
    @Override
    public Level getNextLevel() {
        return nextLevel;
    }
}