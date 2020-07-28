package com.dse.ms4.controller;

import java.util.ArrayList;
import java.util.List;

import com.dse.ms4.*;
import com.dse.ms4.model.BlockedArea;
import com.dse.ms4.rmsExceptions.AreaNotValidException;

import constants.Constraints;
import messages.Field;
import messages.Message;
import messages.Street;

	/**
	 * 
	 * @author Adem
	 * 
	 * Controller where are chosen street blocked and unblocked
	 * 
	 * @param blockedAreas List in which the blocked Areas are stored
	 * @param msID unique ID of the MS consisting of the IP Address and the Port number on witch the MS is available
	 */
public class AreaController {

	private List<BlockedArea> blockedAreas;
	private RmsConnection rms;
	private String msID; 
	
	
	/**
	 *  Empty Constructor with list initialization for testing
	 */
	public AreaController() {
		this.blockedAreas = new ArrayList<>();
	}
	
			
	/**
	 * 
	 * @param rms
	 */
	public AreaController(RmsConnection rms) {
		this.blockedAreas = new ArrayList<>();
		this.rms = rms;
		this.msID = rms.getConnection().getConnection().getMyNode().getIpAddress() + ":" +
					rms.getConnection().getConnection().getMyNode().getPortNmr();
	} 

	
	
	/**
	 * Creates a Blocked Area and inserts it into message that is forwarded to the Client 
	 *  
	 * @param rms 
	 * @param streetName
	 * @param von
	 * @param to
	 * @throws AreaNotValidException 
	 */
	public void blockArea(Street streetName, int von, int to) throws AreaNotValidException {
			BlockedArea area = null ;
			
			area = new BlockedArea(streetName, von, to);
			
			Message blockedStreet = new Message(Constraints.MS1_ID, msID, "block", area.getStreet());
			
			try {
				// System.out.println("Blocked SENT");
				this.rms.getConnection().getConnection().getClient().addMessage(blockedStreet);
			} catch (InterruptedException e) {
				System.out.println("#### Blocked Area Message coudn't be Send #####");
				//e.printStackTrace();
			}
	}

	/**
	 * Unblock a Street if the street is successfully blocked and generates a message with the Street which is than sent to MS1
	 * 
	 * @param rms
	 * @param street
	 */
	public boolean unBlockArea(String street) {
		
		boolean areaState = false;

		for(BlockedArea bArea: blockedAreas)
			if(bArea.getStreet().getName().equals(street)) {
				bArea.unblockFields();
				areaState = true; 
			
				Message unBlockedStreet = new Message(Constraints.MS1_ID, msID, "block" , bArea.getStreet());
				
				try {
					this.rms.getConnection().getConnection().getClient().addMessage(unBlockedStreet);
				} catch (InterruptedException e) {
					System.out.println("#### UnBlocked Area Message coudn't be Send #####");
					//System.err.println(e);
				}
				
				break;
			}
		
		return areaState;
	}

	
	/**
	 * Getter for blockedArea list
	 * @return
	 */
	public List<BlockedArea> getBlockedAreas() {
		return blockedAreas;
	}
	
	
	public synchronized void setBlockedAreas(BlockedArea area) {
		
		boolean hasBlockedArea = false;
		for(BlockedArea bArea: this.blockedAreas) 
			if(bArea.getStreet().getName().equals(area.getStreet().getName()))
				hasBlockedArea = true;
		
		if(!hasBlockedArea)this.blockedAreas.add(area);
	}
	
	
	/**
	 * Method loops the Street and checks if it contains Fields that are not drivable
	 * 
	 * @param street
	 * @return 
	 */
	public boolean checkIfStreetHasBlockedField(Street street) {
		
		for(Field f: street.getFields()){
			if(!f.isDrivable()) return true;
		}
		
		return false;
	}

}
