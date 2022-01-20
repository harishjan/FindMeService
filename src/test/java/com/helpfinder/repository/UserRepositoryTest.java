/*
 * BU Term project for cs622
 
  Test class to verify UserRepository
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;

public class UserRepositoryTest {
	
	GoogleLocationRepository locationRepo;
	SQLiteUserRepository userRepo;
	@Before
	public void setup()
	{
		locationRepo = new GoogleLocationRepository();
		userRepo = new SQLiteUserRepository(locationRepo);
	}
	
	@Test
	public void test_Create_new_WorkerUser_should_have_latLong_userid()
	{	
		User workerUser = new WorkerUser();
		ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
		workerUserSkills.add(userRepo.getAllSkillsets().get(0));
		((WorkerUser) workerUser).setUserInformation("test workeruser address", "Peter", "h", "peterh@test.com",
				workerUserSkills);
		workerUser = userRepo.createUser(workerUser);
		assertEquals( workerUser.getLatLong()[0], 1.1);
		assertEquals( workerUser.getLatLong()[1], 2.2);
		assertNotNull( ((BasicUser) workerUser).userId);
		
	}

	@Test
	public void test_Create_new_HelpFinderUser_should_have_latLong_userid()
	{	
		//test creating new helpfinder user 
		User helpFinderUser = new HelpFinderUser();
		helpFinderUser.setUserInformation("test helpfinderuser address", "David", "walt", "waltDavid@test.com");
		helpFinderUser = userRepo.createUser(helpFinderUser);
		assertEquals( helpFinderUser.getLatLong()[0], 1.1);
		assertEquals( helpFinderUser.getLatLong()[1], 2.2);
		assertNotNull( ((BasicUser) helpFinderUser).userId);
		
		
	}

}
