package com.nmm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import parsers.Marshaller;

public class SelectDB {

	Connection connection;
	Statement statement;
	String database;

	public SelectDB(String base) {
		try {
			this.database = base;
			connection = DriverManager.getConnection("jdbc:sqlite:.\\" + database);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* GET ALL MESSAGES */
	@SuppressWarnings("unchecked")
	public List<com.nmm.entities.Message> getAllMessages() {
		List<com.nmm.entities.Message> allMessages = new ArrayList<com.nmm.entities.Message>();

		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM messages");
			while (rs.next()) {
				String id = rs.getString("id");
				String messageId = rs.getString("messageID");
				String sourceIP = rs.getString("source");
				String destinationIP = rs.getString("destination");
				String destinatedMS = rs.getString("destinatedMS");
				byte[] visitedMS = rs.getBytes("visitedMS");

				com.nmm.entities.Message message = new com.nmm.entities.Message();
				message.setId(Integer.parseInt(id));
				message.setMessageID(messageId);
				message.setSource(sourceIP);
				message.setDestination(destinationIP);
				message.setDestinatedMS(destinatedMS);
				Marshaller marshaller = new Marshaller();
				Object transformedArray = marshaller.transformByteToObject(visitedMS);
				message.setVisited((List<String>) transformedArray);
				allMessages.add(message);
			}

			rs.close();
			statement.close();
			connection.commit();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return allMessages;
	}

	/* GET ALL DIAGRAMS */
	@SuppressWarnings("unchecked")
	public List<com.nmm.entities.Message> getAllDiagrams() {
		List<com.nmm.entities.Message> allDiagrams = new ArrayList<com.nmm.entities.Message>();
		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM diagrams");
			while (rs.next()) {
				String id = rs.getString("id");
				String messageId = rs.getString("messageID");
				String sourceIP = rs.getString("source");
				String destinationIP = rs.getString("destination");
				String destinatedMS = rs.getString("destinatedMS");
				byte[] visitedMS = rs.getBytes("visited");

				com.nmm.entities.Message message = new com.nmm.entities.Message();
				message.setId(Integer.parseInt(id));
				message.setMessageID(messageId);
				message.setSource(sourceIP);
				message.setDestination(destinationIP);
				message.setDestinatedMS(destinatedMS);
				Marshaller marshaller = new Marshaller();
				Object transformedArray = marshaller.transformByteToObject(visitedMS);
				message.setVisited((List<String>) transformedArray);
				allDiagrams.add(message);
			}

			rs.close();
			statement.close();
			connection.commit();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return allDiagrams;
	}

	/* GET ALL DROPS */
	@SuppressWarnings("unchecked")
	public List<com.nmm.entities.Message> getAllDrops() {
		List<com.nmm.entities.Message> allDrops = new ArrayList<com.nmm.entities.Message>();
		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM messages_drop");
			while (rs.next()) {
				String id = rs.getString("id");
				String messageId = rs.getString("messageID");
				String sourceIP = rs.getString("source");
				String destinationIP = rs.getString("destination");
				String destinatedMS = rs.getString("destinatedMS");
				byte[] visitedMS = rs.getBytes("visited");

				com.nmm.entities.Message message = new com.nmm.entities.Message();
				message.setId(Integer.parseInt(id));
				message.setMessageID(messageId);
				message.setSource(sourceIP);
				message.setDestination(destinationIP);
				message.setDestinatedMS(destinatedMS);
				Marshaller marshaller = new Marshaller();
				Object transformedArray = marshaller.transformByteToObject(visitedMS);
				message.setVisited((List<String>) transformedArray);
				allDrops.add(message);
			}

			rs.close();
			statement.close();
			connection.commit();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return allDrops;
	}

	/************** CLEAR DROP MESSAGES TABLE *****************************/
	public void clearDrop() {
		String sqlClear = "DELETE FROM messages_drop ";
		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(sqlClear);
			statement.close();
			connection.commit();
			System.out.println("------------------------");
			System.out.println("TABLE DROP CLEARD!");
			System.out.println("------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/************** CLEAR DIAGRAMS MESSAGE TABLE *****************************/
	public void clearDiagram() {
		String sqlClear = "DELETE FROM diagrams ";
		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(sqlClear);
			statement.close();
			connection.commit();
			System.out.println("------------------------");
			System.out.println("TABLE DIAGRAM CLEARD!");
			System.out.println("------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}