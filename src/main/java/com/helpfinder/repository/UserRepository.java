/*
 * BU Term project for cs622
 
  This interface is the implementation for the data access layer to get user related information
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;

@Repository
public interface UserRepository {
	// get the user for the given userId
	public User getUser(long userId);

	// create a new User and return the User with the new userId
	public User createUser(User user);

	// finds if a user already exist with the email address
	public boolean userExist(String emailAddrss);

	// gets the skills added to a user
	public List<WorkerSkill> getUserSkills(long userId);

	// update the user with the skills
	public boolean updateUserSkills(long userId, List<WorkerSkill> skills);

	// get the not expired work inquiries sent by a user
	public List<WorkInquiry> getUserWorkInquiriesSent(long userId);

	// get the not expired inquiries received by a user
	public List<WorkInquiry> getUserWorkInquiriesReceived(long userId);

	// add a new inquiry and return the WorkInquiry with inquiryId updated
	public WorkInquiry addInquiry(WorkInquiry inquiry);

	// update lat long for a user
	public boolean updateLatLong(long userId, Double[] latLong);

	// update committed status for an inquiry
	public boolean updateCommittedStatusForEnquiry(int inquiryId, boolean committedStatus);

	// update hired status for inquiry
	public boolean updateHiredStatusForEnquiry(int inquiryId, boolean hiredStatus);

	// get all allowed skills in the system for users
	public List<WorkerSkill> getAllSkillsets();

}
