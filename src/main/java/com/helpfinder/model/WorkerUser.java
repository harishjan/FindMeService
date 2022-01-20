/*
 * BU Term project for cs622
 
  Concrete class which is the worker user in the system, the method setUserInformation is overloaded here to accept users skills

 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import java.util.ArrayList;
import java.util.List;

public class WorkerUser extends BasicUser{
	
	//the property which stores all skills sets of this user
	protected List<WorkerSkill> workSkills = new ArrayList<WorkerSkill>();
	
	
	//empty constructor
	public WorkerUser()	{
		this.userType = EUserEnum.ROLE_WORKER_USER;
	}
	
	/**
	 * constructor
	 * @param userId user id 
	 * @param address the address of the user 
	 * @param firstName first name of the user
	 * @param lastname last name of the user	
	 * @param emailAddress email address of the user
	 * @param workSkills list of WorkSkill that are specific for this user
	 */
	public WorkerUser(long userId, String address, String firstName, String lastname, String emailAddress, ArrayList<WorkerSkill> workSkills)
	{
		this.userId = userId;
		super.setUserInformation(address, firstName, lastname, emailAddress);
		this.workSkills = workSkills;
	}
	
	
	/**
	 * Gets users work skills 
	 *@return List<WorkerSkill> this users WorkSkills
	 * */	
	public List<WorkerSkill> getSkills()
	{
		return workSkills;
	}
	
	/**
	 * This method is and overloading this method that sets the user information
	 * @param userId the id of the user 
	 * @param address the address of the user 
	 * @param firstName first name of the user
	 * @param lastname last name of the user
	 * @param workSkills a list of WorkerSkill of the user 		
	 */
	public void setUserInformation(String address, String firstName, String lastname, String emailAddress, ArrayList<WorkerSkill> workSkills)
	{
		this.userType = EUserEnum.ROLE_WORKER_USER;
		super.setUserInformation(address, firstName, lastname, emailAddress);
		this.workSkills = workSkills;
	}
	
	
	/**
	 * gets the list of inquiries received by  user
	 * @return List<WorkInquiry> List of Work inquiries
	 */
	public List<WorkInquiry> getWorkInquirieReceived()	{
		//hard coded values for now
		List<WorkInquiry> workInquiries = new ArrayList<>();
		//filter		
		return workInquiries;			
		
	}
	
	/**
	 * gets all the work inquiries made by the user which are committed by this worker user
	 * @return List<WorkInquiry> list of work inquiry which has a committed status as true
	 */
	public List<WorkInquiry> getWorkInquiryCommited()	{
		
		//Hard coded value, implementation pending
		List<WorkInquiry> workInquiries = getWorkInquirieReceived(); 
		//filter
		
		return workInquiries;
	}
	
	
	/**
	 * gets the list of inquiries where user is hired
	 * @return List<WorkInquiry> List of Work inquiries
	 */
	public List<WorkInquiry> getWorkInquiriesHired()	{	
		//hard coded values for now
		List<WorkInquiry> workInquiries = getWorkInquirieReceived()	;
		//filter	
		
		return workInquiries;			
		
	}

}
