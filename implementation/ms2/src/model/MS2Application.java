
package model;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Timer;

import constants.Constraints;
import controllers.CarController;
import controllers.ConnectionHandler;
import controllers.MS2Controller;
import exceptions.CDGException;
import statistic_sender.StatisticSender;

/**
 * The MS2Application class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-06-09
 */
public class MS2Application {

	/*-------------------------------------VARIABLES-------------------------------------*/
	private int portNumber;
	private ConnectionHandler connection;
	private MS2Controller controllerMS2;
	private CarCDG car;
	private CarController controller;

	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	public MS2Application() throws InterruptedException, CDGException, SocketException, UnknownHostException {

		Random random = new Random();
		int port = random.nextInt(10) + 3020;

		this.portNumber = port;

		String msID = Constraints.MS2_ID;
		this.connection = new ConnectionHandler(Integer.toString(portNumber), msID);
		connection.connect();

		this.controllerMS2 = new MS2Controller(connection);

		StatisticSender sender = new StatisticSender(connection.getConnection(),controllerMS2);
		Timer timer = new Timer();
		timer.schedule(sender, 4000, 4000);

		this.car = new CarCDG(port);
		this.controller = new CarController(car, controllerMS2);

	}

	/*------------------------------------- GETTERS -------------------------------------*/

	/**/
	public ConnectionHandler getConnection() {
		return connection;
	}

	public CarController getController() {
		return controller;
	}

	public void setController(CarController controller) {
		this.controller = controller;
	}

}
