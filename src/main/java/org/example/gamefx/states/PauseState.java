package org.example.gamefx.states;

import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.gamefx.display.Display;
import org.example.gamefx.utils.Utils;

/**
 * Handles pause menu UI and game state suspension
 */
public class PauseState implements GameState {
    private StateManager stateManager;
    private Display display;
    private PlayingState playingState;
    private final Image backgroundImage;

    /**
     * Creates pause state interrupting current gameplay
     *
     * @param stateManager Reference to state controller
     * @param playingState Current gameplay state to pause
     */
    public PauseState(StateManager stateManager, PlayingState playingState) {
        this.stateManager= stateManager;
        this.playingState = playingState;
        this.display = playingState.getDisplay();
        this.backgroundImage = Utils.loadImg("/menu/pause_background_k.png");
    }

    /**
     * Initializes pause menu with options to:
     * - Continue game
     * - Save progress
     * - Return to main menu
     */
    @Override
    public void init() {
        display.getRootPane().getChildren().add(display.getCanvas());
        VBox box = new VBox(20);
        box.setLayoutX(display.getWidth()/2 - 75);
        box.setLayoutY(display.getHeight()/2 - 75);
        box.setStyle("-fx-alignment: center;");

        Button cont = createImageButton("/menu/continue.png", 160, 80);
        cont.setOnAction(e -> {
            stateManager.setCurrentGameState(playingState);
        });

        Button save = createImageButton("/menu/save_game.png", 160, 80);
        save.setOnAction(e -> playingState.getGame().saveGame());

        Button backToMenu = createImageButton("/menu/back_to_menu.png", 160, 80);

        backToMenu.setOnAction(e -> stateManager.setCurrentGameState(new MenuState(stateManager, display)));

        box.getChildren().addAll(cont, save, backToMenu);
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
     * Renders semi-transparent pause background overlay
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