package controllers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import constants.Constraints;
import exceptions.ConnectionException;
import node.Node;

public class IPAddressHandler {
	
	/**
	 * finds VPN address of a node 
	 * @return
	 */
	public static String findMyIp() {
		
		Enumeration<NetworkInterface> nets;
		String myIPAdress = null;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
			 for (NetworkInterface netint : Collections.list(nets)) {
				 myIPAdress = searchThroughNetworkInterfaces(netint);
				 if(myIPAdress!=null)
					 break;
			 }
		           
		} catch (SocketException e) {
			
			e.printStackTrace();
		}
		
		if(myIPAdress==null||myIPAdress.isEmpty())throw new IllegalArgumentException("Your IP is null or empty string please "
				+ "check your connection to DSE VPN");
		return myIPAdress;
	}
	
	/**
	 * Searches through network interfaces of a machine
	 * @param netint
	 * @return returns String IP if it corresponds to one of constant IPs 
	 * (MS must be connected to VPN) or null 
	 * @throws SocketException
	 */
	private static String searchThroughNetworkInterfaces(NetworkInterface netint) throws SocketException {

		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			if(compareIPWithConstants(inetAddress.getHostAddress()))
				return inetAddress.getHostAddress();

		}
		return null;
	}
	
	/**
	 * Compares handed IP Address with constant MS's IPs
	 * @param ipAddress
	 * @return true if it corresponds
	 */
	private static boolean compareIPWithConstants(String ipAddress) {
		
		if(ipAddress==null||ipAddress.isEmpty())throw new IllegalArgumentException("IP Adress empty or null");
		
		String[] ip1 = Constraints.IP_ADDRESS_1.split(":");
		String[] ip2 = Constraints.IP_ADDRESS_2.split(":");
		String[] ip3 = Constraints.IP_ADDRESS_3.split(":");
		String[] ip4 = Constraints.IP_ADDRESS_4.split(":");
		
		if (ipAddress.equals(ip1[0]) || ipAddress.equals(ip2[0])
				||ipAddress.equals(ip3[0]) || ipAddress.equals(ip4[0]))
			return true;
		return false;
	}
	
	public static List<String> getAllIPsInNetwork() {
		
		List<String>allIPsAndPortStrings = Collections.synchronizedList(new ArrayList<>());
		
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_1);
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_2);
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_3);
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_4);
		
		return allIPsAndPortStrings;
			
	}
	/**
	 * 
	 * Makes a list of all IP addresses from constraints
	 */
	public static List<String> createAllIPsAndPortStrings() {
		
		List<String>allIPsAndPortStrings = new ArrayList<String>();
		
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_1);
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_2);
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_3);
		allIPsAndPortStrings.add(Constraints.IP_ADDRESS_4);
		
		return allIPsAndPortStrings;
			
	}
	
	
}
