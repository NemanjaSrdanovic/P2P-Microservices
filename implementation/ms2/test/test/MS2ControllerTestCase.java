package test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import constants.Constraints;
import controllers.ConnectionHandler;
import controllers.MS2Controller;
import exceptions.CDGException;
import messages.Field;
import messages.Message;
import messages.TypeOfField;
import model.CarCDG;


public class MS2ControllerTestCase {
	
	/*<------------------Please uncomment the MS2Controller part to do the test----------------->*/


	private static ConnectionHandler handlerName;
	private static MS2Controller controllerToBeTested;
	private static CarCDG car;
	
	/*
	 * Instantiating Variables needed to conduct the tests
	 * */
	@BeforeClass 
	public static void setUpVariables() {
		
		try {
			String msID = Constraints.MS2_ID;
			handlerName = new ConnectionHandler("3025", msID);
			car=new CarCDG(3025);
		} catch (Exception e) {
			e.printStackTrace();
		}
		controllerToBeTested = new MS2Controller(handlerName);
	}
	
	/*
	 * Reseting variables for new tests
	 * */
	@AfterClass 
	public static void tearDown() {
	
		handlerName.setConnection(null);
		handlerName = null;
		controllerToBeTested = null;
	}
	

	/*
	 * Testing methods extractFieldsFromStreets & validateFieldObjects
	 * 
	 * */
	@Test(expected = CDGException.class)
	public void extractFieldsFromStreetsTest() throws CDGException {
		
		controllerToBeTested.extractFieldsFromStreets(controllerToBeTested.getTestObj().getExceptionTestStreets());		
	}
		
	
	/*
	 *  Testing method which returns the drivable fields, and the returned field type
	 * */
	@Test
	public void getDrivableFieldsTest() throws CDGException {
		
		List<Field> returnedFields;
		
		//returnedFields=controllerToBeTested.getDrivableFields();
		
	//	assertTrue(returnedFields.get(0).isDrivable() && returnedFields.size() > 0 );
	}
	
	
	/*
	 * Testing method which returns the car route
	 * */
	@Test
	public void getCarRouteTest() throws CDGException, InterruptedException{
		
		List<Field> returnedFields;
		
		returnedFields=controllerToBeTested.getCarRoute(car);
		
		assertTrue(returnedFields.get(0).getFieldType()== TypeOfField.STREET &&
				returnedFields.get(0).isDrivable() && returnedFields.size()>0 );
	}

	/*
	 * Testing the method which returns the Message based on the endpoint, when messages
	 * are available 
	 * */
	@Test
	public void getMessageFromHandlerTestWithObjects() {
		
		controllerToBeTested.getTestObj().generateFieldMessageObject();
		Message message = controllerToBeTested.getMessageFromServer("drivableFields");
		
		assertTrue(message!=null && message.getEndpoint()=="drivableFields");
	}

	
	
	/*
	 Testing the method which returns the Message based on the endpoint, when messages
	 * are not available. 
	 * */
	@Test
	public void getMessageFromHandlerTestWithOutObjects() throws InterruptedException {
	 
	
		
	   Thread thread = new Thread() {
	    @Override
	    public void run() {
	    	Message message = controllerToBeTested.getMessageFromServer("drivableFields");
		}
	  };
	  thread.start();
	  thread.sleep(5000);
	  
	    
	   assertTrue(thread.isAlive());
	}
	

}

