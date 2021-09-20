package com.nmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.nmm.entities.Message;

import constants.Constraints;

class MessageSorting {

	@Test
	void test() {
		Message message1 = new Message("1", "10.101.101.5:3021", "10.101.101.13:3013", "MS2_CDG",
				new ArrayList<>(Arrays.asList("MS2_CDG_3013", "MS4_RMS_3013", "MS2_CDG_3013")));
		Message message2 = new Message("2", "10.101.101.5:3021", "10.101.101.13:3013", "MS2_CDG",
				new ArrayList<>(Arrays.asList("MS2_CDG_3013", "MS4_RMS_3013")));
		Message message3 = new Message("3", "10.101.101.13:3021", null, "MS1_ARS",
				new ArrayList<>(Arrays.asList("MS2_CDG_3021", "MS1_ARS_3024")));
		Message message4 = new Message("4", "10.101.101.13:3021", null, "MS1_ARS",
				new ArrayList<>(Arrays.asList("MS2_CDG_3021", "MS4_RMS_3013", "MS4_RMS_3013")));
		Message message5 = new Message("5", "10.101.101.5:3021", "10.101.101.13:3013", "MS2_CDG",
				new ArrayList<>(Arrays.asList("MS2_CDG_3013", "MS4_RMS_3013", "MS1_CDG_3013")));
		Message message6 = new Message("6", "10.101.101.5:3021", "10.101.101.13:3013", "MS2_CDG",
				new ArrayList<>(Arrays.asList("MS2_CDG_3013", "MS4_RMS_3013", "MS1_CDG_3013")));
		Message message7 = new Message("7", "10.101.101.17:3021", null, "MS4_RMS",
				new ArrayList<>(Arrays.asList("MS2_CDG_3013", "MS4_RMS_3013", "MS4_RMS_3013")));

		List<Message> list = new ArrayList<Message>();
		list.add(message1);
		list.add(message2);
		list.add(message3);
		list.add(message4);
		list.add(message5);
		list.add(message6);
		list.add(message7);

		Iterable<Message> messages = list;
		Iterator<Message> it = messages.iterator();
		while (it.hasNext()) {
			Message message = it.next();
			if (message.getDestinatedMS().equals(Constraints.MS2_ID)) {
				String[] ipPort = message.getDestination().split(":");
				String result = message.getDestinatedMS() + "_" + ipPort[1];
				System.out.println("FOR MS2: " + result);
				if (result.equals(message.getLastVisited())) {
					System.out.println("INSERT FOR MS2" + message);
				} else {
					System.out.println("DROPPED FOR MS2");
				}
			} else {
				String[] destinatedLV = message.getLastVisited().split("_");
				String result = destinatedLV[0] + "_" + destinatedLV[1];
				System.out.println("NOT FOR MS2" + result);
				if (message.getDestinatedMS().equals(result)) {
					System.out.println("INSERT NOT FOR MS2" + message);
				} else {
					System.out.println("DROPPED NOT FOR MS2");
				}

			}
		}

	}

}
