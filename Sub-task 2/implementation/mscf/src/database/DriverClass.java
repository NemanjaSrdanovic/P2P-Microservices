package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constants.Constraints;
import messages.Car;
import messages.Message;

public class DriverClass {
	
	Connection connection;
	Statement statement;
	String database;
	int portNummber;
	SQLinsert insert;
	SQLselect select;

	public DriverClass(int port) {
		try {
			
			String portDB= Integer.toString(port);
			this.database ="Database"+portDB+".db";
					
			connection = DriverManager.getConnection("jdbc:sqlite:.\\"+database);
			connection.setAutoCommit(false);
			this.portNummber=port;

			this.createMessageTable();
			this.clearMessageTables();
						
			insert=new SQLinsert(database);
			select=new SQLselect(database);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createMessageTable() {

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:.\\"+database);

			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(
					"CREATE TABLE IF NOT EXISTS message (messageId varchar(100) primary key," 
							+ "sourceIP varchar(100)," 
							+ "destinationIP varchar(100)," 
							+ "visitedMS VARBINARY," 
							+ "data VARBINARY,"
							+ "destinatedMS varchar(100))");
			statement.execute(
					"CREATE TABLE IF NOT EXISTS messageStatistic (messageId varchar(100) primary key," 
							+ "sourceIP varchar(100)," 
							+ "destinationIP varchar(100)," 
							+ "visitedMS VARBINARY," 
							+ "data VARBINARY,"
							+ "destinatedMS varchar(100))");
			

			statement.close();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void clearMessageStatisticTable() {
		
		String sqlClear = "DELETE FROM messageStatistic";

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(sqlClear);
			statement.close();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized void insertIntoMessageTable(Message message) throws SQLException {
		
		insert.insertMessageTable(message);
	}
	
	public synchronized void insertIntoMessageStatistic(Message message) throws SQLException{
		
		insert.insertMessageStatistic(message);
	}
	
	public List<Message> getAllDatabaseMessages(){
		
		return select.getAllMessages();
	}
	
	public List<Message> getAllDatabaseToDoMessages(){
		
		return select.getAllToDoMessages();
	}
	
	public boolean messageInDatabase(Message message) throws SQLException{
		
	if(select.getMessageFromDatabase(message).getMessageId() != null) return true;
		else return false;
	
	}
	
	public synchronized List<Message> getStatisticMessages(){
		
		return select.getAllStatisticMessages();
	}
	
/*	<---------------------------------------------STATISTIC------------------------------------------------------> */
	

	public synchronized void makeStatisticBackup(String id) throws SQLException {
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:.\\"+database);

			connection.setAutoCommit(false);
			statement = connection.createStatement();

			statement.execute(
					"CREATE TABLE IF NOT EXISTS"+"\"" +"Backup"+id + "\"" + "("
							+ "data VARBINARY)" );
			

			statement.close();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		insert.insertBackupData(id, getStatisticMessages());
		
		clearMessageStatisticTable();
	}
	
	public synchronized void dropBackup(String id) {
	
		
		String sqlDrop = "DROP TABLE"+"\"" +"Backup"+id + "\"";

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(sqlDrop);
			statement.close();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public synchronized List<Message> getBackupData(String id){
		
		
		return select.getBackup(id);
		
	}
	
	
/*	<-----------------------------------------------------------------------------------------------------------> */
	
	public void clearMessageTables() {
		
		String sqlClear1 = "DELETE FROM message";
		String sqlClear2 = "DELETE FROM messageStatistic";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(sqlClear1);
			statement.execute(sqlClear2);
			
			for(int i=0; i!=100; ++i) {
				
			String sqlDrop = "DROP TABLE IF EXISTS"+"\"" +"Backup"+i+ "\"";
			statement.execute(sqlDrop);
			}
		
			statement.close();
			connection.commit();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
			
	}
	
	
	public void closeDbConnection() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
