package validator;

import exceptions.ARSException;
import messages.Car;
import messages.Coordinate;
import messages.Field;
import messages.Street;
import messages.TypeOfField;

/**
 * Class is used to validate all the received objects from network according to previously defined conditions.
 * @author nenad.cikojevic
 *
 */
public class Validator {
	
	/**
	 * Coordinate validation
	 * @param coordObj
	 * @return true is it is valid
	 * @throws ARSException is some of conditions are not fulfilled
	 */
	public static boolean isCoordinateValid(Object coordObj) throws ARSException {
		try {
			if(coordObj==null)
				throw new ARSException("ARS Exception,Coordinate error", "Coordinate must not be null");
			if(!(coordObj instanceof Coordinate))
				throw new ARSException("ARS Exception,Coordinate error", "Coordinate must be instance of Coordinate");
			
			Coordinate c=(Coordinate)coordObj;
			Coordinate.validateXY(c.getX(), c.getY());
			return true;
		} catch (IllegalArgumentException e) {
			throw new ARSException("ARS Exception,Coordinate error", "Coordinate must BeanContext between 0 and 15");
		}
		
	}
	
	/**
	 * Car validation
	 * @param carObj
	 * @return true is it is valid
	 * @throws ARSException is some of conditions are not fulfilled
	 */
	public static boolean isCarValid(Object carObj) throws ARSException {
		
		if(carObj==null)
			throw new ARSException("ARS Exception,Car error", "Car must not be null");
		if(!(carObj instanceof Car))
			throw new ARSException("ARS Exception,Car error", "Car must be instance of car");
		Car car=(Car)carObj;
		if(car.getCarId()<0)
			throw new ARSException("ARS Exception,Car error", "Negative ID is not allowed");
		
		if(car.getCarPosition()!=null) {
			isFieldValid(car.getCarPosition());
			if(!car.getCarPosition().getFieldType().equals(TypeOfField.STREET))
				throw new ARSException("ARS Exception,Car error", "Car Position is invalid and typeOfGrass");
		}
			
		if(car.getCarDestination()!=null) {
			isFieldValid(car.getCarDestination());
			if(!car.getCarDestination().getFieldType().equals(TypeOfField.STREET))
				throw new ARSException("ARS Exception,Car error", "Car Destination is invalid and typeOfGrass");
		}
		
		return true;
	}
	
	/**
	 * Field validation
	 * @param fieldObj
	 * @return true is it is valid
	 * @throws ARSException is some of conditions are not fulfilled
	 */
	public static boolean isFieldValid(Object fieldObj) throws ARSException {
		
		if(fieldObj==null)
			throw new ARSException("ARS Exception,Field error", "Field must not be null");
		if(!(fieldObj instanceof Field))
			throw new ARSException("ARS Exception,Field error", "Field must be instance of field");
		
		Field field=(Field)fieldObj;
		
		isCoordinateValid(field.getCoordinate());
			
		isFieldTypeValid(field.getFieldType());
		
		return true;
	}
	/**
	 * TypeOfField validation
	 * @param ftObject
	 * @return true is it is valid
	 * @throws ARSException is some of conditions are not fulfilled
	 */
	public static boolean isFieldTypeValid(Object ftObject) throws ARSException {
		
		if(ftObject==null)
			throw new ARSException("ARS Exception, Fieldtype error", "Fieldtype must not be null");
		if(!(ftObject instanceof TypeOfField))
			throw new ARSException("ARS Exception,Fieldtype error", "Fieldtype must be instance of TypeOfField");
		return true;
	}
	
	/**
	 * Street validation
	 * @param sObject
	 * @return true is it is valid
	 * @throws ARSException is some of conditions are not fulfilled
	 */
	public static boolean isStreetValid(Object sObject) throws ARSException {
		
		if(sObject==null)
			throw new ARSException("ARS Exception, street error", "Street must not be null");
		if(!(sObject instanceof Street))
			throw new ARSException("ARS Exception,street error", "Street must be instance of Street");
		Street street=(Street)sObject;
		for(Field f:street.getFields()) {
			isFieldValid(f);
		}
		return true;
	}
}
