/*
 * BU Term project for cs622
 
 This call is used for different functionality for assignment 1 usecases
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.repository.GoogleLocationRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.repository.SqliteWorkForceLocatorRepository;
import com.helpfinder.repository.UserRepository;
import com.helpfinder.repository.WorkForceLocatorRepository;
import com.helpfinder.service.InquiryEmailNotificationService;
import com.helpfinder.service.InquiryNotificationService;
import com.helpfinder.service.WorkforceLocatorService;

public class TestSkillFinder {

	/**
	 * This is the main method for testing following 1) create user where the user
	 * is set with latlog service given by locationrepository(values returned by
	 * repo is dummy) 2) find workers based on skill and lat long distance(Values
	 * returned here from repos are dummy)
	 * 
	 *
	 * @param args is the input parameter to main method
	 */

	public static void main(String[] args) {
		// read the file with all the process details
		try {
			// these repos are singleton instance which will be dependency injected to other
			// repos and classes
			GoogleLocationRepository locationRepo = new GoogleLocationRepository();
			SQLiteUserRepository userRepo = new SQLiteUserRepository(locationRepo);
			SqliteWorkForceLocatorRepository workforceLocatorRepo = new SqliteWorkForceLocatorRepository(userRepo);
			InquiryEmailNotificationService notificationService = new InquiryEmailNotificationService();
			WorkforceLocatorService locatorService = new WorkforceLocatorService(workforceLocatorRepo,userRepo,notificationService);
			
			System.out.println("+++++++++ BEGINING USE CASE 1 TESTS +++++++++++++++");
			System.out.println();
			// 1) Test create User functionality where we are expecting user should have the
			// latlong based on address form location service
			// test first with HelpFinderUser
			
			
			User helpFinderUser = new HelpFinderUser();
			helpFinderUser.setUserInformation("test helpfinderuser address", "David", "walt", "waltDavid@test.com");
			helpFinderUser = userRepo.createUser(helpFinderUser);
			
			System.out.println("Created a HelpFinderUser, printing user details");
			System.out.println("Users should have lat long and userId assigned\n");
			// verify the result all lat log will be same as the location service always
			// return a dummy value
			System.out.println(String.format("The lat is %1$,.2f and log is %2$,.2f ", helpFinderUser.getLatLong()[0],
					helpFinderUser.getLatLong()[1]));
			// verify a userid is set
			System.out.println(String.format("The User Id is %d ", ((BasicUser) helpFinderUser).userId));

			System.out.println();
			// test with workerUser
			User workerUser = new WorkerUser();
			ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
			workerUserSkills.add(userRepo.getAllSkillsets().get(0));
			((WorkerUser) workerUser).setUserInformation("test workeruser address", "Peter", "h", "peterh@test.com",
					workerUserSkills);
			workerUser = userRepo.createUser(workerUser);
			System.out.println("Created a WrokerUser, printing user details\n");
			// verify the result all lat log will be same as the location service always
			// return a dummy value
			System.out.println(String.format("The lat is %1$,.2f and log is %2$,.2f ", workerUser.getLatLong()[0],
					workerUser.getLatLong()[1]));
			// verify a userid is set
			System.out.println(String.format("The User Id is %d ", ((BasicUser) workerUser).userId));
			
			
			System.out.println();
			
			System.out.println("+++++++++ END USE CASE 1 TEST +++++++++++++++");
			
			System.out.println();
			System.out.println();
			
			System.out.println("+++++++++ BEGINING USE CASE 2 TESTS +++++++++++++++");
			System.out.println();
			//find out users who are with in 1 mile radius with the matching skills, 
			//this test is using dummy data , only one user will be returned by the work force locator
		
			System.out.println("Searching for users with in 1 mile..");
			List<User> users = locatorService.findWorkforce(helpFinderUser, workerUserSkills, 1.0);
			List<WorkInquiry> workInquiries = new ArrayList<WorkInquiry>();
			System.out.println(String.format("%s matching user found, and now sending work request to the user\n", users.size()));		
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
			
			System.out.println("\nExecuting logic to commit a work \n");
			//let each user commit to the work
			for(User user: users){
				// commit the work
				// using dummy data								
				locatorService.commitWork(inquiry1, true);
								
			}
			
			System.out.println("\nExecuting logic to hire the work\n");
			//let the user hire the one user
			for(User user: users){
				// commit the work
				// using dummy data								
				locatorService.hireWork(inquiry1, true);
				break;
								
			}

			System.out.println();
			System.out.println("+++++++++ END USE CASE 2 TESTS +++++++++++++++");
			System.out.println();
			
		} catch (Exception e) {
			// for invalid data in the file, print the error message
			System.out.println("Exception while running the test " + e.getMessage());
		}

	}
	
}
