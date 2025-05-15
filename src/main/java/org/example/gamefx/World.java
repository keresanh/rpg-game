package org.example.gamefx;

import javafx.scene.canvas.GraphicsContext;
import org.example.gamefx.display.Camera;
import org.example.gamefx.entities.DuckEnemy;
import org.example.gamefx.entities.Enemy;
import org.example.gamefx.entities.Player;
import org.example.gamefx.entities.SheepEnemy;
import org.example.gamefx.objects.Chest;
import org.example.gamefx.objects.Door;
import org.example.gamefx.objects.GameObject;
import org.example.gamefx.objects.Tree;
import org.example.gamefx.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages game world state including map, entities, and level progression
 */
public class World {
    private int width;
    private int height;
    private Map map;
    private Collision collision;
    private List<Enemy> enemies = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();
    private Player player;
    private Camera camera;
    private Level currentLevel;


    /**
     * Creates new game world with specified dimensions and level
     *
     * @param width Viewport width in pixels
     * @param height Viewport height in pixels
     * @param widthTiles Map width in tiles
     * @param heightTiles Map height in tiles
     * @param level Initial level configuration
     * @throws IOException If map file loading fails
     */
    public World(int width, int height, int widthTiles, int heightTiles, Level level) throws IOException {
        this.width = width;
        this.height = height;
        this.currentLevel = level;
        this.map = new Map(widthTiles, heightTiles, level.getMapFileName());
        this.camera = new Camera(width, height);
        this.map = new Map(widthTiles, heightTiles, level.getMapFileName());
        this.collision = new Collision(this);
        level.load(this);
    }

    public Level getCurLvl() {
        return currentLevel;}

    /**
     * Updates all world entities and systems:
     * - Game object states
     * - Enemy behavior and cleanup
     * - Camera positioning
     *
     * @throws IOException If level transition fails
     */
    public void update() throws IOException {
        // Game object updates
        List<GameObject> objectsToRemove = new ArrayList<>();
        List<Level> levelsToChange = new ArrayList<>();

        for (GameObject obj : new ArrayList<>(gameObjects)) { // Kopie seznamu pro bezpečnou iteraci
            obj.update(player);
        }
        // Change level if needed
        if (!levelsToChange.isEmpty()) {
            changeLevel(levelsToChange.get(0));
            return;
        }

        // Elimination of enemies
        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.update(player);
            if (enemy.shouldRemove()) {
                enemiesToRemove.add(enemy);
            }
        }
        enemies.removeAll(enemiesToRemove);

        // Camera updates
        camera.update(map);
    }

    /**
     * Transitions to new level and resets world state
     *
     * @param newLevel Level to load (null-safe)
     * @throws IOException If level resources can't be loaded
     */
    public void changeLevel(Level newLevel) throws IOException {
        if (newLevel == null) {
            Logger.getInstance().info("The new level is null. No level change takes place.");
            return;
        }

        // Clean up the current state before changing the level
        List<GameObject> objectsToClear = new ArrayList<>(gameObjects);
        List<Enemy> enemiesToClear = new ArrayList<>(enemies);

        gameObjects.removeAll(objectsToClear);
        enemies.removeAll(enemiesToClear);

        this.currentLevel = newLevel;
        LevelManager.setCurLevel(newLevel);
        this.map = new Map(map.getWidthTiles(), map.getHeightTiles(), newLevel.getMapFileName());

        newLevel.load(this);
        player.setPosition(100, 100);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addObject(GameObject object) {
        gameObjects.add(object);
    }

    /**
     * Renders all world elements with camera offset
     *
     * @param gc Graphics context for drawing
     * @param cameraX Horizontal camera offset
     * @param cameraY Vertical camera offset
     */
    public void render(GraphicsContext gc, int cameraX, int cameraY) {
        map.render(gc, cameraX, cameraY);
        for (GameObject obj : gameObjects) {
            obj.render(gc, 64, cameraX, cameraY);
        }
        for (Enemy enemy : enemies) {
            enemy.render(gc, 64, cameraX, cameraY);
            enemy.renderHP(gc, cameraX, cameraY);
        }
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * @return Current collision detection system
     */
    public Collision getCollision() {
        return collision;
    }

    /**
     * @return Active game map configuration
     */
    public Map getMap() {
        return map;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }


    public Player getPlayer() {
        return player;
    }

    /**
     * Links player character to world systems
     *
     * @param player Player entity instance
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Camera getCamera() {
        return camera;
    }
}