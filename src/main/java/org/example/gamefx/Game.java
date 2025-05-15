package org.example.gamefx;

import com.google.gson.Gson;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.gamefx.display.Camera;
import org.example.gamefx.display.Display;
import org.example.gamefx.entities.DuckEnemy;
import org.example.gamefx.entities.Enemy;
import org.example.gamefx.entities.Player;
import org.example.gamefx.entities.SheepEnemy;
import org.example.gamefx.utils.Logger;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Main game controller managing world state, player, and game persistence
 */
public class Game {
    private static final String SAVE_FILE = "save.json";
    private final int WIDTH_TILES = 24;
    private final int HEIGHT_TILES = 24;
    private Display display;
    private Player player;
    private World world;
    private Input input;
    private GraphicsContext gc;

    /**
     * Initializes new game session
     *
     * @param display Display reference for rendering
     * @param width Window width in pixels
     * @param height Window height in pixels
     * @param level Initial game level to load
     * @throws IOException If level initialization fails
     */
    public Game(Display display, int width, int height, Level level) throws IOException {
        this.display = display;
        this.gc = display.getGraphicsContext();
        this.input = Input.getInstance();

        this.world = new World(width, height, WIDTH_TILES, HEIGHT_TILES, level);
        this.player = new Player(80, 80, 100, "/character_down_1.png", world.getCollision(), world, input);
        world.setPlayer(player);
        world.getCamera().setPlayer(player);
    }

    /**
     * Updates game state including player and world
     *
     * @throws IOException If level transition fails
     */
    public void update() throws IOException {
        player.update(
                System.currentTimeMillis(),
                display.getGraphicsContext().getCanvas().getWidth(),
                display.getGraphicsContext().getCanvas().getHeight()
        );
        world.update();
    }

    /**
     * Renders all game elements with camera offset
     */
    public void render() {
        gc.clearRect(0, 0, display.getWidth(), display.getHeight());
        int cameraX = (int) world.getCamera().getX();
        int cameraY = (int) world.getCamera().getY();

        world.render(gc, cameraX, cameraY);

        gc.setStroke(Color.RED);
        gc.strokeRect(player.getX() - cameraX - 24 , player.getY() - cameraY - 24, 48, 48);
        explainHitbox(world.getCamera(), "hitbox", player.getX() - cameraX - 24, player.getY() - cameraY - 24 + 64, Color.RED);

        gc.drawImage(
                player.getImage(),
                player.getX() - cameraX - player.getImageWidth() / 2,
                player.getY() - cameraY - player.getImageHeight() / 2,
                player.getImageWidth(),
                player.getImageHeight()
        );

        player.getInventory().render(gc);
        player.renderHearts(gc);
    }

    /**
     * Debugging utility to draw hitbox labels
     *
     * @param camera Active camera for coordinate translation
     * @param txt Text to display
     * @param x World X position (pre-camera offset)
     * @param y World Y position (pre-camera offset)
     * @param color Text color
     */
    private void explainHitbox(Camera camera, String txt, double x, double y, Color color) {
        gc.setFill(color);
        gc.fillText(txt, x, y);
    }

    /**
     * Serializes game state to JSON file. Saves:
     * - Current level class name
     * - Player position and health
     * - All enemies' types, positions and health
     */
    public void saveGame() {
        Logger.getInstance().info("Saving the game");

        SaveData data = new SaveData();
        data.currentLevel = LevelManager.getCurLevel().getClass().getName();
        data.playerX = (int) player.getX();
        data.playerY = (int) player.getY();
        data.playerHp = player.getHp();

        for (Enemy enemy : world.getEnemies()) {
            SaveData.EnemyState enemyState = new SaveData.EnemyState();
            enemyState.type = enemy.getEnemyName().toLowerCase();
            enemyState.x = (int) enemy.getX();
            enemyState.y = (int) enemy.getY();
            enemyState.hp = enemy.getCurHp();
            data.enemies.add(enemyState);
        }

        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            new Gson().toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads game state from saved file
     *
     * @param display Display reference for restored game
     * @return Loaded game instance
     * @throws IOException If save file is missing or corrupted
     */
    public static Game loadGame(Display display) throws IOException {
        try (FileReader reader = new FileReader(SAVE_FILE)) {
            SaveData data = new Gson().fromJson(reader, SaveData.class);

            // Loading a level
            Level level = (Level) Class.forName(data.getCurrentLevel()).newInstance();
            LevelManager.setCurLevel(level);

            // Creating a new game with a loaded level
            Game game = new Game(display, (int)display.getWidth(), (int)display.getHeight(), level);

            // Loading a player
            game.getPlayer().setPosition(data.getPlayerX(), data.getPlayerY());
            game.getPlayer().setHp(data.getPlayerHp());

            // Loading Enemies
            game.getWorld().getEnemies().clear();
            for (SaveData.EnemyState enemyData : data.getEnemies()) {
                Enemy enemy = createEnemyFromData(enemyData, game.getWorld());
                game.getWorld().addEnemy(enemy);
            }
            return game;
        } catch (Exception e) {
            throw new IOException("Chyba při načítání hry", e);
        }
    }

    /**
     * Creates enemy instances from saved data during game loading
     *
     * @param enemyData Serialized enemy state (type, position, HP)
     * @param world Reference to game world for collision handling
     * @return Concrete Enemy subclass instance
     * @throws IllegalArgumentException For unknown enemy types
     */
    private static Enemy createEnemyFromData(SaveData.EnemyState enemyData, World world) {
        switch (enemyData.getType().toLowerCase()) {
            case "duck":
                return new DuckEnemy(enemyData.getX(), enemyData.getY(), enemyData.getHp(), "duck", world.getCollision(), world);
            case "sheep":
                return new SheepEnemy(enemyData.getX(), enemyData.getY(), enemyData.getHp(), "sheep", world.getCollision(), world);
            default:
                throw new IllegalArgumentException("Neznámý typ nepřítele: " + enemyData.getType());
        }
    }

    public Display getDisplay() { return display; }
    public Player getPlayer() { return player; }
    public World getWorld() { return world; }
}