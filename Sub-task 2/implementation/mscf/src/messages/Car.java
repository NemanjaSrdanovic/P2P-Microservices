package messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import messages.Field;

/**
* The Car class is...
*
* @author Nemanja Srdanovic
* @version 1.0
* @since   2020-04-22 
*/
public class Car implements Serializable {
	
	/*-------------------------------------VARIABLES-------------------------------------*/
	private static final long serialVersionUID = 1L;
	private int carId;
	private Field carPosition;
	private Field carDestination;
	private int traveledFieldsperRoute;
	private List<Field> carRoute = new CopyOnWriteArrayList<Field>();
	

	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/
	public Car(int Id) {
		this.carId=Id;
		this.carRoute=null;
	}
	
	/*------------------------------------- GETTERS -------------------------------------*/

	/**/
	public int getCarId() {
		return carId;
	}

	/**/
	public Field getCarPosition() {
		return carPosition;
	}

	/**/
	public Field getCarDestination() {
		return carDestination;
	}

	/**/
	public synchronized List<Field> getCarRoute() {
		return carRoute;
	}

	/**/
	public int getTraveledFieldsPerRoute() {
		
		return traveledFieldsperRoute;
	}
	
	
	/*------------------------------------- SETTERS -------------------------------------*/
	
	/**/
	public void setTraveledFieldsPerRoute(int num) {
		
		this.traveledFieldsperRoute=num;
	}
	
	/**/
	public void setCarId(int carId) {
		this.carId = carId;
	}

	/**/
	public void setCarPosition(Field carPosition) {
		if(carPosition==null) throw new IllegalArgumentException("The car position for the car"+this.getCarId()+"can�t be null!");
		this.carPosition = carPosition;
	}
	
	/**/
	public void setCarDestination(Field destination) {
		if(destination==null) throw new IllegalArgumentException("The car destination for the car"+this.getCarId()+"can�t be null!");
		this.carDestination = destination;
	}
	
	/**/
	public void setCarRoute(List<Field> carRoute) {
		this.carRoute = carRoute;
	}
	
}
