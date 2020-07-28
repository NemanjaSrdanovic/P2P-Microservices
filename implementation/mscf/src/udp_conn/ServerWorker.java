package udp_conn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import constants.Constraints;
import messages.Message;
import parsers.Marshaller;

public class ServerWorker implements Runnable {

	Message message;
	private UDPServer udpServer;
	private DatagramPacket requestPacket;
	private DatagramPacket packetToSend;
	private byte[] buffer;
	private List<String> tempVisitedMS;
	private Marshaller marshaller;

	public ServerWorker(UDPServer udpServer) {

		this.udpServer = udpServer;
		this.tempVisitedMS = new ArrayList<String>();
		this.marshaller= new Marshaller();

	}

	public void setTempVisited(List<String> input) {

		tempVisitedMS = input;
	}

	@Override
	public void run() {

		String receivedMSg;

		while (true) {
			buffer = new byte[20000];
			this.requestPacket = new DatagramPacket(buffer, buffer.length);

			try {
				udpServer.getSocket().receive(this.requestPacket);
				
				Object recObject = marshaller.makeObjectFrom(requestPacket);

				// Greeting/String received
				if (recObject instanceof String) {
					receivedMSg = (String) recObject;

				}
				// Message received
				if (recObject instanceof Message) {

					message = (Message) recObject;

					//Is message in database?
					if (!udpServer.getDatabase().messageInDatabase(message)) {

						String destinatedMS = message.getDestinatedMS();
						Optional<String> destinationIPOpt = Optional.ofNullable(message.getDestination());

						//Message is for your MS and not for MS2?
						if (!(destinatedMS.equals(Constraints.MS2_ID)) &&
								destinatedMS.equals(udpServer.getMyNode().getMsID())){

							message.getVisitedMS()
									.add(udpServer.getMyNode().getMsID() + "_" + udpServer.getMyNode().getPortNmr());
							
							udpServer.getDatabase().insertIntoMessageTable(message);
							udpServer.getDatabase().insertIntoMessageStatistic(message);
							udpServer.getMessageProcessor().onMessage(message);

						}

						//Message is for MS2
						else {

							if (destinationIPOpt.isPresent()) {

								String destinationIP = destinationIPOpt.get();
								String[] ipPort = destinationIP.split(":");

								//Is message for MS2 and for your port?
								if (destinatedMS.equals(Constraints.MS2_ID) && ipPort[1] != null
										&& ipPort[1].equals("" + udpServer.getMyNode().getPortNmr())) {

									message.getVisitedMS().add(
											udpServer.getMyNode().getMsID() + "_" 
									+ udpServer.getMyNode().getPortNmr());

								udpServer.getDatabase().insertIntoMessageTable(message);
								udpServer.getDatabase().insertIntoMessageStatistic(message);
								udpServer.getMessageProcessor().onMessage(message);
								
								}

							}
							
							//Message is for MS2 but not for your port
							this.udpServer.getClient().addMessage(message);

						}
						
					} else {

						// Message is in DB, so message is droped
					}
				}
				
	

				Arrays.fill(buffer, (byte) 0);
				String toResponse = "200 OK";

				buffer = toResponse.getBytes();

				packetToSend = new Marshaller().makeDatagramPacket(toResponse, buffer,
						requestPacket.getAddress().getHostAddress() + ":" + Integer.toString(requestPacket.getPort()));

				udpServer.getSocket().send(packetToSend);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
