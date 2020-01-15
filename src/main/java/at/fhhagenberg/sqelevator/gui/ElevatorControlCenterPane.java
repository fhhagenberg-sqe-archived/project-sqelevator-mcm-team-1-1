package at.fhhagenberg.sqelevator.gui;

import java.util.ResourceBundle;

import at.fhhagenberg.sqelevator.viewmodel.AlarmViewModel;
import at.fhhagenberg.sqelevator.viewmodel.BuildingViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ElevatorControlCenterPane extends BorderPane {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");

    private static final int SPACING = 10;
    private static final Insets PADDING_NARROW = new Insets(5, 5, 5, 5);
    private static final Insets PADDING_LARGE = new Insets(10, 10, 10, 10);

    private BuildingViewModel buildingViewModel;

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
        Label title = new Label(RESOURCE_BUNDLE.getString("elevators"));
        title.getStyleClass().add("title");
        vBox.getChildren().add(title);

        vBox.getChildren().add(new ElevatorPanel(buildingViewModel));


        return vBox;
    }

    private Node getAlarmList() {

    	VBox vBox = new VBox();
    	vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
    	
        HBox hBox = new HBox();
        hBox.setPadding(PADDING_LARGE);
        Label title = new Label(RESOURCE_BUNDLE.getString("alarms"));
        title.getStyleClass().add("title");
        hBox.getChildren().add(title);
        vBox.getChildren().add(hBox);       
        
        TableView<AlarmViewModel> tableView = new TableView<>();
        tableView.setId("alarms-table");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);       
        
        var typeCol = new TableColumn<AlarmViewModel, ImageView>("Type");
        typeCol.setMinWidth(35);       
        typeCol.setMaxWidth(35);        
		typeCol.setCellValueFactory(cellData -> {
			var imageView = new ImageView();
			imageView.imageProperty().bindBidirectional(cellData.getValue().imageProperty());
			imageView.setFitWidth(25);
			imageView.setFitHeight(25);

			return new SimpleObjectProperty<>(imageView);
		});

		var messageCol = new TableColumn<AlarmViewModel, String>("Message");
		messageCol.setCellValueFactory(cellData -> cellData.getValue().alarmMessageProperty());

		tableView.itemsProperty().bindBidirectional(buildingViewModel.alarmViewModelsProperty());
		tableView.getColumns().addAll(typeCol, messageCol);
        
        vBox.getChildren().add(tableView);

        return vBox;
    }
    
    private Node getControlPanel() {
        return new HBox();
    }

    private Node getStatusBar() {
		HBox hBox = new HBox();
		hBox.setPadding(PADDING_NARROW);
		hBox.setSpacing(SPACING);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.getStyleClass().add("statusBarBorder");

		Label lblCallInfo = new Label();
		lblCallInfo.textProperty().bind(buildingViewModel.callInfoProperty());
		lblCallInfo.setId("statusbar");

		hBox.getChildren().addAll(lblCallInfo);
		return hBox;
    }
}
