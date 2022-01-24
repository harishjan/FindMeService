/*
 * BU Term project for cs622
 
  Test class to verify WorkerUser service
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;



// example of unit test with junit
public class WorkerUserTest {

	@Test
	public void testcreateUser() {

		// create an object of type WorkerUSer
		WorkerSkill skill = new WorkerSkill(1, "handyman");
		ArrayList<WorkerSkill> workerSkills = new ArrayList<WorkerSkill>();
		workerSkills.add(skill);
		//create role
		UserRole userRole = new UserRole();
		userRole.setUserId((long)1);
		userRole.setUserRole(EUserRole.ROLE_WORKER_USER);			
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(userRole);
		
		// set the user attributes
		WorkerUser user = new WorkerUser((long) 1, "test address", "John", "M", "test@email.com",userRoles, workerSkills);

		// assert the values
		assertEquals(user.getAddress(), "test address");
		assertEquals(user.getFirstName(), "John");
		assertEquals(user.getLastName(), "M");
		assertEquals(user.getEmailAddress(), "test@email.com");
		assertEquals(user.getSkills(), workerSkills);
		assertTrue(user.getRoles().contains(userRole));
		
	}

}
