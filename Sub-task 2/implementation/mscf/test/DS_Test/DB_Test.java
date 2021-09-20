package DS_Test;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import constants.Constraints;
import database.DriverClass;
import messages.Message;


@RunWith(JUnitPlatform.class)
class DB_Test {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws SQLException {
		
		DriverClass dc = new DriverClass(3021);
		
		
		Message m = new Message(Constraints.MS1_ID, Constraints.IP_ADDRESS_2);
		
		dc.insertIntoMessageTable(m);
		
		//dc.clearMessageTable();
		
		assertTrue(!dc.messageInDatabase(m));
		
		
	}

}
