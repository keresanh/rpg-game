package org.example.gamefx.states;

import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.gamefx.Game;
import org.example.gamefx.LevelManager;
import org.example.gamefx.display.Display;
import org.example.gamefx.entities.Player;
import org.example.gamefx.utils.Utils;

import java.io.IOException;

/**
 * Handles game over screen UI and restart functionality
 */
public class GameOverState implements GameState {
    private final StateManager stateManager;
    private final Display display;
    private Game game;
    private PlayingState playingState;

    private final Image backgroundImage;

    /**
     * Creates game over state with restart capabilities
     *
     * @param stateManager Reference to state controller
     * @param playingState Previous gameplay state for context
     */
    public GameOverState(StateManager stateManager, PlayingState playingState) {
        this.stateManager = stateManager;
        this.playingState = playingState;
        this.display = playingState.getDisplay();

        this.backgroundImage = Utils.loadImg("/menu/game_over_background_k.png");
    }

    /**
     * Initializes game over screen with restart and menu buttons
     */
    @Override
    public void init() {
        // Clear the previous content and add back the canvas
        display.getRootPane().getChildren().clear();
        display.getRootPane().getChildren().add(display.getCanvas());

        VBox box = new VBox(15);
        box.setLayoutX(display.getWidth() / 2 - 75);
        box.setLayoutY(display.getHeight() / 2 - 50);
        box.setStyle("-fx-alignment: center;");

        Button restartButton = createImageButton("/menu/restart_b.png", 170, 90);
        restartButton.setOnAction(e -> {
            try {
                PlayingState newGame = new PlayingState(stateManager, display);
                stateManager.setCurrentGameState(newGame);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button backToMenu = createImageButton("/menu/back_to_menu.png", 170, 90);
        backToMenu.setOnAction(e -> stateManager.setCurrentGameState
                (new MenuState(stateManager, display)));

        box.getChildren().addAll(restartButton, backToMenu);
        display.getRootPane().getChildren().add(box);
    }

    private Button createImageButton(String imagePath, int width, int height) {
        Image img = Utils.loadImg(imagePath);
        ImageView view = new ImageView(img);
        view.setFitWidth(width);
        view.setFitHeight(height);

        Button btn = new Button();
        btn.setGraphic(view);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        btn.setPadding(Insets.EMPTY);

        return btn;
    }

    @Override
    public void update() {}

    /**
     * Renders game over background image
     *
     * @param gc Graphics context for drawing
     */
    @Override
    public void render(GraphicsContext gc) {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, display.getWidth(), display.getHeight());
        }
    }

    @Override
    public void handleInput() {}

    @Override
    public Display getDisplay() {
        return display;
    }
}

