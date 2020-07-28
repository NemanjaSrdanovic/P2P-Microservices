package map_Service_Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import constants.ARSConstants;
import exceptions.ARSException;
import messages.Coordinate;
import model.ARSCar;
import model.ARSField;
import service.MapService;

@RunWith(JUnitPlatform.class)
class MapService_TestCase {
	
	private MapService mapService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		mapService =new MapService();
		mapService.generateMap();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test_map_generated_look() throws ARSException {
		
		printDirections(mapService);
		System.out.println("\n\n\n\n\n");
		
		
		
		mapService.print();
	
	}
	@Test
	void test_map_get_by_possition() throws ARSException {
		
		Coordinate toFind = new Coordinate(0, 4);
		ARSField field =mapService.getByPosition(toFind);
		
		assertTrue(field.getCoordinate().equals(toFind));
	
	}
	
	@Test
	void test_map_get_neighbour_fields() throws ARSException {
		
		ARSField field = mapService.getByPosition(new Coordinate(3, 3));
		
		Set<ARSField> neighbourStreet = mapService.getStreetNeighbours(field);
		
		assertTrue(neighbourStreet.size()==4);
	
	}
	
	
//	@Test
//	void test_placeCarOnField() throws ARSException {
//		
//		MapService ms = new MapService();
//		ms.generateMap();
//		
//		ARSCar car = new ARSCar(2);
//		Coordinate currentPos = new Coordinate(3, 1);
//		Coordinate nextPos = new Coordinate(3, 2);
//		
//		ms.placeCarOnField(car, currentPos, nextPos);
//		
//		ARSCar car2 = new ARSCar(1);
//		Coordinate currentPos1 = new Coordinate(3, 1);
//		Coordinate nextPos1 = new Coordinate(2, 1);
//		
//		ms.placeCarOnField(car2, currentPos1, nextPos1);
//		
//		ARSCar car3 = new ARSCar(3);
//		Coordinate currentPos2 = new Coordinate(3, 1);
//		Coordinate nextPos2 = new Coordinate(4, 1);
//		
//		ms.placeCarOnField(car3, currentPos2, nextPos2);
//		
//		ARSCar car4 = new ARSCar(4);
//		Coordinate currentPos3 = new Coordinate(3, 1);
//		Coordinate nextPos3 = new Coordinate(4, 1);
//		
//		
//		
//		assertThrows(ARSException.class,()-> ms.placeCarOnField(car4, currentPos3, nextPos3));
//		
//		
//	}
	
	
	@Test
	void test_Removefrom_fields() throws ARSException {
		
		mapService.generateMap();
		mapService.print();
		ARSCar arsCar = new ARSCar(1);
		
		mapService.getByPosition(new Coordinate(1, 1)).getMaxCars().add(arsCar);
		mapService.getByPosition(new Coordinate(7, 1)).getMaxCars().add(arsCar);
		mapService.getByPosition(new Coordinate(8, 1)).getMaxCars().add(arsCar);
		
		mapService.getByPosition(new Coordinate(1, 2)).getMaxCars().add(arsCar);
		mapService.getByPosition(new Coordinate(3, 3)).getMaxCars().add(arsCar);
		mapService.getByPosition(new Coordinate(5, 5)).getMaxCars().add(arsCar);
		mapService.getByPosition(new Coordinate(7, 7)).getMaxCars().add(arsCar);
		
		
		
		mapService.removeCarFromOtherFields(new ARSCar(1), new ARSField(new Coordinate(1, 1)));
		
		
		for(ARSField f:mapService.getMap().getMap().values()) {
			
			for(ARSCar c:f.getMaxCars()) {
				
				if(c.getId()==arsCar.getId())
					System.out.println(f.getCoordinate());
				
			}
			
		}
		
		
	}

	public void printDirections(MapService mapService) {
		
		for (int j = 0; j < ARSConstants.maxY; ++j) {
			for (int i = 0; i < ARSConstants.maxX; ++i) {
				try {
					if(mapService.getField(i, j).isPresent()) {
						ARSField f = mapService.getField(i, j).get();
						System.out.print(f.getCoordinate()+" "+f.getDirections() + " ");
					}
					
				} catch (ARSException e) {
					e.printStackTrace();
				}
			}

			System.out.println();
		}
	}
	


}
