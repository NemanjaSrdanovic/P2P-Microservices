package routeCalculationManagement;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import converter.FieldConverter;
import exceptions.ARSException;
import messages.Car;
import messages.Field;
import messages.Message;
import model.ARSCar;
import model.ARSField;
import service.RouteService;
import validator.Validator;

/**
 * Class is used to unpack a Car object received from Message, trigger a registration if necessary, route calculation and
 * wrap it back to Message envelope for sending it back on network
 * 
 * @author nenad.cikojevic
 *
 */
public class RouteWorker implements Runnable {

	private RouteManager routeManager;

	public RouteWorker(RouteManager routeManager) {

		System.out.println("Route Worker started...");
		this.routeManager = routeManager;
	}
	
	@Override
	public void run() {

		while (true) {
			Message message = routeManager.getMessages().poll();

			if (message == null)
				continue;

			ARSCar car = null;
			Car carReceived = null;

			try {
				
			
				if (!(message.getData() instanceof Car))
					throw new ARSException("Route Exception", "Object receved is not instance of Car");
				carReceived = (Car) message.getData();
				
				System.out.println("****************************************************************************");
				System.out.println(carReceived.getCarDestination().getCoordinate()+ ":   Received DESTINATION" + "  CARid: "+ carReceived.getCarId());
				System.out.println("****************************************************************************");
				System.out.println(carReceived.getCarPosition().getCoordinate()+ ":   Received Position"+"  CARid: "+ carReceived.getCarId());
				System.out.println("****************************************************************************");
				System.out.println("Route for RECEIVED CAR");
				for (Field f : carReceived.getCarRoute()) {
					System.out.println(f.getCoordinate());
				}
				
				System.out.println("****************************************************************************");
				
				/*
				 * Validation of received received object
				 */
				Validator.isCarValid(carReceived);

				car = routeManager.registerCar(carReceived, message.getSource());
				
				System.out.println("****************************************************************************");
				System.out.println(car.getCarDestination().getCoordinate()+ ":       CAR IN REGISTER DESTINATION");
				System.out.println(car.getCarPosition().getCoordinate()+ ":       CAR IN REGISTER POSITION");
				System.out.println("****************************************************************************");
				System.out.println("Route for car in REGISTER: ");
				for (Field f : carReceived.getCarRoute()) {
					System.out.println(f.getCoordinate());
				}
				
				System.out.println("****************************************************************************");
				if (carReceived.getCarRoute() != null && !carReceived.getCarRoute().isEmpty()) {
					System.out.println("Current route size for car id:" + carReceived.getCarId() + " is: "
							+ carReceived.getCarRoute().size());

				}

				System.out.println("Current possition for CARID : " + carReceived.getCarId() + " is "
						+ carReceived.getCarPosition().getCoordinate());
				
				/*
				 * route calculation
				 */
				RouteService routeService = new RouteService(routeManager.getMapService());
				List<ARSField> route = routeService.routeCalc(carReceived.getCarPosition().getCoordinate(),
						carReceived.getCarDestination().getCoordinate());
				
				 System.out.println("ROUTE CALC: "+route.size()+ "    "+ "CURRENT: "+ carReceived.getCarRoute().size());
				
				/*
				 * decides if the route should be set to the received car
				 */
				if (carReceived.getCarRoute() == null 
						|| carReceived.getCarRoute().isEmpty()
						|| !car.getCarDestination().getCoordinate()
								.equals(carReceived.getCarDestination().getCoordinate())
						|| (carReceived.getCarRoute().size() >= route.size())
						||isNotDrivable(carReceived.getCarRoute())){

					List<Field> routeToReturn = new ArrayList<>();
					for (ARSField f : route) {
						routeToReturn.add(FieldConverter.convertFrom(f));
					}

					carReceived.setCarRoute(routeToReturn);
					car.setCarRoute(route);
					System.out.println("****************************************************************************");
					System.out.println("New route for car id " + carReceived.getCarId() + ":");
					for (Field f : carReceived.getCarRoute()) {
						System.out.println(f.getCoordinate());
					}
					System.out.println("****************************************************************************");

				}

				if (!car.getCarDestination().getCoordinate().equals(carReceived.getCarDestination().getCoordinate())) {
					car.setCarDestination(FieldConverter.convertFrom(carReceived.getCarDestination()));
					System.out.println("New destination: " + car.getCarDestination().getCoordinate());
				}

				car.setCarPrevPosition(car.getCarPosition());
				car.setCarPosition(FieldConverter.convertFrom(carReceived.getCarPosition()));

				if (!car.getCarPosition().equals(car.getCarRoute().get(0)))
					routeManager.getMapService().placeCarOnField(car, car.getCarPosition(), car.getCarRoute());

			} catch (ARSException e) {
				System.err.println(e.getMessage());
			} finally {
				if (routeManager.getConnection().getClient() != null)
					try {
						routeManager.passToClient(message, carReceived);
					} catch (SocketException e) {
						e.printStackTrace();
					}
			}

		}

	}
	
	private boolean isNotDrivable(List<Field>route) throws ARSException {
		
		for(Field f: route) {
			if(!routeManager.getMapService().getByPosition(f.getCoordinate()).isDrivable())
				return false;
		}
		return true;
		
	}
}
