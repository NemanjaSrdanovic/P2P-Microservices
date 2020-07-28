package com.dse.ms4.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.dse.ms4.RmsConnection;

import constants.Constraints;
import messageProcessor.MessageProcessor;
import messages.Car;
import messages.Map;
import messages.Message;


public class MessageController implements MessageProcessor {
	private RmsConnection rms;
	private Map map; 
	private String msID;
	private HashMap<Integer, Car> carList;
	private BlockingQueue<Message> messageList;
	private List<Message> messageMS3; 
	private ExecutorService executor; 
	private Boolean firstMap; 
	private Message firstMapRequest;
	private AreaController controller;
	 
	public MessageController() {
		this.carList = new HashMap<Integer, Car>();
	} 

	public MessageController(RmsConnection rms, AreaController controller) {
		this.map = new Map();
		this.rms = rms;		
		this.msID = rms.getConnection().getConnection().getMyNode().getIpAddress() + ":" + rms.getConnection().getConnection().getMyNode().getPortNmr();
		this.carList = new HashMap<Integer, Car>();
		this.messageList = new LinkedBlockingQueue<Message>();
		this.firstMap = false;
		this.controller = controller;
		this.messageMS3 = new CopyOnWriteArrayList<Message>();
		
		rms.getConnection().setMessageProcessor(this);
		automaticMessages();
		
		executor = Executors.newFixedThreadPool(4);
	}


	/**
	 * Generate request Message for Map data from MS1 every 5 seconds until the map arrives back
	 * 	
	 */
	private void automaticMessages() {
	
		Timer t = new Timer();
		t.schedule(new TimerTask() {
		    @Override
		    public void run() {
		    	
		    	System.out.println("Poslano");
		    	if(!firstMap) {
		    		firstMapRequest = new Message(Constraints.MS1_ID, msID, "streets");
					try {
						rms.getConnection().getConnection().getClient().addMessage(firstMapRequest);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    	
		    }
		}, 1, 5 * 1000);
		
	}
	
	
	public Map getMap() {
		return map;
	}

	
	public synchronized void setMap(Map map) {
		this.map = map;
	}

	
	/**
	 * If the Car is not already in the car list it will be added otherwise it replaces the already existing one
	 * @param car
	 */
	public synchronized void insertCar(Car car) {
		
		if(carList.containsKey(car.getCarId())) carList.replace(car.getCarId(), car);
		else carList.put(car.getCarId(), car);
		
		carList.put(car.getCarId(), car);
	}
	
	/**
	 * return the List with car data 
	 * @return
	 */
	public HashMap<Integer, Car> getCarList() {
		return carList;
	}
	
	
	/**
	 *	Puts the received message from server into a BlockingQueue 
	 */
	@Override
	public synchronized void onMessage(Message message) {
		
		try {
			getMessageList().put(message);
			executor.execute(new MessageWorker(this));
		} catch (InterruptedException e) {
			System.err.println("####  On Message Error  ####");
		}
	}
	
	
	public BlockingQueue<Message> getMessageList() {
		return messageList;
	}


	public Boolean getFirstMap() {
		return firstMap;
	}


	public void setFirstMap(Boolean firstMap) {
		this.firstMap = firstMap;
	}
	
	
	public AreaController getController() {
		return controller;
	}

	/**
	 * Send Timed Messages from database to MS3
	 * @param id
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized boolean statisticReceived(String id) throws InterruptedException {
		int timeOut = 0;
		
		while (true) {
			
		 if(timeOut == 8) {
			 return false;
		
		 }	

			if (!messageMS3.isEmpty()) {
				for (Message message : messageMS3) {
					if (message.getResponseId().equals(id)) {
						return true;

					}
				}
			}

			++timeOut;
			Thread.sleep(2000);
		}
	}

}
