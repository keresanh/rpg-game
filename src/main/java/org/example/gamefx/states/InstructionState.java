package org.example.gamefx.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.gamefx.display.Display;
import org.example.gamefx.utils.Utils;
/**
 * Displays game instructions and controls
 */
public class InstructionState implements GameState {
    private StateManager stateManager;
    private Display display;
    private final Image backgroundImage;
    private PlayingState playingState;

    /**
     * Creates instruction screen state
     *
     * @param stateManager Reference to state controller
     * @param display Main display reference
     */
    public InstructionState(StateManager stateManager, Display display) {
        this.stateManager = stateManager;
        this.display = display;
        this.playingState = playingState;
        this.backgroundImage = Utils.loadImg("/menu/instruction_cat.png");
    }

    /**
     * Initializes instruction screen with return button
     */
    @Override
    public void init() {
        display.getRootPane().getChildren().clear();
        display.getRootPane().getChildren().add(display.getCanvas());

        VBox box = new VBox();
        box.setLayoutX(display.getWidth() - 160); // 150 (šířka tlačítka) + 10 (odsazení)
        box.setLayoutY(20); // 20 px od horního okraje

        box.setAlignment(Pos.TOP_RIGHT);

        Button return_menu = createImageButton("/menu/return.png", 100, 100);
        return_menu.setOnAction(e -> stateManager.setCurrentGameState(new MenuState(stateManager, display)));

        box.getChildren().add(return_menu);
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
     * Renders instruction background image
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
