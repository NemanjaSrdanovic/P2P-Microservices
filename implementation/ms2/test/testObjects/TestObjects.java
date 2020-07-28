package testObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import messages.Coordinate;
import messages.Field;
import messages.Message;
import messages.Street;
import messages.TypeOfField;

/**
 * The TestObjects class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-04-22
 */

public class TestObjects {

	/*-------------------------------------VARIABLES-------------------------------------*/

	private List<Field> TestFields = new ArrayList<Field>();
	private BlockingQueue<Message> messageObjects = new LinkedBlockingQueue<>();
	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	public TestObjects() {

		setTestFields();
	}

	/*-------------------------------------GETTERS AND SETTER METHODS-------------------------------------*/

	/*
	 * 
	 * */
	public void setTestFields() {

		try {
			for (int i = 0; i < 15; ++i) {

				for (int j = 0; j < 15; ++j) {

					Coordinate coordinate = new Coordinate(i, j);

					Field newField = new Field(coordinate);

					TestFields.add(newField);

				}
			}

		} catch (IllegalArgumentException e) {
			e.getMessage();
		}

	}

	/*
	 * 
	 * */
	public void setMessageObjects(BlockingQueue<Message> messageObjects) {
		this.messageObjects = messageObjects;
	}

	public void removeMessageFromQueue(Message message) {

		messageObjects.remove(message);
	}

	/*
	 * 
	 * */
	public List<Field> getCarRoute() {

		List<Field> route = new ArrayList<Field>();
		int i = 0;

		do {

			Random randomPosition = new Random();
			Field field = TestFields.get(randomPosition.nextInt(TestFields.size()));

			field.setDrivable(true);
			field.setFieldType(TypeOfField.STREET);
			route.add(field);

			++i;

		} while (i < 10);

		return route;
	}

	/*
	 * 
	 * */
	public List<Street> getTestStreets() {

		List<Street> streets = new ArrayList<Street>();

		streets.add(generateTestStreets(7, "A", TestFields));
		streets.add(generateTestStreets(9, "B", TestFields));
		streets.add(generateTestStreets(5, "C", TestFields));
		streets.add(generateTestStreets(10, "F", TestFields));

		return streets;

	}

	/*
	 * 
	 * */
	public BlockingQueue<Message> getMessageObjects() {
		return messageObjects;
	}

	/*-------------------------------------GENERATE TEST OBJECTS-------------------------------------*/

	/*
	 * 
	 * */
	public Street generateTestStreets(int size, String name, List<Field> list) {

		int i = 0;
		List<Field> street = new ArrayList<Field>();

		do {

			Random randomPosition = new Random();
			Field field = list.get(randomPosition.nextInt(list.size()));

			field.setFieldType(TypeOfField.STREET);
			field.setDrivable(true);

			street.add(field);

			++i;

		} while (i < size);

		Street newStreet = new Street(name, street);

		return newStreet;

	}

	/*
	 * 
	 * */
	public void generateMessageObjects(String endpoint) {

		Message message = new Message();
		message.setSource("MS1");
		message.setDestination("MS2");

		if (endpoint == "drivableFields") {
			message.setData(getTestStreets());
		} else {

			message.setData(getCarRoute());
		}

		message.setEndpoint(endpoint);
		messageObjects.add(message);

	}

	/**/
	public void generateFieldMessageObject() {
		generateMessageObjects("drivableFields");
	}

	/**/
	public void generateRoutedMessageObject() {
		generateMessageObjects("carRoute");
	}

	/*------------------------------- TEST OBJECTS THROWING EXCEPTIONS -------------------------------*/

	/*
	 * 
	 * */
	public List<Field> excetionFields() {

		List<Field> excepitonFields = new ArrayList<Field>();
		int i = 0;

		for (Field field : TestFields) {

			if (i % 3 == 0) {
				field.setDrivable(false);
			}

			excepitonFields.add(field);
			++i;
		}

		return excepitonFields;
	}

	/*
	 * 
	 * */
	public List<Street> getExceptionTestStreets() {

		List<Street> exceptionStreets = new ArrayList<Street>();

		exceptionStreets.add(generateTestStreets(7, "X", excetionFields()));
		exceptionStreets.add(generateTestStreets(9, "Y", excetionFields()));
		exceptionStreets.add(generateTestStreets(5, "Z", excetionFields()));
		exceptionStreets.add(generateTestStreets(10, "D", excetionFields()));

		return exceptionStreets;

	}

}
