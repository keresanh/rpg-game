package org.example.gamefx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.gamefx.display.Display;
import org.example.gamefx.states.MenuState;
import org.example.gamefx.states.StateManager;
import org.example.gamefx.utils.Logger;
import java.io.IOException;

/**
 * Main application entry point and JavaFX initialization
 */
public class Main extends Application {
    /**
     * Initializes game window and core systems
     *
     * @param stage Primary JavaFX window container
     * @throws IOException If initial level loading fails
     */
    @Override
    public void start(Stage stage) throws IOException {
        int w = 800;
        int h = 600;

        boolean enableLogging = getParameters().getRaw().contains("--enable-logging");
        Logger.getInstance().setEnabled(enableLogging);

        LevelManager.initializeLevels();

        Display display = new Display(stage, w, h);

        StateManager stateManager = new StateManager(display.getGraphicsContext());
        MenuState menuState = new MenuState(stateManager, display);
        stateManager.setCurrentGameState(menuState);

        GameLoop gameLoop = new GameLoop(stateManager);
        gameLoop.start();
    }

    /**
     * Launches application with command-line arguments
     *
     * @param args Command-line parameters (use --enable-logging for debug output)
     */
    public static void main(String[] args) {
//        launch();
        launch(args);
    }
}
