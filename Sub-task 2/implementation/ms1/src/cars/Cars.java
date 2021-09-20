package cars;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import model.ARSCar;

/**
 * Holds list of registered cars
 * 
 * @author nenad.cikojevic
 *
 */
public class Cars {
	
	private List<ARSCar> listOfcars;
	
	/**
	 * Ctor initializes list that holds car objects
	 */
	public Cars() {
		 listOfcars =new CopyOnWriteArrayList<>();
	}

	public synchronized List<ARSCar> getListOfcars() {
		return listOfcars;
	}
	
	
}
