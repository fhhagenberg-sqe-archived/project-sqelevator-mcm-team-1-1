package at.fhhagenberg.sqelevator.tests;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import at.fhhagenberg.sqelevator.gui.ApplicationMain;
import javafx.scene.control.Label;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class GUIElevatorTests {

	@Start
	public void start(Stage stage) throws Exception {
		new ApplicationMain().start(stage);
	}
	
	@Test
	public void testIsInAutomaticMode(FxRobot robot) {
		robot.clickOn("#0,3"); 
		Label label = robot.lookup("#statusbar").query();
		assertNull(label.getText());
	}
	
	@Test
	public void testEnableMode(FxRobot robot) {
		robot.clickOn("#M0");
		robot.clickOn("#0,3"); 
		verifyThat("#statusbar", hasText("Next target floor for elevator <1> is 4"));
	}
	
	@Test
	public void testEnableAutomaticMode(FxRobot robot) {
		robot.clickOn("#M0");
		robot.clickOn("#0,3"); 
		verifyThat("#statusbar", hasText("Next target floor for elevator <1> is 4"));
		robot.clickOn("#M0");
		robot.clickOn("#0,5"); 
		verifyThat("#statusbar", hasText("Next target floor for elevator <1> is 4"));
	}
	
}
