package udp_conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import messages.Message;
import parsers.Marshaller;

public class ClientWorker  implements Runnable{
	
	private UDPClient client;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private DatagramPacket recPacket;
	private byte[]buffer;
	private byte[]receivedData;
	private Marshaller marshaller;
	
	public ClientWorker(UDPClient client) throws SocketException {
		
		this.client = client;
		this.marshaller = new Marshaller();
	}

	@Override
	public void run() {
		while (true) {
			String ipPortOfRecipient;

			try {
				
				socket = new DatagramSocket();
				

				if (!client.isFoundBothMSToConnect()) {
					socket.setSoTimeout(200);
					ipPortOfRecipient = client.randomIPAddressAndPortToConnect();
					sendGreeting(ipPortOfRecipient);
				} 
				else {
					socket.setSoTimeout(600);
					client.anotherSendMethod(buffer, packet, marshaller, socket, recPacket, receivedData);

						Message message = client.getMessage();
						
						if(message!=null) {
							
							client.sendToDestination(message, buffer, packet, marshaller, socket, recPacket, receivedData);							
						}	
				}

			} catch (IOException | SQLException e) {
				System.err.println("IO Exception");
				e.printStackTrace();
			}  finally {
				socket.close();

			}

		}
	}
	
	private void sendGreeting(String ipPortOfRecipient) throws IOException {

		String message = "Hello";
		buffer = message.getBytes();

		packet = marshaller.makeDatagramPacket(message, buffer, ipPortOfRecipient);
		socket.send(packet);
		
		receivedData = new byte[1024];
		this.recPacket = new DatagramPacket(receivedData, receivedData.length);
		try {

			socket.receive(this.recPacket);
			try {
				if (!recPacket.getAddress().getHostAddress().equals(ipPortOfRecipient.split(":")[0]))
					throw new IOException("Received packet from unknown source");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			

			client.setIPandPortOfRandomMSs(ipPortOfRecipient, this.recPacket);

		} catch (InterruptedIOException e) {

		}		
		
	}
}
