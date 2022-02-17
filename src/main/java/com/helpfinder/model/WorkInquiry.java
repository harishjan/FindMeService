/*
 * BU Term project for cs622
 
  This class is used to store the Work inquiries made by users
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import java.util.Date;


//work inquiry made by a user
public class WorkInquiry {
    
    //id reference for the inquiry
    private int inquiryId;    
    //status if the worked committed to do this work
    private boolean isCommitted = false;
    //sets the date the work commitment was last updated
    private Date committedDate;
    //the Date when should the work is expected to start 
    private Date workStartDate;
    //the Date when the work is expected to end
    private Date workEndDate;
    //the user who is requesting for the work
    private User helpFinderUser;
    // the user who receiving the work
    private User workerUser;
    //the Date when the hire status is last updated
    private Date hiredDate;
    //status which shows if the worker is hired 
    private boolean isHired = false;
    private Date insertedOn;
    private Date updatedOn;
    private String workDescription;
    private Date cancelledDate;
    private boolean isCancelled;
    //The distance between the worker and the help finder user while inquiry was sent    
    private double distanceFoundAwayfrom;
    
    //empty constructor
    public WorkInquiry() {}
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
    public Date getWorkStartDate()    {
        return workStartDate;
    }
    
    /**
     * Gets the work end date
     *@return Date date when the work should end
     * */
    public Date getWorkEndDate()    {
        return workEndDate;
    }
    
    /**
     * Gets the worker user
     *@return USer the worker user
     * */
    public User getWorkerUser()    {
        return workerUser;
    }
    
    /**
     * Gets the user who requested for work
     *@return User user who requested for work
     * */
    public User getHelpFinderUser()    {
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
     * returns the work committed date
     * @return
     */
    public Date getWorkCommitedDate() {        
        return this.committedDate ;
    }
    
    /**
     * returns the worked hired date
     * @return
     */
    public Date getWorkHiredDate() {        
        return this.hiredDate;
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
    
    public void setHiredDate(Date hiredDate) {        
        this.hiredDate = hiredDate;
    }
    
    public void setIsHired(boolean isHired) {
        this.isHired = isHired;    
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
    
    
    public void setCommitedon(Date committedDate) {
        this.committedDate = committedDate;
    
    }
    
    public void setIsCommitted(boolean isCommitted) {
        this.isCommitted = isCommitted;
    
    }
    
    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    
    }
    
    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    
    }
    
    public Date getCancelledDate() {
        return cancelledDate;
    
    }
    
    public boolean getIsCancelled() {
        return isCancelled;
    
    }
    
    

    /**
     * sets the id reference of the inquiry
     *@param inquiryId int the inquiry reference id
     *
     * */
    public void setInquiryId(int inquiryId) {
        this.inquiryId = inquiryId;
    }
    
    
    /**
     * sets the work start date
     * @param workStartDate the work start date 
     *@return Date date when the work should start
     * */
    public void setWorkStartDate(Date workStartDate)    {
        this.workStartDate = workStartDate;
    }
    
    /**
     * Gets the work end date
     * @param workEndDate the date when work ends
     *@return Date date when the work should end
     * */
    public void setWorkEndDate(Date workEndDate)    {
        this.workEndDate = workEndDate;
    }
    
    /**
     * Gets the worker user
     * @param workerUser the work user instance
     *@return USer the worker user
     * */
    public void setWorkerUser(User workerUser)    {
        this.workerUser = workerUser;
    }
    
    /**
     * Gets the user who requested for work
     * @param helpFinderUser the help finder user instance 
     *@return User user who requested for work
     * */
    public void setHelpFinderUser(User helpFinderUser)    {
        this.helpFinderUser = helpFinderUser;
    }
    
    
    public void setInsertedOn(Date insertedOn) {
        this.insertedOn = insertedOn;
    }
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }
    public void setDistanceFoundAwayfrom(double distanceFoundAwayfrom) {
        this.distanceFoundAwayfrom = distanceFoundAwayfrom;
    }
    
    public Date getInsertedOn() {
        return insertedOn;
    }
    public Date getUpdatedOn() {
        return updatedOn;
    }
    public String getWorkDescription() {
        return workDescription;   
    }        
    public double getDistanceFoundAwayfrom() {
        return distanceFoundAwayfrom;
    }
}