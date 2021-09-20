package field_test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import exceptions.ARSException;
import messages.Coordinate;
import messages.TypeOfField;
import model.ARSCar;
import model.ARSField;
import model.Direction;
import service.MapService;

@RunWith(JUnitPlatform.class)
class Field_Test {

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
	void test_Set_And_Field_Object() {
		
		Set<ARSField> fields = new TreeSet<>();
		
		ARSField f1 = new ARSField(new Coordinate(0, 3));
		ARSField f2  = new ARSField(new Coordinate(0, 3));
		ARSField f3 = f1;
		
		
		
		boolean add1=fields.add(f1);
		boolean add2=fields.add(f2);
		boolean add3=fields.add(f3);

		assertTrue(add1);
		assertFalse(add2);
		assertFalse(add3);
	}
	
	@Test
	void test_Map_And_Field_Object() {
		
		Map<ARSField,Double> fields = new HashMap<>();
		
		ARSField f1 = new ARSField(new Coordinate(0, 3));
		ARSField f3 = f1;
		
		fields.put(f1, Double.POSITIVE_INFINITY);
		fields.put(f1, Double.POSITIVE_INFINITY);
		fields.put(f3, Double.POSITIVE_INFINITY);
		
		assertTrue(fields.size()==1);
		
	}
	
	@Test
	void test_map_get_field_cost() throws ARSException {
		
		MapService mapService = new MapService();
		mapService.generateMap();
		
		ARSField field = mapService.getByPosition(new Coordinate(3, 3));
		
		assertTrue(field.getCost()==1);
		
		field.setDrivable(false);
		
		assertTrue(field.getCost().equals(Double.POSITIVE_INFINITY));
		
		field.setDrivable(true);
		
		ARSCar c1=new ARSCar(1);
		c1.setDirection(Direction.EAST);
		ARSCar c2=new ARSCar(3);
		c2.setDirection(Direction.WEST);
		ARSCar c3=new ARSCar(2);
		c3.setDirection(Direction.SOUTH);
		
		field.placeCar(c1);
		field.placeCar(c2);
		field.placeCar(c3);
		
		assertTrue(field.getCost()==4);
	}
	
	@Test
	void test_Field_Equals() throws ARSException {
		
		ARSField f1 = new ARSField(new Coordinate(1, 1));
		ARSField f2 = new ARSField(new Coordinate(1, 1));
		
		f1.setFieldType(TypeOfField.STREET);
		f2.setFieldType(TypeOfField.STREET);
		
		Set<Direction>directions = new TreeSet<>();
		directions.add(Direction.EAST);
		directions.add(Direction.WEST);
		
		Set<Direction>directions1 = new TreeSet<>();
		directions1.add(Direction.EAST);
		directions1.add(Direction.WEST);
		
		f1.setDirections(directions);
		f2.setDirections(directions1);
		
		f1.setCost((double)1);
		f2.setCost((double)1);
		
		ARSCar c1 = new ARSCar(2);
		ARSCar c2 = new ARSCar(1);
		
		f1.placeCar(c1);
		f2.placeCar(c1);
		
		
		assertTrue(f2.equals(f1));
		
		
	}

}
