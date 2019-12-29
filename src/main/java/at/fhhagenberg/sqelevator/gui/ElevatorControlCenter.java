package at.fhhagenberg.sqelevator.gui;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ElevatorControlCenter extends Application {

	private static final int SPACING = 10;
	private static ResourceBundle elevatorCCBundle = ResourceBundle.getBundle("elevatorCC");
	private static boolean AUTOMATIC_MODE = true;

	private final Insets paddingNarrow = new Insets(5, 5, 5, 5);
	private final Insets paddingLarge = new Insets(10, 10, 10, 10);

	private Button btnChangeMode;
	private Label lblMode;

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(getElevatorPanel());
		borderPane.setRight(getAlarmList());
		borderPane.setTop(getControlPanel());
		borderPane.setBottom(getStatusBar());

		var scene = new Scene(borderPane, 1000, 500);
		scene.getStylesheets().add("styles.css");
		stage.setScene(scene);
		stage.setTitle(elevatorCCBundle.getString("title"));
		stage.getIcons().add(new Image("icons/ic_ecc.png"));
		stage.show();
	}

	private Node getElevatorPanel() {
		HBox hBox = new HBox();
		hBox.setPadding(paddingLarge);
		hBox.getChildren().add(new Label("(Elevator Panel)"));

		// TODO elevator

		return hBox;
	}

	private Node getAlarmList() {

		HBox hBox = new HBox();
		hBox.setPadding(paddingLarge);
		Label title = new Label(elevatorCCBundle.getString("alarms"));
		title.getStyleClass().add("title");
		hBox.getChildren().add(title);

		// TODO dynamic alarm list

		return hBox;
	}

	private Node getControlPanel() {
		HBox hBox = new HBox();
		hBox.setPadding(paddingLarge);
		hBox.setSpacing(SPACING);
		hBox.setAlignment(Pos.CENTER_RIGHT);

		btnChangeMode = new Button();
		btnChangeMode.setMaxSize(145, 25);
		btnChangeMode.setMinSize(145, 25);
		btnChangeMode.setText(String.format(elevatorCCBundle.getString("enableMode"), getUnselectedMode()));
		btnChangeMode.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				AUTOMATIC_MODE = !AUTOMATIC_MODE;
				btnChangeMode.setText(String.format(elevatorCCBundle.getString("enableMode"), getUnselectedMode()));
				lblMode.setText(String.format(elevatorCCBundle.getString("mode"), getSelectedMode()));
			}
		});
		hBox.getChildren().add(btnChangeMode);

		return hBox;
	}

	private Node getStatusBar() {
		HBox hBox = new HBox();
		hBox.setPadding(paddingNarrow);
		hBox.setSpacing(SPACING);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.getStyleClass().add("statusBarBorder");
		lblMode = new Label(String.format(elevatorCCBundle.getString("mode"), getSelectedMode()));
		hBox.getChildren().add(lblMode);
		return hBox;
	}

	private String getSelectedMode() {
		return AUTOMATIC_MODE ? elevatorCCBundle.getString("automaticMode") : elevatorCCBundle.getString("manualMode");
	}

	private String getUnselectedMode() {
		return AUTOMATIC_MODE ? elevatorCCBundle.getString("manualMode") : elevatorCCBundle.getString("automaticMode");
	}
}
