package controllers;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import exceptions.CDGException;
import messages.Field;
import model.CarCDG;

/**
 * The CarController class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-06-07
 */

public class CarController {

	/*-------------------------------------VARIABLES-------------------------------------*/

	private CarCDG car;
	private List<Field> drivableFields = new CopyOnWriteArrayList<Field>();
	private List<Field> carRoute = new CopyOnWriteArrayList<Field>();
	private MS2Controller msController;

	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	/**
	 * @param MS2Controller controllerName
	 * @param Car           car
	 */
	public CarController(MS2Controller controllerName, CarCDG car) { // Test constructor
		this.car = car;
		this.msController = controllerName;
	}

	/**
	 * @param Car           carObject
	 * @param MS2Controller controllerName
	 * 
	 *                      Initialising parameters, and starting an endless process
	 *                      in which the car is getting his position, destination
	 *                      field and travelling across the map.
	 */

	public CarController(CarCDG carObject, MS2Controller controllerName) {

		this.car = carObject;
		this.msController = controllerName;

		try {
			setPositionAndDestinationFields();
		} catch (CDGException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/*-------------------------------------GETTERS AND SETTER METHODS-------------------------------------*/

	/**
	 * @param Field position
	 * @param Field destination
	 * 
	 *              Sending request to MS2Controller, to initialise Message request
	 *              and retrieve carRoute (List<Field>)
	 */

	public void getCalculatedCarRoute(CarCDG carWithRoute) throws CDGException, InterruptedException {

		carRoute = msController.getCarRoute(car);

		car.setCarRoute(carRoute);
	}

	/**/
	public CarCDG getCar() { // test method
		return car;
	}

	/**
	 * Sending request to MS2Controller, to initialise Message request and retrieve
	 * drivableFields/Streets (List<Field>) Then choosing from the List<Field> to
	 * random fields for position and destination. This process is done as long the
	 * position and destination field are equal.
	 */
	public void setPositionAndDestinationFields() throws InterruptedException, CDGException {

		System.out.println("Car " + car.getCarId() + " setting position and destination...");

		Field position = null;
		Field destination = null;

		drivableFields = msController.getDrivableFields(car);

		do {

			Random randomPosition = new Random();
			position = drivableFields.get(randomPosition.nextInt(drivableFields.size()));

			Random randomDestination = new Random();
			destination = drivableFields.get(randomDestination.nextInt(drivableFields.size()));

		} while (position.equals(destination));

		car.setCarPosition(position);
		car.setCarDestination(destination);

		System.out.println("\n");
		System.out.println("####### Car " + car.getCarId() + " position field::" + car.getCarPosition().toString());
		System.out.println("####### Car " + car.getCarId() + " destination field:" + car.getCarDestination().toString());
		System.out.println("\n");
		
		startTaveling();
	}

	/**
	 * After finishing a route, this method is called, to set the car object a new
	 * destination Field. In this process the current destination is set as the
	 * car-position and a new random Field is choosen as the new destination field
	 */
	public void setNewDestinationField() throws InterruptedException, CDGException {

		System.out.println("\n");
		System.out.println("#######Car " + car.getCarId() + " setting new destination..");
		System.out.println("\n");
		
		Field destination = null;

		drivableFields = msController.getDrivableFields(car);

		do {

			Random randomDestination = new Random();
			destination = drivableFields.get(randomDestination.nextInt(drivableFields.size()));

		} while (car.getCarDestination().equals(destination));

		car.setCarPosition(car.getCarDestination());
		car.setCarDestination(destination);

		System.out.println("\n");
		System.out.println("####### New destination field set...."); // <------------TEST - DELETE
		System.out.println("\n");
		
		car.setTraveledFieldsPerRoute(0);
		msController.informRMS(car);
		startTaveling();

	}

	/*-------------------------------------MOVING METHODS-------------------------------------*/

	/**
	 * The travelling method iterates through the route List<Field> and updates the
	 * car position with the next field. After every update the method is hold for
	 * several seconds, to give the travel a more real function, and the update
	 * route ( getCalculatedCarRoute ) method is triggered. This is done to check if
	 * a better route can be found or a field is blocked on the existing route.
	 */
	public void startTaveling() throws InterruptedException {

		try {
			getCalculatedCarRoute(car);

			System.out.println("\n");
			System.out.println(
					"#######################"
					+ "########################"
					+ "<< Car" + car.getCarId() + 
					" is now traveling >>"
					+ "########################"
					+ "########################");

				
				do{

				System.out.println("\n");
				System.out.println("    ##################"
						+ "####################"
						+ " Estimated travel time: " 
						+ car.getCarRoute().size() * 17
						+ " seconds ##################"
						+ "#################### ");
				System.out.println("\n");

				car.setCarPosition(car.getCarRoute().get(0));
				car.setTraveledFieldsPerRoute(car.getTraveledFieldsPerRoute() + 1);
				msController.informRMS(car);
								
				if (!car.equalsFields(car.getCarPosition(), car.getCarDestination())) {

					carRoute.remove(car.getCarRoute().get(0));
					car.setCarRoute(carRoute);
					getCalculatedCarRoute(car);
				} else {
					
					System.out.println("\n");
					System.out.println("####### Car " + car.getCarId() + " current possition:" + car.getCarPosition());
					System.out.println("####### Car " + car.getCarId() + " current destination: "
							+ car.getCarDestination().toString());
					System.out.println("####### Traveled fields:" + car.getTraveledFieldsPerRoute());
					System.out.println("\n");
					
					break;
				}
				System.out.println("\n");
				System.out
						.println("####### Car " + car.getCarId() + " current possition:" + car.getCarPosition());
				System.out.println(
						"####### Car " + car.getCarId() + " current destination: " + car.getCarDestination().toString());
				System.out.println("####### Traveled fields:" + car.getTraveledFieldsPerRoute());
				System.out.println("\n");
				
				Thread.sleep(10000);

			}while((!car.equalsFields(car.getCarPosition(), car.getCarDestination())));
				
				
				System.out.println("\n");
				System.out.println(
						"#######################"
						+ "########################"
						+ "<< Car" + car.getCarId() + 
						" has arrived! >>"
						+ "########################"
						+ "########################");
				System.out.println("\n");
				
				
			setNewDestinationField();
		} catch (CDGException e) {
			e.getMessage();
		}

	}

}
