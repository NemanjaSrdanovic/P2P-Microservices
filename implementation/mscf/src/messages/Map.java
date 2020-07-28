package messages;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Coordinate, Field> map;
	private List<Street> streets;
	private List<Field> drivableFields;
	
	
	public Map() {
		map = new HashMap<>();
		this.streets = new ArrayList<Street>();
		this.drivableFields= new ArrayList<Field>();
	}
	
	public void setField(int x, int y) {
		
		Coordinate coordinate = new Coordinate(x, y);
		Field field = new Field(coordinate);
		map.put(coordinate, field);
	}
	
	public HashMap<Coordinate,Field> getFields() {
		return map;
	}

	public HashMap<Coordinate, Field> getMap() {
		return map;
	}

	public void setMap(HashMap<Coordinate, Field> map) {
		this.map = map;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public void setStreets(List<Street> streets) {
		this.streets = streets;
	}
	
	public void setDrivableFields(List<Field> fieldsInput) {
		
		this.drivableFields=fieldsInput;
	}
	
	public List<Field> getDrivableFields(){
		
		List<Field> helper = new ArrayList<Field>();
		
		for(Street street: getStreets() ) {
			
			for(Field field: street.getFields()) {

				if(field.isDrivable()) {
					helper.add(field);
				}
			}
		}
		
		setDrivableFields(helper);
		
		return drivableFields;
	}
}
