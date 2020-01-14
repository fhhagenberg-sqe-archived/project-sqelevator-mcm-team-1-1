package at.fhhagenberg.sqelevator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import at.fhhagenberg.sqelevator.gui.ApplicationMain;
import at.fhhagenberg.sqelevator.model.AlarmsService;
import at.fhhagenberg.sqelevator.viewmodel.AlarmViewModel;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class GUIElevatorTests {

	@Start
	public void start(Stage stage) throws Exception {
		new ApplicationMain().start(stage);
	}


	@Test
	public void testInitialStateStatusBar (FxRobot robot) {
		Label label = robot.lookup("#statusbar").query();
		assertNull(label.getText());
	}

	@Test
	public void testInitialStatePayload (FxRobot robot) {
		Label payload = robot.lookup("#p0").query();
		assertEquals("-", payload.getText());
	}

	@Test
	public void testInitialStateSpeed (FxRobot robot) {
		Label speed = robot.lookup("#s0").query();
		assertEquals("-", speed.getText());
	}

	@Test
	public void testInitialStateTargets(FxRobot robot) {
		Label targets = robot.lookup("#t0").query();
		assertEquals("-", targets.getText());
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

	@Test
	public void testPayload(FxRobot robot) {
		// TODO
	}

	@Test
	public void testSpeed(FxRobot robot) {
		// TODO
	}

	@Test
	public void testTargetFloor(FxRobot robot) {
		robot.clickOn("#M0");
		robot.clickOn("#0,3");
		verifyThat("#t0", hasText("4"));

		robot.clickOn("#0,1");
		verifyThat("#t0", hasText("2"));
	}

	@Test
	public void testAddAlarm(FxRobot robot) {
		String message = "Test alarm";

		AlarmsService.getInstance().clear();
		AlarmsService.getInstance().addAlert(message);

		TableView<AlarmViewModel> tableView = robot.lookup("#alarms-table").query();
		AlarmViewModel item = tableView.getItems().get(0);

		assertEquals(message, item.getAlarmMessage());
	}

	@Test
	public void testAddAlarms(FxRobot robot) {
		String message1 = "Test alarm 1";
		String message2 = "Test alarm 2";
		String message3 = "Test alarm 3";

		AlarmsService.getInstance().clear();
		AlarmsService.getInstance().addAlert(message1);
		AlarmsService.getInstance().addAlert(message2);
		AlarmsService.getInstance().addAlert(message3);

		TableView<AlarmViewModel> tableView = robot.lookup("#alarms-table").query();

		assertEquals (3, tableView.getItems().size());
		assertEquals(message1, tableView.getItems().get(0).getAlarmMessage());
		assertEquals(message2, tableView.getItems().get(1).getAlarmMessage());
		assertEquals(message3, tableView.getItems().get(2).getAlarmMessage());
	}

	@Test
	public void testCallInfoLight(FxRobot robot) {
		// TODO
	}

	@Test
	public void testDirectionLight(FxRobot robot) {
		// TODO
	}

}
