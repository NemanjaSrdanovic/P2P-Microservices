package com.nmm.worker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.nmm.controllers.NMMController;
import com.nmm.database.InsertDB;
import com.nmm.database.SelectDB;
import com.nmm.entities.Message;

public class MessageWorker implements Runnable {
	private NMMController controller;
	Connection connection;
	Statement statement;
	String database;
	InsertDB insert;
	SelectDB select;

	public MessageWorker(NMMController controllerr) throws ClassNotFoundException, SQLException {
		this.controller = controllerr;
		setConnection();
	}

	public void setConnection() throws ClassNotFoundException, SQLException {
		this.database = "DatabaseMessages" + ".db";
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:.\\" + database);
		connection.setAutoCommit(false);
		insert = new InsertDB(database);
		select = new SelectDB(database);
	}

	@Override
	public void run() {
		while (true) {
			messages.Message message1 = null;
			if ((message1 = controller.getMessages().poll()) != null) {
				System.out.println("GETING MESSAGE");

				try {
					@SuppressWarnings("unchecked")
					List<messages.Message> list = (List<messages.Message>) message1.getData();
					System.out.println("RECEIVED MESSAGE: " + message1);
					System.out.println("LIST SIZEE:################## " + list.size());
					for (messages.Message message : list) {
						System.out.println("====OVDE ME ipisi============");
						System.out.println(message);
						Message m = new Message();
						m.setMessageID(message.getMessageId());
						m.setSource(message.getSource());
						m.setDestination(message.getDestination());
						m.setDestinatedMS(message.getDestinatedMS());
						m.setVisited(message.getVisitedMS());

						try {
							insert.insertMessageTable(m);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println("=====INSERTED IN DATABASE===========");
					}
				} catch (Exception e) {
					System.out.println(".");
				}

			}
		}
	}

}
