package com.dse.ms4;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.mockito.Mockito.when;
import org.springframework.web.client.RestTemplate;

import com.dse.ms4.controller.AreaController;
import com.dse.ms4.controller.MessageController;
import com.dse.ms4.controller.RmsController;
import com.dse.ms4.model.BlockedArea;

import messages.Coordinate;
import messages.Field;
import messages.Street;

class RmsControllerTest {

	RmsController rmsControllertest = null;
	

	AreaController aController; 
	
	@Mock
	MessageController mController; 
	
	@Mock
	List<String> blockedStreets; 
		
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
	Street t = null;
	List<Field> drivablecarRoute = new ArrayList<Field>();
	List<Field> notDrivableCarRoute = new ArrayList<Field>();
	
	BlockedArea bTest = null;
	BlockedArea cTest = null;
	
	
	@BeforeEach
	void setUp() throws Exception {
				
		position.setDrivable(true);
		destination.setDrivable(true);
		b.setDrivable(true);
		c.setDrivable(true);
		nD.setDrivable(false);

		
		drivablecarRoute.add(position);
		drivablecarRoute.add(b);
		drivablecarRoute.add(c);
		drivablecarRoute.add(destination);
		
		//---------------------------
		notDrivableCarRoute.add(nD);
		notDrivableCarRoute.add(c);
		notDrivableCarRoute.add(c);
		
		
		
		s = new Street("Nussdorfer", drivablecarRoute);
		t = new Street("Nothingness", notDrivableCarRoute);
		
		bTest = new BlockedArea(s, 0, 1);
		cTest = new BlockedArea(t, 2, 3);
		
		
	}

	@Test
	void testRmsController() {
		rmsControllertest = new RmsController();
	}

	@Test
	void testGetCarRoutes() {
			
	}

//	@Test
//	void testGetCarIds() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testTravelProgress() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetStreetNames() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testBlockArea() {
//		fail("Not yet implemented");
//	}

	@Test
	void testGetBlockedStreets() {
		aController = new AreaController();
		
		
		List<BlockedArea> blockedListTest = new ArrayList<BlockedArea>();
		blockedListTest.add(bTest);
		blockedListTest.add(cTest);
		aController.setBlockedAreas(bTest);
		aController.setBlockedAreas(cTest);

		System.out.println(aController.getBlockedAreas().size());
		//Mockito.when(aController.getBlockedAreas()).thenReturn(blockedListTest);
	
	
	
	List<String> blockedStreets = new ArrayList<String>();
	
	for(BlockedArea area: aController.getBlockedAreas()) {
		blockedStreets.add(area.getStreet().getName());
	}
	
	assertEquals(2, blockedStreets.size());
	assertTrue(blockedStreets.contains(aController.getBlockedAreas().get(0).getStreet().getName()));
	assertTrue(blockedStreets.contains(aController.getBlockedAreas().get(1).getStreet().getName()));
	
	}

//	@Test
//	void testUnBlockArea() {
//		fail("Not yet implemented");
//	}

}
