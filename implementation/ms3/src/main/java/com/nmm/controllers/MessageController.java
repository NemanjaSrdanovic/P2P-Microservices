package com.nmm.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nmm.database.InsertDB;
import com.nmm.database.SelectDB;
import com.nmm.entities.Message;

@RestController
@RequestMapping("/messages")
public class MessageController {
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

	@GetMapping("all")
	public ModelAndView getAll() throws ClassNotFoundException, SQLException {
		setConnection();
		List<Message> list = select.getAllMessages();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("messages", list);
		modelAndView.setViewName("allmessage");
		System.out.println("========ljista svih===========");
		System.out.println(list);
		System.out.println("===================");
		return modelAndView;
	}

}
