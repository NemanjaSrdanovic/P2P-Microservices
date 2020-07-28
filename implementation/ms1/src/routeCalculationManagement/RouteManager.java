package routeCalculationManagement;

import java.net.SocketException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import cars.Cars;
import connection.Connection;
import constants.Constraints;
import controller.MessageWorker;
import converter.CarConverter;
import exceptions.ARSException;
import messages.Car;
import messages.Map;
import messages.Message;
import model.ARSCar;
import service.MapService;

/**
 * Class is used to hold BlockingQueue of Messages to extract Car object for route to calculate
 * as well pool of workers that are calculating route
 * @author nenad.cikojevic
 *
 */
public class RouteManager {
	
	private BlockingQueue<Message> messages;
	private ExecutorService routeWorkers; 
	private Connection connection;
	private MapService mapService;
	private Cars cars;
	
	
	public RouteManager() {}
	public RouteManager(Connection connection, MapService mapService) {
		
		System.out.println("Route manager started..");
		
		this.messages = new LinkedBlockingQueue<>();
		this.routeWorkers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.connection = connection;
		this.mapService = mapService;
		this.cars = new Cars();
		
//		for(int i = 0;i<Runtime.getRuntime().availableProcessors();++i)
//		this.routeWorkers.execute(new RouteWorker(this));
	}
	
	/**
	 * Method is called on MessageController to place a Message into a Queue which is 
	 * further going to be processed by a single worker
	 * @param message
	 * @throws InterruptedException
	 */
	public void unpackAndCalculateRoute(Message message) throws InterruptedException {
		
		this.messages.put(message);
		routeWorkers.execute(new RouteWorker(this));
		
	}

	public BlockingQueue<Message> getMessages() {
		return messages;
	}
	public void setMessages(BlockingQueue<Message> messages) {
		this.messages = messages;
	}
	public MapService getMapService() {
		return mapService;
	}
	public void setMapService(MapService mapService) {
		this.mapService = mapService;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Method wraps a car object with a new route into a Message object (an envelope) 
	 * with proper header data for sending and passes it to UDPClient  
	 * @param message
	 * @param carToReturn
	 * @throws SocketException
	 */
	protected void passToClient(Message message, Car carToReturn) throws SocketException {
		
		String myIP = getConnection().getMyNode().getIpAddress()+":"+
				 getConnection().getMyNode().getPortNmr();
		
		Message messageToReturn = new Message(Constraints.MS2_ID, myIP, message.getSource(),
				message.getMessageId(), carToReturn);

		try {
			connection.getClient().addMessage(messageToReturn);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method converts an registers a car object
	 * @param carReceived
	 * @return
	 * @throws ARSException
	 */
	protected synchronized ARSCar registerCar(Car carReceived, String ipPort) throws ARSException {
		
		Optional<ARSCar> carOpt =cars.getListOfcars().stream().filter(c->c.getId()==carReceived.getCarId()).findFirst();
		ARSCar car;
		ARSCar help = CarConverter.convertFrom(carReceived, ipPort);
	
		if(carOpt.isPresent()) {
			car = carOpt.get();
			System.out.println("ID: "+car.getId()+" exists in the list");
			
		}
			
		else {
			
			car = CarConverter.convertFrom(carReceived, ipPort);
			System.out.println("ID: "+ car.getId()+" is registered now");
			
			cars.getListOfcars().add(car);
			
		}
		car.setCarRoute(help.getCarRoute());
		return car;
	}
	
	/**
	 * Updates all cars if area is blocked
	 * @param toReturnMap
	 * @throws InterruptedException
	 */
	public void sendNewMapToAll(Map toReturnMap) throws InterruptedException {
		
		List<ARSCar>carsList = cars.getListOfcars();
		
		for(int i = 0;i<carsList.size();++i) {
			String myIP = getConnection().getMyNode().getIpAddress()+":"+
					 getConnection().getMyNode().getPortNmr();
			Message message = new Message(Constraints.MS2_ID, myIP
					, carsList.get(i).getIpPort()
					,toReturnMap);
			
			connection.getClient().addMessage(message);
			
		}
		
	}
}
