module org.example.gamefx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;


    opens org.example.gamefx to javafx.fxml;
    exports org.example.gamefx;
    exports org.example.gamefx.blocks;
    opens org.example.gamefx.blocks to javafx.fxml;
    exports org.example.gamefx.entities;
    opens org.example.gamefx.entities to javafx.fxml;
    exports org.example.gamefx.display;
    opens org.example.gamefx.display to javafx.fxml;
}