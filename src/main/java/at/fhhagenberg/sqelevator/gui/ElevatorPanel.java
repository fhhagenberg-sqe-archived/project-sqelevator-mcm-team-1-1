package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.utils.StringAdapter;
import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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

        this.getChildren().add(gridPane);

        int floorNum = 7; // TODO get number of floors
        int elevatorNum = 5; // TODO get number of elevators

        for(int i = 0; i < elevatorNum; i++) {
            gridPane.add(new Label((i+1) + ""), i + 1, 0);

            Slider slider = new Slider(0, floorNum - 1, 0);
            slider.setShowTickMarks(true);
            slider.setOrientation(Orientation.VERTICAL);
            slider.setMax(floorNum - 1);
            slider.setMin(0);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);
            slider.setSnapToTicks(true);

            // liftSliders.add(slider);
            gridPane.add(slider, i+1, 1, 1, floorNum);

            for(int j = 0; j < floorNum; j++) {
                gridPane.add(new Label("o"), elevatorNum + 2, j + 1);
                gridPane.add(new Label((floorNum - j) + ""), elevatorNum + 3, j + 1);
            }

            gridPane.add(new Label("0"), i + 1, floorNum + 1);
            gridPane.add(new Label("0"), i + 1, floorNum + 2);
            gridPane.add(new Label("0"), i + 1, floorNum + 3);
        }

        gridPane.add(new Label("Payload"), 0, floorNum + 1);
        gridPane.add(new Label("Speed"), 0, floorNum + 2);
        gridPane.add(new Label("Targets"), 0, floorNum + 3);
    }
}
