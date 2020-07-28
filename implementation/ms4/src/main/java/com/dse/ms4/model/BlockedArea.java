package com.dse.ms4.model;

import com.dse.ms4.rmsExceptions.AreaNotValidException;

import messages.Street;

public class BlockedArea {
	private Street street; 
	private int vonField; 
	private int toField;
	
	
	public BlockedArea(Street street) {
		this.street = street;	
	}
	
	public BlockedArea(Street street, int vonField, int toField) throws AreaNotValidException {
		super();
		if(street.getFields().size() < vonField || street.getFields().size() < toField || vonField < 0 || toField < 0) throw new AreaNotValidException("Area is NOT Valid");
		this.street = street;
		this.vonField = vonField;
		this.toField = toField;
		
		blockFields();
	}

	
	public Street getStreet() {
		return street;
	}

	
	public void setStreet(Street street) {
		this.street = street;
	}
	
	
	/**
	 * Blocks Field in the street
	 *
	 */
	private void blockFields() {
		
		if(vonField > toField) vonField = vonField^toField^(toField = vonField);
		
		for(int i = 0; i < this.street.getFields().size(); i++)
			if(i >= this.vonField )
				if(i <= this.toField) 
					this.street.getFields().get(i).setDrivable(false);
	}

	/**
	 * Setting all fields on Drivable in the Street
	 * @param street2
	 */
	public void unblockFields() {
		for(int i = 0; i < this.street.getFields().size(); i++)
			this.street.getFields().get(i).setDrivable(true);
		
	}
	
}
