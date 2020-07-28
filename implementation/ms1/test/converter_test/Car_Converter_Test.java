package converter_test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import converter.CarConverter;
import exceptions.ARSException;
import messages.Car;
import messages.Coordinate;
import messages.Field;
import model.ARSCar;

@RunWith(JUnitPlatform.class)
class Car_Converter_Test {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws ARSException {
		
		Car carFrom = new Car(9);
		carFrom.setCarDestination(new Field(new Coordinate(3, 4)));
		carFrom.setCarPosition(new Field(new Coordinate(6, 10)));
		
		ARSCar car = CarConverter.convertFrom(carFrom, "111.1111.1111.11:1234");
		
		System.out.println(car.getId());
		System.out.println(car.getCarPosition().getCoordinate());
		System.out.println(car.getCarDestination().getCoordinate());
		System.out.println(car.getCarRoute());
	}

}
