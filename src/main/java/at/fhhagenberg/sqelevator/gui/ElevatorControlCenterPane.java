package at.fhhagenberg.sqelevator.gui;

import at.fhhagenberg.sqelevator.model.Alarm;
import at.fhhagenberg.sqelevator.utils.StringAdapter;
import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

public class ElevatorControlCenterPane extends BorderPane {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    private static final int SPACING = 10;
    private static final Insets PADDING_NARROW = new Insets(5, 5, 5, 5);
    private static final Insets PADDING_LARGE = new Insets(10, 10, 10, 10);

    private BuildingViewModel buildingViewModel;

    private Button btnChangeMode;
    private Label lblMode;

    public ElevatorControlCenterPane(BuildingViewModel buildingViewModel) {
        this.buildingViewModel = buildingViewModel;

        setCenter(getElevatorPanel());
        setRight(getAlarmList());
        setTop(getControlPanel());
        setBottom(getStatusBar());
    }

    private Node getElevatorPanel() {
        VBox vBox = new VBox();
        vBox.setPadding(PADDING_LARGE);
        vBox.getChildren().add(new Label("(Elevator Panel)"));

        vBox.getChildren().add(new ElevatorPanel());

        return vBox;
    }

    private Node getAlarmList() {

    	VBox vBox = new VBox();
    	vBox.setMinWidth(300);
    	vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
    	
        HBox hBox = new HBox();
        hBox.setPadding(PADDING_LARGE);
        Label title = new Label(RESOURCE_BUNDLE.getString("alarms"));
        title.getStyleClass().add("title");
        hBox.getChildren().add(title);
        vBox.getChildren().add(hBox);       
        
        // TODO dynamic alarm list
        ObservableList<Alarm> data = FXCollections.observableArrayList(
                		new Alarm("This is an alarm", false), //
                		new Alarm("This is an error", true), //
                		new Alarm("A second alarm!", false) //
        );
        
        TableView<Alarm> tableView = new TableView<Alarm>();
        tableView.setId("alarms-table");
        
        TableColumn<Alarm, ImageView> typeCol = new TableColumn<Alarm, ImageView>("Type");
        typeCol.setMinWidth(20);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("image"));
 
        TableColumn<Alarm, String> messageCol = new TableColumn<Alarm, String>("Message");
        messageCol.setMinWidth(200);
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
 
        tableView.setItems(data);
        tableView.getColumns().addAll(typeCol, messageCol);        
        
        vBox.getChildren().add(tableView);

        return vBox;
    }
    
    private Node getControlPanel() {
        HBox hBox = new HBox();
        hBox.setPadding(PADDING_LARGE);
        hBox.setSpacing(SPACING);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        btnChangeMode = new Button();
        btnChangeMode.setMaxSize(145, 25);
        btnChangeMode.setMinSize(145, 25);
        btnChangeMode.textProperty().bindBidirectional(buildingViewModel.automaticModeProperty(), new AutomaticModeButtonFormatter());
        btnChangeMode.setOnAction(actionEvent -> {
            buildingViewModel.toggleAutomaticMode();
        });

        hBox.getChildren().add(btnChangeMode);

        return hBox;
    }

    private Node getStatusBar() {
        HBox hBox = new HBox();
        hBox.setPadding(PADDING_NARROW);
        hBox.setSpacing(SPACING);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getStyleClass().add("statusBarBorder");
        lblMode = new Label();
        lblMode.textProperty().bindBidirectional(buildingViewModel.automaticModeProperty(), new AutomaticModeLabelFormatter());
        hBox.getChildren().add(lblMode);
        return hBox;
    }

    private class AutomaticModeLabelFormatter extends StringAdapter<Boolean> {
        @Override
        public String toString(Boolean aBoolean) {
            return String.format(RESOURCE_BUNDLE.getString("mode"), getModeText(aBoolean));
        }
    }

    private class AutomaticModeButtonFormatter extends StringAdapter<Boolean> {
        @Override
        public String toString(Boolean aBoolean) {
            return String.format(RESOURCE_BUNDLE.getString("enableMode"), getModeText(!aBoolean));
        }
    }

    private String getModeText(boolean automaticMode) {
        return automaticMode ? RESOURCE_BUNDLE.getString("automaticMode") : RESOURCE_BUNDLE.getString("manualMode");
    }
}
