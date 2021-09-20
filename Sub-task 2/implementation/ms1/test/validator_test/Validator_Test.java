package validator_test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import exceptions.ARSException;
import messages.Car;
import messages.Coordinate;
import messages.Field;
import validator.Validator;

@RunWith(JUnitPlatform.class)
class Validator_Test {

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
	void test_fieldWithCorruptedObject_ThrowsARSException() {
		
		Coordinate f = new Coordinate(0, 1);
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isFieldValid(f);});
		
	}
	
	@Test
	void test_FieldWithNullbject_ThrowsARSException() {
		
		Field f = null;
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isFieldValid(f);});
		
	}
	
	@Test
	void test_FieldWithCorruptedCoordinate_ThrowsARSException() {
		
		Field f =new Field(new Coordinate(1, 2));
		
		f.getCoordinate().setX(-1);
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isFieldValid(f);});
		
		f.getCoordinate().setX(15);
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isFieldValid(f);});
		
		f.getCoordinate().setY(-1);
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isFieldValid(f);});
		
		f.getCoordinate().setY(15);
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isFieldValid(f);});
		
	}
	
	@Test
	void test_FieldWithCorruptedFieldType_ThrowsARSException() {
		
		Field f = new Field(new Coordinate(4, 5));
		f.setFieldType(null);
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isFieldValid(f);});
		
	}
	
	@Test
	void test_CarObjectNotInstanceOfCar_ThrowsARSException() {
		
		Field car = new Field(new Coordinate(0, 1));
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isCarValid(car);});
		
	}
	
	@Test
	void test_CarObjectNull_ThrowsARSException() {
		
		Car car = null;
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isCarValid(car);});
		
	}
	
	@Test
	void test_CarWithCorruptedID_ThrowsARSException() {
		
		Car car = new Car(-1);
		
		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isCarValid(car);});
		
	}
	
//	@Test
//	void test_CarWithPossition_ThrowsARSException() {
//		
//		Car car = new Car(1);
//		
//		Assertions.assertThrows(ARSException.class, ()->{validator.Validator.isCarValid(car);});
//		
//	}
	


	
	

}
