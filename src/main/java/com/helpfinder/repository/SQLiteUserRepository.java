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

import org.springframework.stereotype.Repository;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;


@Repository
// implementation is still not completed
public class SQLiteUserRepository implements UserRepository {

	// dummy data for testing
	static HashMap<Long, User> dummyUsers = new HashMap<Long, User>();
	// stores the location service repo
	LocationServiceRepository locationServiceRepo;

	// constructor added to create dummy data
	public SQLiteUserRepository(LocationServiceRepository locationServiceRepo) {
		this.locationServiceRepo = locationServiceRepo;
		// create some dummy data
		// 1 is a HelpFinderUser
		// 2 and 3 is a WorkerUser
		HelpFinderUser user1 = new HelpFinderUser(1, "test address", "John", "m", "johnm@test.com");
		user1.latLong[0] = 1.1;
		user1.latLong[1] = 2.2;

		ArrayList<WorkerSkill> user2Skills = new ArrayList<WorkerSkill>();
		user2Skills.add(getAllSkillsets().get(0));
		user2Skills.add(getAllSkillsets().get(1));		
		WorkerUser user2 = new WorkerUser(2, "test2 address", "Matt", "p", "mattm@test.com", user2Skills);
		user1.latLong[0] = 1.2;
		user1.latLong[1] = 2.3;

		ArrayList<WorkerSkill> user3Skills = new ArrayList<WorkerSkill>();
		user3Skills.add(getAllSkillsets().get(0));
		user3Skills.add(getAllSkillsets().get(3));
		WorkerUser user3 = new WorkerUser(3, "test3 address", "Micheal", "S", "micheals@test.com", user3Skills);
		user1.latLong[0] = 12.1;
		user1.latLong[1] = 16.2;

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
	 * 
	 * @param user is the instance of User object
	 * @return user the user instance after setting the latlong and userId
	 */

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		// implementation with dummy data.

		// downcast
		BasicUser basicUser = ((BasicUser) user);
		// set the userId, dummy value
		basicUser.userId = java.time.Instant.now().toEpochMilli();
		// get lat long , dummy value
		Double[] latLong = locationServiceRepo.getLatLogFromAddress(basicUser.address);
		// set the latlong in user object
		basicUser.latLong = latLong;
		dummyUsers.put(basicUser.userId, basicUser);
		return basicUser;
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

	@Override
	public List<WorkInquiry> getUserWorkInquiriesReceived(long userId) {
		// TODO Auto-generated method stub
		return null;
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

	/**
	 * gets the list of skills which are available in the system to hire
	 * 
	 * @return List<WorkerSkill> List of skills available in the system to hire
	 */
	public List<WorkerSkill> getAllSkillsets() {

		// hard coded skills
		List<WorkerSkill> skills = new ArrayList<WorkerSkill>();
		skills.add(new WorkerSkill(1, "Handy Man"));
		skills.add(new WorkerSkill(2, "Painting"));
		skills.add(new WorkerSkill(3, "Cooking"));
		skills.add(new WorkerSkill(4, "House cleaning"));
		return skills;
	}

}
