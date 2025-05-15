package org.example.gamefx;

import javafx.scene.input.KeyCode;
/**
 * Centralized keyboard input handler with key state tracking
 */
public class Input {
    private static Input instance;
    private boolean[] keyStates;
    private boolean[] consumedKeyStates;

    private Input() {
        int keyCount = KeyCode.values().length;
        keyStates = new boolean[keyCount];
        consumedKeyStates = new boolean[keyCount];
    }

    /**
            * @return Singleton input instance
     */
    public static Input getInstance() {
        if (instance == null) {
            instance = new Input();
        }
        return instance;
    }

    /**
     * Marks key as pressed
     *
     * @param code KeyCode to update
     */
    public void handleKeyPress(KeyCode code) {
        keyStates[code.ordinal()] = true;
    }

    /**
     * Marks key as released
     *
     * @param code KeyCode to update
     */
    public void handleKeyRelease(KeyCode code) {
        keyStates[code.ordinal()] = false;
        consumedKeyStates[code.ordinal()] = false;
    }

    /**
     * Checks if key is currently pressed
     *
     * @param code KeyCode to check
     * @return True if key is down
     */
    public boolean isKeyPressed(KeyCode code) {
        return keyStates[code.ordinal()];
    }

    /**
     * Marks key press as handled to prevent reuse
     *
     * @param code KeyCode to consume
     */
    public void consumeKeyPress(KeyCode code) {
        consumedKeyStates[code.ordinal()] = true;
    }

    public boolean isKeyConsumed(KeyCode code) {
        return consumedKeyStates[code.ordinal()];
    }
}