package com.nmm.entities;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	public static int myID = 0;
	private int id;
	private String messageID;
	private String source;
	private String destination;
	private String destinatedMS;
	private List<String> visited;

	public Message() {
		this.id = myID++;
	}

	public Message(String messageID, String source, String destination, String destinatedMS, List<String> visited) {
		this.id = myID++;
		this.messageID = messageID;
		this.source = source;
		this.destination = destination;
		this.destinatedMS = destinatedMS;
		this.visited = visited;
	}

	public static int getMyID() {
		return myID;
	}

	public static void setMyID(int myID) {
		Message.myID = myID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestinatedMS() {
		return destinatedMS;
	}

	public void setDestinatedMS(String destinatedMS) {
		this.destinatedMS = destinatedMS;
	}

	public List<String> getVisited() {
		return visited;
	}

	public void setVisited(List<String> visited) {
		this.visited = visited;
	}

	public String getLastVisited() {
		return this.visited.get(this.visited.size() - 1);
	}

	public String getFirstVisited() {
		return this.visited.get(0);
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", messageID=" + messageID + ", source=" + source + ", destination=" + destination
				+ ", destinatedMS=" + destinatedMS + ", visited=" + visited + "]";
	}
}