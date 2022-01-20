
/*
 * BU Term project for cs622
 
  Unit test for HelpFinderUser
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

//not implemented
public class HelpFinderUserTests {

		@Test
		public void testcreateUser() {

			HelpFinderUser user = new HelpFinderUser((long) 1, "test address", "John", "M", "test@email.com");
			// assert the values
			assertEquals(user.getAddress(), "test address");
			assertEquals(user.getFirstName(), "John");
			assertEquals(user.getLastName(), "M");
			assertEquals(user.getEmailAddress(), "test@email.com");
		}

}
