/*
 * BU Term project for cs622
 
  Test class to verify workforcelocator service
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.service;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserRole;
import com.helpfinder.model.User;
import com.helpfinder.model.UserRole;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.repository.CoreWorkerSkillRepository;
import com.helpfinder.repository.DatabaseRepository;
import com.helpfinder.repository.GoogleLocationRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.repository.SqliteWorkForceLocatorRepository;
import com.helpfinder.repository.SqlliteRepository;

public class WorkforceLocatorServiceTest {
	GoogleLocationRepository locationRepo;
	SQLiteUserRepository userRepo;
	SqliteWorkForceLocatorRepository workforceLocatorRepo;
	InquiryEmailNotificationService notificationService;
	WorkforceLocatorService locatorService;
	User helpFinderUser;
	ArrayList<WorkerSkill> workerUserSkills;
	DatabaseRepository databaseRepo;	
	CoreWorkerSkillRepository workerSkillRepository;	
	UserService userService;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	@Before
	public void setup()
	{
		//create role
		UserRole userRole = new UserRole();		
		userRole.setUserRole(EUserRole.ROLE_HELPFINDER_USER);			
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(userRole);
		// create an object of type WorkerUSer
		WorkerSkill skill = new WorkerSkill(1, "handyman");
		ArrayList<WorkerSkill> workerSkills = new ArrayList<WorkerSkill>();
		workerSkills.add(skill);
		locationRepo = new GoogleLocationRepository();
		databaseRepo = new SqlliteRepository();
		workerSkillRepository = new CoreWorkerSkillRepository(databaseRepo);
		userRepo = new SQLiteUserRepository(locationRepo, workerSkillRepository);
		userService = new UserService(userRepo);
		workforceLocatorRepo = new SqliteWorkForceLocatorRepository(userRepo);
		notificationService = new InquiryEmailNotificationService();
		locatorService = new WorkforceLocatorService(workforceLocatorRepo, userService, notificationService);
		//create user and work skills for testing
		helpFinderUser = new BasicUser();
		helpFinderUser.setUserInformation("test helpfinderuser address", "David", "walt", "waltDavid@test.com", userRoles);
		helpFinderUser = userRepo.createUser(helpFinderUser);
		workerUserSkills = new ArrayList<WorkerSkill>();
		workerUserSkills.add(workerSkillRepository.getAllSkillsets().get(0));		
		System.setOut(new PrintStream(outputStreamCaptor));

	}
	
	@Test
	public void test_findWorder_within_1_mile()
	{	
		
		List<User> users = locatorService.findWorkforce(helpFinderUser, workerUserSkills, 1.0);
		//check size and email address returned are correct
		assertEquals(users.size(), 1);		
		assertEquals(((BasicUser)users.get(0)).getEmailAddress(), "mattm@test.com");
	}
	@Test
	public void test_send_workinquiry_flow_until_hire() throws IOException
	{	
		
		List<User> users = locatorService.findWorkforce(helpFinderUser, workerUserSkills, 1.0);
		assertEquals(users.size(), 1);		
		assertEquals(((BasicUser)users.get(0)).getEmailAddress(), "mattm@test.com");
		List<WorkInquiry> workInquiries = new ArrayList<WorkInquiry>();				
		WorkInquiry inquiry1 = null;
		// create work inquiries to send to all users returned by work force locator
		for(User user: users){
			// send a work inquiry to this user.
			// using dummy data
			//create a work inquiry		
			 inquiry1 = new WorkInquiry(10, new Date(System.currentTimeMillis()),
					 new Date(new Date(System.currentTimeMillis()).getTime()  +  172800 * 1000), // added 2 days
						helpFinderUser, user);
			workInquiries.add(inquiry1);
							
		}
		//send inquiry to all users
		locatorService.sendWorkInquiry(workInquiries);
		
		assertTrue(outputStreamCaptor.toString().trim().startsWith("Email Sent requesting Work from waltDavid@test.com"));
		
		outputStreamCaptor.flush();
		//let each user commit to the work
		for(User user: users){
			// commit the work
			// using dummy data								
			locatorService.commitWork(inquiry1, true);
			assertTrue( outputStreamCaptor.toString().trim().endsWith("mail sent to waltDavid@test.com informing that user mattm@test.com committed the work"));
							
		}
		
	
		//let the user hire the one user
		for(User user: users){
			// commit the work
			// using dummy data								
			locatorService.hireWork(inquiry1, true);
			assertTrue(outputStreamCaptor.toString().trim().endsWith("Email sent to mattm@test.com informing that the waltDavid@test.com has hired the user for the work"));			
			break;
							
		}
		
	}

	

}
