/*
 * BU Term project for cs622
 
  This interface is the implementation for the data access layer to detect the works  based on different criterias
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;

@Component
public interface WorkForceLocatorRepository {

	// find and return the users matching the skills and mileRadius
	public List<User> findWorkforceForSkills(Double[] latlong, List<WorkerSkill> skills, double mileRadius);
	
	// get the inquiry for the given inquiryId
	public WorkInquiry getInquiry(int inquiryId);

}
