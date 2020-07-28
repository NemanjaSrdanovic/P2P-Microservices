package com.dse.ms4;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.dse.ms4.model.BlockedArea;
import com.dse.ms4.rmsExceptions.AreaNotValidException;

import messages.Coordinate;
import messages.Field;
import messages.Street;

class BlockedAreaTest {
	
	BlockedArea areaTest = null;
	BlockedArea bAreaTest = null;
	
	Coordinate d = new Coordinate(0, 3);
	Coordinate e = new Coordinate(1, 3);
	Coordinate f = new Coordinate(2, 3);
	Coordinate g = new Coordinate(3, 3);
	Coordinate n = new Coordinate(2, 3);
	Field destination = new Field(g);
	Field position = new Field(d);
	Field b = new Field(e);
	Field c = new Field(f);
	Field nD = new Field(n);
	
	Street streetTest = null;
	Street ulicaTest = null;
	
	List<Field> carRouteTest;;

	@BeforeEach
	void setUp() throws Exception {
		
		
		position.setDrivable(true);
		destination.setDrivable(true);
		b.setDrivable(true);
		c.setDrivable(true);
		nD.setDrivable(false);

		carRouteTest = new ArrayList<Field>();
		
		carRouteTest.add(position);
		carRouteTest.add(b);
		carRouteTest.add(c);
		carRouteTest.add(destination);
		
		streetTest = new Street("2nd_Avenue", carRouteTest);
		ulicaTest = new Street("strasse", carRouteTest);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	public void testBlockedAreaException() {


		assertThrows(AreaNotValidException.class, () -> {
			 new BlockedArea(streetTest, -1, 2);
		}, "from field less than 0");
		
		assertThrows(AreaNotValidException.class, () -> {
			 new BlockedArea(streetTest, 5, 2);
		}, "from field less larger that list size");
		
		assertThrows(AreaNotValidException.class, () -> {
			 new BlockedArea(streetTest, 5, 7);
		}, "from field less larger that list size");
		
		assertThrows(AreaNotValidException.class, () -> {
			 new BlockedArea(streetTest, -3, -7);
		}, "from field less larger that list size");
	}
	
	
	
	
	@Test
	void testBlockedAreaStreetIntInt() {
		try {
			bAreaTest = new BlockedArea(streetTest);
			areaTest = new BlockedArea(streetTest, 0, 2);
			areaTest = new BlockedArea(streetTest, 2, 1);
		} catch (AreaNotValidException e) {
		}
	}
	
	

	@Test
	void testGetStreet() {
	
	}
	
	@Test
	public void setStreetTest() {
		bAreaTest = new BlockedArea(streetTest);
		bAreaTest.setStreet(ulicaTest);
		
		assertEquals(ulicaTest.getFields().size(), bAreaTest.getStreet().getFields().size());
	}


	@Test
	void testUnblockFields() {
		//fail("Not yet implemented");
	}

}
