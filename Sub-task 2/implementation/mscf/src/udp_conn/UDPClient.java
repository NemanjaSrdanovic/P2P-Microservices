package udp_conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import constants.Constraints;
import database.DriverClass;
import messages.Message;
import node.Node;
import parsers.Marshaller;

public class UDPClient implements Runnable {

	private List<String> allIPsInNetwork;
	private int port;
	private int addressCounter;
	private BlockingQueue<Message> messages;
	private ExecutorService threadPool;
	private boolean foundBothMSToConnect;
	private boolean isInterrupted;
	private List<String> connectedIPsPort;
	private Node myNode;
	private Lock lock1;
	private Lock lock2;
	private DriverClass database;
	private int index = 0;
	private List<String> tempVisitedMS;

	private Queue<HashMap<String, Message>> messageForBothConnected;

	public UDPClient(List<String> allIPsInNetwork, Node myNode, DriverClass database)
			throws SocketException, UnknownHostException {

		System.out.println("Starting client");

		this.allIPsInNetwork = allIPsInNetwork;
		this.myNode = myNode;

		this.port = Constraints.PORTNUM_MIN;
		this.addressCounter = 0;
		this.threadPool = Executors.newFixedThreadPool(20);

		// this.connectedIPsPort = Collections.synchronizedList(new ArrayList<>());
		this.connectedIPsPort = new CopyOnWriteArrayList<String>();
		this.messages = new LinkedBlockingQueue<Message>();
		this.foundBothMSToConnect = false;
		this.isInterrupted = false;
		this.lock1 = new ReentrantLock();
		this.lock2 = new ReentrantLock();
		this.database = database;
		this.tempVisitedMS = new ArrayList<String>();

		this.messageForBothConnected = new LinkedBlockingQueue<HashMap<String, Message>>();

		try {

			for (int i = 0; i < 20; ++i)

				this.threadPool.execute(new ClientWorker(this));
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public List<String> getAllIPsInNetwork() {

		return allIPsInNetwork;
	}

	public void setAllIPsInNetwork(List<String> allIPsInNetwork) {
		this.allIPsInNetwork = allIPsInNetwork;
	}

	public synchronized int getPort() {
		return port;
	}

	public synchronized void setPort(int port) {
		this.port = port;
	}

	public synchronized int getAddressCounter() {
		return addressCounter;
	}

	public synchronized void setAddressCounter(int addressCounter) {
		this.addressCounter = addressCounter;
	}

	public synchronized Queue<HashMap<String, Message>> getMessageForBothConnected() {
		return messageForBothConnected;
	}

	public Lock getLock1() {
		return lock1;
	}

	public synchronized Queue<Message> getMessages() {
		return messages;
	}

	public synchronized void addMessage(Message message) throws InterruptedException {
		messages.put(message);
	}

	public synchronized Message getMessage() {
		return messages.poll();
	}

	public synchronized void setInterrupted(boolean isInterrupted) {
		this.isInterrupted = isInterrupted;
	}

	public synchronized boolean isInterrupted() {
		return isInterrupted;
	}

	public synchronized boolean isFoundBothMSToConnect() {
		return foundBothMSToConnect;
	}

	public void setFoundBothMSToConnect(boolean foundBothMSToConnect) {
		this.foundBothMSToConnect = foundBothMSToConnect;
	}

	public synchronized List<String> getConnectedIPsPort() {
		return connectedIPsPort;
	}

	public synchronized void setConnectedIPsPort(List<String> connectedIPsPort) {
		this.connectedIPsPort = connectedIPsPort;
	}

	public synchronized void setTempVisited(List<String> input) {

		tempVisitedMS = input;
	}

	// probaj nesinhronizovano
	public void setForBothConnected(Message message) {


		List<String> connectedIPs = getConnectedIPsPort();
		for (int i = 0; i < connectedIPs.size(); ++i) {
			String ip = connectedIPs.get(i);
			Map<String, Message> mapToAdd = new HashMap<String, Message>();
			mapToAdd.put(ip, message);
			messageForBothConnected.add((HashMap<String, Message>) mapToAdd);
		}
	}

	private synchronized String iterateThroughIPsandPorts() {
		
		
		int ip;
		int port;

		Random random = new Random();

		ip = random.nextInt(3) + 0;
		port = random.nextInt(10) + 3020;

		return allIPsInNetwork.get(ip) + ":" + port;
	
	}

	public synchronized String randomIPAddressAndPortToConnect() {

		String[] ipPort;
		String random_ip_port_comb;

		do {

			random_ip_port_comb = iterateThroughIPsandPorts();

			ipPort = random_ip_port_comb.split(":");

		} while (ipPort[1].equals(Integer.toString(myNode.getPortNmr())) && ipPort[0].equals(myNode.getIpAddress()));

		return random_ip_port_comb;

	}

	protected synchronized void setIPandPortOfRandomMSs(String ipPortOfRecipient, DatagramPacket recPacket)
			throws IOException {

		if (!foundBothMSToConnect) {

			Collections.shuffle(allIPsInNetwork);

			if (connectedIPsPort.stream()
					.filter(s -> s.equals(recPacket.getAddress().getHostAddress() + ":" + recPacket.getPort()))
					.findAny().isEmpty()) {
				connectedIPsPort
						.add(String.valueOf(recPacket.getAddress().getHostAddress() + ":" + recPacket.getPort()));
			}

			if (connectedIPsPort.size() == Constraints.MAX_CONNECTED_NODES) {
				
				System.out.println("\n");
				System.out.println("Connected peers : ["+connectedIPsPort.get(0)+" | "+ connectedIPsPort.get(1)+"]");
				System.out.println("\n");
				
				foundBothMSToConnect = true;
				setAddressCounter(0);
				setPort(Constraints.PORTNUM_MIN - 1);
			}
		}

	}

	// da li ovo treba uopste
	public boolean canISendTo(String messageDest) {

		return !getConnectedIPsPort().isEmpty() && getConnectedIPsPort().contains(messageDest);

	}

	// probaj nesinhronizovano
	public void sendToDestination(Message message, byte[] buffer, DatagramPacket packet, Marshaller marshaller,
			DatagramSocket socket, DatagramPacket recPacket, byte[] receivedData) throws IOException, SQLException {

		try {
			Optional<String> destinationIPandPortOPt = Optional.ofNullable(message.getDestination());

			if (destinationIPandPortOPt.isPresent() && canISendTo(message.getDestination())) {
//				System.out.println("Inside if");
				sendMessage(message, destinationIPandPortOPt.get(), buffer, packet, marshaller, socket, recPacket,
						receivedData);

			} else {
				setForBothConnected(message);
			}

		} catch (NullPointerException e) {

		}

	}

	// probaj asinhrono i hvatas unknownSource
	public void sendMessage(Message message, String ipPortDestination, byte[] buffer, DatagramPacket packet,
			Marshaller marshaller, DatagramSocket socket, DatagramPacket recPacket, byte[] receivedData)
			throws IOException, SQLException {

		// client.getLock1().lock();

		if (!database.messageInDatabase(message)) {

			message.getVisitedMS().add(myNode.getMsID() + "_" + myNode.getPortNmr());

			database.insertIntoMessageTable(message);
			database.insertIntoMessageStatistic(message);
		}

		packet = marshaller.makeDatagramPacket(message, buffer, ipPortDestination);
		socket.send(packet);


		receivedData = new byte[1024];

		try {
			recPacket = new DatagramPacket(receivedData, receivedData.length);

			socket.receive(recPacket);
			
			try {
				if (!(recPacket.getAddress().getHostAddress() + ":" + recPacket.getPort())
						.equals(ipPortDestination))
					throw new IOException("Received packet from unknown source");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		} catch (InterruptedIOException e) {

			socket.send(packet);

			recPacket = new DatagramPacket(receivedData, receivedData.length);

			try {

				socket.receive(recPacket);

				try {
					if (!(recPacket.getAddress().getHostAddress() + ":" + recPacket.getPort())
							.equals(ipPortDestination))
						throw new IOException("Received packet from unknown source");
				} catch (IOException e3) {
					System.out.println(e3.getMessage());
				}
				if (!(recPacket.getAddress().getHostAddress() + ":" + recPacket.getPort())
						.equals(ipPortDestination))
					throw new IOException("Received packet from unknown source");

			} catch (InterruptedIOException e2) {

			}

		}

	}

	public void anotherSendMethod(byte[] buffer, DatagramPacket packet, Marshaller marshaller, DatagramSocket socket,
			DatagramPacket recPacket, byte[] receivedData) throws IOException, SQLException {


		Map<String, Message> toSendMap = messageForBothConnected.poll();

		if (toSendMap == null)
			return;

		Optional<String> recipientOpt = toSendMap.keySet().stream().findFirst();

		if (recipientOpt.isPresent()) {
			Message toSend = toSendMap.get(recipientOpt.get());
			sendMessage(toSend, recipientOpt.get(), buffer, packet, marshaller, socket, recPacket, receivedData);
		}

	}


	@Override
	public void run() {

		while (true) {

			if (isInterrupted()) {
				connectedIPsPort.clear();
				foundBothMSToConnect = false;
				isInterrupted = false;
			}
		}

	}

}
