package controllers;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.TimerTask;

import connection.Connection;
import constants.Constraints;
import messageProcessor.MessageProcessor;
import node.Node;
import udp_conn.UDPServer;

public class ConnectionHandler extends TimerTask {
	
	private Connection connection = null;
	private String portNummber; //@Nemanja

	public ConnectionHandler(String port, String msID) throws SocketException, UnknownHostException {

		this.connection = new Connection(port, msID);
		this.portNummber=port;

	}
	
	public Connection getConnection() {
		
		return connection;
	}

	//@Nemanja
	public void setConnection(Connection connection) {
		this.connection=connection;
	}

	//@Nemanja
	public String getPortNummber() {
		return portNummber;
	}
	
	public void setMessageProcessor(MessageProcessor messageProcessor) {
		this.connection.setMessageProcessor(messageProcessor); 
	}

	/**
	 * works in parallel with server and repeats every 10 seconds, randomly finds 2 ip addresses and ports to connect with
	 */
	@Override
	public void run() {
		Thread connectionThread = new Thread(this.connection);
		connectionThread.start();
	}
	/**
	 * encapsulation of communication functionality
	 */
	public void connect() {
		Thread thread2 = new Thread(connection.getClient());
		thread2.start();
		connection.getTimer().schedule(this, Constraints.TIME_TO_BECONNECTED_MILIS,Constraints.TIME_TO_BECONNECTED_MILIS);

	}
	

}