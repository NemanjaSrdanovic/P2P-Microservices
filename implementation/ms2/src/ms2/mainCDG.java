package ms2;

import java.net.SocketException;

import java.net.UnknownHostException;

import exceptions.CDGException;
import model.MS2Application;

public class mainCDG {

	public static void main(String[] args)
			throws InterruptedException, CDGException, SocketException, UnknownHostException {

		MS2Application CDG = null;

		while (CDG == null) {

			try {
				CDG = new MS2Application();
			} catch (SocketException e) {

				System.out.println(
						"<------------------------- UDPServer Exception, Port in use ------------------------->");
				System.out.println("Restarting process!");
			}
		}
	}
}
