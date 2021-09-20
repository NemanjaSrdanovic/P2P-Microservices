package route_service_test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import model.ARSField;
import service.MapService;
import service.RouteService;

@RunWith(JUnitPlatform.class)
class Route_Service_Test {
	RouteService routeService;
	MapService mapService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		routeService = new RouteService();
		mapService = new MapService();
		mapService.generateMap();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test_distanceBeweenTwoPoints() {
		
		assertTrue(routeService.distanceBetweenTwoPints(new Coordinate(3,4), new Coordinate(7, 1))==5);
	}
	
	@Test
	void test_positive_infinity() {
		
		long size=mapService.getFields().values().stream().filter(f->f.getFieldType().equals(TypeOfField.STREET)).count();
		
		Map<ARSField,Double> infiniteMap = routeService.initializeAllStreetFieldsToPosInfinity(mapService.getFields());
		
		assertTrue(size==infiniteMap.size());
	}
	

	
	@Test
	public void test_route() throws ARSException {
		
		RouteService r1 = new RouteService(mapService);
		
		mapService.getByPosition(new Coordinate(1, 3)).setDrivable(false);
		
		
		mapService.print();
		
		
		List<ARSField> path = r1.routeCalc(new Coordinate(1, 1), new Coordinate(1, 4));
		
		assertTrue(path.size()==13);
		System.out.println(path.size());
		
	
	}

}
