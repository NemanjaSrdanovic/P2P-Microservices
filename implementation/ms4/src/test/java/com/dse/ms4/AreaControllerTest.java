package com.dse.ms4;

import static org.junit.jupiter.api.Assertions.*;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.dse.ms4.controller.AreaController;
import com.dse.ms4.model.BlockedArea;
import com.dse.ms4.rmsExceptions.AreaNotValidException;

import messages.Coordinate;
import messages.Field;
import messages.Street;

class AreaControllerTest {
	
	AreaController controller = null;
	
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
	
	Street s = null;
	Street k = null;
	Street t = null;
	List<Field> drivablecarRoute = new ArrayList<Field>();
	List<Field> notDrivableCarRoute = new ArrayList<Field>();
 
	BlockedArea bTest;
	BlockedArea cTest;

	@BeforeEach
	void setUp() throws Exception {
		
		position.setDrivable(true);
		destination.setDrivable(true);
		b.setDrivable(true);
		c.setDrivable(true);
		nD.setDrivable(false);

		
		drivablecarRoute.add(position);
		drivablecarRoute.add(b);
		//drivablecarRoute.add(c);
		drivablecarRoute.add(destination);
		
		//---------------------------
		notDrivableCarRoute.add(nD);
		notDrivableCarRoute.add(c);
		notDrivableCarRoute.add(c);
		
		
		
		s = new Street("Nussdorfer", drivablecarRoute);
		t = new Street("Nothingness", notDrivableCarRoute);		
		k = new Street("First Avenue", notDrivableCarRoute);	
		
		
		cTest = new BlockedArea(t, 2, 3);
		bTest = new BlockedArea(k, 0, 1);
	}
	
	
	@Test
	void testAreaController() {
		controller = new AreaController();
	}
	
	@Test
	void testSetBlockedAreas() {
		controller = new AreaController();
		
		controller.setBlockedAreas(bTest);
		controller.setBlockedAreas(cTest);
		
		
		assertEquals(controller.getBlockedAreas().size(), 2);
		assertTrue(controller.getBlockedAreas().contains(bTest));
		assertTrue(controller.getBlockedAreas().contains(cTest));
		assertNotEquals(controller.getBlockedAreas().size(), 1);
		assertFalse(controller.getBlockedAreas().isEmpty());

	}
	
	
	@Test
	void testBlockArea() {
		controller = new AreaController(new RmsConnection(3023));
		
			try {
				controller.blockArea(s, 1, 2);
			} catch (AreaNotValidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
	
	
	@Test
	void testGetBlockedAreas() {
		controller = new AreaController();
		
		controller.setBlockedAreas(bTest);
		controller.setBlockedAreas(cTest);
		
		
		assertEquals(controller.getBlockedAreas().size(), 2);
		assertTrue(controller.getBlockedAreas().contains(bTest));
		assertTrue(controller.getBlockedAreas().contains(cTest));
		assertNotEquals(controller.getBlockedAreas().size(), 1);
		assertFalse(controller.getBlockedAreas().isEmpty());

	}
	
	@Test
	void testUnBlockArea() {
		controller = new AreaController(new RmsConnection(3020));
		
		controller.setBlockedAreas(bTest);
		controller.setBlockedAreas(cTest);
	
		controller.unBlockArea(bTest.getStreet().getName());
	}

	@Test
	void testCheckIfStreetHasBlockedField() {
		controller = new AreaController();
		
		assertTrue(controller.checkIfStreetHasBlockedField(t));
		assertFalse(controller.checkIfStreetHasBlockedField(s));
	}
	
	

}
