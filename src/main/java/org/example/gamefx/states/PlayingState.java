package org.example.gamefx.states;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import org.example.gamefx.*;
import org.example.gamefx.display.Display;
import org.example.gamefx.objects.Door;
import org.example.gamefx.utils.Logger;

import java.io.IOException;

/**
 * Manages active gameplay session and state transitions
 */
public class PlayingState implements GameState {
    private StateManager stateManager;
    private Game game;

    /**
     * Starts new game session
     *
     * @param stateManager Reference to state controller
     * @param display Main display reference
     * @throws IOException If level loading fails
     */
    public PlayingState(StateManager stateManager, Display display) throws IOException {
        this.stateManager = stateManager;
//        this.game = new Game(display, (int)display.getWidth(), (int)display.getHeight());
        this.game = new Game(display, (int)display.getWidth(), (int)display.getHeight(), LevelManager.getCurLevel());
    }

    /**
    * Resumes existing game session
     *
     * @param stateManager Reference to state controller
     * @param game Loaded game instance
     */
    public PlayingState(StateManager stateManager, Game game) {
        this.stateManager = stateManager;
        this.game = game;
    }

    /**
     * @return Active game instance being played
     */
    public Game getGame() {
        return game;
    }

    @Override
    public void init() {
        game.getDisplay().getRootPane().getChildren().add(game.getDisplay().getCanvas());
    }

    /**
     * Updates game logic and handles:
     * - Pause requests (ESC key)
     * - Level completion checks
     * - Player death conditions
     *
     * @throws IOException If level transition fails
     */
    @Override
    public void update() throws IOException {
        game.update();
        if (Input.getInstance().isKeyPressed(KeyCode.ESCAPE)) {
            stateManager.setCurrentGameState(new PauseState(stateManager, this));
        }

        Level curLevel = LevelManager.getCurLevel();
        if (curLevel instanceof Level2) {
            Level2 level2 = (Level2) curLevel;
            Door door = level2.getDoor();

            if (door != null && door.isOpened()) {
                Logger.getInstance().info("The door is open. Switching to LevelCompleteState.");
                stateManager.setCurrentGameState(new LevelCompleteState(stateManager, this));
                return;
            }
        }

        if (game.getWorld().getPlayer().isDead()) {
            Logger.getInstance().info("The player died. Transition to GameOverState.");
            stateManager.setCurrentGameState(new GameOverState(stateManager, this));
            return;
        }
    }

    /**
     * Delegates rendering to game engine
     *
     * @param gc Graphics context for drawing
     */
    @Override
    public void render(GraphicsContext gc) {
        game.render();
    }

    @Override
    public void handleInput() {}

    @Override
    public Display getDisplay() {
        return game.getDisplay();
    }

}