package org.example.gamefx.states;

import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.gamefx.Game;
import org.example.gamefx.LevelManager;
import org.example.gamefx.display.Display;
import org.example.gamefx.utils.Logger;
import org.example.gamefx.utils.Utils;

import java.io.IOException;
/**
 * Main menu with game start, continue, and exit options
 */
public class MenuState implements GameState{
    private StateManager stateManager;
    private Display display;
    private final Image backgroundImage;

    /**
     * Creates main menu state
     *
     * @param stateManager Reference to state controller
     * @param display Main display reference
     */
    public MenuState(StateManager stateManager, Display display) {
        this.stateManager = stateManager;
        this.display = display;
        this.backgroundImage = Utils.loadImg("/menu/menu_background_k.png");
    }

    /**
     * Initializes menu UI with buttons:
     * - Start new game
     * - Continue saved game
     * - Instructions
     * - Exit game
     */
    @Override
    public void init() {
        display.getRootPane().getChildren().clear();
        display.getRootPane().getChildren().add(display.getCanvas());

        // HBox for 3 main buttons
        HBox topButtons = new HBox(20);
        topButtons.setStyle("-fx-alignment: center;");
        topButtons.setPrefWidth(display.getWidth());

        Button start = createImageButton("/menu/play.png", 160, 80);
        start.setOnAction(e -> {
            try {
                // Reset level manager to initial state
                LevelManager.initializeLevels(); //pro to aby zacinal z 1 levelu
                stateManager.setCurrentGameState(new PlayingState(stateManager, display));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        Button continue_game = createImageButton("/menu/continue.png", 160, 80);
        continue_game.setOnAction(e -> {
            try {
                Game loadedGame = Game.loadGame(display);
                PlayingState playingState = new PlayingState(stateManager, loadedGame);
                stateManager.setCurrentGameState(playingState);
            } catch (IOException ex) {
                Logger.getInstance().log(Logger.Level.WARNING, "Saved game not available");
            }
        });

        Button instruction = createImageButton("/menu/instruction.png", 160, 80);
        instruction.setOnAction(e -> stateManager.setCurrentGameState(new InstructionState(stateManager, display)));

        topButtons.getChildren().addAll(start, continue_game, instruction);

        Button exit = createImageButton("/menu/exit.png", 160, 80);
        exit.setOnAction(e -> System.exit(0));



        VBox box = new VBox(50); //  space between lines
        box.setStyle("-fx-alignment: center;");
        box.setLayoutY(display.getHeight() / 2 + 500);
        box.setPrefWidth(display.getWidth());

        box.getChildren().addAll(topButtons, exit);
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
     * Renders menu background image
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