package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import messages.Coordinate;

/**
 * This Class holds Collections of single units (ARSFields) as a list Street Objects (comprised only of ARSFields of type Street) 
 * and a HashMap of all ARSFields- of type Grass and Streets. Both are referenced on each other when map is generated.   
 * @author nenad.cikojevic
 *
 */
public class ARSMap {
	
	private HashMap<Coordinate, ARSField> map;
	
	private List<ARSStreet> streets;
	
	
	public ARSMap() {
		map = new HashMap<>();
		this.streets = new ArrayList<>();
	}
	
	
	
	public HashMap<Coordinate, ARSField> getMap() {
		return map;
	}



	public void setMap(HashMap<Coordinate, ARSField> map) {
		this.map = map;
	}



	public List<ARSStreet> getStreets() {
		return streets;
	}



	public void setStreets(List<ARSStreet> streets) {
		this.streets = streets;
	}


	/**
	 * Creates ARSField object and puts it into HashMap collection
	 * @param x
	 * @param y
	 */
	public void setField(int x, int y) {
		
		Coordinate coordinate = new Coordinate(x, y);
		ARSField field = new ARSField(coordinate);
		map.put(coordinate, field);
	}
	

	
}
