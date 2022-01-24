/*
 * BU Term project for cs622
 
  verify all user service functionalities
 * @author  Harish Janardhanan * 
 * @since   23-jan-2022
 */
package com.helpfinder.service;

import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserRole;
import com.helpfinder.model.User;
import com.helpfinder.model.UserRole;
import com.helpfinder.repository.CoreWorkerSkillRepository;
import com.helpfinder.repository.DatabaseRepository;
import com.helpfinder.repository.GoogleLocationRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.repository.SqlliteRepository;

//test user service related functionalities
public class UserServiceTest {
	
	GoogleLocationRepository locationRepo;
	DatabaseRepository databaseRepo;
	SQLiteUserRepository userRepo;
	CoreWorkerSkillRepository workerSkillRepository;
	UserService userService;
	@Before
	public void setup()
	{
		locationRepo = new GoogleLocationRepository();
		databaseRepo = new SqlliteRepository();		
		workerSkillRepository = new CoreWorkerSkillRepository(databaseRepo);
		userRepo = new SQLiteUserRepository(locationRepo, workerSkillRepository);
		userService = new UserService(userRepo);
		
	}

	
	/***
	 * This test check if the method to create user throws UserExistException if user already exist
	 */
	@Test
	public void test_Create_new_user_which_alreadyexist_throws_UserExistException()
	{	//create role
		UserRole userRole = new UserRole();		
		userRole.setUserRole(EUserRole.ROLE_HELPFINDER_USER);			
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(userRole);
		//test creating new helpfinder user who already is registered
		User helpFinderUser = new BasicUser();
		helpFinderUser.setUserInformation("test helpfinderuser address", "John", "m", "johnm@test.com", userRoles);
		boolean userExistExceptionThrown = false;
		try	{
			helpFinderUser = userService.createUser(helpFinderUser);
		}
		catch(UserExistException ex)
		{
			userExistExceptionThrown = true;
		}
		assertTrue(userExistExceptionThrown);
		
	}
	

}
