package database;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import messages.Message;
import parsers.Marshaller;

public class SQLinsert {
	
	Connection connection;
	Statement statement;
	String database;
	
	public SQLinsert(String base) {
		try {
			
			this.database=base;
			 connection = DriverManager
		              .getConnection("jdbc:sqlite:.\\"+database);

	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	}

	
    public void insertMessageTable(Message message) throws SQLException{
		
    	
    	Marshaller transformObject = new Marshaller(); 	
    	byte[] transformedObject = transformObject.transformObjectToByte(message.getData());
    	byte[] transformedArray = transformObject.transformObjectToByte(message.getVisitedMS());
    	
    		
			PreparedStatement stmt=connection.prepareStatement("INSERT OR IGNORE INTO message values(?,?,?,?,?,?)");
			stmt.setString(1, message.getMessageId());
			stmt.setString(2, message.getSource());
			stmt.setString(3, message.getDestination());
			stmt.setBytes(4, transformedArray);
			stmt.setBytes(5, transformedObject);
			stmt.setString(6, message.getDestinatedMS());
			
			
			try {
			//	connection.setAutoCommit(false);
				stmt.executeUpdate();
			//	connection.commit();

			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			
	}
    
    public void insertMessageStatistic(Message message) throws SQLException{
    	
    	Marshaller transformObject = new Marshaller(); 	
    	byte[] transformedObject = transformObject.transformObjectToByte(message.getData());
    	byte[] transformedArray = transformObject.transformObjectToByte(message.getVisitedMS());
    		
			PreparedStatement stmt=connection.prepareStatement("INSERT OR IGNORE INTO messageStatistic values(?,?,?,?,?,?)");
			stmt.setString(1, message.getMessageId());
			stmt.setString(2, message.getSource());
			stmt.setString(3, message.getDestination());
			stmt.setBytes(4, transformedArray);
			stmt.setBytes(5, transformedObject);
			stmt.setString(6, message.getDestinatedMS());
			
			
			try {
			//	connection.setAutoCommit(false);
				stmt.executeUpdate();
			//	connection.commit();

			} catch (Exception ex) {
				System.out.println(ex.toString());
			}			
    	
    }
    
	
	
	public void insertBackupData(String id, List<Message> data) throws SQLException {

		Marshaller transformObject = new Marshaller();
		byte[] transformedArray = transformObject.transformObjectToByte(data);
		
		String tableName = "Backup"+id;
		
		PreparedStatement stmt=connection.prepareStatement("INSERT OR IGNORE INTO"+"\"" + tableName +"\" values(?)");
		stmt.setBytes(1, transformedArray);
		
		try {
			stmt.executeUpdate();	
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}	

	}
}
