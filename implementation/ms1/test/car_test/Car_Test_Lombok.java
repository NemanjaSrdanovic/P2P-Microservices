package car_test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import model.ARSCar;
@RunWith(JUnitPlatform.class)
class Car_Test_Lombok {

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
	void test_getter_setter() {
		ARSCar car = new ARSCar(1);
		car.setId(0);
		
		assertTrue(car.getId()==0);
		
	}
	
	@Test
	void test_direction_constructor() {
	
		assertThrows(NullPointerException.class,()-> new ARSCar(3));
		
	}
	

}
