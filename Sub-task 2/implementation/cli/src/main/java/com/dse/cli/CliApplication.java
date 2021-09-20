package com.dse.cli;

import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dse.cli.controller.CliController;

@SpringBootApplication
public class CliApplication {

	public static void main(String[] args) {
		SpringApplication.run(CliApplication.class, args);
		
		CliController cliController = new CliController(); 
		
		Scanner scanner = new Scanner(System.in);
		String option;
		
		while(true) {
			System.out.println("\nEnter an Option");
			System.out.println("1 - Display all Car and their Routes");
			System.out.println("2 - Display Travel Progress of a car");
			System.out.println("3 - Block Area");
			System.out.println("4 - Un-Block Area");
			System.out.println("0 - Exit");
			System.out.print("\nType an option: ");
			
			option = null;
			try {
				option = scanner.next();
			} catch (Exception e) {
				System.err.println("Enter a Number!!!");
			}
				
			/**
			 * Depending on user input, proper request to the MS2 will be sent
			 */
			switch(option) {
				case "1":
					
					cliController.displayAllCarRoutes();
					
				break;
				
				case "2":
		
					cliController.displayProgress(scanner);
					
				break;
				
				case "3":
					
					cliController.blockArea(scanner);
					
				break;
				
				case "4":
					
					cliController.unBlockArea(scanner);
					
				break;

				case "0":
					
					System.out.println("Exit...");
					System.exit(1);
					
				break;
				default:
					System.err.println("Option doesn't exist! Please choose again.");
			}
			
		}
	}

}
