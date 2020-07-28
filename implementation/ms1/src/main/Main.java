package main;

import constants.Constraints;
import controller.ARSController;
import exceptions.ARSException;

/**
 * Main Class
 * @author nenad.cikojevic
 *
 */
public class Main {

	public static void main(String[] args) throws ARSException, InterruptedException {

		String port = "3022";
		String msID = Constraints.MS1_ID;
		ARSController arsController = new ARSController(port, msID);

		arsController.start();
		

	}

}
