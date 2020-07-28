package controllers;

import java.util.ArrayList;
import java.util.List;

import constants.Constraints;
import exceptions.CDGException;
import messages.Car;
import messages.Field;
import messages.Message;
import messages.Street;
import model.CarCDG;
import receiver.ReceiveMessage;
import testObjects.TestObjects;

/**
 * The MS2Controller class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-06-09
 */
public class MS2Controller {

	/*-------------------------------------VARIABLES-------------------------------------*/
	private ConnectionHandler connection;
	private List<Field> drivableFields;
	private List<Field> carRoute;
	private ReceiveMessage receiver;
	private messages.Car carToSend = new Car(0);
	private TestObjects testObjects; // JUnit test

	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/
	public MS2Controller(ConnectionHandler handlerName) {

		this.connection = handlerName;
		this.drivableFields = new ArrayList<Field>();
		this.carRoute = new ArrayList<Field>();
		this.receiver = new ReceiveMessage();
		this.connection.setMessageProcessor(receiver);
		this.testObjects = new TestObjects(); // JUnit test

	}

	/**
	 * This method is triggered by the CarController and sends a request into the
	 * network containing a message which triggers a method at the destination
	 * microservice. That method is sending back to the requester all streets
	 * (drivable fields) on the map.
	 * 
	 * This List<Field> is then hand over to the validateFieldObjects method which
	 * extracts all fields and checks if the fields are all drivable.
	 * 
	 * If all test are passed a List<Field> is hand over to the CarController.
	 */
	public List<Field> getDrivableFields(CarCDG car) throws CDGException, InterruptedException {

		String endpoint = "drivableFields";
		drivableFields = null;

		// <-------------------- MS2Controller implementiation, comment to do JUnit tests -------------------->

		do {

			Message messageField = new Message(Constraints.MS1_ID,
					connection.getConnection().getMyNode().getIpAddress() + ":" + 
			connection.getConnection().getMyNode().getPortNmr(),endpoint);

			connection.getConnection().getClient().addMessage(messageField);

			System.out.println("# MESSAGE: " + messageField.getMessageId()
					+ " # ----------------------------------------------- > ARS Fields requested.");

			setDrivableFields(
					(List<Field>) receiver.getSearchMessage().getMessagebyRequestIDField(messageField.getMessageId()));

		} while (getDrivableFields() == null);

		if (validateFieldObjects(drivableFields))
			return drivableFields;

		return drivableFields;

		/* <-- JUnit test MS2ContollerTest decomment to test --> */

		// drivableFields = drivableFieldsTestMethod(endpoint);
		// return drivableFields;

		/* <-- JUnit test CarControllerTest decomment to test--> */

		// return extractFieldsFromStreets(testObjects.getTestStreets());
	}

	/**
	 * This method is triggered by the CarController and sends a request into the
	 * network containing a message which triggers a method at the destination
	 * microservice. That method is calculating the best route ( List<Field>) on the
	 * map, based on the two fields hand over to the method.
	 * 
	 * The returned route is checked based on the destination and correctness of the
	 * returned fields.
	 * 
	 * If all test are passed a List<Field> is hand over to the CarController.
	 */
	public List<Field> getCarRoute(CarCDG car) throws CDGException, InterruptedException {

		String endpoint = "carRoute";
		carRoute = null;

		// <-------------------- MS2Controller implementiation, comment to do JUnit test -------------------->

		do {

			Message messageCarRoute = new Message(Constraints.MS1_ID,
					connection.getConnection().getMyNode().getIpAddress() + ":" 
			+ connection.getConnection().getMyNode().getPortNmr(), endpoint,
					createCarToSend(car));

			connection.getConnection().getClient().addMessage(messageCarRoute);

			System.out.println("# MESSAGE: " + messageCarRoute.getMessageId()
					+ " # ----------------------------------------------- > ARS Route requested.");

			setCarCDGRoute(
					receiver.getSearchMessage().getMessagebyRequestIDRoute(messageCarRoute.getMessageId(), carToSend));

		} while (getCarCDGRoute() == null);

		if (validateFieldObjects(getCarCDGRoute()))
			return getCarCDGRoute();
		else
			return getCarCDGRoute();

		/* <-- JUnit test MS2ContollerTest decomment to test --> */

		// carRoute = caRouteTestMethod(endpoint);
		// return carRoute;

		/* <--JUnit test CarControllerTest decomment to test --> */

		// return testObjects.getCarRoute();

	}

	/**
	 * @param CarCDG car
	 * 
	 *               This method informs the MS4_RMS about the current car position.
	 */
	public void informRMS(CarCDG car) throws InterruptedException {

		Message messageInformRMS = new Message(Constraints.MS4_ID,
				connection.getConnection().getMyNode().getIpAddress() + ":" 
		+ connection.getConnection().getMyNode().getPortNmr(),
				createCarToSend(car));

		connection.getConnection().getClient().addMessage(messageInformRMS);

		System.out.println("# MESSAGE: " + messageInformRMS.getMessageId()
				+ " #------------------------------------------------ > RMS informed.");

	}

	/*---------------------------------- VALIDATING AND CONVERTING OBJECTS ----------------------------------*/

	/**
	 * Validating that all streets from a list are drivable
	 * 
	 * @param List<Field> fieldsToValidate
	 */
	public boolean validateFieldObjects(List<Field> fieldsToValidate) {

		for (Field field : fieldsToValidate) {
			if (field.isDrivable() == false)
				return false;
		}

		return true;
	}

	public messages.Car createCarToSend(CarCDG carInput) {

		carToSend.setCarId(carInput.getCarId());

		if (carInput.getCarDestination() != null)
			carToSend.setCarDestination(carInput.getCarDestination());
		if (carInput.getCarPosition() != null)
			carToSend.setCarPosition(carInput.getCarPosition());
		carToSend.setCarRoute(carInput.getCarRoute());
		carToSend.setTraveledFieldsPerRoute(carInput.getTraveledFieldsPerRoute());

		return carToSend;

	}

	/*-------------------------------------GETTERS AND SETTERS -------------------------------------*/

	public synchronized List<Field> getCarCDGRoute() {

		return carRoute;
	}

	public synchronized void setCarCDGRoute(List<Field> input) {

		carRoute = input;
	}

	public synchronized List<Field> getDrivableFields() {

		return drivableFields;
	}

	public ReceiveMessage getReceiver() {
		return receiver;
	}

	public synchronized void setDrivableFields(List<Field> inputFields) {

		drivableFields = inputFields;
	}

	/*----------------------------------------- TEST METHODS -----------------------------------------*/

	/*
	 * JUnit MS2ControllerTest
	 */
	public TestObjects getTestObj() {

		return testObjects;
	}

	/*
	 * Extracting fields out of Streets returned from the Automatic Routing Service
	 * 
	 * @param List<Street> listStreets
	 */
	public List<Field> extractFieldsFromStreets(List<Street> listStreets) throws CDGException {

		List<Street> streetObjectsToextract = new ArrayList<Street>();
		streetObjectsToextract = listStreets;
		List<Field> extractedFields = new ArrayList<Field>();

		for (Street street : streetObjectsToextract) {

			for (Field field : street.getFields()) {

				extractedFields.add(field);
			}
		}

		System.out.println("MS2Controller exctractedFields:" + extractedFields.size());

		if (validateFieldObjects(extractedFields))
			return extractedFields;
		else
			throw new CDGException("Field type extraction error", " Not all fields are drivable");

	}

	/*
	 * The method is connected to the message Server which is taking over Messages
	 * and storing them in a queue if they are destined for this microservice.
	 * 
	 * That queue is iterated by this method, and the messages are compared by
	 * endpoints. If two endpoints are matching the message is hand over to the
	 * method which triggered this method.
	 * 
	 * @param String endpoint
	 */
	public Message getMessageFromServer(String endpoint) {

		Message returnMessage = null;

		while (true) {

			if (testObjects.getMessageObjects().size() != 0) {
				for (Message message : testObjects.getMessageObjects()) {

					if (message.getEndpoint() == endpoint) {
						returnMessage = message;
						testObjects.removeMessageFromQueue(message);
						return returnMessage;
					}
				}
			}
		}
	}

	/*
	 * Represents forwarding the message containing the request to the client
	 * Requesting the class to wait for the list to return and passing the list to
	 * the car object
	 */
	@SuppressWarnings("unchecked")
	public List<Field> drivableFieldsTestMethod(String endpoint) throws CDGException {

		testObjects.generateMessageObjects(endpoint);

		Message returnMessage;
		returnMessage = getMessageFromServer(endpoint);

		if (returnMessage.getEndpoint() == endpoint) {
			drivableFields = extractFieldsFromStreets((List<Street>) returnMessage.getData());
			return drivableFields;
		} else {
			throw new CDGException("MS2Controller - Field error", " Endpoints are not matching");
		}

	}

	/*
	 * Represents forwarding the message containing the request to the client
	 * Requesting the class to wait for the list to return and passing the list to
	 * the car object
	 */
	@SuppressWarnings("unchecked")
	public List<Field> caRouteTestMethod(String endpoint) throws CDGException {

		testObjects.generateMessageObjects(endpoint);
		Message returnMessage;
		returnMessage = getMessageFromServer(endpoint);

		if (returnMessage.getEndpoint() == endpoint && validateFieldObjects((List<Field>) returnMessage.getData())) {
			carRoute = (List<Field>) returnMessage.getData();
			return carRoute;
		} else {
			throw new CDGException("MS2Controller - Route error", " Endpoints are not matching");

		}
	}
}
