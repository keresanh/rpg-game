package org.example.gamefx.states;

import javafx.scene.canvas.GraphicsContext;
import org.example.gamefx.display.Display;

import java.io.IOException;
/**
 * Defines common interface for all game states
 */
public interface GameState {
    /**
     * Initializes state UI components
     */
    void init();
    /**
     * Updates state logic
     * @throws IOException For state transitions requiring I/O
     */
    void update() throws IOException;
    /**
     * Renders state visual elements
     * @param gc Graphics context for drawing
     */
    void render(GraphicsContext gc);
    /**
     * Handles user input specific to state
     */
    void handleInput();
    /**
     * @return Display reference for UI management
     */
    Display getDisplay();
}