package com.dse.ms4.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.Map.Entry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dse.ms4.RmsConnection;
import com.dse.ms4.model.BlockedArea;
import com.dse.ms4.rmsExceptions.AreaNotValidException;
import com.dse.ms4.sender.StatisticSender;

import messages.Car;
import messages.Coordinate;
import messages.Field;
import messages.Street;


@RestController
public class RmsController{
	private AreaController aController; 
	private RmsConnection rms;
	private MessageController mController; 
	private List<String> blockedStreets; 
	


	public RmsController() {
		rms = new RmsConnection(3022);
		aController = new AreaController(rms);
		mController = new MessageController(rms, aController);
		
		
		StatisticSender sender = new StatisticSender(rms.getConnection().getConnection(), mController);
		Timer timer = new Timer();
		timer.schedule(sender, 4000, 4000);
	}
	
	
	/**
	 *  GET Method that returns car s and their Routes to the CLI 
	 * @return
	 * 
	 * 
	 */
	@GetMapping("/carRoutes")
	public @ResponseBody HashMap<Integer, List<Coordinate>> getCarRoutes() {
				
		HashMap<Integer, List<Coordinate>> carRoutes = new HashMap<Integer, List<Coordinate>>();
		
		for(Entry<Integer, Car> m : mController.getCarList().entrySet()) {
			List<Coordinate> c = new ArrayList<Coordinate>(); 
			for(Field a : m.getValue().getCarRoute())
				c.add(a.getCoordinate());
			
			carRoutes.put(m.getKey(), c);
		}

		return carRoutes;
	} 
	
	/**
	 * Get Method to get all car IDs that are on the map 
	 *
	 */
	@GetMapping("/carIDs")
	public @ResponseBody List<Integer> getCarIds(){
		
		List<Integer> carIds = new ArrayList<Integer>();
		
		for(Entry<Integer, Car> m : mController.getCarList().entrySet()) {
			carIds.add(m.getKey());
		}
		
		return carIds;
	}
	
	/**
	 *  Methode to get the progress of a car, fields he traveled and number of fields the car still has to travel
	 * @param id
	 * @return
	 * 
	 * DONE
	 */
	@GetMapping("/progress/{id}")
	public @ResponseBody List<Integer> travelProgress(@PathVariable int id) {
	
		List<Integer> carProgress = new ArrayList<Integer>();
		Car car = mController.getCarList().get(id);
		if(car != null) {
			carProgress.add(car.getTraveledFieldsPerRoute());
			carProgress.add(car.getCarRoute().size() - 1);
		}else carProgress.add(0);
		
		return carProgress;
	}
	
	
	/**
	 * Returns a List of all streets to the CLI so that they can be shown and if wanted Blokced
	 * @return
	 */
	@GetMapping("/allStreets")
	public @ResponseBody HashMap<String, List<Coordinate>> getStreetNames() {
		
		HashMap<String, List<Coordinate>> streetNames = new HashMap<String, List<Coordinate>>();
		
		for(Street s: mController.getMap().getStreets()) {
			List<Coordinate> c = new ArrayList<Coordinate>();
			for (int i = 0; i < s.getFields().size() - 1; i++) {
				c.add(s.getFields().get(i).getCoordinate());
			}
			streetNames.put(s.getName(), c);
		}
		
		return streetNames;
	}
	
	
	/**
	 * 
	 * When a User enters a Street name and the Two integers From and To and blockes the specified Fields
	 * 
	 * @param streetName
	 * @param von
	 * @param to
	 * @return
	 */
	@PostMapping("/blockArea/{streetName}/{von}/{to}")
	public @ResponseBody String blockArea(@PathVariable String streetName, @PathVariable int von, @PathVariable int to) {
		
		String state = new String("Something went WRONG! TRY AGAIN");
		
		for(Street s: mController.getMap().getStreets()) 
			if(s.getName().equals(streetName)) {
				try {
					aController.blockArea(s, von, to);
				} catch (AreaNotValidException e) { 
					return state;
				}
			state = "Area was sent to be blocked";
		}
		
		return state;
	}
	
	/**
	 * return Names of all Blocked Streets to CLI
	 * @return
	 */
	@GetMapping("/blockedStreets")
	public @ResponseBody List<String> getBlockedStreets() {
		
		blockedStreets = new ArrayList<String>();
		
		for(BlockedArea area: aController.getBlockedAreas()) {
			blockedStreets.add(area.getStreet().getName());
		}
		
		return blockedStreets;
	}
	
	
	/**
	 * 
	 * invokes the method in AreaController which unblocks all Fields of the street
	 * that has the name as the received parameter
	 *  
	 * 
	 * @param street
	 * @return
	 */
	@PostMapping("/unblock/{street}")
	public @ResponseBody String unBlockArea(@PathVariable String street) {
		
		if(blockedStreets.contains(street))
			if(aController.unBlockArea(street))
				return "Street was Sent to be Unblocked";
		
		return "Something went WRONG! - TRY AGAIN";
	}

}
