package com.nmm.controllers;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.nmm.worker.MessageWorker;

import constants.Constraints;
import controllers.ConnectionHandler;
import messageProcessor.MessageProcessor;
import messages.Message;

public class NMMController implements MessageProcessor {

	private BlockingQueue<messages.Message> messages;
	private ConnectionHandler connHandler;
	private ExecutorService pool;
	boolean hasMessage = false;

	public NMMController(ConnectionHandler conn) throws InterruptedException, ClassNotFoundException, SQLException {
		System.out.println("NMMController loading messages...");
		messages = new LinkedBlockingQueue<messages.Message>();
		this.connHandler = conn;
		this.pool = Executors.newFixedThreadPool(4);
		connHandler.setMessageProcessor(this);
		for (int i = 0; i < 4; ++i) {
			this.pool.execute(new MessageWorker(this));
		}

	}

	public BlockingQueue<messages.Message> getMessages() {
		return messages;
	}

	public void setMessages(BlockingQueue<messages.Message> messages) {
		this.messages = messages;
	}

	@Override
	public void onMessage(messages.Message message) {
		System.out.println("#######  Message size: " + messages.size());
		System.out.println("------------------- ON MESSAGE ----------------------");
		try {
			messages.put(message);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
		
//			Message returnMessage = new Message("",
//					connHandler.getConnection().getMyNode().getIpAddress() + ":"
//							+ connHandler.getConnection().getMyNode().getPortNmr(),
//					message.getSource(), message.getMessageId(), message.getData());
//
//			String[] destinated = message.getVisitedMS().get(0).split("_");
//
//			switch (destinated[0]) {
//
//			case "MS2":
//				returnMessage.setDestinatedMS(Constraints.MS2_ID);
//				break;
//			case "MS1":
//				returnMessage.setDestinatedMS(Constraints.MS1_ID);
//				break;
//			case "MS4":
//				returnMessage.setDestinatedMS(Constraints.MS4_ID);

//			}

//			connHandler.getConnection().getClient().addMessage(returnMessage);
//
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}
