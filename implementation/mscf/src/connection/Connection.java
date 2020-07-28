package connection;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import controllers.IPAddressHandler;
import database.DriverClass;
import messageProcessor.MessageProcessor;
import node.Node;
import udp_conn.UDPClient;
import udp_conn.UDPServer;

public class Connection implements Runnable {
	
	private Node myNode;
	private String myIPAdress;
	private Timer timer= null;	
	private List<String> allIPsInNetwork = null;
	private DriverClass database;
	
	private UDPServer server = null;
	
	private UDPClient client = null;
	
	
	public Connection(String port, String msID) throws SocketException, UnknownHostException {
		

		System.out.println("Connection constructor");
		this.myIPAdress = IPAddressHandler.findMyIp();
		this.myNode = new Node(myIPAdress, Integer.valueOf(port), msID);
		this.allIPsInNetwork = IPAddressHandler.getAllIPsInNetwork();
		this.timer = new Timer();
		database = new DriverClass(Integer.parseInt(port));
		this.client = new UDPClient(allIPsInNetwork, myNode, database);
		this.server = new UDPServer(client, myNode, database);
		

	}

	public List<String> getAllIPsInNetwork() {
		return allIPsInNetwork;
	}

	public void setAllIPsInNetwork(List<String> allIPsInNetwork) {
		this.allIPsInNetwork = allIPsInNetwork;
	}

	public Node getMyNode() {
		return myNode;
	}

	public String getMyIPAdress() {
		return myIPAdress;
	}

	public Timer getTimer() {
		return timer;
	}

	public UDPServer getServer() {
		return server;
	}
	
	public UDPClient getClient() {
		return client;
	}

	public void setClient(UDPClient client) {
		this.client = client;
	}
	
	public void setMessageProcessor(MessageProcessor messageProcessor) {
		server.setMessageProcessor(messageProcessor);
	}
	
	public DriverClass getDatabase() {
		
		return database;
	}
	
	@Override
	public void run() {
	
		Collections.shuffle(allIPsInNetwork);
		client.setInterrupted(true);
		System.out.println("Reseting Connection...");
	}

	
}




