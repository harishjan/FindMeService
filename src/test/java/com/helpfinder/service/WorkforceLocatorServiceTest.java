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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.repository.GoogleLocationRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.repository.SqliteWorkForceLocatorRepository;

public class WorkforceLocatorServiceTest {
	GoogleLocationRepository locationRepo;
	SQLiteUserRepository userRepo;
	SqliteWorkForceLocatorRepository workforceLocatorRepo;
	InquiryEmailNotificationService notificationService;
	WorkforceLocatorService locatorService;
	User helpFinderUser;
	ArrayList<WorkerSkill> workerUserSkills;
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	@Before
	public void setup()
	{
		locationRepo = new GoogleLocationRepository();
		userRepo = new SQLiteUserRepository(locationRepo);
		workforceLocatorRepo = new SqliteWorkForceLocatorRepository(userRepo);
		notificationService = new InquiryEmailNotificationService();
		locatorService = new WorkforceLocatorService(workforceLocatorRepo,userRepo,notificationService);
		//create user and work skills for testing
		helpFinderUser = new HelpFinderUser();
		helpFinderUser.setUserInformation("test helpfinderuser address", "David", "walt", "waltDavid@test.com");
		helpFinderUser = userRepo.createUser(helpFinderUser);
		workerUserSkills = new ArrayList<WorkerSkill>();
		workerUserSkills.add(userRepo.getAllSkillsets().get(0));		
		System.setOut(new PrintStream(outputStreamCaptor));

	}
	
	@Test
	public void test_findWorder_within_1_mile()
	{	
		
		List<User> users = locatorService.findWorkforce(helpFinderUser, workerUserSkills, 1.0);
		//check size and email address retured are correct
		assertEquals(users.size(), 1);		
		assertEquals(((BasicUser)users.get(0)).emailAddress, "mattm@test.com");
	}
	@Test
	public void test_send_workinquiry_flow_until_hire() throws IOException
	{	
		
		List<User> users = locatorService.findWorkforce(helpFinderUser, workerUserSkills, 1.0);
		assertEquals(users.size(), 1);		
		assertEquals(((BasicUser)users.get(0)).emailAddress, "mattm@test.com");
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
