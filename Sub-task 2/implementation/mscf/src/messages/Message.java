package messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


/**
* The Message class is...
*
* @author Nemanja Srdanovic
* @version 1.0
* @since   2020-04-22 
*/
public class Message implements Serializable {
	
	/*-------------------------------------VARIABLES-------------------------------------*/
	
	private static final long serialVersionUID = 1L;
	

	private String messageId;
	private String responseId;
	private String source;
	private String destination;
	private String status;
	private String endpoint;
	private String destinatedMS;
	private Object data;
	private Error error;
	private List<String> visitedMS;

	
	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	/*Message object for Database to use*/
	public Message() {
		
		this.messageId=null;
		this.source=null;
		this.destination=null;
		this.status=null;
		this.data=null;
		this.destinatedMS=null;
		this.visitedMS=  new CopyOnWriteArrayList<String>();

	}
	
	//DAFAULT
	public Message(String destinatedMS, String source){
		this.messageId = UUID.randomUUID().toString();
		this.destinatedMS =destinatedMS;
		this.source = source;
		this.visitedMS= new CopyOnWriteArrayList<String>();
	}
	
	//RESPONSE MESSAGE
	public Message(String destinatedMS, String source, String destination, String responseID, Object data) {

		this.messageId = UUID.randomUUID().toString();
		this.destinatedMS=destinatedMS;
		this.source=source;
		this.destination = destination;
		this.responseId = responseID;
		this.data = data;
		this.visitedMS= new CopyOnWriteArrayList<String>();
	}
	
	
	//REQUEST CTOR WITH AN OBJECT
	public Message(String destinatedMS, String source, String endpoint, Object data){
		
		this.messageId = UUID.randomUUID().toString();
		this.destinatedMS=destinatedMS;
		this.source=source;
		this.endpoint = endpoint;
		this.data = data;
		this.visitedMS= new CopyOnWriteArrayList<String>();
		
	}
	//REQUEST CTOR WITHOUT AN OBJECT
	public Message(String destinatedMS, String source, String endpoint){
		
		this.messageId = UUID.randomUUID().toString();
		this.destinatedMS=destinatedMS;
		this.source=source;
		this.endpoint = endpoint;
		this.visitedMS= new CopyOnWriteArrayList<String>();
	}
	
	// CTOR FOR STATISTIC
	public Message(String destinatedMS, String source, Object data){
		
		this.messageId = UUID.randomUUID().toString();
		this.destinatedMS=destinatedMS;
		this.source=source;
		this.data = data;
		this.visitedMS= new CopyOnWriteArrayList<String>();
	}
	
	/*------------------------------------- GETTERS -------------------------------------*/
	
	/**/
	public String getSource() {
		return source;
	}
	
	/**/
	public synchronized String getDestination() {
		return destination;
	}
	
	/**/
	public Object getData() {
		return data;
	}

	/**/
	public String getMessageId() {
		return messageId;
	}
	
	/**/
	public String getResponseId() {
		
		return responseId;
	}
	
	/**/
	public List<String> getVisitedMS() {
		return visitedMS;
	}
	

	/**/
	public String getStatus() {
		return status;
	}

	/**/
	public String getEndpoint() {
		return endpoint;
	}

	public String getDestinatedMS() {
		return destinatedMS;
	}
	
	public Error getError() {
		return error;
	}
	/*------------------------------------- SETTERS -------------------------------------*/

	public void setError(Error error) {
		this.error = error;
	}


	/**/
	public void setSource(String source) {
		this.source = source;
	}
	
	/**/
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	/**/
	public void setData(Object data) {
		this.data = data;
	}
	
	/**/
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	/**/
	public void setResponseId(String responseId) {
		
		this.responseId=responseId;
	}
	
	/**/
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**/
	public void setVisitedMS(List<String> visitedMSInput) {
		this.visitedMS=visitedMSInput;
	}
	
	/**/
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public void setDestinatedMS(String destinatedMS) {
		this.destinatedMS = destinatedMS;
	}
	
	/*------------------------------------- OTHER METHODS -------------------------------------*/
	
	/**/
	public void addVisitedMS(String visitedMS) {
		this.visitedMS.add(visitedMS);
	}

	/**/
	public String getDestinationPath(Message m) {
		
		String output="";
		
			for(int i=0; i<m.visitedMS.size(); ++i) {
				output+= " | " + m.visitedMS.get(i).toString() + " | ";		
			}
				
			
		return output;
	}
	
	/**/
	public String toString() {
		return  " message ##" + messageId 
				+ "## source: " + source 
				+"## destinatedMS "+ destinatedMS
				+ "## visitedMS: " + getDestinationPath(this) + " ##"
				;		
	}

}
