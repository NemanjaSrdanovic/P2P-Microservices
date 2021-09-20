package database;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import messages.Message;
import parsers.Marshaller;

public class SQLselect {
	
	Connection connection;
	Statement statement;
	String database;
	
	public SQLselect(String base) {
		try { 
			
			this.database=base;
			connection = DriverManager.getConnection("jdbc:sqlite:.\\"+database);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Message> getAllMessages(){
		
		List<Message> allMessages = new ArrayList<Message>();


		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM message");

			while (rs.next()) {
				
				String messageId = rs.getString("messageId");
				String sourceIP = rs.getString("sourceIP");
				String destinationIP = rs.getString("destinationIP");
				String destinatedMS = rs.getString("destinatedMS");
				byte[] byteOutput =(byte[])rs.getBytes("data");	
				byte[] visitedMS = rs.getBytes("visitedMS");
				
				Marshaller marshaller = new Marshaller();
				Object transformedObject = marshaller.transformByteToObject(byteOutput);
				Object transformedArray = marshaller.transformByteToObject(visitedMS);
				
				Message message=new Message();
				message.setMessageId(messageId);
				message.setSource(sourceIP);
				message.setDestination(destinationIP);
				message.setDestinatedMS(destinatedMS);
				message.setData(transformedObject);
				message.setVisitedMS((List<String>) transformedArray);	
								
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

	/*Gets all Messages from buffer which are not processed 
	 * and prepares a List of these messages for the message 
	 * handler to work with  */
	public List<Message> getAllToDoMessages(){
		
		
		List<Message> allMessages = new ArrayList<Message>();

		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();

			String empty ="";
			ResultSet rs = statement.executeQuery("SELECT * FROM message WHERE status is null or status ='' ");
			
			while (rs.next()) {
				
				String messageId = rs.getString("messageId");
				String sourceIP = rs.getString("sourceIP");
				String destinationIP = rs.getString("destinationIP");
				String destinatedMS = rs.getString("destinatedMS");
				byte[] byteOutput =(byte[])rs.getBytes("data");	
				byte[] visitedMS = rs.getBytes("visitedMS");
				
				Marshaller marshaller = new Marshaller();
				Object transformedByte = marshaller.transformByteToObject(byteOutput);
				Object transformedArray = marshaller.transformByteToObject(visitedMS);
				
				Message message=new Message();
				message.setMessageId(messageId);
				message.setSource(sourceIP);
				message.setDestination(destinationIP);
				message.setDestinatedMS(destinatedMS);
				message.setData(transformedByte);
				message.setVisitedMS((List<String>) transformedArray);			
				
				allMessages.add(message);			
			}

			rs.close();
			statement.close();
			connection.commit();


		} catch (Exception ex) {
			System.out.println("SQLselect getAllToDoMessages<---------------");
			System.out.println(ex.toString());
		}

		return allMessages;

	}
	
	public List<Message> getAllStatisticMessages(){
		
		List<Message> allMessages = new ArrayList<Message>();

		try {
			//connection.setAutoCommit(false);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM messageStatistic");
			
			while (rs.next()) {
				
				String messageId = rs.getString("messageId");
				String sourceIP = rs.getString("sourceIP");
				String destinationIP = rs.getString("destinationIP");
				String destinatedMS = rs.getString("destinatedMS");
				byte[] byteOutput =(byte[])rs.getBytes("data");	
				byte[] visitedMS = rs.getBytes("visitedMS");
				
				Marshaller marshaller = new Marshaller();
				//Object transformedByte = marshaller.transformByteToObject(byteOutput);
				Object transformedArray = marshaller.transformByteToObject(visitedMS);
				
				
				
				
				Message message=new Message();
				message.setMessageId(messageId);
				message.setSource(sourceIP);				
				message.setDestination(destinationIP);
				message.setDestinatedMS(destinatedMS);
				message.setVisitedMS((List<String>) transformedArray);
			
				
				allMessages.add(message);			
			}

			rs.close();
			statement.close();
			//connection.commit();


		} catch (Exception ex) {
			System.out.println("SQLselect getAllStatisticDoMessages<---------------");
			System.out.println(ex.toString());
		}

		return allMessages;	
	}
	
     public List<Message> getBackup(String id){
		
		List<Message> arrayList = new ArrayList<Message>();

		try {
			//connection.setAutoCommit(false);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM"+"\""+"Backup"+id+"\"");
			
			while (rs.next()) {
				
				byte[] data = rs.getBytes("data");
				
				Marshaller marshaller = new Marshaller();
				//Object transformedByte = marshaller.transformByteToObject(byteOutput);
				Object transformedArray = marshaller.transformByteToObject(data);
				
				
				arrayList=(List<Message>) transformedArray;
			}

			rs.close();
			statement.close();
			//connection.commit();


		} catch (Exception ex) {
			System.out.println("SQLselect getAllStatisticDoMessages<---------------");
			System.out.println(ex.toString());
		}

		return arrayList;	
	}
	
	
	public Message getMessageFromDatabase (Message messageInput) {
		
		Message DbMessage = new Message();
		
		try {
			//connection.setAutoCommit(false);
			Statement statement = connection.createStatement();

			String searchMessageId =messageInput.getMessageId();
			ResultSet rs = statement.executeQuery("SELECT * FROM message WHERE messageId "+ "=\"" + searchMessageId + "\"");
			
			while (rs.next()) {
				
				String messageId = rs.getString("messageId");
				String sourceIP = rs.getString("sourceIP");
				String destinationIP = rs.getString("destinationIP");
				String destinatedMS = rs.getString("destinatedMS");
				byte[] byteOutput =(byte[])rs.getBytes("data");	
				byte[] visitedMS = rs.getBytes("visitedMS");  
				
				Marshaller marshaller = new Marshaller();
				Object transformedByte = marshaller.transformByteToObject(byteOutput);	
				Object transformedArray = marshaller.transformByteToObject(visitedMS);
				
				DbMessage.setMessageId(messageId);
				DbMessage.setSource(sourceIP);
				DbMessage.setDestination(destinationIP);
				DbMessage.setDestinatedMS(destinatedMS);
				DbMessage.setData(transformedByte);
				DbMessage.setVisitedMS((List<String>) transformedArray);
						
			}

			rs.close();
			statement.close();
			//connection.commit();


		} catch (Exception ex) {
			System.out.println("SQLselect getMessageFromDatabase<---------------");
			System.out.println(ex.toString());
		}

		return DbMessage;

	}

	
}
