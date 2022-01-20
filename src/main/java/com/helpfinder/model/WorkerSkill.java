/*
 * BU Term project for cs622
 
  This class the different skills used in the system which can be associated with a WorkUser
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

public class WorkerSkill {
	// id reference of the work skill
	public int workSkillId;
	//the name of the skill
	public String workSkillName;
	
	/**
	 * constructor
	 * @param workSkillId id reference of the work skill 
	 * @param workSkillName the name of the skill	
	 */
	public WorkerSkill(int workSkillId, String workSkillName)
	{
		this.workSkillId = workSkillId;
		this.workSkillName = workSkillName;
		
	}
	
	/**
	 * gets the name of the work skill	 * 	
	 */
	public String getSkillName()	{
		return this.workSkillName;
	}
}
