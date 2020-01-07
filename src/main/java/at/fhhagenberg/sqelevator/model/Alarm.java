package at.fhhagenberg.sqelevator.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Alarm {

	private ImageView imageView; 
	private String message; 
	
	public Alarm(String message, boolean isError) {
		this.imageView=new ImageView(new Image(isError ? "icons/ic_error.png" : "icons/ic_warning.png"));
		imageView.setFitWidth(25);
        imageView.setFitHeight(25);  
		this.message=message; 
	}

	public ImageView getImage() {
		return imageView;
	}
	
	public String getMessage() {
		return message;
	}

}
