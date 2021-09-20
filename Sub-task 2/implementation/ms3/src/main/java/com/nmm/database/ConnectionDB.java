package com.nmm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.nmm.entities.Message;

public class ConnectionDB {

	Connection connection;
	Statement statement;
	String database;
	InsertDB insert;
	SelectDB select;

	/*************** CONNECT TO DATABASE ****************************************/
	/*************** ON NEW RUN OF APPLICATION CLEAR TABLES ********************/
	/*************** ALWAYS EMPTY AT START ************************************/

	public ConnectionDB() throws ClassNotFoundException {
		try {
			this.database = "DatabaseMessages" + ".db";
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:.\\" + database);
			connection.setAutoCommit(false);
			this.createMessageTable();
			this.clearAllTables();
			insert = new InsertDB(database);
			select = new SelectDB(database);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*************** CREATE TABLES *****************************************/
	public void createMessageTable() {

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:.\\" + database);

			connection.setAutoCommit(false);
			statement = connection.createStatement();
			/***************
			 * CREATE TABLE FOR ALL MESSAGES
			 ***********************************/
			statement.execute("CREATE TABLE IF NOT EXISTS messages ( id	INTEGER NOT NULL,"
					+ "messageID	VARCHAR(255) NOT NULL, " + "source	VARCHAR(255) NOT NULL,"
					+ "destination	VARCHAR(255)," + "destinatedMS	VARCHAR(255) NOT NULL,"
					+ "visitedMS VARBINARY NOT NULL," + "PRIMARY KEY('id'));");
			System.out.println("MESSAGE TABLE CREATED IN DATABASE");
			/***************
			 * CREATE TABLE FOR ALL SUCCESSFULLY RECEIVED MESSAGES
			 *****************************/
			statement.execute("CREATE TABLE IF NOT EXISTS diagrams ( id	INTEGER," + "messageID	VARCHAR(255),"
					+ "source	VARCHAR(255)," + "destination	VARCHAR(255)," + "destinatedMS	VARCHAR(255),"
					+ "visited VARBINARY, PRIMARY KEY('id'));");
			System.out.println("DIAGRAMS TABLE CREATED  IN DATABASE");
			/***************
			 * CREATE TABLE FOR ALL DROPPED MESSAGES
			 *****************************/
			statement.execute("CREATE TABLE IF NOT EXISTS messages_drop ( id	INTEGER," + "messageID	VARCHAR(255),"
					+ "source	VARCHAR(255)," + "destination	VARCHAR(255)," + "destinatedMS	VARCHAR(255),"
					+ "visited VARBINARY NOT NULL, PRIMARY KEY('id'));");
			System.out.println("DROP MESSAGES TABLE CREATED  IN DATABASE");

			statement.close();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/***********************************************************************/

	/*************** GETERS FOR TABLES *****************************************/
	public List<Message> getMessages() {
		return select.getAllMessages();
	}

	public List<Message> getDiagrams() {
		return select.getAllDiagrams();
	}

	public List<Message> getDrops() {
		return select.getAllDrops();
	}

	/***********************************************************************/

	/********************* CLEAR TABLES *************************************/
	public void clearAllTables() {
		String sqlClear1 = "DELETE FROM messages";
		String sqlClear2 = "DELETE FROM diagrams";
		String sqlClear3 = "DELETE FROM messages_drop ";

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(sqlClear1);
			statement.execute(sqlClear2);
			statement.execute(sqlClear3);
			statement.close();
			connection.commit();
			System.out.println("------------------------");
			System.out.println("TABLES CLEARD!");
			System.out.println("------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/***********************************************************************/

	/*************** Inserting into tables **********************************/
	public void insertIntoMessageTable(Message message) throws SQLException {
		insert.insertMessageTable(message);
	}

	public void insertIntoDiagramTable(Message message) throws SQLException {
		insert.insertDiagramTable(message);
	}

	public void insertIntoDrop(Message message) throws SQLException {
		insert.insertDropTable(message);
	}

	/*****************************************************************/

	/************** close DB connection ********************************/
	public void closeDbConnection() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
