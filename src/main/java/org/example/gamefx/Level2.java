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
 * Secondary level with increased difficulty and final progression
 */
public class Level2 implements Level {

    private Level nextLevel = null;
    private Door door;

    /**
     * Creates final level with stronger enemies and key door
     */
    public Level2() {}

    @Override
    public void setNextLevel(Level nextLevel) {}

    @Override
    public String getMapFileName() {
        return "world_create.txt";
    }

    /**
     * Loads level content:
     * - Spawns enhanced DuckEnemy (60-70 HP) and SheepEnemy (120 HP)
     * - Places 2 chests with healing potions
     * - Adds final exit door at position (780, 600)
     *
     * @param world World instance to populate with entities
     */
    @Override
    public void load(World world) {
        // Add enemies - different positions and strengths compared to level 1
        world.addEnemy(new DuckEnemy(400, 150, 60, "duck", world.getCollision(), world));
        world.addEnemy(new DuckEnemy(200, 100, 70, "duck", world.getCollision(), world));
        world.addEnemy(new SheepEnemy(200, 200, 120, "sheep", world.getCollision(), world));

        // Add objects
//        world.addObject(new Tree(200, 300, "tree_1.png"));
//        world.addObject(new Tree(400, 150, "tree_0.png"));
//        world.addObject(new Tree(600, 400, "tree_1.png"));
//        world.addObject(new Chest(450, 350, "/objects/chest.png"));

        world.addObject(new Chest(750, 200, "/objects/chest.png",
                Arrays.asList(new HealingPotion(2))));

        world.addObject(new Chest(850, 300, "/objects/chest.png",
                Arrays.asList(new HealingPotion(2), new Key(1))));

//        world.addObject(new Door(500, 100));

        door = new Door(780, 600);
        world.addObject(door);
    }

    /**
     * @return null indicating this is the final level
     */
    @Override
    public Level getNextLevel() {
        return null;
    }

    /**
     * @return Main level exit door for completion checks
     */
    public Door getDoor() {
        return door;
    }
}