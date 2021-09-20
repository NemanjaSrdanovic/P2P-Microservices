package controller;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import connection.Connection;
import messageProcessor.MessageProcessor;
import messages.Message;
import routeCalculationManagement.RouteManager;
import service.MapService;

/**
 * This class implements functional interface MessageProcessor
 * The main purpose of this class is to process messages received on "ServerWorker".
 * It connects functionality of MSCF and MS1-ARS. 
 * @author nenad.cikojevic
 *
 */
public class MessageController implements MessageProcessor {
	

	private Connection connection;
	private MapService mapService;
	private RouteManager routeManager;
	private List<Message> confirmationMessages;
	
	/**
	 * Collection of Messages
	 */
	private BlockingQueue<Message> messages;
	private ExecutorService threadPool;
	
	public MessageController(Connection connection, MapService mapService, RouteManager routeManager) {

		this.connection = connection;

		this.mapService = mapService;
		this.routeManager = routeManager;
		this.messages = new LinkedBlockingQueue<Message>();
		this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.confirmationMessages = new CopyOnWriteArrayList<Message>();

	}

	protected BlockingQueue<Message> getMessages() {

		return messages;
	}

	protected Connection getConnection() {

		return connection;
	}

	protected MapService getMapService() {

		return mapService;
	}

	protected RouteManager getRouteManager() {

		return routeManager;
	}
	
	
	public List<Message> getConfirmationMessages() {
		return confirmationMessages;
	}

	/**
	 * Method overridden from functional interface, which is triggered each time a received Message belongs 
	 * to this microservice. It is then put into BlockingQueue, collection of Messages and a new MessageWorker 
	 * is activated to process the task  
	 * 
	 */
	
	@Override
	public void onMessage(Message received) {
		try {
			messages.put(received);

			threadPool.execute(new MessageWorker(this));

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	
//	public synchronized boolean statisticReceived(String requestID) throws InterruptedException {
//
//		int timeOut = 0;
//
//		while (true) {
//			
//		 if(timeOut==3) {
//			
////		clearUnusedMessages(requestID);
//			 return false;
//		 }	
//
//			if (!confirmationMessages.isEmpty()) {
//				
//				for (Message message : confirmationMessages) {
//				
//					if (message.getResponseId().equals(requestID)) {
//						
//						System.out.println("RESPONSE ID EQUALS REQUEST ID");
//						
//						System.out.println("VELICINA PRE: " + confirmationMessages.size());
//						
//						confirmationMessages.remove(message);
//						
//						System.out.println("VELICINA POSLE: " + confirmationMessages.size());
//						
//						return true;
//
//					}
//				}
//			}
//			++timeOut;
//			Thread.sleep(1000);
//		}
//
//	}
//	
//	public synchronized void clearUnusedMessages(String requestID) {
//
//		Runnable runnable = new Runnable() {
//
//			@Override
//			public void run() {
//
//				boolean found = false;
//
//				while (!found) {
//
//					if (!messages.isEmpty()) {
//
//						for (Message message : messages) {
//
//							if (message.getResponseId().equals(requestID)) {
//								messages.remove(message);
//								found = true;
//							}
//
//						}
//
//					}
//
//				}
//
//			}
//		};
//
//		Thread thread = new Thread(runnable);
//		thread.start();
//
//	}
	
	

}
