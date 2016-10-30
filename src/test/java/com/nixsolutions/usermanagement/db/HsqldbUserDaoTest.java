package com.nixsolutions.usermanagement.db;

import com.nixsolutions.usermanagement.User;

import junit.framework.TestCase;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;

public class HsqldbUserDaoTest extends DatabaseTestCase{

	private HsqldbUserDao dao;
	private ConnectionFactory connectionFactory;
	private Date tempDate;
	
	
	protected void setUp() throws Exception {
		super.setUp();
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

	public void testFindAll() {
		try {
			Collection collection = dao.findAll();
			assertNotNull("Collection is null", collection);
			assertEquals("Collection size.", 2, collection.size());
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testFind(){
		try {		
			User tempUser = dao.find(new Long(1000));
			assertEquals("Incorrect first name", tempUser.getFirstName(), "Bill");
			assertEquals("Incorrect last name", tempUser.getLastName(), "Gates");
			assertEquals("Incorrect date", tempUser.getDateOfBirth().toString(), "1968-04-26");
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testUpdate() {
		try {
			User user = new User();
			user.setFirstName("Marti");
			user.setLastName("Luter");
			user.setId(new Long(1000));
			user.setDateOfBirth(tempDate);
			
			dao.update(user);

			User tempUser = dao.find(new Long(1000));
			assertEquals("Incorrect first name", tempUser.getFirstName(), "Marti");
			assertEquals("Incorrect last name", tempUser.getLastName(), "Luter");
			assertEquals("Incorrect date", tempUser.getDateOfBirth().toString(), "1984-05-26");
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	
	public void testDelete(){
		try {
			User user = new User();

			user.setFirstName("John");
			user.setLastName("Doe");
			user.setDateOfBirth(tempDate);
			user.setId(new Long(1000));
			dao.delete(user);
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		connectionFactory = new ConnectionFactoryImpl("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:db/usermanagement", "sa", "");
		return new DatabaseConnection(connectionFactory.createConnection());
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		IDataSet dataSet = new XmlDataSet(getClass().getClassLoader().getResourceAsStream("usersDataSet.xml"));
		return dataSet;
	}

}
