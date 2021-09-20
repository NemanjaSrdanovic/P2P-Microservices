package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import converter.FieldConverter;
import exceptions.ARSException;
import messages.Car;
import messages.Coordinate;
import messages.TypeOfField;
import model.ARSField;

/**
 * Class used for route calculation
 * @author nenad.cikojevic
 *
 */
public class RouteService {

	private MapService mapService;
	
	public RouteService() {}
	
	public RouteService(MapService mapService) {
		this.mapService = mapService;
	}
	
	/**
	 * Method is called out of this class and encapsulates all the methods for route calculation 
	 * It takes car current position and destination Fields from which it unpacks Coordinate 
	 * Objects for calculation purpose
	 * @param car
	 * @return
	 * @throws ARSException
	 */
	public List<ARSField> calculateRoute(Car car) throws ARSException {

		ARSField start = FieldConverter.convertFrom(car.getCarPosition());
		ARSField dest = FieldConverter.convertFrom(car.getCarDestination());

		return routeCalc(start.getCoordinate(), dest.getCoordinate());
	}
	
	/**
	 * Implementation of an AStar algorithm customized for the needs of MS1_ARS and the assignment.
	 * 
	 * It uses a physical distance in the coordinate system of two points as a base for a path calculation.
	 * 
	 * source: https://dzenanhamzic.com/2016/12/16/a-star-a-algorithm-implementation-in-java/
	 * 
	 * @param startpos
	 * @param goalpos
	 * @return
	 * @throws ARSException
	 */
	public synchronized List<ARSField> routeCalc(Coordinate startpos, Coordinate goalpos) throws ARSException {

		ARSField start = mapService.getByPosition(startpos);
		ARSField goal = mapService.getByPosition(goalpos);
		
		HashMap<ARSField, ARSField> parentMap = new HashMap<>();
		Set<ARSField> visited = new HashSet<>();
		Map<ARSField, Double> distances = initializeAllStreetFieldsToPosInfinity(mapService.getMap().getMap());
		
		Queue<ARSField> priorityQueue = new PriorityQueue();

		start.setDistanceFromStart((double) 0);
		distances.put(start, (double) 0);
		priorityQueue.add(start);
		ARSField current = null;

		while (!priorityQueue.isEmpty()) {
			
			current = priorityQueue.remove();
			
			if (!visited.contains(current)) {
				visited.add(current);
				
				if (current.equals(goal)) {
					return reconstructPath(parentMap, start, goal, 0);
				}
				
				Set<ARSField> neighbours = mapService.getStreetNeighbours(current);
				for (ARSField neighbour : neighbours) {
					
					if (!visited.contains(neighbour)) {

						double predictedDistance = distanceBetweenTwoPints(neighbour.getCoordinate(),
								goal.getCoordinate());
						
						double neighborDistance = neighbour.getCost();
						double currentDistFromStart = current.getDistanceFromStart();
						
						double totalDistance = currentDistFromStart + neighborDistance + predictedDistance;
							
						if (totalDistance < distances.get(neighbour)) {

							distances.put(neighbour, totalDistance);
		
							neighbour.setDistanceFromStart(totalDistance);
							neighbour.setPredictedDistance(predictedDistance);
		
							parentMap.put(neighbour, current);
			
							priorityQueue.add(neighbour);
						}
					}
				}
			}
		}

		return null;
	}
	
	/**
	 * Method reconstructs given map and returns list that represent path, with start, goal and first index (0) that places 
	 * found next field- from the goal to the start
	 * @param parentMap
	 * @param start
	 * @param goal
	 * @param firstIndex
	 * @return
	 * @throws ARSException
	 */
	public List<ARSField> reconstructPath(Map<ARSField,ARSField> parentMap, ARSField start, ARSField goal, int firstIndex) throws ARSException{
		
		
		List<ARSField> pathToGoal = new ArrayList<>();
		ARSField helpNextPointer = null;
		ARSField helpNext1Pointer = null;
		
		pathToGoal.add(firstIndex, goal);
		
		helpNextPointer = getPrev(parentMap, goal);
		
		while(!helpNextPointer.equals(start)) {
			pathToGoal.add(firstIndex,helpNextPointer);
			helpNext1Pointer = getPrev(parentMap, helpNextPointer);
			
			helpNextPointer = helpNext1Pointer;
			
		}
		
		return pathToGoal;
		
		
		
	}
	
	/**
	 * Method returns previous ARSField of the route in order to sort all fields the in according order to move on
	 * @param parentMap
	 * @param last
	 * @return
	 * @throws ARSException
	 */
	private ARSField getPrev(Map<ARSField,ARSField> parentMap, ARSField last) throws ARSException {
		for (Entry<ARSField, ARSField> entry : parentMap.entrySet()) {
			if(entry.getKey().equals(last)) {
				return entry.getValue();
			}	
		}
		throw new ARSException("Route search", "Can't find previous");
		
	}
	
	/**
	 * Mathematical calculation of distance from a to b based on given coordinate
	 * @param a
	 * @param b
	 * @return
	 */
	public double distanceBetweenTwoPints(Coordinate a, Coordinate b) {

		return Math.sqrt((Math.pow((a.getY() - b.getY()), 2) + Math.pow((a.getX() - b.getX()), 2)));
	}
	
	/**
	 * At the start of calculation A* method creates map with key ARSField and value Double.POSITIVE_INFINITY for a given map
	 * <Coordinate,ARSField> 
	 * @param map
	 * @return
	 */
	public Map<ARSField, Double> initializeAllStreetFieldsToPosInfinity(Map<Coordinate, ARSField> map) {

		Map<ARSField, Double> mapWithInfDistances = new HashMap<ARSField, Double>();

		for (ARSField f : map.values()) {

			if (f.getFieldType().equals(TypeOfField.STREET)) {
				mapWithInfDistances.put(f, Double.POSITIVE_INFINITY);
			}

		}
		return mapWithInfDistances;
	}
}
