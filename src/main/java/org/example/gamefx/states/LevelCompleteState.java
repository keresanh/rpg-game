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
 * Shows level completion screen and progression options
 */
public class LevelCompleteState implements GameState {
    private StateManager stateManager;
    private Display display;
    private final Image backgroundImage;
    private PlayingState playingState;

    /**
     * Creates level completion state
     *
     * @param stateManager Reference to state controller
     * @param playingState Completed level state
     */
    public LevelCompleteState(StateManager stateManager, PlayingState playingState) {
        this.stateManager = stateManager;
        this.display = playingState.getDisplay();
        this.playingState = playingState;
        this.backgroundImage = Utils.loadImg("/menu/level_complete_background_k.png");
    }

    /**
     * Initializes completion screen with menu return button
     */
    @Override
    public void init() {
        display.getRootPane().getChildren().clear();
        display.getRootPane().getChildren().add(display.getCanvas());

        VBox box = new VBox(20);
        box.setLayoutX(display.getWidth() / 2 - 100);
        box.setLayoutY(display.getHeight() / 2 - 100);
        box.setStyle("-fx-alignment: center;");

        Button backToMenu = createImageButton("/menu/back_to_menu.png", 160, 80);
        backToMenu.setOnAction(e -> stateManager.setCurrentGameState(new MenuState(stateManager, display)));

        box.getChildren().add(backToMenu);
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
     * Renders level complete background image
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
