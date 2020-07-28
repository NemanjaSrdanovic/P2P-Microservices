package service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import constants.ARSConstants;
import exceptions.ARSException;
import messages.Coordinate;
import messages.TypeOfField;
import model.ARSCar;
import model.ARSField;
import model.ARSMap;
import model.ARSStreet;
import model.Direction;

/**
 * Class used for communication with ARSMap and the rest of the system. It holds ARSMap and 
 * street controller
 * @author nenad.cikojevic
 *
 */
public class MapService {
	
	private ARSMap map;
	private ARSField field;
	private StreetService streetController;
	

	
	public MapService() {
		this.map = new ARSMap();
		this.streetController = new StreetService(this.map);
		
	}
	

	public ARSMap getMap() {
		return map;
	}



	public void setMap(ARSMap map) {
		this.map = map;
	}



	public StreetService getStreetController() {
		return streetController;
	}



	public void setStreetController(StreetService streetController) {
		this.streetController = streetController;
	}


	/**
	 * Method returns Optional<ARSField> from ARSMap by passed values of int X and int Y
	 * @param x int
	 * @param y int
	 * @return 
	 * @throws ARSException
	 */
	public Optional<ARSField> getField(int x, int y) throws ARSException {

		Coordinate.validateXY(x, y);

		Coordinate coordinate = new Coordinate(x, y);
		for (Coordinate c : map.getMap().keySet()) {
			if (c.equals(coordinate))
				return Optional.of(map.getMap().get(c));
		}
		return Optional.ofNullable(null);

	}
	
	public HashMap<Coordinate,ARSField> getFields(){
		return map.getMap();
	}
	
	/**
	 * Method creates Street object by given game and start and end values for position and
	 * places them into a list and alignes them by position. It is a encapsulation of 
	 * smaller methods for creating a street
	 * 
	 * @param name String
	 * @param startX int
	 * @param startY int 
	 * @param endX int
	 * @param endY int
	 */
	public void composeStreet(String name, int startX, int startY, int endX, int endY) {
		
		Coordinate.validateXY(startX, startY);
		Coordinate.validateXY(endX, endY);
		
		List<ARSField>streetFields = new ArrayList<>();
		
		for(Coordinate c:map.getMap().keySet()) {
			if(c.getY()==startY && c.getY()== endY) {
				createHorizontalStreetFieldList(startX, startY, endX, endY, c, streetFields);

			}
			if (c.getX() == startX && c.getX() == endX) {

				createVerticalStreetFieldList(startX, startY, endX, endY, c, streetFields);


			}

		}
		Collections.sort(streetFields);
		ARSStreet street = new ARSStreet(name, streetFields);
		this.map.getStreets().add(street);
		
	
	}
	
	/**
	 * Method provides creating a Street object of ARSFields horizontal and vertical field
	 * @param name
	 * @param startX1
	 * @param startY1
	 * @param endX1
	 * @param endY1
	 * @param startX2
	 * @param startY2
	 * @param endX2
	 * @param endY2
	 */
	public void composeStreet(String name, int startX1, int startY1, int endX1, int endY1, int startX2, int startY2, int endX2, int endY2) {
		
		composeStreet( name, startX1, startY1, endX1, endY1);
		composeStreet( name, startX2, startY2, endX2, endY2);
		
	}
	
	/**
	 * Method encapsulates methods for creating vertical street
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param c
	 * @param streetFields
	 */
	public void createVerticalStreetFieldList(int startX, int startY, int endX, int endY, Coordinate c,List<ARSField>streetFields) {
		if(startY>endY) {
			createVerticalSN(startX,startY,endX,endY,c,streetFields);
		}
		else if (startY<endY) {
			createVerticalNS(startX, startY, endX, endY, c, streetFields);
		}
		else {
			createSingleFieldStreet(startX, startY, endX, endY, c, streetFields);
		}
	}
	
	/**
	 * Method encapsulates methods for creating horizontal street
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param c
	 * @param streetFields
	 */
	private void createHorizontalStreetFieldList(int startX, int startY, int endX, int endY, Coordinate c,List<ARSField>streetFields) {
		
		if(startX>endX) {
			createHorizontalEW(startX,startY,endX,endY,c,streetFields);
		}
		else if (startX<endX) {
			createHorizontalWE(startX, startY, endX, endY, c, streetFields);
		}
		else {
			createSingleFieldStreet(startX, startY, endX, endY, c, streetFields);
		}
	}
	
	/**
	 * Methods alignes list of fields in West- East direction 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param c
	 * @param streetFields
	 */
	private void createHorizontalWE(int startX, int startY, int endX, int endY, Coordinate c,List<ARSField> streetFields) {

		if ((c.getX() >= startX && c.getX() <= endX)) {
			setTypeAddToStreetList(c, streetFields);
		}
	}
	
	/**
	 * Methods alignes list of fields in East- West direction 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param c
	 * @param streetFields
	 */
	private void createHorizontalEW(int startX, int startY, int endX, int endY, Coordinate c,List<ARSField>streetFields) {
		if((c.getX()<=startX && c.getX()>=endX)) {
			setTypeAddToStreetList(c, streetFields);
		}
	}
	
	/**
	 * Methods alignes list of fields in South- North direction 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param c
	 * @param streetFields
	 */
	private void createVerticalSN(int startX, int startY, int endX, int endY, Coordinate c,List<ARSField>street) {
		if((c.getY()<=startY && c.getY()>=endY)) {
			setTypeAddToStreetList(c, street);
		}
	}
	
	/**
	 * Methods alignes list of fields in North- South direction 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param c
	 * @param streetFields
	 */
	private void createVerticalNS(int startX, int startY, int endX, int endY, Coordinate c,List<ARSField>street) {
		if((c.getY()>=startY && c.getY()<=endY)) {
			setTypeAddToStreetList(c, street);
		}
	}
	
	/**
	 * Methods creates one field Street
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param c
	 * @param streetFields
	 */
	private void createSingleFieldStreet(int startX, int startY, int endX, int endY, Coordinate c,List<ARSField> street) {
		if(c.getX()==startX && c.getY()==startY && c.getX() ==endX && c.getY()==endY)
			setTypeAddToStreetList(c, street);
	}

	
	/**
	 * Method sets type of each field to Street, cost to 1, drivable to true and sets and list of max number of cars 
	 * that can be placed on it at the same time
	 * @param c
	 * @param street
	 */
	private void setTypeAddToStreetList(Coordinate c, List<ARSField> street) {
		
		field = map.getMap().get(c);
		field.setFieldType(TypeOfField.STREET);
		field.setDrivable(true);
		field.setCost((double)1);
		field.setMaxCars(new ArrayList<ARSCar>());
		street.add(field);
	}

	/**
	 * Visualisation to ensure that the whole map looks the way it should
	 * @throws ARSException
	 */
	public void print() throws ARSException {
		for (int j = 0; j < ARSConstants.maxY; ++j) {
			for (int i = 0; i < ARSConstants.maxX; ++i) {
				if(getField(i, j).isPresent())
					System.out.print(getField(i, j).get() + " ");
			}

			System.out.println();
		}
	}
	
	/**
	 * Creates a map of only Grass type fields at the beginning
	 */
	public void createJustGrassMap() {
		
		for(int j = 0;j<ARSConstants.maxY;++j) {
			for(int i=0;i<ARSConstants.maxX;++i)
				this.map.setField(i, j);
		}
	}
	
	/**
	 * Method is used to change isDrivable variable of ARSField for given ARSStreet
	 * if the street exists on the map
	 * @param street
	 * @throws ARSException
	 */
	public void blockStreet(ARSStreet street) throws ARSException {
		
		if(!getStreetController().doesExistInMap(street.getName()))	
			throw new ARSException("ARSException", "Street "+ street.getName()+" doesn not exist");
		
		for(ARSField f: street.getFields()) {
			getByPosition(f.getCoordinate()).setDrivable(f.isDrivable());
		}
	}
	
	/**
	 * Method sets all possible directions for moving for each field of type Street on the map
	 * It is based on its relationship (position) with other neighbours of type Street
	 */
	private void setDirections() {
		Optional<ARSField> f1opt = Optional.ofNullable(null);
		Optional<ARSField> f2opt = Optional.ofNullable(null);
		Optional<ARSField> f3opt = Optional.ofNullable(null);
		Optional<ARSField> f4opt = Optional.ofNullable(null);
		
		Set<Direction>directions; 
		
		for(ARSField f: map.getMap().values()) {
			if(f.getFieldType().equals(TypeOfField.GRASS))
				continue;
			
			directions = new TreeSet<Direction>(); 
			try {
				f1opt = getField(f.getCoordinate().getX(), f.getCoordinate().getY()+1);
			}catch (Exception e) {
			}
			try {
				f2opt = getField(f.getCoordinate().getX(), f.getCoordinate().getY()-1);
			} catch (Exception e) {
			}
			try {
				f3opt = getField(f.getCoordinate().getX()+1, f.getCoordinate().getY());
			} catch (Exception e) {
			}
			try {
				f4opt = getField(f.getCoordinate().getX()-1, f.getCoordinate().getY());
			} catch (Exception e) {
			}
			
			if(f1opt.isPresent() && f1opt.get().getFieldType().equals(TypeOfField.STREET)) 
				directions.add(Direction.SOUTH);
			
			if(f2opt.isPresent()&& f2opt.get().getFieldType().equals(TypeOfField.STREET))
				directions.add(Direction.NORTH);
			
			if(f3opt.isPresent()&& f3opt.get().getFieldType().equals(TypeOfField.STREET)) 
				directions.add(Direction.EAST);
				
			if(f4opt.isPresent()&& f4opt.get().getFieldType().equals(TypeOfField.STREET)) 
				directions.add(Direction.WEST);
		
				
			f.setDirections(directions);

		}
		
	}
	
	/**
	 * Method returns field from map for a given Coordinate object (position)
	 * @param coordinate
	 * @return
	 * @throws ARSException
	 */
	public ARSField getByPosition(Coordinate coordinate) throws ARSException {
		
		 Optional<Coordinate> copt = map.getMap().keySet().stream().filter(c->c.equals(coordinate)).findFirst();
		 
		 if(copt.isPresent())
			 return map.getMap().get(copt.get());
		 throw new ARSException("No field exception", "There is no field with this position");
	}
	
	/**
	 * Method returns set of neighbour fields of type Street for given field 
	 * @param field
	 * @return
	 * @throws ARSException
	 */
	public Set<ARSField> getStreetNeighbours(ARSField field) throws ARSException{
		
		if(field==null || field.getFieldType().equals(TypeOfField.GRASS)) {
			throw new ARSException("Illegal field", "This field is either null or of type GRASS");
		}
		
		Set<ARSField> neighbours = new HashSet<>();
		
		ARSField neigh1 = null;
		ARSField neigh2 = null;
		ARSField neigh3 = null;
		ARSField neigh4 = null;
		
		try {
			neigh1 = getByPosition(new Coordinate(field.getCoordinate().getX()+1,field.getCoordinate().getY()));
			if(neigh1.getFieldType().equals(TypeOfField.STREET))
				neighbours.add(neigh1);
		} catch (Exception e) {}
		
		try {
			neigh2 = getByPosition(new Coordinate(field.getCoordinate().getX()-1,field.getCoordinate().getY()));
			if(neigh2.getFieldType().equals(TypeOfField.STREET))
				neighbours.add(neigh2);
		} catch (Exception e) {}
		
		try {
			neigh3 = getByPosition(new Coordinate(field.getCoordinate().getX(),field.getCoordinate().getY()+1));
			if(neigh3.getFieldType().equals(TypeOfField.STREET))
				neighbours.add(neigh3);
		} catch (Exception e) {}
		
		try {
			neigh4 = getByPosition(new Coordinate(field.getCoordinate().getX(),field.getCoordinate().getY()-1));
			if(neigh4.getFieldType().equals(TypeOfField.STREET))
				neighbours.add(neigh4);
		} catch (Exception e) {}
		
		return neighbours;	
	}
	
	/**
	 * Method is used for updating the map. It placed car on current field and removes from previously placed
	 * @param car
	 * @param current
	 * @param route
	 * @throws ARSException
	 */
	public synchronized void placeCarOnField(ARSCar car, ARSField current, List<ARSField> route) throws ARSException {
		
		ARSField currentOnMap = getByPosition(current.getCoordinate());
//		ARSField prevOnMap = getByPosition(car.getCarPrevPosition().getCoordinate());
		
		ARSField next = getByPosition(route.get(0).getCoordinate());
		Direction carDirection;
		
		if(currentOnMap.getFieldType().equals(TypeOfField.GRASS) || next.getFieldType().equals(TypeOfField.GRASS))
			throw new ARSException("Map Service Error", "One of fields next or current is of type grass");
		if(!currentOnMap.isDrivable() )//|| !next.isDrivable())
			throw new ARSException("Map Service Error", "current is not drivable");
		
		if(currentOnMap.getCoordinate().getX()+1==next.getCoordinate().getX())
			carDirection = Direction.EAST;
		else if(currentOnMap.getCoordinate().getX()-1==next.getCoordinate().getX())
			carDirection = Direction.WEST;
		else if(currentOnMap.getCoordinate().getY()+1==next.getCoordinate().getY())
			carDirection = Direction.SOUTH;
		else if(currentOnMap.getCoordinate().getY()-1==next.getCoordinate().getY())
			carDirection = Direction.NORTH;
		else 
			throw new ARSException("Map Service Error", "Cant place car from" +currentOnMap.getCoordinate() + " type: "+
					currentOnMap.getFieldType()+ " to:"+next.getCoordinate()+" type: "+next.getFieldType());
	
		car.setDirection(carDirection);
		
//		if(!currentOnMap.equals(prevOnMap)) {
//			System.out.println("+++++++++ not equal +++++++++++");
//			OptionalInt indexOpt = IntStream.range(0, prevOnMap.getMaxCars().size())
//				     .filter(i->car.getId()==car.getId())
//				     .findFirst();
//			System.out.println("before");
//			prevOnMap.getMaxCars().forEach(System.out::println);
//			if(!indexOpt.isPresent())
//				throw new ARSException("Map Service Error", "Cant place car from" +currentOnMap.getCoordinate() + " type: "+
//						currentOnMap.getFieldType()+ " to:"+next.getCoordinate()+" can't find car on previous field");
//			prevOnMap.getMaxCars().remove(indexOpt.getAsInt());
//			
//			System.out.println("after");
//			System.out.println("Removed car from :"+prevOnMap.getCoordinate());
//			prevOnMap.getMaxCars().forEach(System.out::println);
//			
//		}
//		else {
//			if(current.getMaxCars().stream().filter(c->c.getId()==car.getId()).findFirst().isPresent()) {
//				System.out.println("****** equal current and previous fields, and car is on current allready, can't place it again ******");
//				return;
//			}
//			System.out.println("****** equal current and previous fields ******");
//		}
		
		removeCarFromOtherFields(car,current);

		
		currentOnMap.placeCar(car);
		
	}
	
	/**
	 * Help method for placing a car on a field- goes through map and removes from other fields
	 * @param car
	 * @param current
	 */
	public void removeCarFromOtherFields(ARSCar car, ARSField current) {
		
		for(ARSField f: map.getMap().values()){
			
			if(!f.getCoordinate().equals(current.getCoordinate())
					&& f.getFieldType().equals(TypeOfField.STREET)) {
				
				
				List<ARSCar> cars = f.getMaxCars();
				if(!cars.isEmpty()) {
					for(int i=0;i<cars.size();++i) {
						
						if(cars.get(i).getId()==car.getId()) {
							f.getMaxCars().remove(i);
						}
					
					}
				}
			}
		}
	}
	
	/**
	 * Encapsulates all methods for generating map 
	 */
	public void generateMap() {
		
		createJustGrassMap();
		
		composeStreet("High_Street", 1, 1, 13, 1);
		composeStreet("2nd_Avenue", 1, 3, 13, 3);
		composeStreet("Walnut_Street", 1, 7, 13, 7);
		composeStreet("Maple_Street", 1, 11, 13, 11);
		composeStreet("Dogwood_Lane", 1, 13, 13, 13);
		
		composeStreet("Berkshire_Drive", 1, 1, 1, 13);
		composeStreet("Route_32", 3, 1, 3, 13);
		composeStreet("Durham_Road", 5, 3, 5, 11);
		composeStreet("4th_Treet", 7, 1, 7, 13);
		composeStreet("Heather_Lane", 9, 3, 9, 11);
		composeStreet("Pine_Street", 11, 1, 11, 13);
		composeStreet("Oak_Street", 13, 1, 13, 13);
		
		setDirections();
	}
	
}
