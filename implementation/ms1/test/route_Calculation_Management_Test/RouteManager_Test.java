package route_Calculation_Management_Test;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import converter.FieldConverter;
import exceptions.ARSException;
import messages.Car;
import messages.Coordinate;
import messages.Field;
import model.ARSField;
import service.MapService;
import service.RouteService;

@RunWith(JUnitPlatform.class)
class RouteManager_Test {

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
		
		MapService ms = new MapService();
		ms.generateMap();
		
		ms.print();
		
		Car car = new Car(1);
		
		Field fp = FieldConverter.convertFrom(ms.getByPosition(new Coordinate(1, 1)));
		Field fd = FieldConverter.convertFrom(ms.getByPosition(new Coordinate(10, 7)));
		
		car.setCarPosition(fp);
		
		car.setCarDestination(fd);
		
		System.out.println(car.getCarPosition().getCoordinate()+" "+ car.getCarPosition().getFieldType());
		System.out.println(car.getCarDestination().getCoordinate()+" "+ car.getCarDestination().getFieldType());
		
		RouteService rs = new RouteService(ms);
		
		List<ARSField>routeArs= rs.calculateRoute(car);
		
		routeArs.forEach(f->System.out.println(f.getCoordinate()+" "+f.getFieldType()));
		
		ARSField fsearch = new ARSField(new Coordinate(8, 3));
		
		int indexCounter = 1;
		
		for(ARSField field:routeArs) {
			if(!field.getCoordinate().equals(fsearch.getCoordinate())) {
				++indexCounter;
				System.out.println(field.getCoordinate());
			}
				
			else 
				break;
		}
		
		System.out.println(routeArs.size()-indexCounter);
		
		System.out.println(routeArs.get(indexCounter-1).getCoordinate());
		
		
		
	}
	
	

}
