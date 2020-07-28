import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Field;
import model.Street;


//
public class MainTest {	
	
	public static int Y= 10;
	public static int X= 10;
	
	public static void main(String[] args) throws MapExeption {
		
		
//		String meString = "qweSq";
//		
//		if(meString.matches("[A-Z][a-z]+"))System.out.println("ok");
//		
//		else System.out.println("not");
	
		MapController mapController = new MapController();
		
		
		
		mapController.createAllGrassFields();
		
		//System.out.println(mapController.getField(4, 0))
		
		mapController.composeStreet("Prva ulica", 1, 1, 8, 1);
		mapController.composeStreet("Druga ulica", 1, 1, 1, 8);
		Object object = new ArrayList<Field>();
		
		
//		mapController.print();
//		
//		mapController.closeStreet("prva ulica", 7,8);
//		
//		System.out.println("\n\n");
//		
//		mapController.print();
		
		
		
		mapController.closeStreet("prva ulica", 7,8);
		
		Street street = mapController.getStreetController().getStreetByName("prva ulica");
		
		System.out.println(street.getFields());
		
		for(Field f: street.getFields())
		System.out.println(f.getCoordinate());
		
	
		for(Field f: street.getFields())
			System.out.println(f.getCoordinate());
		
		
//		for(Field f: mapController.getStreetController().getStreetByName("prva ulica").getFields()) {
//			System.out.println(f.isDrivable());
//		}
//		mapController.print();
//		for(Coordinate c: mapController.getFields().keySet()) {
//			if(c.getY()==1) {
//				System.out.print(c);
//				System.out.println(mapController.getFields().get(c).isDrivable());
//			}
//			
//		}
		
		
	}

}
