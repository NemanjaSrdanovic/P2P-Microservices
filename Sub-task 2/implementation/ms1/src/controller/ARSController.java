package controller;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;

import controllers.ConnectionHandler;
import routeCalculationManagement.RouteManager;
import service.MapService;
import statistic_sender.StatisticSender;

/**
 * Main App controller initialized in Main class 
 * @author nenad.cikojevic
 *
 */
public class ARSController {
	
	/**
	 * port number of an app
	 */
	private String port;
	
	/**
	 * unique ID of a micro service  
	 */
	private String msID;
	
	public ARSController(String port, String msID) {
		
		this.port = port;;
		this.msID = msID;
		
	}
	
	/**
	 * Calling this method starts this microservice. It initializes ConnectionHandler behind which is covered connection logic,
	 * MapService- holds all map creating, placing cars on fields, blocking and unblocking areas, StatisticSender- sands messages
	 * for statistical processing. Timer schedules sending statistical messages on each 4 seconds.
	 */
	public void start() {
			
		try {
			ConnectionHandler connectionHandler = new ConnectionHandler(port, msID);
			MapService mapService = new MapService();
			mapService.generateMap();
			
			RouteManager routeManager = new RouteManager(connectionHandler.getConnection(), mapService);
			
			MessageController messageController = new MessageController(connectionHandler.getConnection(), mapService, routeManager);
			
			StatisticSender sender = new StatisticSender(connectionHandler.getConnection(), messageController);
			Timer timer = new Timer();
			timer.schedule(sender,4000,4000); 
			
			connectionHandler.setMessageProcessor(messageController);
			
			connectionHandler.connect();

			
		
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		} 	
		
	}
}
