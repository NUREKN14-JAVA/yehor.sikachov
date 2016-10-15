package com.nixsolutions.usermanagement.db;

import com.nixsolutions.usermanagement.User;

import junit.framework.TestCase;
import static org.junit.Assert.*;

import java.util.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

public class HsqldbUserDaoTest extends TestCase{

	private HsqldbUserDao dao;
	private ConnectionFactory connectionFactory;
	private Date tempDate;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		connectionFactory = new ConnectionFactoryImpl();
		dao = new HsqldbUserDao(connectionFactory);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(1984, Calendar.MAY, 26);
		tempDate = calendar.getTime();		 
	}

	public void testCreate() {
		try {
			User user = new User();
			user.setFirstName("John");
			user.setLastName("Doe");
			user.setDateOfBirth(tempDate);
			
			assertNull(user.getId());
			
			user = dao.create(user);
			assertNotNull(user);
			assertNotNull(user.getId());
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
