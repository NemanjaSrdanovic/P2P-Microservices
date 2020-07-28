package statistic_sender;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import connection.Connection;
import constants.Constraints;
import controllers.MS2Controller;
import messages.Field;
import messages.Message;

/**
 * The StatisticSender class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-06-09
 */
public class StatisticSender extends TimerTask implements Runnable {

	/*-------------------------------------VARIABLES-------------------------------------*/

	private Connection connection;
	private List<Message> messages;
	private MS2Controller controller;
	private boolean received=false;
	private int incrementer=0;
	
	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	public StatisticSender(Connection connectionInput, MS2Controller conntroller) {

		this.connection = connectionInput;
		messages = new ArrayList<Message>();
		this.controller=conntroller;
	}


/* 
 * 	java.base/java.io.ObjectInputStream$PeekInputStream.readFully
 */
//	public void setReceived(boolean input) {
//		
//		received=input;
//	}
//	
//	public boolean getReceived() {
//		
//		return received;
//	}

	
	/**
	   Sending a ArrayList<Message> to MS3 with the statistic. 
	   The timer for this method is defined in MS2Application
	*/ 
	@Override
	public void run() {
	
    if (connection.getDatabase().getStatisticMessages().size() > 0) {	
		String ID = ++incrementer + "";

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
			
			System.out.println("# MESSAGE: " + messageStatistic.getMessageId()
			+ " # ----------------------------------------------- > NMM Statistic send.");


		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		connection.getDatabase().dropBackup(ID);
		
    }	
   /*
    *  Due to java.base/java.io.ObjectInputStream$PeekInputStream.readFully error this code part could not be
    *  finished on time. The purpose was to make this part more resilient with backups and resending unreceived messages
    *   */
		
		/*
		if (connection.getDatabase().getStatisticMessages().size() > 0) {

					String ID = ++incrementer + "";
							
					try {
						connection.getDatabase().makeStatisticBackup(ID);
					} catch (SQLException e2) {
						e2.printStackTrace();
					}

					do {

						Message messageStatistic = new Message(Constraints.MS3_ID,
								connection.getMyNode().getIpAddress() + ":" + connection.getMyNode().getPortNmr(),
								null);

						messages=connection.getDatabase().getBackupData(ID);
						messageStatistic.setData(messages);

						try {
							connection.getClient().addMessage(messageStatistic);

							setReceived(controller.getReceiver().getSearchMessage()
									.statisticReceived(messageStatistic.getMessageId()));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					} while (!getReceived());

					connection.getDatabase().dropBackup(ID);

					System.out.println("IZASAO SENDER");

				}
		 	*/
	}

}
