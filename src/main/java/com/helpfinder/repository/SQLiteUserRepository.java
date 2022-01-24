/*
 * BU Term project for cs622
 
  This class implements the sqlite data access layer for access user related information
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;

@Component
// implementation is still not completed
public class SQLiteUserRepository implements UserRepository {

	// dummy data for testing
	static HashMap<Long, User> dummyUsers = new HashMap<Long, User>();
	// stores the location service repo
	@Autowired
	LocationServiceRepository locationServiceRepo;
	@Autowired
	WorkerSkillRepository workerSkillRepository;
	
	
	// constructor added to create dummy data
	public SQLiteUserRepository(LocationServiceRepository locationServiceRepo, WorkerSkillRepository workerSkillRepository) {
		this.locationServiceRepo = locationServiceRepo;
		this.workerSkillRepository = workerSkillRepository;
		// create some dummy data
		// 1 is a HelpFinderUser
		// 2 and 3 is a WorkerUser
		BasicUser user1 = new BasicUser(1, "test address", "John", "m", "johnm@test.com", null);
		user1.setLatLong(new Double[] {1.1,2.2});

		List<WorkerSkill> user2Skills = new ArrayList<WorkerSkill>();
		user2Skills.add(this.workerSkillRepository.getAllSkillsets().get(0));
		user2Skills.add(this.workerSkillRepository.getAllSkillsets().get(1));		
		WorkerUser user2 = new WorkerUser(2, "test2 address", "Matt", "p", "mattm@test.com", null, user2Skills);
		user1.setLatLong(new Double[] {1.2,2.3});

		List<WorkerSkill> user3Skills = new ArrayList<WorkerSkill>();
		user3Skills.add(this.workerSkillRepository.getAllSkillsets().get(0));
		user3Skills.add(this.workerSkillRepository.getAllSkillsets().get(3));
		WorkerUser user3 = new WorkerUser(3, "test3 address", "Micheal", "S", "micheals@test.com", null, user3Skills);
		user1.setLatLong(new Double[] {12.1,16.2});

		dummyUsers.put((long) 1, user1);
		dummyUsers.put((long) 2, user2);
		dummyUsers.put((long) 3, user3);
	}

	@Override
	public User getUser(long userId) {
		// TODO Auto-generated method stub

		return dummyUsers.get((long) userId);
	}

	/***
	 * This method creates a new user and stores it in sqlite
	 * @param user is the instance of User object
	 * @return user the user instance after setting the latlong and userId
	 */

	@Override
	public User createUser(User user) {
		// implementation with dummy data.

		// downcast
		BasicUser basicUser = ((BasicUser) user);
		// set the userId, dummy value
		basicUser.setUserId(java.time.Instant.now().toEpochMilli());
		if(basicUser.getAddress() != null && basicUser.getAddress().trim().length() > 1)		{
			// get lat long , dummy value
			Double[] latLong = locationServiceRepo.getLatLogFromAddress(basicUser.getAddress());			
			// set the latlong in user object
			basicUser.setLatLong(latLong);
		}
	
		dummyUsers.put(basicUser.getUserId(), basicUser);
		return basicUser;
	}
	
	
	@Override
	public void updateLatLongByAddress(long userID, String address) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public boolean userExist(String emailAddrss) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WorkerSkill> getUserSkills(long userId) {
		// TODO Auto-generated method stub
		WorkerUser user = (WorkerUser) dummyUsers.get((long) userId);
		return user.getSkills();
	}

	@Override
	public boolean updateUserSkills(long userId, List<WorkerSkill> skills) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WorkInquiry> getUserWorkInquiriesSent(long userId) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * gets the list of inquiries received by  user
	 * @param userId the id of the user
	 * @return List<WorkInquiry> List of Work inquiries
	 */
	@Override
	public List<WorkInquiry> getUserWorkInquiriesReceived(long userId) {
		//hard coded values for now
		List<WorkInquiry> workInquiries = new ArrayList<>();
		//filter		
		return workInquiries;		
	}

	@Override
	public WorkInquiry addInquiry(WorkInquiry inquiry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateLatLong(long userId, Double[] latLong) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateCommittedStatusForEnquiry(int inquiryId, boolean committedStatus) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateHiredStatusForEnquiry(int inquiryId, boolean hiredStatus) {
		// TODO Auto-generated method stub
		return false;
	}

	
	/***
	 * get user based on userName
	 * @return User the matching user with userName
	 */
	@Override
	public User findByUsername(String userName)
	{
		User user = null;
		for(User val : dummyUsers.values())
		{	
			if(val.getUserName().equals(userName)) {				
				user  = val;
				break;
			}
		}
		return user;
	}
	
	//get user by emailAddress
	/***
	 * get user based on email address
	 * @param emailAddress email address of the user
	 * @return User the matching user with email address
	 */
	@Override
	public User findByEmailAddress(String emailAdress)
	{
		User user = null;
		for(User val : dummyUsers.values())
		{	
			if(val.getEmailAddress().equals(emailAdress)) {				
				user  = val;
				break;
			}
		}
		return user;
	}
	
	
	/**
	 * gets all the work inquiries made by the user which are committed by this worker user
	 * @param userId the id of the user
	 * @return List<WorkInquiry> list of work inquiry which has a committed status as true
	 */
	public List<WorkInquiry> getWorkInquiryCommited(Long userId)	{
		
		//Hard coded value, implementation pending
		List<WorkInquiry> workInquiries = getUserWorkInquiriesReceived(userId);
		return workInquiries;
	}
	
	
	/**
	 * gets the list of inquiries where user is hired
	 * @param userId the id of the user
	 * @return List<WorkInquiry> List of Work inquiries
	 */
	public List<WorkInquiry> getWorkInquiriesHired(Long userId)	{	
		//hard coded values for now
		List<WorkInquiry> workInquiries = getUserWorkInquiriesReceived(userId)	;
		return workInquiries;			
		
	}
	
}
