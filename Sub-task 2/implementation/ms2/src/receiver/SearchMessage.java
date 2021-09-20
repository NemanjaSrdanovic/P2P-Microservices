package receiver;

import java.util.ArrayList;
import java.util.List;

import messages.Car;
import messages.Field;
import messages.Map;
import messages.Message;
import messages.Street;

/**
 * The SearchMessage class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-06-09
 */
public class SearchMessage {

	/*-------------------------------------VARIABLES-------------------------------------*/

	private Map mapa = new Map();
	private ReceiveMessage receiver;
	public boolean varijabla = false;
	private Car car = new Car(0);
	private List<Street> messageStreets;

	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	public SearchMessage(ReceiveMessage receiver) {

		this.receiver = receiver;
		this.messageStreets = new ArrayList<Street>();
	}

	/**
	 * @String requestID
	 * 
	 *         Receiving the request ID which will be put in the message and is
	 *         equals to the messageID send with an request to get the drivable
	 *         fields from MS1_ARS.
	 * 
	 *         This method will check the messageList filed by the ReceiveMessage
	 *         (UDPServer) to see if an response with the requestID is in the list.
	 *         If it finds the response message it will extract the fields from the
	 *         response object and give it to the function which called this method,
	 *         else it will call clearUnusedMessages which will remove this overdue
	 *         message from the list when it is received and return NULL. Null will
	 *         trigger the call function to send another request whit another
	 *         messageID (requestID), which will be the new requestID.
	 * 
	 */
	public synchronized List<Field> getMessagebyRequestIDField(String requestID) throws InterruptedException {

		Message returnMessage = null;
		int timeOut = 0;

		while (true) {

			if (timeOut >= 15) {

				System.out.println("# MESSAGE REQUEST: " + requestID
						+ " #---------------------------------------- > Timeout request resending.");

				clearUnusedMessages(requestID);
				return null;
			}

			if (receiver.getMessageInList()) {

				for (Message message : receiver.getMessageList()) {

					// System.out.println("USAO PROVERAVA ID");

					if (message.getResponseId().equals(requestID)) {
						returnMessage = message;
						receiver.removeMessageFromList(message);
						if (receiver.getMessageList().size() == 0)
							receiver.setMessageInList(false);

						mapa = (Map) returnMessage.getData();
						messageStreets = mapa.getStreets();
						mapa.setStreets(messageStreets);

						System.out.println("# MESSAGE RESPONSE: " + message.getResponseId()
								+ " #--------------------------------------- > ARS Fields received.");

						return mapa.getDrivableFields();

					}
				}
			}

			++timeOut;
			Thread.sleep(1000);

		}
	}

	/**
	 * @param String requestID
	 * 
	 *               Receiving the request ID which will be put in the message and
	 *               is equals to the messageID send with an request to get the car
	 *               route for the given position and destination from MS1_ARS.
	 * 
	 *               This method will check the messageList filed by the
	 *               ReceiveMessage (UDPServer) to see if an response with the
	 *               requestID is in the list. If it finds the response message it
	 *               will extract the fields from the response object and give it to
	 *               the function which called this method, else it will call
	 *               clearUnusedMessages which will remove this overdue message from
	 *               the list when it is received and return NULL or the old car
	 *               route if it is not empty. Null will trigger the call function
	 *               to send another request whit another messageID (requestID),
	 *               which will be the new requestID. The old route will make the
	 *               car travel on the route it already has and send another request
	 *               after it has travel to the next field.
	 * 
	 */
	public synchronized List<Field> getMessagebyRequestIDRoute(String requestID, Car carInput)
			throws InterruptedException {

		Message returnMessage = null;
		int timeOut = 0;

		while (true) {

			if (timeOut >= 15 && carInput.getCarRoute().size() != 0) {

				System.out.println("# MESSAGE REQUEST: " + requestID
						+ " #--------------------------------------- > Timeout continue travel on old route.");

				clearUnusedMessages(requestID);
				return carInput.getCarRoute();
			} else if (timeOut >= 15) {

				System.out.println("# MESSAGE REQUEST: " + requestID
						+ " #---------------------------------------- > Timeout request send again.");

				clearUnusedMessages(requestID);
				return null;
			}

			if (receiver.getMessageInList()) {

				for (Message message : receiver.getMessageList()) {

					// System.out.println("USAO PROVERAVA ID");

					if (message.getResponseId().equals(requestID)) {
						returnMessage = message;
						receiver.removeMessageFromList(message);
						if (receiver.getMessageList().size() == 0)
							receiver.setMessageInList(false);

						car = (Car) returnMessage.getData();
						System.out.println("# MESSAGE RESPONSE: " + message.getResponseId()
								+ " #--------------------------------------- > New ARS route received.");

						return car.getCarRoute();

					}
				}
			}

			++timeOut;
			Thread.sleep(1000);
		}

	}

	/*
	 * java.base/java.io.ObjectInputStream$PeekInputStream.readFully
	 */
	/*
	 * public synchronized boolean statisticReceived(String requestID) throws
	 * InterruptedException {
	 * 
	 * int timeOut = 0;
	 * 
	 * while (true) {
	 * 
	 * if(timeOut==3) {
	 * 
	 * clearUnusedMessages(requestID); return false;
	 * 
	 * }
	 * 
	 * if (receiver.getMessageInList()) {
	 * 
	 * for (Message message : receiver.getMessageList()) {
	 * 
	 * if (message.getResponseId().equals(requestID)) {
	 * 
	 * receiver.removeMessageFromList(message); if (receiver.getMessageList().size()
	 * == 0) receiver.setMessageInList(false);
	 * 
	 * return true;
	 * 
	 * } } }
	 * 
	 * ++timeOut; Thread.sleep(1000); }
	 * 
	 * }
	 * 
	 */
	/**
	 * @param String requestID
	 * 
	 *               This method is called when a message request is not received in
	 *               the given time. Its purpose is to remove an overdue message
	 *               from the messageList due that the function has send another
	 *               request to the microservice or has given the old object to the
	 *               function.
	 */

	public synchronized void clearUnusedMessages(String requestID) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				boolean found = false;

				while (!found) {

					if (receiver.getMessageInList()) {

						for (Message message : receiver.getMessageList()) {

							if (message.getResponseId().equals(requestID)) {

								System.out.println("# SYSTEM REQUEST: " + requestID
										+ " #--------------------------------------- > Unused message removed.");
								
								receiver.removeMessageFromList(message);

								System.out.println("# SYSTEM CLEANER: " + "#-------------"
										+ "--------------------------" + "--------------------------"
										+ "----------- > Messages to be prcessed:" + " "
										+ receiver.getMessageList().size());

								found = true;

							}

						}

					}

				}

			}
		};

		Thread thread = new Thread(runnable);
		thread.start();

	}

}
