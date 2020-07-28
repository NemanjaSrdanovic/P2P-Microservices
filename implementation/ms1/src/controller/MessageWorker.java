package controller;

import java.util.ArrayList;
import java.util.List;

import cars.Cars;
import connection.Connection;
import constants.Constraints;
import converter.FieldConverter;
import converter.STreetConverter;
import exceptions.ARSException;
import messages.Field;
import messages.Map;
import messages.Message;
import messages.Street;
import model.ARSField;
import model.ARSStreet;
import routeCalculationManagement.RouteManager;
import service.MapService;
import validator.Validator;

/**
 * This class is a concrete thread- pool worker which processes received
 * Message. It implements Runnable interface so it provides simultaneous
 * processing of messages along with other processes of a system
 * 
 * @author nenad.cikojevic
 *
 */
public class MessageWorker implements Runnable {

	private MessageController messageController;
	private Connection connection;
	private MapService mapService;
	private Message toReturn;
	private RouteManager routeManager;

	public MessageWorker(MessageController messageController) {

		this.messageController = messageController;
		this.connection = messageController.getConnection();
		this.mapService = messageController.getMapService();
		this.routeManager = messageController.getRouteManager();
	}

	/**
	 * Method overridden from Runnable interface uses switch- case to filter what
	 * kind of processing is going to happen on a received Message. In order to make
	 * a difference, Messages are filtered by its' "endpoint" field. After
	 * processing a Message if wraps object to return into a Message object as an
	 * envelope with header data and passes it to UDPClient to send on network
	 */

	@Override
	public void run() {

		Message received = messageController.getMessages().poll();

		if (received != null) {

			String myIP = connection.getMyNode().getIpAddress() + ":" + connection.getMyNode().getPortNmr();

			switch (received.getEndpoint()) {

			/*
			 * in this case MS4 is asking for list of streets, when it starts in order to
			 * have an overview of drivable areas
			 */
			case "streets": {
				System.out.println("Setting map with list of streets to return for MS4_RMM...");
	
				ArrayList<ARSStreet> streets = (ArrayList<ARSStreet>) mapService.getMap().getStreets();

				ArrayList<Street> streetsToReturn = new ArrayList<Street>();
				new ArrayList<>();

				for (ARSStreet s : streets) {
					Street streetToReturn = STreetConverter.convertFrom(s);
					streetsToReturn.add(streetToReturn);
				}
				Map mapToSend1 = new Map();
				mapToSend1.setStreets(streetsToReturn);

				toReturn = new Message(Constraints.MS4_ID, myIP, received.getSource(), received.getMessageId(),
						mapToSend1);

				try {
					connection.getClient().addMessage(toReturn);
					System.out.println("Map with list of streets ready for MS4_RMM");
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}

				break;
			}
			/*
			 * on this endpoint, it is intended to activate engine to calculate route for
			 * MS2, which works in multithreaded manner
			 */
			case "carRoute": {

				try {
					System.out.println("Setting car route to return for MS2_CDG...");
					routeManager.unpackAndCalculateRoute(received);
					return;
				} catch (InterruptedException e1) {
					System.out.println(e1.getMessage());
				}
				break;
			}
			/*
			 * it triggers blocking an area received from MS4_RMM
			 */
			case "block": {

				System.out.println("Setting block area for MS4_RMS...");

				try {
					Street toBlock = (Street) received.getData();
					Validator.isStreetValid(toBlock);

					ARSStreet arsToBlock = STreetConverter.convertFrom(toBlock);

					mapService.blockStreet(arsToBlock);

					System.out.println("Blocked area: ");
					for (ARSField f : arsToBlock.getFields()) {
						System.out.println(f.getCoordinate() + " blocked: " + !f.isDrivable());
					}

					toReturn = new Message(Constraints.MS4_ID, myIP, received.getSource(), received.getMessageId(),
							toBlock);
					connection.getClient().addMessage(toReturn);

					ArrayList<Street> streets = getDrivableStreets();
					Map toReturnMap = new Map();

					toReturnMap.setStreets(streets);

//					routeManager.sendNewMapToAll(toReturnMap);

				} catch (ARSException e) {
					System.out.println(e.getMessage());
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}

				break;
			}
			/*
			 * it triggers MapService to return list of Street objects that contain lists of
			 * only drivable fields for MS2_CDG in order to choose its' position at the
			 * beginning and new destination each time a previous one is reached
			 */
			case "drivableFields": {

				System.out.println("Setting drivable fields for MS2_CDG...");

				List<Street> streetsToReturn = getDrivableStreets();

				Map mapToSend1 = new Map();

				mapToSend1.setStreets(streetsToReturn);

				toReturn = new Message(Constraints.MS2_ID, myIP, received.getSource(), received.getMessageId(),
						mapToSend1);

				/*
				 * this block is used to pass Message to UDPClient for sending
				 */
				try {
					connection.getClient().addMessage(toReturn);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			}

		}
	}
	
	/**
	 * Method is used to return only fields inside of street object where isDrivable property is true
	 * @return ArrayList<Street> for MS2 CDG in order to pick only drivable as Destination and 
	 * Position(when started) 
	 */
	private synchronized ArrayList<Street> getDrivableStreets() {

		ArrayList<ARSStreet> streets = (ArrayList<ARSStreet>) mapService.getMap().getStreets();
		ArrayList<Street> streetsToReturn = new ArrayList<>();

		for (ARSStreet s : streets) {
			List<Field> list = new ArrayList<>();

			for (ARSField f : s.getFields()) {
				if (f.isDrivable())
					list.add(FieldConverter.convertFrom(f));
			}

			Street s1 = new Street(s.getName(), list);
			streetsToReturn.add(s1);

		}

		return streetsToReturn;

	}

}
