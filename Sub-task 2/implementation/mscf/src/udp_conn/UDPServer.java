package udp_conn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import constants.Constraints;
import exceptions.ConnectionException;
import messageProcessor.MessageProcessor;
import messages.Message;
import node.Node;
import parsers.Marshaller;
import database.DriverClass;

public class UDPServer{
	
	private Message message;
	private DatagramSocket socket = null;
	private DatagramPacket requestPacket;
	private DatagramPacket packetToSend;
	private byte[]buffer;
	private UDPClient client;
	private Node myNode;
	private DriverClass database;
	private MessageProcessor messageProcessor;
	private ExecutorService threadPool;
	
	public UDPServer(UDPClient client, Node myNode, DriverClass database) throws SocketException {
	
			System.out.println("Server constructor on port "+ myNode.getPortNmr() );
			this.client = client;
			this.myNode = myNode;
			this.socket = new DatagramSocket(myNode.getPortNmr());
			this.database = database;
			this.threadPool = Executors.newFixedThreadPool(4);
	
			for(int i = 0;i<4;++i)
				this.threadPool.execute(new ServerWorker(this));

	}
	
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	public void setMessageProcessor(MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public UDPClient getClient() {
		return client;
	}

	public void setClient(UDPClient client) {
		this.client = client;
	}

	public DriverClass getDatabase() {
		return database;
	}

	public void setDatabase(DriverClass database) {
		this.database = database;
	}

	public Node getMyNode() {
		return myNode;
	}

	public void setMyNode(Node myNode) {
		this.myNode = myNode;
	}

}
