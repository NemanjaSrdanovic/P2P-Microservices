package com.dse.ms4.controller;

import com.dse.ms4.model.BlockedArea;

import messages.Car;
import messages.Map;
import messages.Message;
import messages.Street;

public class MessageWorker implements Runnable {
	
	MessageController controller; 
	
	public MessageWorker(MessageController controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		  
			Message message = null;
		
			if((message = (Message) controller.getMessageList().poll()) != null) {
				
					if(message.getData() instanceof Map) {
						
						controller.setMap((Map)message.getData());
						System.out.println("#######     Map And Street Data     #######");
						controller.setFirstMap(true);
					}	
					else if(message.getData() instanceof Car) {
						
						 
						Car car = (Car)message.getData();
						System.out.println("#######    POSITION and PROGRES     #######");
						controller.insertCar(car);
						
					}
					else if(message.getData() instanceof Street) {
						
						
						if(controller.getController().checkIfStreetHasBlockedField((Street)message.getData())) {
						System.out.println("#######       Block Street     #######");
						 	//System.out.println("messageID : " + message.getMessageId());
							BlockedArea area = new BlockedArea((Street)message.getData());
							
							controller.getController().setBlockedAreas(area);
						}
						else {
							System.out.println("#######     Unblock Street     #######");			
							for(BlockedArea bArea: controller.getController().getBlockedAreas())
								if(bArea.getStreet().getName().equals(((Street) message.getData()).getName() )) {
									controller.getController().getBlockedAreas().remove(bArea);
									break;
								}
							
						}
						
						
						
					}
			}

		
	}
}
