package model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class Field {
	/**
	 * 
	 */
	@Autowired
	private Coordinate coordinate;
	
	@Autowired
	private TypeOfField fieldType;
	 
	@Autowired
	private boolean drivable;

	
	public Field(Coordinate coordinate) {
		
		this.coordinate = coordinate;
		this.fieldType = TypeOfField.GRASS;
		this.drivable = false;
		
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}


	public TypeOfField getFieldType() {
		return fieldType;
	}

	@Bean
	public void setFieldType(TypeOfField fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isDrivable() {
		return drivable;
	}

	@Bean
	public void setDrivable(boolean drivable) {
		this.drivable = drivable;
	}

	@Override
	public String toString() {
			if(this.drivable==true) 
			return "D "+ this.getCoordinate().toString();
			
				return "N "+ this.getCoordinate().toString();
	
		
	}
	
	
}
