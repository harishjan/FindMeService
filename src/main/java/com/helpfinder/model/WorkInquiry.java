/*
 * BU Term project for cs622
 
  This class is used to store the Work inquiries made by users
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import java.util.Date;

public class WorkInquiry {
	
	//id reference for the inquiry
	protected int inquiryId;	
	//status if the worked committed to do this work
	protected boolean isCommitted = false;
	//sets the date the work commitment was last updated
	protected Date committedDate;
	//the Date when should the work is expected to start 
	protected Date workStartDate;
	//the Date when the work is expected to end
	protected Date workEndDate;
	//the user who is requesting for the work
	protected User helpFinderUser;
	// the user who receiving the work
	protected User workerUser;
	//the Date when the hire status is last updated
	protected Date hiredDate;
	//status which shows if the worker is hired 
	protected boolean isHired = false;
	
	/**
	 * constructor
	 * @param workStartDate work start date
	 * @param workEndDate work end date 
	 * @param helpFinderUser user requesting for work
	 * @param workerUser worker to whom the request is sent
	 
	 */
	public WorkInquiry(int inquiryId, Date workStartDate, Date workEndDate, User helpFinderUser,User workerUser) {		
		this.inquiryId = inquiryId;
		this.workStartDate = workStartDate;
		this.workEndDate = workEndDate;
		this.helpFinderUser = helpFinderUser;
		this.workerUser = workerUser;
	}
	
	/**
	 * Gets the id reference of the inquiry
	 *@return int the id reference of the inquiry
	 * */
	public int getInquiryId() {
		return inquiryId;
	}
	
	/**
	 * Gets the committed status from the worker
	 *@return boolean  committed status 
	 * */
	public boolean hasWorkCommited() {
		return isCommitted;
	}
	
	/**
	 * Gets the work start date
	 *@return Date date when the work should start
	 * */
	public Date getWorkStartDate()	{
		return workStartDate;
	}
	
	/**
	 * Gets the work end date
	 *@return Date date when the work should end
	 * */
	public Date getWorkEndDate()	{
		return workEndDate;
	}
	
	/**
	 * Gets the worker user
	 *@return USer the worker user
	 * */
	public User getWorkerUser()	{
		return workerUser;
	}
	
	/**
	 * Gets the user who requested for work
	 *@return User user who requested for work
	 * */
	public User getHelpFinderUser()	{
		return helpFinderUser;
	}
	
	/**
	 * Gets the hired status
	 *@return boolean the hired status
	 * */
	public boolean isHired() {
		return isHired;
	}
	
	/**
	 * Sets the status of hired along with the date when the status is changed
	 * @param isHired boolean value to set the hired status 
	 * @param hiredDate the Date value when the status is updated
	 * */
	public void setWorkerHired(boolean isHired, Date hiredDate) {
		this.isHired = isHired;
		this.hiredDate = hiredDate;
	}
	
	/**
	 * Sets work committed status along with date when the status is changed
	 *@param isCommitted the boolean value of committed status
	 *@param committedDate is the date when the status is changed
	 * */
	public void setWorkCommited(boolean isCommitted, Date committedDate) {
		this.isCommitted = isCommitted;
		this.committedDate = committedDate;
	}
	


}
