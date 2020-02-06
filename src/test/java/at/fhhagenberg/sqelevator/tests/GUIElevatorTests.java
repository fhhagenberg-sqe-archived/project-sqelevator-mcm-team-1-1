package at.fhhagenberg.sqelevator.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import at.fhhagenberg.sqelevator.mock.MockElevator;
import at.fhhagenberg.sqelevator.MockElevatorServiceFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.converter.NumberStringConverter;
import org.junit.jupiter.api.Disabled;
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
import org.testfx.util.WaitForAsyncUtils;
import sqelevator.IElevator;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@ExtendWith(ApplicationExtension.class)
public class GUIElevatorTests {

	private NumberStringConverter nsc = new NumberStringConverter();
	private ApplicationMain app;

	@Start
	public void start(Stage stage) throws Exception {
		app = new ApplicationMain();
		app.setDisableAutomaticControl(true);
		app.setElevatorServiceFactory(new MockElevatorServiceFactory());
		app.start(stage);
	}


	@Test
	public void testInitialStateStatusBar (FxRobot robot) {
		Label label = robot.lookup("#statusbar").query();
		assertNull(label.getText());
	}

	@Test
	public void testInitialStatePayload (FxRobot robot) {
		Label payload = robot.lookup("#p0").query();
		assertEquals(nsc.toString(MockElevator.ELEVATOR_WEIGHT_MOCK_VALUE), payload.getText());
	}

	@Test
	public void testInitialStateSpeed (FxRobot robot) {
		Label speed = robot.lookup("#s0").query();
		assertEquals(nsc.toString(MockElevator.ELEVATOR_SPEED_MOCK_VALUE), speed.getText());
	}

	@Test
	public void testInitialStateTargets(FxRobot robot) {
		Label targets = robot.lookup("#t0").query();
		assertEquals("1", targets.getText());
	}

	@Test
	public void testIsInAutomaticMode(FxRobot robot) {
		robot.clickOn("#0,3");
		Label label = robot.lookup("#statusbar").query();
		assertNull(label.getText());
	}

	@Test
	public void testEnableAutomaticMode(FxRobot robot) {
		robot.clickOn("#M0");
		robot.clickOn("#0,3");
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
		var elevatorService = (MockElevator) app.getElevatorService();
		var elevator0 = elevatorService.getElevators().get(0);

		robot.clickOn("#M0");
		robot.clickOn("#0,3");
		waitForUpdate(robot);
		verifyThat("#t0", hasText("4"));
		assertEquals(3, elevator0.getTargetFloor());

		robot.clickOn("#0,1");
		waitForUpdate(robot);
		verifyThat("#t0", hasText("2"));
		assertEquals(1, elevator0.getTargetFloor());

	}

	@Test
	public void testAddAlarm(FxRobot robot) {
		String message = "Test alarm";

		AlarmsService.getInstance().clear();
		AlarmsService.getInstance().addWarning(message);

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
		AlarmsService.getInstance().addWarning(message1);
		AlarmsService.getInstance().addWarning(message2);
		AlarmsService.getInstance().addWarning(message3);

		TableView<AlarmViewModel> tableView = robot.lookup("#alarms-table").query();

		assertEquals (3, tableView.getItems().size());
		assertEquals(message1, tableView.getItems().get(0).getAlarmMessage());
		assertEquals(message2, tableView.getItems().get(1).getAlarmMessage());
		assertEquals(message3, tableView.getItems().get(2).getAlarmMessage());
	}

	@Test
	public void testCallInfoLight(FxRobot robot) {
		var elevatorService = (MockElevator) app.getElevatorService();
		var floor0 = elevatorService.getFloors().get(0);
		floor0.setDownButtonActive(true);

		waitForUpdate(robot);
		//TODO
	}

	@Test
	public void testDirectionLight(FxRobot robot) {
		// TODO
	}

	@Test
	public void testDoorStatus(FxRobot robot) {
		var elevatorService = (MockElevator) app.getElevatorService();
		var elevator0 = elevatorService.getElevators().get(0);

		try{
			elevator0.setDoorStatus(MockElevator.ELEVATOR_DOORS_OPENING);
			waitForUpdate(robot);
			verifyThat("#d0", hasText("Opening"));

			elevator0.setDoorStatus(MockElevator.ELEVATOR_DOORS_OPEN);
			waitForUpdate(robot);
			verifyThat("#d0", hasText("Open"));

			elevator0.setDoorStatus(MockElevator.ELEVATOR_DOORS_CLOSING);
			waitForUpdate(robot);
			verifyThat("#d0", hasText("Closing"));

			elevator0.setDoorStatus(MockElevator.ELEVATOR_DOORS_CLOSED);
			waitForUpdate(robot);
			verifyThat("#d0", hasText("Closed"));

		}
		catch (Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testChangeServicesFloors(FxRobot robot){
		var elevatorService = (MockElevator) app.getElevatorService();
		var elevator0 = elevatorService.getElevators().get(0);

		try{
			assertTrue(elevator0.getServicesFloors(0));

			robot.clickOn("#EditServicesFloors");
			waitForUpdate(robot);
			robot.clickOn("#sfc0,0");
			waitForUpdate(robot);

			assertFalse(elevator0.getServicesFloors(0));
		}
		catch (Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
	}

	private void waitForUpdate(FxRobot robot){
		robot.sleep(250);	//need to wait for model to run update()
	}
}
