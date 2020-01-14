package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.model.AlarmsService;
import at.fhhagenberg.sqelevator.model.ElevatorController;
import at.fhhagenberg.sqelevator.model.autocontroller.SimpleControlAlgorithm;
import at.fhhagenberg.sqelevator.services.MockElevatorServiceFactory;
import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sqelevator.IElevator;

import java.util.ResourceBundle;

public class ApplicationMain extends Application {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    @Override
    public void start(Stage stage) throws Exception {
        var elevatorFactory = new MockElevatorServiceFactory();
        IElevator elevatorService = null;

        try {
            elevatorService = elevatorFactory.getElevatorService();
        } catch (Exception e) {
            e.printStackTrace();

            AlarmsService.getInstance().addAlert(e.getMessage());
        }

        var elevatorController = new ElevatorController(elevatorService);

        var buildingViewModel = new BuildingViewModel(elevatorController);

        var eccPane = new ElevatorControlCenterPane(buildingViewModel);

        var scene = new Scene(eccPane, 1000, 600);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(RESOURCE_BUNDLE.getString("title"));
        stage.getIcons().add(new Image("icons/ic_ecc.png"));
        stage.setOnCloseRequest(windowEvent -> elevatorController.stopUpdates());

        //TODO: implement control algorithm
        var controlAlgorithm = new SimpleControlAlgorithm();
        controlAlgorithm.setElevatorController(elevatorController);
        controlAlgorithm.start();

        elevatorController.initialize();
        elevatorController.setUpdateInterval(250);
        elevatorController.startPeriodicUpdates();

        stage.show();
    }
}
