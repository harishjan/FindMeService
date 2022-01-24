/*
 * BU Term project for cs622
 
  This interface is the implementation to manage work skills
  
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.helpfinder.model.WorkerSkill;

@Component
public interface WorkerSkillRepository {
	// get all allowed skills in the system for users
	public List<WorkerSkill> getAllSkillsets();
	
	// get all allowed skills in the system for users
	public void captureNewSkillRequest(Long userId, List<WorkerSkill> skill); 

	// get all allowed skills in the system for users
	public List<WorkerSkill> getUnreviewedSkillRequest(List<WorkerSkill> skill);

	// get all allowed skills in the system for users
	public List<WorkerSkill> addNewSkill(List<WorkerSkill> skill);

}
