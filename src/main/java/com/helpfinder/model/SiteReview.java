
/*
 * BU Term project for cs622
 
 This class stores the information of a site review  
 * @author  Harish Janardhanan * 
 * @since   21-Jan-2022
 */
package com.helpfinder.model;

import java.util.Date;

//This class stores the information of a site review
public class SiteReview {
	
	//all properties
	private long userId;
	private String reviewId;
	private String title;
	private String comment;
	private Date submittedDate;
	private Date reviewedDate;
	
	//setters 
	public void setUserId(long userId) {
		this.userId = userId;		
	}
	
	public void setReviewId(String reviewId){
		this.reviewId = reviewId;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public void setSubmittedDate(Date submittedDate){
		this.submittedDate = submittedDate;
	}
	
	public void setReviewedDate(Date reviewedDate){
		this.reviewedDate = reviewedDate;
	}
	
	//getters
	public long getUserId() {
		return userId;		
	}
	
	public String getTitle() {
		return title;		
	}
	
	public String getReviewId() {
		return reviewId;		
	}
	
	public  String getComment() {
		return comment;
	}
	
	public Date getSubmittedDate() {
		return submittedDate;
	}
	
	public  Date getReviewedDate() {
		return reviewedDate;
	}

}
