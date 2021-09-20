package com.nmm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import com.nmm.entities.Message;
import parsers.Marshaller;

public class InsertDB {
	Connection connection;
	Connection connection_visited;
	Statement statement;
	String database;

	public InsertDB(String base) {
		try {
			this.database = base;
			connection = DriverManager.getConnection("jdbc:sqlite:.\\" + database);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/******************
	 * INSERT IN MESSAGES TABLE
	 **************************************/
	public void insertMessageTable(Message message) throws SQLException {
		System.out.println("INSERT TO MS3 -> MESSAGES: " + message);
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO messages values(?,?,?,?,?,?)");
		stmt.setLong(1, message.getId());
		stmt.setString(2, message.getMessageID());
		stmt.setString(3, message.getSource());
		stmt.setString(4, message.getDestination());
		stmt.setString(5, message.getDestinatedMS());

		Marshaller marshaller = new Marshaller();
		byte[] transformedArray = marshaller.transformObjectToByte(message.getVisited());

		stmt.setBytes(6, transformedArray);
		// connection.setAutoCommit(false);
		stmt.executeUpdate();
		System.out.println("*#*#*#*#*#*#*#*#*#*#*#*#");
		System.out.println("ADD MESSAGE TO MESSAGES");
		System.out.println("*#*#*#*#*#*#*#*#*#*#*#*#");
		// connection.commit();
	}

	/****************
	 * INSERT IN DIAGRAMS TABLE
	 ****************************************************/
	public void insertDiagramTable(Message message) throws SQLException {
		System.out.println("INSERT TO MS3 -> DIAGRAMS: " + message);
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO diagrams values(?,?,?,?,?,?)");
		stmt.setLong(1, message.getId());
		stmt.setString(2, message.getMessageID());
		stmt.setString(3, message.getSource());
		stmt.setString(4, message.getDestination());
		stmt.setString(5, message.getDestinatedMS());

		Marshaller marshaller = new Marshaller();
		byte[] transformedArray = marshaller.transformObjectToByte(message.getVisited());

		stmt.setBytes(6, transformedArray);
		// connection.setAutoCommit(false);
		stmt.executeUpdate();
		System.out.println("*#*#*#*#*#*#*#*#*#*#*#*#");
		System.out.println("ADD MESSAGE TO DIAGRAMS");
		System.out.println("*#*#*#*#*#*#*#*#*#*#*#*#");
		// connection.commit();
	}

	/*********************************
	 * INSERT IN DROP MESSAGES TABLE
	 **************************************/
	public void insertDropTable(Message message) throws SQLException {
		System.out.println("INSERT TO MS3 -> DROPS: " + message);
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO messages_drop values(?,?,?,?,?,?)");
		stmt.setLong(1, message.getId());
		stmt.setString(2, message.getMessageID());
		stmt.setString(3, message.getSource());
		stmt.setString(4, message.getDestination());
		stmt.setString(5, message.getDestinatedMS());

		Marshaller marshaller = new Marshaller();
		byte[] transformedArray = marshaller.transformObjectToByte(message.getVisited());

		stmt.setBytes(6, transformedArray);
		// connection.setAutoCommit(false);
		stmt.executeUpdate();
		System.out.println("*#*#*#*#*#*#*#*#*#*#*#*#");
		System.out.println("ADD MESSAGE TO DROPS");
		System.out.println("*#*#*#*#*#*#*#*#*#*#*#*#");

		// connection.commit();
	}
}
