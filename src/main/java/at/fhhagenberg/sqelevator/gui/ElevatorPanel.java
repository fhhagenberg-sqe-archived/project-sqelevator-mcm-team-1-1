package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.ResourceBundle;

public class ElevatorPanel extends HBox {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    private static final int SPACING = 10;
    private static final Insets PADDING_NARROW = new Insets(5, 5, 5, 5);
    private static final Insets PADDING_LARGE = new Insets(10, 10, 10, 10);

    private BuildingViewModel buildingViewModel;

    private List<Slider> liftSliders;

    public ElevatorPanel() {

        GridPane gridPane = new GridPane();
        gridPane.setId("elevator-grid");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.LEFT);
        col1.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(col1);

        int floorNum = 7; // TODO get number of floors
        int elevatorNum = 5; // TODO get number of elevators

        for(int i = 0; i < floorNum; i++) {
            RowConstraints row = new RowConstraints();
            row.setValignment(VPos.CENTER);
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
        }

        for(int i = 0; i < elevatorNum; i++) {

            ColumnConstraints sliderCol = new ColumnConstraints();
            sliderCol.setHalignment(HPos.CENTER);
            sliderCol.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(sliderCol);

            Label elevatorNumLabel = new Label((i + 1) + "");
            gridPane.add(elevatorNumLabel, i + 1, 0);

            Slider slider = new Slider(0, floorNum - 1, 0);
            slider.setShowTickMarks(true);
            slider.setMax(floorNum - 1);
            slider.setMin(0);

            gridPane.add(slider, i+1, 1, 1, floorNum);

            Label payload = new Label("0");
            gridPane.add(payload, i + 1, floorNum + 1);

            Label speed = new Label("0");
            gridPane.add(speed, i + 1, floorNum + 2);

            Label targets = new Label("0");
            gridPane.add(targets, i + 1, floorNum + 3);

        }

        ColumnConstraints lightCol = new ColumnConstraints();
        lightCol.setHalignment(HPos.LEFT);
        gridPane.getColumnConstraints().add(lightCol);

        ColumnConstraints floorNumCol = new ColumnConstraints();
        floorNumCol.setHalignment(HPos.LEFT);
        gridPane.getColumnConstraints().add(floorNumCol);

        for(int j = 0; j < floorNum; j++) {

            Circle elevatorLight = new Circle();
            elevatorLight.setRadius(6);
            elevatorLight.setFill(Color.LIGHTBLUE);


            Label floorNumLabel = new Label((floorNum - j) + "");
            gridPane.add(floorNumLabel, elevatorNum + 3, j + 1);

        }

        Label payloadLabel = new Label("Payload");
        gridPane.add(payloadLabel, 0, floorNum + 1);


        Label speedLabel = new Label("Speed");
        gridPane.add(speedLabel, 0, floorNum + 2);


        Label targetsLabel = new Label("Targets");
        gridPane.add(targetsLabel, 0, floorNum + 3);


        gridPane.prefWidthProperty().bind(this.widthProperty());
        gridPane.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(gridPane);
    }
}
