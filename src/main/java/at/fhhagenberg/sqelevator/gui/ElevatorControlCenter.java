package at.fhhagenberg.sqelevator.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ElevatorControlCenter extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        var root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(10,10,10,10));

        root.getChildren().add(new Label("Test"));

        var scene = new Scene(root, 500, 200);
        stage.setScene(scene);
        stage.setTitle("Hello World");
        stage.show();
    }
}
