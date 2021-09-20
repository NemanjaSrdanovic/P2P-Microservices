package com.dse.ms4.sender;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import com.dse.ms4.controller.MessageController;

import connection.Connection;
import constants.Constraints;
import messages.Message;

public class StatisticSender extends TimerTask {

	/*-------------------------------------VARIABLES-------------------------------------*/

	private Connection connection;
	private List<Message> messages;
	private int incrementer;
	boolean received=false;
	
	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	public StatisticSender(Connection connectionInput, MessageController conntroller) {
 
		this.connection = connectionInput;
		messages = new ArrayList<Message>();
		this.incrementer = 0; 
	}
	
//	public synchronized void setReceived(boolean input) {
//		
//		received=input;
//	}
//	
//	public synchronized boolean getReceived() {
//		
//		return received;
//	}

	/*
	 *  Sending a ArrayList<Message> to MS3 with the statistic. 
	 *  The timer for this method is defined in MS2Application
	 */
	@Override
	public void run() {
			
		if(connection.getDatabase().getStatisticMessages().size() > 0) {
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
	 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	
			connection.getDatabase().dropBackup(ID);
		
			
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
		}
	
	}
	*/
		}
	}

}
