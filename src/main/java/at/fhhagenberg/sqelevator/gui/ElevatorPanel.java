package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ElevatorPanel extends HBox {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    private static final int SPACING = 10;
    private static final Insets PADDING_NARROW = new Insets(5, 5, 5, 5);
    private static final Insets PADDING_LARGE = new Insets(10, 10, 10, 10);

    private BuildingViewModel buildingViewModel;

    private List<Slider> liftSliders;

    public ElevatorPanel(BuildingViewModel buildingViewModel) {
        this.buildingViewModel = buildingViewModel;

        buildingViewModel.elevatorFloorConfigurationProperty().addListener((observableValue, o, t1) -> {
            buildUI();
        });
    }

    private void buildUI() {
        this.getChildren().clear();

        GridPane gridPane = new GridPane();
        gridPane.setId("elevator-grid");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.LEFT);
        col1.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(col1);

        int floorNum = buildingViewModel.getFloors().size();
        int elevatorNum = buildingViewModel.getElevators().size();

        if(floorNum == 0|| elevatorNum == 0){
            this.getChildren().add(new Label("no elevators or no floors" + floorNum + "--" + elevatorNum));
            return;
        }

        for (int i = 0; i < floorNum; i++) {
            RowConstraints row = new RowConstraints();
            row.setValignment(VPos.CENTER);
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
        }

        liftSliders = new ArrayList<Slider>();

        for (int i = 0; i < elevatorNum; i++) {

            ColumnConstraints sliderCol = new ColumnConstraints();
            sliderCol.setHalignment(HPos.CENTER);
            sliderCol.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(sliderCol);

            Label elevatorNumLabel = new Label((i + 1) + "");
            gridPane.add(elevatorNumLabel, i + 1, 0);

            HBox hBox = new HBox();

            Slider slider = new Slider(0, floorNum - 1, 0);
            slider.setShowTickMarks(true);
            slider.setMax(floorNum - 1);
            slider.setMin(0);
            slider.disableProperty().bind(buildingViewModel.getElevators().get(i).automaticModeProperty().not());
            slider.valueProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println("Elevator reached target floor");
                    //TODO mark that target floor is reached  (if necessary?)
                }
            });
            liftSliders.add(slider);

            GridPane buttons = new GridPane();

            for (int j = 0; j < floorNum; j++) {

                Circle elevatorLight = new Circle();

                elevatorLight.setId(i + "," + (floorNum - j));
                System.out.println(elevatorLight.getId());

                elevatorLight.setRadius(6);
                elevatorLight.setFill(Color.YELLOW);
                //gridPane.add(elevatorLight, elevatorNum +2, j+1);
                buttons.add(elevatorLight, 0, j);
                elevatorLight.setOnMouseReleased(new TargetFloorSelectionEventHandler());

                RowConstraints buttonrow = new RowConstraints();
                buttonrow.setValignment(VPos.CENTER);
                buttonrow.setVgrow(Priority.ALWAYS);
                buttons.getRowConstraints().add(buttonrow);

                //vBox.prefHeightProperty().bind(hBox.heightProperty());
                //vBox.setVgrow(elevatorLight, Priority.ALWAYS);
                //buttons.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            }

            hBox.getChildren().add(slider);
            hBox.getChildren().add(buttons);
            gridPane.add(hBox, i + 1, 1, 1, floorNum);

            var manualToggle = new ToggleButton("M");
            manualToggle.selectedProperty().bindBidirectional(buildingViewModel.getElevators().get(i).automaticModeProperty());

            gridPane.add(manualToggle, i + 1, floorNum + 1);

            Label payload = new Label("0");
            gridPane.add(payload, i + 1, floorNum + 2);

            Label speed = new Label("0");
            gridPane.add(speed, i + 1, floorNum + 3);

            Label targets = new Label("0");
            gridPane.add(targets, i + 1, floorNum + 4);
        }

        ColumnConstraints lightCol = new ColumnConstraints();
        lightCol.setHalignment(HPos.LEFT);
        gridPane.getColumnConstraints().add(lightCol);

        ColumnConstraints floorNumCol = new ColumnConstraints();
        floorNumCol.setHalignment(HPos.LEFT);
        gridPane.getColumnConstraints().add(floorNumCol);

        for (int j = 0; j < floorNum; j++) {
            VBox buttons = new VBox();
            buttons.setSpacing(5);

            Polygon triangleUp = new Polygon();
            triangleUp.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    5.0, -9.0,
                    10.0, 0.0});
            triangleUp.fillProperty().bind(Bindings.when(buildingViewModel.getFloors().get(j).upButtonActiveProperty()).then(Color.DODGERBLUE).otherwise(Color.LIGHTBLUE));
            buttons.getChildren().add(triangleUp);

            Polygon triangleDown = new Polygon();
            triangleDown.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    5.0, 9.0,
                    10.0, 0.0});
            triangleDown.fillProperty().bind(Bindings.when(buildingViewModel.getFloors().get(j).downButtonActiveProperty()).then(Color.DODGERBLUE).otherwise(Color.LIGHTBLUE));
            buttons.getChildren().add(triangleDown);

            gridPane.add(buttons, elevatorNum + 2, j + 1);

            Label floorNumLabel = new Label((floorNum - j) + "");
            floorNumLabel.setOnMouseReleased(new TargetFloorSelectionEventHandler());
            gridPane.add(floorNumLabel, elevatorNum + 3, j + 1);

        }

        Label manualModeLabel = new Label("Manual");
        gridPane.add(manualModeLabel, 0, floorNum + 1);


        Label payloadLabel = new Label("Payload");
        gridPane.add(payloadLabel, 0, floorNum + 2);


        Label speedLabel = new Label("Speed");
        gridPane.add(speedLabel, 0, floorNum + 3);


        Label targetsLabel = new Label("Targets");
        gridPane.add(targetsLabel, 0, floorNum + 4);


        gridPane.prefWidthProperty().bind(this.widthProperty());
        gridPane.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(gridPane);
    }

    private class TargetFloorSelectionEventHandler implements EventHandler<Event> {

        @Override
        public void handle(Event event) {
            if (true/*!buildingViewModel.isAutomaticMode()*/) { // Set the target floor

                if (event.getSource() instanceof Label) {
                    int floor = Integer.parseInt(((Label) event.getSource()).getText());
                    buildingViewModel.setCallInfo(String.format("Next target floor for elevator <1> is %s", floor));

                    // TODO reduce speed for the slider to move slower (as animation for elevator)
                    // TODO handle target floor for specific elevator (currently only for the first one)
                    liftSliders.get(0).setValue(floor - 1);
                } else {

                    String text = ((Shape) event.getSource()).getId();
                    System.out.println(text.substring(0, text.indexOf(',')));
                    System.out.println(text.substring(text.indexOf(',') + 1));

                    int elevator = Integer.parseInt(text.substring(0, text.indexOf(',')));
                    int floor = Integer.parseInt(text.substring(text.indexOf(',') + 1));

                    buildingViewModel.setCallInfo(String.format("Next target floor for elevator <%s> is %s", elevator + 1, floor));

                    // TODO reduce speed for the slider to move slower (as animation for elevator)
                    // TODO handle target floor for specific elevator (currently only for the first one)
                    liftSliders.get(elevator).setValue(floor - 1);
                }

            }
        }
    }
}
