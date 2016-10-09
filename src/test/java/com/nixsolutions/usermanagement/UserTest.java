package com.nixsolutions.usermanagement;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private User user;
	private Date dateOfBirth;
	
	@Before
	public void setUp() throws Exception {
		user = new User();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(1984, Calendar.MAY, 26);
		dateOfBirth = calendar.getTime();
	}
	
	@Test
	public void testGetFullName(){
		user.setFirstName("John");
		user.setLastName("Doe");
		assertEquals("Doe, John", user.getFullName());
	}
	
	@Test
	public void testGetAge(){
		user.setDateOfBirth(dateOfBirth);
		assertEquals(2016-1984, user.getAge());
	}
	

}
