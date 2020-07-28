package service;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exceptions.ARSException;
import model.ARSField;
import model.ARSMap;
import model.ARSStreet;

/**
 * Class communicates directly with ASRStreet objects
 * @author nenad.cikojevic
 *
 */
public class StreetService {
	
	private ARSMap map;
	
	public StreetService(ARSMap map) {
		
		this.map = map;
	}
	
	/**
	 * Method checks if the street exists in the map
	 * @param name
	 * @return true if exists
	 * @throws ARSException if there is no street in the map
	 */
	public boolean doesExistInMap(String name) throws ARSException {
		
		if(name==null||name.isEmpty())throw new IllegalArgumentException("Sreet name null or empty");
		
		List<ARSStreet> streets = map.getStreets();
		
		for(ARSStreet s: streets) {
			if(s.getName().equalsIgnoreCase(name))
				return true;
		}
		throw new ARSException("Street name ERROR", "There is no street "+name);
		
	}
	
	/**
	 * Method orders ARSFields by position
	 * @param street
	 */
	public void sortStreetFieldsByCoordinate(ARSStreet street) {

		Collections.sort(street.getFields(), new Comparator<ARSField>() {
			@Override
			public int compare(ARSField f1, ARSField f2) {
				int result = Integer.compare(f1.getCoordinate().getY(), f2.getCoordinate().getY());
				if (result==0)
					result = Integer.compare(f1.getCoordinate().getY(), f2.getCoordinate().getY());
				return result;
			}
			
		});
	}
	
}
