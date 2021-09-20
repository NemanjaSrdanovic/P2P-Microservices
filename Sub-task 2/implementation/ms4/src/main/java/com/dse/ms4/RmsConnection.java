package com.dse.ms4;

import java.net.SocketException;
import java.net.UnknownHostException;

import constants.Constraints;
import controllers.ConnectionHandler;
import database.DriverClass;

public class RmsConnection {
	private int portNumber;
	private ConnectionHandler connection;
	private DriverClass database;
	
	
	public RmsConnection(int port) {
		
		this.portNumber=port;
		String connectionPort = Integer.toString(portNumber);
			
		try {
			connection = new ConnectionHandler(connectionPort, Constraints.MS4_ID);
			connection.connect();
		} catch (SocketException | UnknownHostException e) {
			System.out.println("RmsConnection - FAILED");
			e.printStackTrace();
		}
		
		database = new DriverClass(portNumber);
		connection.run();	
	}


	public ConnectionHandler getConnection() {
		return connection;
	}


	public DriverClass getDatabase() {
		return database;
	}
	
}
