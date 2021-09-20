package converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import exceptions.ARSException;
import messages.Car;
import messages.Field;
import model.ARSCar;
import model.ARSField;

/**
 * Class is used to convert Car objects from network into ARSCar objects of MS1_ARS and vice- versa
 * @author nenad.cikojevic
 *
 */
public class CarConverter {

	public static ARSCar convertFrom(messages.Car car, String ipPort) throws ARSException {

		if (car.getCarId() < 0)
			throw new ARSException("Car exception", "Corrupted ID of vehicle");

		ARSCar to = new ARSCar(car.getCarId());
		to.setCarDestination(FieldConverter.convertFrom(car.getCarDestination()));
		to.setCarPosition(FieldConverter.convertFrom(car.getCarPosition()));
		to.setIpPort(ipPort);

		List<ARSField> route = new ArrayList<>();

		Optional<List<Field>> routeOptional = Optional.ofNullable(car.getCarRoute());

		if (routeOptional.isPresent()) {
			for (Field f : routeOptional.get()) {
				route.add(FieldConverter.convertFrom(f));
			}
			to.setCarRoute(route);
		}
		return to;
	}

	public static Car convertFrom(ARSCar car) throws ARSException {

		if (car.getId() < 0)
			throw new ARSException("Car exception", "Corrupted ID of vehicle");

		Car to = new Car(car.getId());
		to.setCarDestination(FieldConverter.convertFrom(car.getCarDestination()));
		to.setCarId(car.getId());
		to.setCarPosition(FieldConverter.convertFrom(car.getCarPosition()));

		List<Field> route = new ArrayList<>();
		if (car.getCarRoute() != null && !car.getCarRoute().isEmpty()) {
			for (ARSField f : car.getCarRoute()) {
				route.add(FieldConverter.convertFrom(f));
			}
		}

		to.setCarRoute(route);

		return to;
	}
}
