package com.dse.ms4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dse.ms4.controller.AreaController;
import com.dse.ms4.controller.MessageController;

import messages.Car;
import messages.Coordinate;
import messages.Field;

class MesssageControllerTest {
	
	HashMap<Integer, Car> carList;

	MessageController messageControllerTest = null;
	RmsConnection rmsTest = new RmsConnection(3030);
	
	AreaController areaControllerTest = new AreaController(rmsTest);
	
	Car car1 = new Car(1);
	Car car2 = new Car(2);
	
	Coordinate d = new Coordinate(0, 3);
	Coordinate e = new Coordinate(1, 3);
	Coordinate f = new Coordinate(2, 3);
	Coordinate g = new Coordinate(3, 3);
	Field destination = new Field(g);
	Field position = new Field(d);
	Field b = new Field(e);
	Field c = new Field(f);
	
	List<Field> carRoute = new ArrayList<Field>();
	

	@BeforeEach
	void setUp() throws Exception {
		
		carRoute.add(position);
		carRoute.add(b);
		carRoute.add(c);
		carRoute.add(destination);
		
		position.setDrivable(true);
		destination.setDrivable(true);
		b.setDrivable(true);
		c.setDrivable(true);
		
		car1.setCarRoute(carRoute);
		car2.setCarRoute(carRoute);
		
	}
	
	@Test
	public void MesssageControllerTest() {
		messageControllerTest = new MessageController();
		messageControllerTest = new MessageController(rmsTest, areaControllerTest);
	}
}
