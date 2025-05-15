package org.example.gamefx;

import javafx.animation.AnimationTimer;
import org.example.gamefx.states.StateManager;
import java.io.IOException;

/**
 * Manages game timing and execution loop with fixed update rate
 */
public class GameLoop {
    private final double UPDATE_RATE = 60.0;
    private final double TIME_PER_UPDATE = 1_000_000_000.0 / UPDATE_RATE;
    private long lastUpdateTime;
    private int fps = 0, ups = 0;
    private long lastFpsCheck = System.currentTimeMillis();
    private double accumulator = 0;
    private StateManager stateManager;

    /**
     * Creates game loop tied to state manager
     *
     * @param stateManager State controller to drive updates
     */
    public GameLoop(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    /**
     * Starts game loop with 60 UPS (updates per second) and variable FPS
     * Fixed timestep implementation details:
     * - Updates run at consistent 60 UPS
     * - Rendering happens as fast as possible
     * - Accumulator prevents spiral of death
     */
    public void start() {
        lastUpdateTime = System.nanoTime();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update logika
                double passedTime = now - lastUpdateTime;
                lastUpdateTime = now;
                accumulator += passedTime;

                while (accumulator >= TIME_PER_UPDATE) {
                    try {
                        stateManager.update();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ups++;
                    accumulator -= TIME_PER_UPDATE;
                }
                stateManager.render();
                fps++;

                if (System.currentTimeMillis() - lastFpsCheck >= 1000) {
                    System.out.println("FPS: " + fps + " | UPS: " + ups);
                    fps = 0;
                    ups = 0;
                    lastFpsCheck = System.currentTimeMillis();
                }
            }
        }.start();
    }
}

