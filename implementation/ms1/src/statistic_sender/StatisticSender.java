package statistic_sender;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import connection.Connection;
import constants.Constraints;
import controller.MessageController;
import messages.Message;

/**
 * Class is used to get all messages from database for statisical purposes and
 * to wrap them into one message and send to MS3_NMM for graphical
 * visualisation. This task will be repeated each 4 seconds. Messages are kept
 * along with their objects which are not necessary for this purpose, so they
 * will be ommited and not sent in order not to congest the network and overload
 * the packet size.
 * 
 * @author nenad.cikojevic
 *
 */
public class StatisticSender extends TimerTask {

	private Connection connection;
	private List<Message> messages;
	private MessageController messageController;
	private List<String> dropMessageList;
	boolean received = false;
	private int increment = 0;

	public StatisticSender(Connection connectionInput, MessageController messageController) {

		this.connection = connectionInput;
		messages = new ArrayList<Message>();
		this.messageController = messageController;
		this.dropMessageList = new ArrayList<String>();
	}

	public synchronized void setReceived(boolean input) {

		received = input;
	}

	public synchronized boolean getReceived() {

		return received;
	}

	@Override
	public void run() {
		
		System.out.println("Sending statistics...");
		
		
		if(connection.getDatabase().getStatisticMessages().size()>0) {
		String ID = ++increment + "";

		try {
			connection.getDatabase().makeStatisticBackup(ID);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		Message messageStatistic = new Message(Constraints.MS3_ID,
				connection.getMyNode().getIpAddress() + ":" + connection.getMyNode().getPortNmr(), null);

		messages = connection.getDatabase().getBackupData(ID);
		
			messageStatistic.setData(messages);

			try {
				connection.getClient().addMessage(messageStatistic);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		

		connection.getDatabase().dropBackup(ID);
		
		}

//		System.out.println("123456789");
//
//		System.out.println("MS1_ARS: Sending statistics...");
//
//		if(connection.getDatabase().getStatisticMessages().size()>0) {		
//		
//		String ID = ++increment+"";
//		try {
//			connection.getDatabase().makeStatisticBackup(ID);
//		} catch (SQLException e2) {
//			e2.printStackTrace();
//		}
//		
//		System.out.println("MEssage data:"+ messageController.getConfirmationMessages().size());
//		do {
//			System.out.println("USOOOOOOOOOO");
//
//			Message messageStatistic = new Message(Constraints.MS3_ID,
//					connection.getMyNode().getIpAddress() + ":" + connection.getMyNode().getPortNmr(), null);
//
//			try {
//
//				messageStatistic.setData(connection.getDatabase().getBackupData(ID));
//				
//				
//				connection.getClient().addMessage(messageStatistic);
//
//				setReceived(messageController.statisticReceived(messageStatistic.getMessageId()));
//
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//		} while (!getReceived());
//		System.out.println("IZASLO");
//		connection.getDatabase().dropBackup(ID);
//
//	}
  }
}
