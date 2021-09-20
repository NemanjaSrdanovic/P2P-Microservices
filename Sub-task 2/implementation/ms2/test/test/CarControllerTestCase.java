package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import constants.Constraints;
import controllers.CarController;
import controllers.ConnectionHandler;
import controllers.MS2Controller;
import exceptions.CDGException;
import model.CarCDG;

public class CarControllerTestCase {
	
	
	/*<------------------Please uncomment the MS2Controller part to do the test----------------->*/

	private static CarCDG car;
	private static ConnectionHandler handlerName;
	private static MS2Controller msController;
	private static CarController controller;
	
	/*
	 * Instantiating Variables needed to conduct the tests
	 * */
	@BeforeClass 
	public static void setUpVariables() {
		
		try {
			String msID = Constraints.MS2_ID;
			handlerName = new ConnectionHandler("3025", msID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		msController = new MS2Controller(handlerName);
		car = new CarCDG(3025);
		controller = new CarController(msController,car);
	}

	/*
	 * Reseting variables for new tests
	 * */
	@AfterClass 
	public static void tearDown() {
	
		handlerName.setConnection(null);
		handlerName = null;
		msController = null;
		car = null;
		controller = null;
		
	}
	
	/*
	 * Testing the exceptions in the Car class methods
	 * */
	@Test(expected = IllegalArgumentException.class)
	public void carDestinationAndPositionNullExceptionsTest() throws IllegalArgumentException {
		
		
		car.setCarPosition(null);
		car.setCarDestination(null);
		
	}

	
	/*
	 * Testing if the getCarRoute method returns a valid value
	 * */
	@Test
	public void getCalculatedCarRouteTest() {
		
		assertNotNull(controller.getCar().getCarRoute());
	}
	
	
	/*Die Methode ist andauernd aktiv, da Autos immer eine neue destination auswählen 
	 * und erneut die travel Methode starten. Daher überprüft man ob die Methode korekte 
	 * Variablen zuweist.Weil sie aber nicht unterbrochen werden kann muss man sie kurz 
	 * pausieren, um die Variable lesen zu können
	 * 
	 * Die position wird nach einem Zykel gleich der ersten destination, da es sich aber
	 * um testo objekte handelt, wird es beim jetzigen Test nicht der Fall sein*/
	@Test
	public void setPositionAndDestinationFieldsTest() throws InterruptedException, CDGException {	 
		 		
		 Thread thread = new Thread() {
			    @Override
			    public void run() {try {
					controller.setPositionAndDestinationFields();
				} catch (InterruptedException | CDGException e) {
					e.getMessage();
				}}
			  };
			  thread.start();
			  thread.sleep(10);

			  System.out.println("1. <----- Car position:"+controller.getCar().getCarPosition().toString());
			  System.out.println("1. <----- Car destination:"+controller.getCar().getCarDestination().toString());
			  
			  assertTrue(controller.getCar().getCarPosition().isDrivable());
	
	}
	
	/*
	 * See setPositionAndDestinationFieldsTest
	 * */
	@Test
	public void setNewDestinationFieldTest() throws InterruptedException {
		
		Thread thread = new Thread() {
			
			@Override
			public void run() {
					try {
						controller.setNewDestinationField();
					} catch (InterruptedException | CDGException e) {
						e.getMessage();
					}
			}
		};
		
		thread.start();
		thread.sleep(10);
		
		 System.out.println("\n");

		 System.out.println("2. <----- Car position:"+controller.getCar().getCarPosition().toString());
		 System.out.println("2. <----- Car destination:"+controller.getCar().getCarDestination().toString());
		  
		  assertTrue(controller.getCar().getCarPosition().isDrivable());
		
	}
	
	/*
	 * See setPositionAndDestinationFieldsTest
	 * */
	@Test
	public void startTavelingTest() throws InterruptedException {
		
		Thread thread = new Thread() {
			
			@Override
			public void run() {
					try {
						controller.setNewDestinationField();
					} catch (InterruptedException | CDGException e) {
						e.getMessage();
					}
			}
		};
		
		thread.start();
		thread.sleep(5000);
		
		assertTrue(thread.isAlive());
	}

}
