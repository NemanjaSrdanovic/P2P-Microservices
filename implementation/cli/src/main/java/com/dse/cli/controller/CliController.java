package com.dse.cli.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import model.Coordinate;

@Controller
public class CliController {
	private HashMap<Integer, List<Coordinate>> carRoutes;
	private RestTemplate restTemplate = new RestTemplate();
	private List<Integer> carIds; 

	public CliController() {}
	
	
	@SuppressWarnings("unchecked")
	public void displayAllCarRoutes() {
		try {
			carRoutes = (HashMap<Integer, List<Coordinate>>) restTemplate.getForObject("http://localhost:8084/carRoutes/", Object.class);
			if(carRoutes != null){
				for(Entry<Integer, List<Coordinate>> m : carRoutes.entrySet()) {
					System.out.println("Car: " + m.getKey());
					System.out.println("Route: " + m.getValue());
				}
			}
			
		}catch (Exception e) {
			System.err.println("\nMS4 is Offline or Map has not arrived!");
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void displayProgress(Scanner scanner) {
		try {
			carIds = (List<Integer>) restTemplate.getForObject("http://localhost:8084/carIDs/", Object.class);
			
			//System.out.println("\n");
			for(int i: carIds) {
				//System.out.println(i);
				List<Integer> response = (List<Integer> ) restTemplate.getForObject("http://localhost:8084/progress/" + i, Object.class);
			
				System.out.println("Car: " + i + " Has traveled: " + response.get(0)  + " and has: " 
							+ response.get(1) + " Fields to travel on current Route!");
			}
			
			
		} catch (Exception e) {
			System.err.println("\nMS4 is Offline or Map has not arrived!\"");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void blockArea(Scanner scanner) {
		try {
			HashMap<String, List<Coordinate>> streetNames = (HashMap<String, List<Coordinate>>) restTemplate.getForObject("http://localhost:8084/allStreets/", Object.class);
			if(streetNames.keySet().isEmpty()) {
				System.err.println("Map Data Has not Arrived");
				return;
			}
			
			for(Entry<String, List<Coordinate>> m : streetNames.entrySet()) {
				System.out.println("\n\nStreet Name: " + m.getKey() + ", Enter between FROM 0 TO " + (m.getValue().size()) + "\n");
				for (int i = 0; i < m.getValue().size(); i++) {
					if(i != 0) System.out.print("| ");		
					if(i % 3 == 0 && i != 0) System.out.println("");
					System.out.print("Field Number: " + i +" Coordinate: ");
					System.out.print(m.getValue().get(i));				
				}
				//System.out.println("\n");
			}
			
		} catch (Exception e) {
			System.err.println("\nMS4 is Offline or Map has not arrived!");
			return;
		}
		
		
		try {
			carRoutes = (HashMap<Integer, List<Coordinate>>) restTemplate.getForObject("http://localhost:8084/carRoutes/", Object.class);
			System.out.println("\n");
			for(Entry<Integer, List<Coordinate>> m : carRoutes.entrySet()) {
				System.out.println("Car: " + m.getKey() + " Route: " + m.getValue());
			}
		} catch (Exception e) {
			System.err.println("\nMS4 is Offline or Map has not arrived!\n");
		}
	
		String option = null;
		boolean looping = true; 
		
		while(looping) {
			System.out.println("Insert a STREET NAME the FROM and TO fields as NUMBERS with  -  between \nEXAMPLE 2nd_Avenue-4-6\n");
			System.out.println("'Break' to go back");
			System.out.println("Type an option: ");			
			
			try {
				option = scanner.next();
			} catch (Exception e) {
				return;
			}
				
			/**
			 * Depending on user input, proper request to the MS2 will be sent
			 */
			switch(option) {
			
				case "Break":
					looping = false;
				break;
				default:
					String[] data = option.trim().split("-");
									
					try {
						String state = restTemplate.postForObject("http://localhost:8084/blockArea/" + data[0] + "/" + data[1] + "/" + data[2], null, String.class);
						System.out.println(state);
					} catch (Exception e) {
						System.err.println("Street Name or From/To was not valid");
						continue;
					}
			}
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void unBlockArea(Scanner scanner) {
		List<String> streetNames = new ArrayList<String>();
		
		try {
			streetNames = (List<String>) restTemplate.getForObject("http://localhost:8084/blockedStreets/", Object.class);
			System.out.println("### Successfully Blocked Streets  ###\n");
			for(String streetName : streetNames)
				System.out.println("Blocked Street: " + streetName);
		} catch (Exception e) {
			System.err.println("\nMS4 is Offline or Map has not arrived!\n");
			return;
		}
		
		if(streetNames.isEmpty()) {
			System.out.println("There are no Blocked Areas");
			return;
		}
			
		String option = null;
		boolean looping = true; 
		
		while(looping) {
			System.out.println("\nInsert a Street Name that should be Un-Blocked");
			System.out.println("'Break' to go back");
			System.out.println("Type an option: ");
			
			try {
				option = scanner.next();
			} catch (Exception e) {
				System.err.println("\nEnter a String");
				return;
			}
			
				
			/**
			 * Depending on user input, proper request to the MS2 will be sent
			 */
			switch(option) {
			
				case "Break":
					looping = false;
				break;
				default:
						
					try {
						restTemplate.postForObject("http://localhost:8084/unblock/" + option, null, String.class);
						System.out.println("\n" + option + " was sent to be Unblocked ");
					} catch (Exception e) {
						System.out.println("There war an ERROR, try again!");
					}
					

			}
		
		
		}
	}
}
