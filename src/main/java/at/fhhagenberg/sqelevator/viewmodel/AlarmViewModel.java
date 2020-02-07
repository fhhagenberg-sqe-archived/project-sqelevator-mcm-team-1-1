package at.fhhagenberg.sqelevator.viewmodel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class AlarmViewModel {
	private static final Image errorImage = new Image("icons/ic_error.png");
	private static final Image warningImage = new Image("icons/ic_warning.png");

	private SimpleStringProperty alarmMessage = new SimpleStringProperty();

	private SimpleObjectProperty<Image> image = new SimpleObjectProperty<>();

	public AlarmViewModel(String alarmMessage, boolean isError) {
		alarmMessageProperty().set(alarmMessage);

		if (isError) {
			imageProperty().set(errorImage);
		} else {
			imageProperty().set(warningImage);
		}
	}

	public String getAlarmMessage() {
		return alarmMessage.get();
	}

	public SimpleStringProperty alarmMessageProperty() {
		return alarmMessage;
	}

	public Image getImage() {
		return image.get();
	}

	public SimpleObjectProperty<Image> imageProperty() {
		return image;
	}
}
