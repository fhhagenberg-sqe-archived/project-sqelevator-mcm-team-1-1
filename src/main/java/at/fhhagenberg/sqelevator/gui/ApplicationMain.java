package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.mocks.MockElevator;
import at.fhhagenberg.sqelevator.model.AlarmsService;
import at.fhhagenberg.sqelevator.model.ElevatorController;
import at.fhhagenberg.sqelevator.model.autocontroller.SimpleControlAlgorithm;
import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sqelevator.IElevator;

import java.rmi.Naming;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationMain extends Application {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    Timer timer = new Timer();
    @Override
    public void start(Stage stage) throws Exception {
        //uncomment to test local RMI connection ...
        //at.fhhagenberg.sqelevator.ElevatorExample.run_example();

        IElevator elevatorService;

        var useMockElevator = true;
        if(useMockElevator){
            elevatorService = new MockElevator(5,7,5,10);
        }
        else{
            elevatorService = (IElevator) Naming.lookup("rmi://localhost/ElevatorSim");
        }

        var elevatorController = new ElevatorController(elevatorService);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AlarmsService.getInstance().addAlert("test");
            }
        }, 1000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AlarmsService.getInstance().addAlert("2. test", false);
            }
        }, 2000);

        var buildingViewModel = new BuildingViewModel(elevatorController);

        //TODO: implement control algorithm
        var controlAlgorithm = new SimpleControlAlgorithm();
        controlAlgorithm.setElevatorController(elevatorController);
        controlAlgorithm.start();

        var eccPane = new ElevatorControlCenterPane(buildingViewModel);

        var scene = new Scene(eccPane, 1000, 600);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(RESOURCE_BUNDLE.getString("title"));
        stage.getIcons().add(new Image("icons/ic_ecc.png"));

        elevatorController.initialize();

        var updateTask = new TimerTask(){
            @Override
            public void run() {
                elevatorController.update();
            }
        };

        timer.scheduleAtFixedRate(updateTask, 0, 1000);

        stage.setOnCloseRequest(windowEvent -> {
            timer.cancel();
        });
        stage.setResizable(false);
        stage.show();
    }
}
