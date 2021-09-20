package model;

import java.util.ArrayList;
import java.util.List;

/**
 * MS1_ARS version of Car data from Network
 * 
 * @author nenad.cikojevic
 *
 */
public class ARSCar{
	
	/**
	 * Unique ID of a vehicle
	 */
	private int id;
	
	/**
	 * Unique address of a concrete car used to inform it about updated map 
	 */
	private String ipPort;
	
	/**
	 * ARSField instance which represents current position on map. 
	 */
	private ARSField carPosition;
	
	/**
	 * ARSField instance which represents previous position on map. 
	 */
	private ARSField carPrevPosition;
	
	/**
	 * ARSField instance which represents destination on map. 
	 */
	private ARSField carDestination;
	
	/**
	 * List of ARSField objects that represent route
	 */
	private List<ARSField> carRoute = new ArrayList<ARSField>();

	/**
	 * Represents direction of movement based on current position and route
	 */
	private Direction direction;

	public ARSCar(int id) {
		if (id < 0)
			throw new IllegalArgumentException("id of car must not be negative");
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ARSField getCarPosition() {
		return carPosition;
	}

	public void setCarPosition(ARSField carPosition) {
		this.carPosition = carPosition;
	}

	public ARSField getCarPrevPosition() {
		return carPrevPosition;
	}

	public void setCarPrevPosition(ARSField carPrevPosition) {
		this.carPrevPosition = carPrevPosition;
	}

	public ARSField getCarDestination() {
		return carDestination;
	}

	public void setCarDestination(ARSField carDestination) {
		this.carDestination = carDestination;
	}

	public List<ARSField> getCarRoute() {
		return carRoute;
	}

	public void setCarRoute(List<ARSField> carRoute) {
		this.carRoute = carRoute;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
	
	


}
