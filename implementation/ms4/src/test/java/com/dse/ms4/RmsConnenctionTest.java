package com.dse.ms4;

import org.junit.jupiter.api.Test;

class RmsConnenctionTest {

	RmsConnection rmsConnectonTest = null;
	
	@Test
	void testRmsConnection() {
		
		rmsConnectonTest = new RmsConnection(3028);
	}

}
