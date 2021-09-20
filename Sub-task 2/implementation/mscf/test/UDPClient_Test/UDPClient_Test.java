package UDPClient_Test;


import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import connection.Connection;
import constants.Constraints;
import controllers.IPAddressHandler;
import database.DriverClass;
import udp_conn.UDPClient;

@RunWith(JUnitPlatform.class)
class UDPClient_Test {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test_random_ip_port_to_connect() throws SocketException, UnknownHostException {
		String myPort = "3030";
		
		Connection connection = new Connection(myPort, Constraints.MS1_ID);
		String myIP = IPAddressHandler.findMyIp();
		node.Node myNode = new node.Node(myIP, Integer.parseInt(myPort), Constraints.MS1_ID);
		List<String>allIPsInNetwork= IPAddressHandler.getAllIPsInNetwork();
		DriverClass database= new DriverClass(Integer.parseInt(myPort));
		
		UDPClient client = new UDPClient(allIPsInNetwork, myNode, database);
		
		while(true) {
			System.out.println(client.randomIPAddressAndPortToConnect());
		}
		
	}

}
