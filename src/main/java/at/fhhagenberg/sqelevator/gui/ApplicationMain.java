package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.mocks.MockElevator;
import at.fhhagenberg.sqelevator.model.ElevatorDataProvider;
import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationMain extends Application {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    Timer timer = new Timer();
    @Override
    public void start(Stage stage) throws Exception {
        var elevatorService = new MockElevator(3,4,5,10);

        var dataProvider = new ElevatorDataProvider(elevatorService);

        var updateTask = new TimerTask(){
            @Override
            public void run() {
                dataProvider.update();
            }
        };

        timer.scheduleAtFixedRate(updateTask, 0, 1000);

        var buildingViewModel = new BuildingViewModel(dataProvider);

        dataProvider.addElevatorChangeObserver(buildingViewModel);
        dataProvider.addFloorChangeObserver(buildingViewModel);

        var eccPane = new ElevatorControlCenterPane(buildingViewModel);

        var scene = new Scene(eccPane, 1000, 500);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(RESOURCE_BUNDLE.getString("title"));
        stage.getIcons().add(new Image("icons/ic_ecc.png"));

        stage.setOnCloseRequest(windowEvent -> {
            timer.cancel();
        });

        stage.show();
    }
}
