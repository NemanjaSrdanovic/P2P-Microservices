package messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Field implements Serializable, Comparable<Field>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Coordinate coordinate;
	private TypeOfField fieldType;
	private boolean drivable;

	
	public Field(Coordinate coordinate) {
		
		Coordinate.validateXY(coordinate.getX(), coordinate.getY());
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

	public void setFieldType(TypeOfField fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isDrivable() {
		return drivable;
	}

	public void setDrivable(boolean drivable) {
		this.drivable = drivable;
	}

	@Override
	public String toString() {
		if(fieldType==TypeOfField.GRASS)
		return "."+ this.getCoordinate().toString();
		else 
			if(fieldType==TypeOfField.STREET && this.drivable==true) 
			return "S"+ this.getCoordinate().toString();
			if (fieldType==TypeOfField.STREET && this.drivable==false) 
				return "x"+ this.getCoordinate().toString();
		return "?";
		
	}

	@Override
	public int compareTo(Field o) {
		
		if(this.getCoordinate().getX()>o.getCoordinate().getX())
			return 1;
		if(this.getCoordinate().getX()<o.getCoordinate().getX())
			return -1;
		if(this.getCoordinate().getY()>o.getCoordinate().getY())
			return 1;
		if(this.getCoordinate().getY()<o.getCoordinate().getY())
			return -1;
		return 0;
	}
	
	
}
