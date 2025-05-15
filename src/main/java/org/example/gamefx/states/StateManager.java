package org.example.gamefx.states;

import javafx.scene.canvas.GraphicsContext;
import java.io.IOException;

/**
 * Controls game state transitions and execution flow
 */
public class StateManager {
    private GameState currentGameState;
    private GraphicsContext gc;

    /**
     * Creates state manager with rendering context
     *
     * @param gc Graphics context for state rendering
     */
    public StateManager(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Switches active game state and initializes it
     *
     * @param newState State to transition to
     */
    public void setCurrentGameState(GameState newState) {
        if (currentGameState != null) {
            currentGameState.getDisplay().getRootPane().getChildren().clear();
        }
        this.currentGameState = newState;
        currentGameState.init();
    }

    /**
     * Updates current state's logic
     *
     * @throws IOException Propagated from active state
     */
    public void update() throws IOException {
        if (currentGameState != null) currentGameState.update();
    }

    /**
     * Renders current state's visual elements
     */
    public void render() {
        if (currentGameState != null) currentGameState.render(gc);
    }

    public void handleInput() {
        if (currentGameState != null) currentGameState.handleInput();
    }
}