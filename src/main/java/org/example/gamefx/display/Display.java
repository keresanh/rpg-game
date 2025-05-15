package org.example.gamefx.display;


import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import org.example.gamefx.Input;

/**
 * Manages game window and JavaFX UI components
 */
public class Display {
    private Stage stage;
    private Canvas canvas;
    private StackPane root;

    /**
    * Initializes the game display by creating the window, canvas, and setting up input handling.
    *
    * @param stage The JavaFX stage to use.
    * @param width The width of the canvas.
    * @param height The height of the canvas.
    */
    public Display(Stage stage, int width, int height) {
        this.stage = stage;

        canvas = new Canvas(width, height);
        root = new StackPane(canvas);
        Scene scene = new Scene(root, width, height);
        Input input = Input.getInstance();
        scene.setOnKeyPressed(e -> input.handleKeyPress(e.getCode()));
        scene.setOnKeyReleased(e -> input.handleKeyRelease(e.getCode()));

        stage.setTitle("My game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * @return Graphics context for drawing on game canvas
     */
    public GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Stage getStage() {
        return stage;
    }

    public StackPane getRootPane() {
        return root;
    }

    public double getWidth() {
        return canvas.getWidth();
    }

    public double getHeight() {
        return canvas.getHeight();
    }
}