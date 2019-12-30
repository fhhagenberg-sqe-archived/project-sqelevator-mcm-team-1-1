package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class ApplicationMain extends Application {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    @Override
    public void start(Stage stage) throws Exception {
        var buildingViewModel = new BuildingViewModel();

        var eccPane = new ElevatorControlCenterPane(buildingViewModel);

        var scene = new Scene(eccPane, 1000, 500);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(RESOURCE_BUNDLE.getString("title"));
        stage.getIcons().add(new Image("icons/ic_ecc.png"));
        stage.show();
    }
}
