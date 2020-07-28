package com.nmm.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nmm.database.InsertDB;
import com.nmm.database.SelectDB;
import com.nmm.entities.Message;

import constants.Constraints;

@RestController
@RequestMapping("/messages/diagrams")
public class DiagramController {
	Connection connection;
	Statement statement;
	String database;
	InsertDB insert;
	SelectDB select;

	public void setConnection() throws ClassNotFoundException, SQLException {
		this.database = "DatabaseMessages" + ".db";
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:.\\" + database);
		connection.setAutoCommit(false);
		insert = new InsertDB(database);
		select = new SelectDB(database);
	}

	public void sort() throws ClassNotFoundException, SQLException {
		select.clearDiagram();
		select.clearDrop();
		Iterable<Message> messages = select.getAllMessages();
		Iterator<Message> it = messages.iterator();

		while (it.hasNext()) {
			Message message = it.next();
			if (message.getDestinatedMS().equals(Constraints.MS2_ID)) {
				String[] ipPort = message.getDestination().split(":");
				String result = message.getDestinatedMS() + "_" + ipPort[1];
				System.out.println("ZA MS: " + result);
				if (result.equals(message.getLastVisited())) {
					// insert.insertDiagramTable(message);
					System.out.println("INSERT za ms2" + message);
					insert.insertDiagramTable(message);
				} else {
					System.out.println("DROPED ZA MS2");
					insert.insertDropTable(message);
				}
			} else {
				String[] destinatedLV = message.getLastVisited().split("_");
				String result = destinatedLV[0] + "_" + destinatedLV[1];
				System.out.println("NE ZA MS2" + result);
				if (message.getDestinatedMS().equals(result)) {
					System.out.println("INSERT ne ms2" + message);
					insert.insertDiagramTable(message);
				} else {
					System.out.println("DROPED NE ZA MS2");
					insert.insertDropTable(message);
				}

			}
		}
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ModelAndView getDiagrams() throws ClassNotFoundException, SQLException {
		setConnection();
		sort();
		setConnection();
		List<Message> storedMessages = select.getAllDiagrams();
		StringBuilder sb = new StringBuilder();
		Map<String, String> list = new HashMap<>();
		for (int i = 0; i < storedMessages.size(); ++i) {
			sb.append("sequenceDiagram");
			sb.append("\n");
			List<String> visited = storedMessages.get(i).getVisited();
			for (int j = 0; j < visited.size(); ++j) {
				if (visited.size() == 1) {
					sb.append(visited.get(j));
					sb.append("->>");
					sb.append(visited.get(j));
					sb.append(": ");
					sb.append("received");
					sb.append("\n");
				}
				/*-----------------ELSE--------------------------------*/
				else {
					if (j != 0) {
						sb.append(visited.get(j - 1));
						sb.append("->>");
						sb.append(visited.get(j));
						sb.append(": ");
						if (j == visited.size() - 1) {
							sb.append("received");
							sb.append("\n");
						} else {
							sb.append("sent");
							sb.append("\n");
						}
					}

				}
			}

			list.put(storedMessages.get(i).getMessageID(), sb.toString());
			sb = new StringBuilder();
		}

		System.out.println(list);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("diagrams", list);
		modelAndView.setViewName("diagrams");
		System.out.println("========ljista diagrama===========");
		System.out.println(list);
		System.out.println("===================");
		return modelAndView;
	}

}
