    
/*
 
 
  This class is used to sent a workInquiryRequest
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;

//work inquiry made by a user
public class WorkInquiryRequest {
    
    //the Date when should the work is expected to start 
    @NotBlank
    private Date workStartDate;
    //the Date when the work is expected to end
    @NotBlank
    private Date workEndDate;
    
    //the user who is requesting for the work
    @NotBlank
    private long helpFinderUserId;
    // the user who receiving the work
    @NotBlank
    private long workerUserId;
    //the work description
    @NotBlank
    private String workDescription;
    //The distance between the worker and the help finder user while inquiry was sent
    @NotBlank
    private double distanceFoundAwayfrom;
    
 
    public Date getWorkStartDate() {
        return this.workStartDate;
    }

    public Date getWorkEndDate() {
        return this.workEndDate;
    }

    public long getHelpFinderUserId() {
        return helpFinderUserId;
    }

    public void setHelpFinderUserId(long helpFinderUserId) {
        this.helpFinderUserId = helpFinderUserId;
    }

    public long getWorkerUserId() {
        return workerUserId;
    }

    public String getWorkDescription() {
        return workDescription;
    }
 
    public double getDistanceFoundAwayfrom() {
        return distanceFoundAwayfrom;
    }

     
    public void setWorkStartDate(Date workStartDate) {
        this.workStartDate = workStartDate;
    }
    
    public void setWorkEndDate(Date workEndDate) {
        this.workEndDate = workEndDate;
    }
    
    
    public void setWorkerUserId(long WorkerUserId) {
        this.workerUserId = WorkerUserId;
    }
    
    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }
     
    public void setDistanceFoundAwayfrom(double distanceFoundAwayfrom) {
        this.distanceFoundAwayfrom = distanceFoundAwayfrom;
    }

}
