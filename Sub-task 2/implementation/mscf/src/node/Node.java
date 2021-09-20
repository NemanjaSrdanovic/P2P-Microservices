package node;

import constants.Constraints;
import exceptions.ConnectionException;

public class Node {
	
	/**
	 * Class Node saves ip address and port number
	 */
	
	private String ipAddress;
	private int portNmr;
	private String msID;
	
	public Node(String ipAddress, int portNmr, String msID){
		
		isValid(portNmr);
		
		this.ipAddress = ipAddress;
		this.portNmr = portNmr;
		this.msID = msID;
	}
	
	
	public String getIpAddress() {
		return ipAddress;
	}


	public int getPortNmr() {
		return portNmr;
	}
	
	public String getMsID() {
		return msID;
	}


	public void setPortNmr(int portNmr) {
		this.portNmr = portNmr;
	}


	/**
	 * Validates passed ip address and port number, they must be handed in accordance 
	 * to Constraints class which holds constant IPaddress and port number
	 * @param ipAddress
	 * @param portNmr
	 */
	private boolean isValid(int portNmr) {
		
		if(portNmr<Constraints.PORTNUM_MIN||portNmr>Constraints.PORTNUM_MAX) 
			throw new ConnectionException("Illegal port number", "Port number can't be smaller than "
					+Constraints.PORTNUM_MIN+ " and bigger than "+ Constraints.PORTNUM_MAX);
		
		return true;
	}
	
	@Override
	public String toString() {
		
		return "IPAddress: "+ this.ipAddress+ " port: "+ portNmr;
	}
	
}
