package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import exceptions.ARSException;
import messages.Coordinate;
import messages.TypeOfField;;

/**
 * Class represents a single unit of map
 * 
 * @param coordinate
 */
public class ARSField implements Comparable<ARSField> {

	private Coordinate coordinate;

	private TypeOfField fieldType;

	private Double distanceFromStart;
	private Double predictedDistance;
	private boolean drivable;
	
	/**
	 * Each field has a number of potential directions determined by its position(relationship with other Street ARSFields)
	 * It can be 1 to 4 (on a crossroad e.g.). It is used to determine how many cars can be simultaneously placed on the same ARSField
	 */
	private Set<Direction> directions;
	
	/**
	 * Each Field can contain particular number of ARSCar objects. It depends on its position and relationship with
	 * neighbour fields of type Street- determined by size of Directions set 
	 */
	private List<ARSCar> maxCars;
	private Double cost;

	
	public ARSField(Coordinate coordinate) {

		Coordinate.validateXY(coordinate.getX(), coordinate.getY());

		this.coordinate = coordinate;
		this.fieldType = TypeOfField.GRASS;
		this.drivable = false;
		this.directions = new TreeSet<>();
		this.cost = Double.MAX_VALUE;
		this.maxCars = new ArrayList<>();

	}

	public ARSField(Coordinate coordinate, TypeOfField fieldType, boolean drivable) {
		super();
		this.coordinate = coordinate;
		this.fieldType = fieldType;
		this.drivable = drivable;
		this.directions = new TreeSet<>();
		this.cost = Double.MAX_VALUE;
		this.maxCars = new ArrayList<>();
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public TypeOfField getFieldType() {
		return fieldType;
	}

	public void setFieldType(TypeOfField fieldType) {
		this.fieldType = fieldType;
	}

	public Double getDistanceFromStart() {
		return distanceFromStart;
	}

	public void setDistanceFromStart(Double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}

	public Double getPredictedDistance() {
		return predictedDistance;
	}

	public void setPredictedDistance(Double predictedDistance) {
		this.predictedDistance = predictedDistance;
	}

	public List<ARSCar> getMaxCars() {
		return maxCars;
	}

	public void setMaxCars(List<ARSCar> maxCars) {
		this.maxCars = maxCars;
	}

	public boolean isDrivable() {
		return drivable;
	}

	public Set<Direction> getDirections() {
		return directions;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	/**
	 * This method sets directions on a field based on its' position and relation with neighbour- fields
	 * (with type of Street) 
	 * @param directions
	 */
	public void setDirections(Set<Direction> directions) {

		if (directions == null || directions.size() == 0 || directions.size() > 4)
			throw new IllegalArgumentException("Directions must not be empty, null, more than 4");
		this.directions.addAll(directions);

	}

	/**
	 * Not a classic setter, it also sets cost to Double.POSITIVE_INFINITY if false- which is useful for route calculation
	 * otherwise it sets it to 1  
	 * @param isDrivable
	 */
	public void setDrivable(boolean isDrivable) {

		if (isDrivable)
			cost = (double) 1;
		else {
			cost = Double.POSITIVE_INFINITY;
		}
		drivable = isDrivable;
	}

	public synchronized Double getCost() {

		return cost + maxCars.size();

	}
	
	/**
	 * Metho that places a car on a particular field. If any of condition is not fullfilled, the exception is thrown.
	 * @param car
	 * @throws ARSException
	 */
	public synchronized void placeCar(ARSCar car) throws ARSException {

		if (directions.size() > maxCars.size()) {

			Optional<ARSCar> coptDir = maxCars.stream().filter(c -> c.getDirection().equals(car.getDirection()))
					.findFirst();
			Optional<ARSCar> coptId = maxCars.stream().filter(c -> c.getId() == car.getId()).findFirst();
			Optional<Direction> dirOpt = directions.stream().filter(d -> d.equals(car.getDirection())).findFirst();

			if (coptId.isPresent())
				throw new ARSException("Field exception",
						"Car with id:" + car.getId() + "is already on field " + getCoordinate());

			if (coptDir.isPresent())
				throw new ARSException("Field exception",
						"There is already a car on that field that " + "travels in same direction, please wait");
			if (!dirOpt.isPresent()) {
				for (Direction d : directions) {
					System.out.println(d);
				}

				throw new ARSException("Field exception",
						"Car can't move in this: " + car.getDirection() + "on this field");
			}

			maxCars.add(car);
		} else {
			throw new ARSException("Field exception", "There can be only " + directions.size() + " cars on this field");
		}

	}

	@Override
	public String toString() {
		if (fieldType == TypeOfField.GRASS)
			return ".";
		else if (fieldType == TypeOfField.STREET && this.drivable == true)
			return "S";
		if (fieldType == TypeOfField.STREET && this.drivable == false)
			return "x";
		return "?";

	}
	
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof ARSField)) {
			return false;
		}

		ARSField f = (ARSField) obj;

		if (!this.getCoordinate().equals(f.getCoordinate()))
			return false;

		if (!this.getFieldType().equals(f.getFieldType()))
			return false;

		if (!this.directions.isEmpty() && !f.getDirections().isEmpty()) {
			if (this.getDirections().size() != f.getDirections().size())
				return false;

			for (Direction d : directions) {
				if (!f.getDirections().contains(d))
					return false;
			}
		}

		if (!this.getMaxCars().isEmpty() && !f.getMaxCars().isEmpty()) {
			if (this.getMaxCars().size() != f.getMaxCars().size())
				return false;

			for (ARSCar c : maxCars) {
				Optional<ARSCar> caropt = f.getMaxCars().stream().filter(car -> car.getId() == c.getId()).findFirst();
				if (!caropt.isPresent())
					return false;
			}
		}

		if (this.cost.equals(Double.MAX_VALUE)) {
			return this.cost.compareTo(f.getCost()) == 0;
		}

		if (!this.getCost().equals(f.getCost()))
			return false;

		return true;
	}

	@Override
	public int compareTo(ARSField o) {

		if (this.getCoordinate().getX() > o.getCoordinate().getX())
			return 1;
		if (this.getCoordinate().getX() < o.getCoordinate().getX())
			return -1;
		if (this.getCoordinate().getY() > o.getCoordinate().getY())
			return 1;
		if (this.getCoordinate().getY() < o.getCoordinate().getY())
			return -1;
		return 0;
	}

}
