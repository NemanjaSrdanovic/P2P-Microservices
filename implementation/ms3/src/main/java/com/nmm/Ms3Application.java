package com.nmm;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nmm.controllers.NMMController;
import com.nmm.database.ConnectionDB;
import com.nmm.entities.Message;

import constants.Constraints;
import controllers.ConnectionHandler;

@SpringBootApplication
public class Ms3Application {

	public static void main(String[] args)
			throws SocketException, UnknownHostException, InterruptedException, ClassNotFoundException, SQLException {
		SpringApplication.run(Ms3Application.class, args);
		@SuppressWarnings("unused")
		ConnectionDB database = new ConnectionDB();
		String msID = Constraints.MS3_ID;
		String port = "3021";
		ConnectionHandler connectionHandler = new ConnectionHandler(port, msID);
		connectionHandler.connect();
		@SuppressWarnings("unused")
		NMMController nmm = new NMMController(connectionHandler);
	}
}
